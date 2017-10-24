package easy.framework.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.common.FrameworkConfigConstant;
import easy.framework.utils.PropertyUtil;

/**
 * @author limengyu
 * @create 2017/08/21
 */
public class ConfigHelper {
	private static final Logger logger = LoggerFactory.getLogger(ConfigHelper.class);

	public static String getAppBasePackage() {
		String basePackage = PropertyUtil.getStringValue(FrameworkConfigConstant.APP_BASE_PACKAGE_CONFIG);
		if (StringUtils.isBlank(basePackage)) {
			logger.error("缺少配置项: {}", FrameworkConfigConstant.APP_BASE_PACKAGE_CONFIG);
			throw new RuntimeException("缺少配置项: "+ FrameworkConfigConstant.APP_BASE_PACKAGE_CONFIG+"");
		}
		return basePackage;
	}
	public static String getJspDir() {
		return PropertyUtil.getStringValue(FrameworkConfigConstant.JSP_DIR_CONFIG, FrameworkConfigConstant.JSP_DIR_CONFIG_DEFAULT);
	}
	public static String getStaticDir() {
		return PropertyUtil.getStringValue(FrameworkConfigConstant.STATIC_DIR_CONFIG, FrameworkConfigConstant.STATIC_DIR_CONFIG_DEFAULT);
	}
	public static String getHomePage() {
		return PropertyUtil.getStringValue(FrameworkConfigConstant.HOME_PAGE, FrameworkConfigConstant.HOME_PAGE_DEFAULT);
	}
	public static String getConfigValue(String key) {
		return PropertyUtil.getStringValue(key);
	}
	public static long getFileUploadMaxSize() {
		return PropertyUtil.getLongDefaultValue(FrameworkConfigConstant.FILE_UPLOAD_MAX_SIZE, FrameworkConfigConstant.FILE_UPLOAD_MAX_SIZE_DEFAULT);
	}
	public static long getFileUploadSingleFileMaxSize() {
		return PropertyUtil.getLongDefaultValue(FrameworkConfigConstant.FILE_UPLOAD_SINGLE_FILE_SIZE, FrameworkConfigConstant.FILE_UPLOAD_SINGLE_FILE_SIZE_DEFAULT);
	}
	public static String getFileUploadDirectory() {
		return PropertyUtil.getStringValue(FrameworkConfigConstant.FILE_UPLOAD_DIR_NAME, FrameworkConfigConstant.FILE_UPLOAD_DIR_NAME_DEFAULT);
	}
	public static String getJdbcType() {
		return PropertyUtil.getStringValue(FrameworkConfigConstant.JDBC_TYPE, FrameworkConfigConstant.JDBC_TYPE_DEFAULT);
	}
	public static String getJdbcDriver() {
		return PropertyUtil.getStringValue(FrameworkConfigConstant.JDBC_DRIVER, FrameworkConfigConstant.JDBC_DRIVER_DEFAULT);
	}
	public static String getJdbcUrl() {
		return PropertyUtil.getStringValue(FrameworkConfigConstant.JDBC_URL, FrameworkConfigConstant.JDBC_URL_DEFAULT);
	}
	public static String getJdbcUserName() {
		return PropertyUtil.getStringValue(FrameworkConfigConstant.JDBC_USERNAME, FrameworkConfigConstant.JDBC_USERNAME_DEFAULT);
	}
	public static String getJdbcPassWord() {
		return PropertyUtil.getStringValue(FrameworkConfigConstant.JDBC_PASSWORD, FrameworkConfigConstant.JDBC_PASSWORD_DEFAULT);
	}
}
