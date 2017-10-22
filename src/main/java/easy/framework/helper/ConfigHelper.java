package easy.framework.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.common.PropertyConfigConstant;
import easy.framework.utils.PropertyUtil;

/**
 * @author limengyu
 * @create 2017/08/21
 */
public class ConfigHelper {
	private static final Logger logger = LoggerFactory.getLogger(ConfigHelper.class);

	public static String getAppBasePackage() {
		String basePackage = PropertyUtil.getStringValue(PropertyConfigConstant.APP_BASE_PACKAGE_CONFIG);
		if (StringUtils.isBlank(basePackage)) {
			logger.error("缺少配置项: {}", PropertyConfigConstant.APP_BASE_PACKAGE_CONFIG);
			throw new RuntimeException("缺少配置项: "+PropertyConfigConstant.APP_BASE_PACKAGE_CONFIG+"");
		}
		return basePackage;
	}
	public static String getJspDir() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.JSP_DIR_CONFIG, PropertyConfigConstant.JSP_DIR_CONFIG_DEFAULT);
	}
	public static String getStaticDir() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.STATIC_DIR_CONFIG, PropertyConfigConstant.STATIC_DIR_CONFIG_DEFAULT);
	}
	public static String getHomePage() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.HOME_PAGE, PropertyConfigConstant.HOME_PAGE_DEFAULT);
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
	public static String getJdbcType() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.JDBC_TYPE, PropertyConfigConstant.JDBC_TYPE_DEFAULT);
	}
	public static String getJdbcDriver() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.JDBC_DRIVER, PropertyConfigConstant.JDBC_DRIVER_DEFAULT);
	}
	public static String getJdbcUrl() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.JDBC_URL, PropertyConfigConstant.JDBC_URL_DEFAULT);
	}
	public static String getJdbcUserName() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.JDBC_USERNAME, PropertyConfigConstant.JDBC_USERNAME_DEFAULT);
	}
	public static String getJdbcPassWord() {
		return PropertyUtil.getStringValue(PropertyConfigConstant.JDBC_PASSWORD, PropertyConfigConstant.JDBC_PASSWORD_DEFAULT);
	}
}
