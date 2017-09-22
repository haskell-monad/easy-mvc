package easy.framework.mvc.impl;

import java.io.BufferedReader;
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.ioc.BeanHelper;
import easy.framework.mvc.HandlerInvoke;
import easy.framework.mvc.annotation.Param;
import easy.framework.mvc.annotation.RequestBody;
import easy.framework.mvc.model.ParamModel;
import easy.framework.mvc.model.RequestHandler;
import easy.framework.utils.JsonUtils;
import easy.framework.utils.ReflectUtils;
import easy.framework.utils.ServletUtils;

/**
 * Created by limengyu on 2017/9/19. POST: @PathVariable @RequestBody(顺序) GET: @PathVariable @Param
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
		List<ParamModel> paramList = this.getParamInfo(method, methodParamNameList);
		List<ParamModel> requestBodyList = paramList.stream().filter(model -> model.isRequestBody()).collect(Collectors.toList());
		Map<String, String> pathParamsMap = this.parsePathParams(requestHandler, requestPath);
		Map<String, String> kvParamsMap = this.parseQueryParams(request);
		Map<String, Object> jsonParamsMap = this.parseJsonParams(request, requestBodyList);
		Map<String, String[]> formParamsMap = this.parseFormParams(request);
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
				} else if (formParamsMap.containsKey(paramName)) {
					paramValue = formParamsMap.get(paramName);
				}
				paramModel.setParamValue(paramValue);
				paramValueList.add(this.convertValue(paramModel.getParamType(), paramValue));
			}
		}
		logger.debug("方法参数信息: {}", paramList);
		logger.debug("方法参数值列表: {}", paramValueList);
		Object[] objects = paramValueList.toArray();
		return objects;
	}
	/**
	 * 转换参数类型
	 * @param paramType
	 * @param paramValue
	 * @return Object
	 */
	private Object convertValue(Class<?> paramType, Object paramValue) {
		if (paramValue == null) {
			return paramValue;
		}
		if (paramType.isInstance(paramValue)) {
			return paramValue;
		}
		String param = null;
		if (paramValue instanceof String) {
			param = (String) paramValue;
		}
		if (paramType.isPrimitive()) {
			if (paramType.getName().equals("byte")) {
				return Integer.parseInt(param);
			} else if (paramType.getName().equals("short")) {
				return Short.parseShort(param);
			} else if (paramType.getName().equals("int")) {
				return Integer.parseInt(param);
			} else if (paramType.getName().equals("long")) {
				return Long.parseLong(param);
			} else if (paramType.getName().equals("double")) {
				return Double.parseDouble(param);
			} else if (paramType.getName().equals("float")) {
				return Float.parseFloat(param);
			} else if (paramType.getName().equals("bool")) {
				return Boolean.parseBoolean(param);
			} else {
				logger.error("paramValue: {},paramType: {}", paramValue, paramType);
				throw new RuntimeException("类型转换异常");
			}
		} else {
			if (paramType.isAssignableFrom(Byte.class)) {
				return Byte.valueOf(param);
			} else if (paramType.isAssignableFrom(Short.class)) {
				return Short.valueOf(param);
			} else if (paramType.isAssignableFrom(Integer.class)) {
				return Integer.valueOf(param);
			} else if (paramType.isAssignableFrom(Long.class)) {
				return Long.valueOf(param);
			} else if (paramType.isAssignableFrom(Float.class)) {
				return Float.valueOf(param);
			} else if (paramType.isAssignableFrom(Double.class)) {
				return Double.valueOf(param);
			} else if (paramType.isAssignableFrom(Boolean.class)) {
				return Boolean.valueOf(param);
			} else {
				return paramType.cast(paramValue);
			}
		}
	}
	/**
	 * 获取json数据
	 * @param request
	 * @param requestBodyList
	 * @return params
	 */
	private Map<String, Object> parseJsonParams(HttpServletRequest request, List<ParamModel> requestBodyList) {
		Map<String, Object> params = new HashMap<>();
		if (requestBodyList == null || requestBodyList.size() == 0) {
			return params;
		}
		String buffer = this.readData(request);
		logger.debug("string-buffer: {}", buffer);
		for (ParamModel paramModel : requestBodyList) {
			try {
				Object obj = JsonUtils.toBean(buffer, paramModel.getParamType());
				params.put(paramModel.getParamName(), obj);
				break;
			} catch (Exception e) {
			}
		}
		return params;
	}
	/**
	 * 读取流数据
	 * @param request
	 * @return json参数数据
	 */
	private String readData(HttpServletRequest request) {
		StringBuffer buffer = new StringBuffer();
		String line;
		BufferedReader reader = null;
		try {
			reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			throw new RuntimeException("读取数据异常", e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException("读取数据异常", e);
			}
		}
		return buffer.toString();
	}
	private List<ParamModel> getParamInfo(Method method, List<String> methodParamName) {
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
	/**
	 * 获取kv请求参数
	 * @param request
	 * @return params
	 */
	public Map<String, String> parseQueryParams(HttpServletRequest request) {
		Map<String, String> params = new HashMap<>();
		String queryString = request.getQueryString();
		if (StringUtils.isNotBlank(queryString)) {
			String[] kv = queryString.split("&");
			if (kv != null && kv.length > 0) {
				for (String param : kv) {
					String[] pairs = param.split("=");
					if (pairs != null && pairs.length == 2) {
						params.put(pairs[0], pairs[1]);
					}
				}
			}
		}
		return params;
	}
	/**
	 * 获取form表单参数
	 * @param request
	 * @return parameterMap
	 */
	public Map<String, String[]> parseFormParams(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterMap != null && parameterMap.size() > 0) {
			parameterMap.forEach((key, values) -> {
				logger.debug("form===key===values: {}===={}", key, values);
			});
		}
		return parameterMap;
	}
}
