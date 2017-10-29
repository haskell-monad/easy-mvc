package easy.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import easy.framework.mvc.common.Constant;
import easy.framework.mvc.helper.FileUploadHelper;
import easy.framework.mvc.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.mvc.model.ParamModel;

/**
 * @author limengyu
 * @create 2017/9/20
 */
public class ServletUtils {
	private static final Logger logger = LoggerFactory.getLogger(ServletUtils.class);

	/**
	 * 获取请求的url，去掉contextPath
	 * @param request
	 * @return
	 */
	public static String requestPath(HttpServletRequest request) {
		String requestPath = request.getRequestURI();
		if (StringUtils.isNotBlank(request.getContextPath())) {
			requestPath = requestPath.substring(request.getContextPath().length(), requestPath.length());
		}
		return requestPath;
	}
	/**
	 * 读取流数据
	 * @param request
	 * @return json参数数据
	 */
	public static String readData(HttpServletRequest request) {
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
	/**
	 * 获取kv请求参数
	 * @param request
	 * @return params
	 */
	public static Map<String, String> parseQueryParams(HttpServletRequest request) {
		Map<String, String> params = new HashMap<>(16);
		String queryString = request.getQueryString();
		if (StringUtils.isBlank(queryString)) {
			return params;
		}
	        String[] kv = queryString.split("&");
		if (kv == null || kv.length == 0) {
			return params;
		}
		for (String param : kv) {
			String[] pairs = param.split("=");
			if (pairs != null && pairs.length == 2) {
				params.put(pairs[0], pairs[1]);
			}
		}
		return params;
	}
	/**
	 * 获取json数据
	 * @param request
	 * @param requestBodyList
	 * @return params
	 */
	public static Map<String, Object> parseJsonParams(HttpServletRequest request, List<ParamModel> requestBodyList) {
		Map<String, Object> params = new HashMap<>(16);
		if(FileUploadHelper.isMultipart(request)){
			return params;
		}
		if (requestBodyList == null || requestBodyList.size() == 0) {
			return params;
		}
		String buffer = readData(request);
		if(StringUtils.isBlank(buffer)){
			return params;
		}
		logger.debug("string-buffer: {}", buffer);
		if(!JsonUtils.isJson(buffer)){
			readerParams(params,buffer);
			buffer = JsonUtils.toJson(params);
		}
		//这里需要优化 TODO
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

	private static void readerParams(Map<String, Object> params,String buffer){
		String[] paramList = buffer.split("&");
		if(paramList != null && paramList.length > 0){
			for (String param : paramList) {
				String[] pairs = param.split("=");
				if(pairs != null && pairs.length == 2){
					try {
						params.put(pairs[0], URLDecoder.decode(pairs[1], Constant.DEFAULT_ENCODE));
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	/**
	 * 获取Parameter参数
	 * @param request
	 * @return params
	 */
	public static Map<String, List<String>> parseParameterParams(HttpServletRequest request) {
		Map<String, List<String>> result = new HashMap<>(16);
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterMap != null && parameterMap.size() > 0) {
			parameterMap.forEach((param, values) -> {
				result.put(param, Arrays.asList(values));
			});
		}
		return result;
	}

	public static void sendError(int errCode,String msg,HttpServletResponse response){
		try {
			logger.error("sendError: {},{}",errCode,msg);
			response.sendError(errCode,msg);
		} catch (Exception e) {
			logger.error("sendError异常",e);
			throw new RuntimeException(e);
		}
	}

	public static void redirect(String viewPath, HttpServletRequest request, HttpServletResponse response){
		logger.debug("redirect: {}", request.getContextPath() + viewPath);
		try {
			response.sendRedirect(request.getContextPath() + viewPath);
		} catch (IOException e) {
			logger.error("请求重定向发生异常",e);
			throw new RuntimeException(e.getMessage());
		}
	}
	public static void forward(String viewPath, HttpServletRequest request, HttpServletResponse response){
		try {
			request.getRequestDispatcher(viewPath).forward(request, response);
		} catch (Exception e) {
			logger.error("请求转发发生异常",e);
			throw new RuntimeException(e.getMessage());
		}
	}
	public static void sendJson(HttpServletResponse response, Result result){
		response.setContentType("application/json");
		response.setCharacterEncoding(Constant.DEFAULT_ENCODE);
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(JsonUtils.toJson(result));
			writer.flush();
		} catch (IOException e) {
			logger.error("响应json数据异常",e);
			throw new RuntimeException(e.getMessage());
		} finally {
			writer.close();
		}
	}
}
