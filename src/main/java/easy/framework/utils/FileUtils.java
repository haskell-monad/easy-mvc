package easy.framework.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author limengyu
 * @create 2017/10/17
 */
public class FileUtils {
    public static final String FILE = "file";
    public static final String JAR = "jar";
    public static final String CLASS_FILE_SUFFIX = ".class";
    /**
     * File.separator
     */
    public static final String SYSTEM_SEPARATOR = "/";

	/**
	 * 获取类全类名，不带扩展名(.class)
	 * @param packageName
	 * @param fileName
	 * @return easy.mvc.service.impl.UserServiceImpl
	 */
	public static String builderFullClassName(String packageName, String fileName) {
		return FilenameUtils.removeExtension(builderFullPackageName(packageName, fileName));
	}
	/**
	 * 获取当前包名
	 * @param packageName
	 * @param fileName
	 * @return easy.mvc.service.impl
	 */
	public static String builderFullPackageName(String packageName, String fileName) {
		if (StringUtils.isBlank(packageName)) {
			return fileName;
		} else {
			return packageName + "." + fileName;
		}
	}
    public static String getBaseName(String fileName){
        String baseName = FilenameUtils.getBaseName(fileName.replaceAll(SYSTEM_SEPARATOR, "."));
		if(StringUtils.isBlank(baseName)){
			throw new RuntimeException("没有获取到["+baseName+"]类文件名");
		}
        return baseName;
    }
	public static void main(String[] args) {
		System.out.println(builderFullClassName("easy.mvc.service.impl", "UserServiceImpl.class"));
	}
}
