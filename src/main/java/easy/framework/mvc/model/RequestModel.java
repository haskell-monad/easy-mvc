package easy.framework.mvc.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by limengyu on 2017/9/18.
 */
public class RequestModel {
	private String path;
	private String method;

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		if (StringUtils.isNotBlank(path)) {
			path = path.replaceAll("//", "/");
		}
		this.path = path;
	}
	public String getMethod() {
		return method;
	}
	public boolean isRegex() {
		return path.contains("{") && path.contains("}");
	}
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * 获取url中的参数名
	 * @return
	 */
	public List<String> pathParams() {
		List<String> params = new ArrayList<>();
		String requestPath = path;
		while (requestPath.indexOf("{") > 0) {
			String param = requestPath.substring(requestPath.indexOf("{") + 1, requestPath.indexOf("}"));
			if (StringUtils.isNotBlank(param)) {
				params.add(param);
			}
			requestPath = requestPath.substring(requestPath.indexOf("}") + 1, requestPath.length());
		}
		return params;
	}
}
