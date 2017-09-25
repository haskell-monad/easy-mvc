package easy.framework.mvc.model;

/**
 * Created by limengyu on 2017/9/22.
 *  某个方法的参数信息
 */
public class ParamModel {
	private String paramName;
	private Object paramValue;
	private Class<?> paramType;
	private int paramIndex;
	private boolean requestBody;
	private boolean fileBody;

	public boolean isFileBody() {
		return fileBody;
	}

	public void setFileBody(boolean fileBody) {
		this.fileBody = fileBody;
	}

	public boolean isRequestBody() {
		return requestBody;
	}
	public void setRequestBody(boolean requestBody) {
		this.requestBody = requestBody;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public Object getParamValue() {
		return paramValue;
	}
	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}
	public Class<?> getParamType() {
		return paramType;
	}
	public void setParamType(Class<?> paramType) {
		this.paramType = paramType;
	}
	public int getParamIndex() {
		return paramIndex;
	}
	public void setParamIndex(int paramIndex) {
		this.paramIndex = paramIndex;
	}
}
