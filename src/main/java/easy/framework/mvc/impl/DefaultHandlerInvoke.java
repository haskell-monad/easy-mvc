package easy.framework.mvc.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.InstanceFactory;
import easy.framework.ioc.BeanHelper;
import easy.framework.mvc.HandlerInvoke;
import easy.framework.mvc.HandlerViewResolver;
import easy.framework.mvc.annotation.FileBody;
import easy.framework.mvc.annotation.Param;
import easy.framework.mvc.annotation.RequestBody;
import easy.framework.mvc.helper.FileUploadHelper;
import easy.framework.mvc.model.FileModel;
import easy.framework.mvc.model.ParamModel;
import easy.framework.mvc.model.RequestHandler;
import easy.framework.mvc.model.RequestParamModel;
import easy.framework.utils.ReflectUtils;
import easy.framework.utils.ServletUtils;

/**
 * Created by limengyu on 2017/9/19.
 */
public class DefaultHandlerInvoke implements HandlerInvoke {
	private static final Logger logger = LoggerFactory.getLogger(DefaultHandlerInvoke.class);

	/**
	 * @param request
	 * @param response
	 * @param requestHandler
	 */
	@Override
	public void invoke(HttpServletRequest request, HttpServletResponse response, RequestHandler requestHandler) {
		if (requestHandler == null) {
			try {
				String requestPath = ServletUtils.requestPath(request);
				logger.warn("资源handler不存在[{}]", requestPath);
				response.sendError(404, "资源映射不存在");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Method method = requestHandler.getMethod();
		Object beanInstance = BeanHelper.getBeanInstance(requestHandler.getControllerClass());
		Object[] params = this.builderMethodParam(request, requestHandler);
		try {
			logger.debug("开始执行方法....");
			Object resultObj = method.invoke(beanInstance, params);
			logger.debug("开始方法结果....{}", resultObj);
			HandlerViewResolver viewResolver = InstanceFactory.getHandlerViewResolver();
			viewResolver.resolver(request, response, resultObj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 构造方法执行参数
	 * @param request
	 * @param requestHandler
	 * @return objects
	 */
	private Object[] builderMethodParam(HttpServletRequest request, RequestHandler requestHandler) {
		Method method = requestHandler.getMethod();
		List<String> methodParamNameList = ReflectUtils.findMethodParamName(method);
		String requestPath = ServletUtils.requestPath(request);
		int parameterCount = method.getParameterCount();
		logger.debug("方法参数名称列表: {}", methodParamNameList);
		List<ParamModel> paramList = this.getMethodParamInfo(method, methodParamNameList);
		List<ParamModel> requestBodyList = paramList.stream().filter(model -> model.isRequestBody()).collect(Collectors.toList());
		Map<String, String> pathParamsMap = this.parsePathParams(requestHandler, requestPath);
		Map<String, String> kvParamsMap = ServletUtils.parseQueryParams(request);
		Map<String, Object> jsonParamsMap = ServletUtils.parseJsonParams(request, requestBodyList);
		RequestParamModel requestParamModel = FileUploadHelper.parseFormParam(request);
		Map<String, List<String>> formParamsMap = requestParamModel.getFormFileMap();
		List<FileModel> fileList = requestParamModel.getFileList();
		// checkParamCount(pathParamsMap, kvParamsMap, parameterCount);
		List<Object> paramValueList = new ArrayList<>();
		if (paramList != null && paramList.size() > 0) {
			for (ParamModel paramModel : paramList) {
				String paramName = paramModel.getParamName();
				Object paramValue = paramModel.getParamValue();
				if (pathParamsMap.containsKey(paramName)) {
					paramValue = pathParamsMap.get(paramName);
				} else if (kvParamsMap.containsKey(paramName)) {
					paramValue = kvParamsMap.get(paramName);
				} else if (jsonParamsMap.containsKey(paramName)) {
					paramValue = jsonParamsMap.get(paramName);
				} else if (formParamsMap != null && formParamsMap.containsKey(paramName)) {
					paramValue = formParamsMap.get(paramName);
				} else if (paramModel.isFileBody() && fileList != null && fileList.size() > 0) {
					if (List.class.isAssignableFrom(paramModel.getParamType())) {
						paramValue = fileList;
					} else {
						paramValue = fileList.get(0);
					}
				}
				paramModel.setParamValue(paramValue);
				paramValueList.add(ReflectUtils.convertValue(paramModel.getParamType(), paramValue));
			}
		}
		logger.debug("方法参数信息: {}", paramList);
		logger.debug("方法参数值列表: {}", paramValueList);
		Object[] objects = paramValueList.toArray();
		return objects;
	}
	private List<ParamModel> getMethodParamInfo(Method method, List<String> methodParamName) {
		List<ParamModel> paramList = new ArrayList<>();
		if (methodParamName == null || methodParamName.size() == 0) {
			return paramList;
		}
		Parameter[] parameters = method.getParameters();
		ParamModel paramModel;
		for (int i = 0; i < parameters.length; i++) {
			paramModel = new ParamModel();
			paramModel.setParamName(methodParamName.get(i));
			paramModel.setParamType(parameters[i].getType());
			paramModel.setParamIndex(i);
			if (parameters[i].isAnnotationPresent(Param.class)) {
				String defaultValue = parameters[i].getAnnotation(Param.class).defaultValue();
				paramModel.setParamValue(defaultValue);
			}
			if (parameters[i].isAnnotationPresent(RequestBody.class)) {
				paramModel.setRequestBody(true);
			}
			if (parameters[i].isAnnotationPresent(FileBody.class)) {
				paramModel.setFileBody(true);
			}
			paramList.add(paramModel);
		}
		return paramList;
	}
	private Map<String, String> parsePathParams(RequestHandler requestHandler, String requestPath) {
		List<String> pathParams = requestHandler.getPathParams();
		List<String> pathParamsValue = requestHandler.matchGroup(requestPath);
		Map<String, String> params = new HashMap<>();
		if (pathParams == null || pathParamsValue == null) {
			return params;
		}
		if (pathParams.size() != pathParamsValue.size()) {
			throw new RuntimeException("path参数不匹配");
		}
		for (int i = 0; i < pathParams.size(); i++) {
			params.put(pathParams.get(i), pathParamsValue.get(i));
		}
		return params;
	}
	public void checkParamCount(Map<String, String> pathParamsMap, Map<String, String> kvParams, int parameterCount) {
		int pathNum = pathParamsMap.keySet().stream().mapToInt(key -> 1).sum();
		int kvNum = kvParams.keySet().stream().mapToInt(key -> 1).sum();
		if (pathNum + kvNum != parameterCount) {
			logger.debug("path参数个数: {},kv参数个数: {},方法参数总数: {}", pathNum, kvNum, parameterCount);
			throw new RuntimeException("参数缺失");
		}
	}
}
