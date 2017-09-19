package easy.framework.mvc.model;

import easy.framework.mvc.common.RequestMethod;

/**
 * Created by limengyu on 2017/9/18.
 */
public class RequestModel {
	private String path;
	private RequestMethod method;

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public RequestMethod getMethod() {
		return method;
	}
	public void setMethod(RequestMethod method) {
		this.method = method;
	}
}
