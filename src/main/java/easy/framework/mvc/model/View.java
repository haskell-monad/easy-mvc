package easy.framework.mvc.model;

import java.util.Map;

/**
 * @author limengyu
 * @create 2017/09/18
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
