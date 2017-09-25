package easy.framework.helper;

import easy.framework.common.PropertyConfigConstant;
import easy.framework.utils.PropertyUtil;

/**
 * Created by limengyu on 2017/8/21.
 */
public class ConfigHelper {
	public static String getBasePackage() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.BASE_PACKAGE_CONFIG, PropertyConfigConstant.BASE_PACKAGE_CONFIG_DEFAULT);
	}
	public static String getJspDir() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.JSP_DIR_CONFIG, PropertyConfigConstant.JSP_DIR_CONFIG_DEFAULT);
	}
	public static String getStaticDir() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.STATIC_DIR_CONFIG, PropertyConfigConstant.STATIC_DIR_CONFIG_DEFAULT);
	}
	public static String getConfigValue(String key) {
		return PropertyUtil.getStringValue(key);
	}
	public static long getFileUploadMaxSize() {
		return PropertyUtil.getLongDefaultValue(PropertyConfigConstant.FILE_UPLOAD_MAX_SIZE, PropertyConfigConstant.FILE_UPLOAD_MAX_SIZE_DEFAULT);
	}
	public static long getFileUploadSingleFileMaxSize() {
		return PropertyUtil.getLongDefaultValue(PropertyConfigConstant.FILE_UPLOAD_SINGLE_FILE_SIZE, PropertyConfigConstant.FILE_UPLOAD_SINGLE_FILE_SIZE_DEFAULT);
	}
	public static String getFileUploadDirectory() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.FILE_UPLOAD_DIR_NAME, PropertyConfigConstant.FILE_UPLOAD_DIR_NAME_DEFAULT);
	}
}
