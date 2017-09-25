package easy.framework.mvc.model;

import java.util.List;
import java.util.Map;

/**
 * Created by limengyu on 2017/9/25.
 */
public class RequestParamModel {
	private Map<String, List<String>> formFileMap;
	private List<FileModel> fileList;

	public List<FileModel> getFileList() {
		return fileList;
	}
	public void setFileList(List<FileModel> fileList) {
		this.fileList = fileList;
	}
	public Map<String, List<String>> getFormFileMap() {
		return formFileMap;
	}
	public void setFormFileMap(Map<String, List<String>> formFileMap) {
		this.formFileMap = formFileMap;
	}
}
