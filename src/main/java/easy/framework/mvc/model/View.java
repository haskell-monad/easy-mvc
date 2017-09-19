package easy.framework.mvc.model;

import java.util.Map;

/**
 * Created by limengyu on 2017/9/18.
 */
public class View {
	private String path;
	private Map<String, Object> params;

	public View(String path, Map<String, Object> params) {
		this.path = path;
		this.params = params;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
