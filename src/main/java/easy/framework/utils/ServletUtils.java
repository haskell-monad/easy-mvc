package easy.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import easy.framework.mvc.common.Constant;
import easy.framework.mvc.helper.FileUploadHelper;
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
}
