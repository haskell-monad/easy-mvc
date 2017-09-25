package easy.framework.mvc.model;

import java.io.InputStream;

/**
 * Created by limengyu on 2017/9/25.
 */
public class FileModel {
	private String fileRealName;//文件真实名称
	private String fileTempName;//文件临时名称
	private long fileSize;//文件大小
	private String fileSuffix;//文件后缀
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
