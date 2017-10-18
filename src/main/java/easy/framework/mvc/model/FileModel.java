package easy.framework.mvc.model;

import java.io.InputStream;

/**
 * @author limengyu
 * @create 2017/09/25
 */
public class FileModel {
	/**
	 * 文件真实名称
	 */
	private String fileRealName;
	/**
	 * 文件临时名称
	 */
	private String fileTempName;
	/**
	 * 文件大小
	 */
	private long fileSize;
	/**
	 * 文件后缀
	 */
	private String fileSuffix;
	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getFileRealName() {
		return fileRealName;
	}
	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}
	public String getFileTempName() {
		return fileTempName;
	}
	public void setFileTempName(String fileTempName) {
		this.fileTempName = fileTempName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileSuffix() {
		return fileSuffix;
	}
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
}
