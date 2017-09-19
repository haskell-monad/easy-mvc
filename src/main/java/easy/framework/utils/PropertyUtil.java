package easy.framework.utils;

import java.io.IOException;
import java.util.Properties;

import easy.framework.common.PropertyConfigConstant;

/**
 * Created by limengyu on 2017/8/21.
 */
public class PropertyUtil {
	private static Properties properties = new Properties();
	static {
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PropertyConfigConstant.CONFIG_FILE_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getStringValue(String key) {
		return properties.getProperty(key);
	}
	public static String getStringValue(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	public static Integer getIntValue(String key) {
		return getStringValue(key) == null ? null : Integer.valueOf(getStringValue(key));
	}
	public static Long getLongValue(String key) {
		return getStringValue(key) == null ? null : Long.valueOf(getStringValue(key));
	}
	public static Double getDoubleValue(String key) {
		return getStringValue(key) == null ? null : Double.valueOf(getStringValue(key));
	}
}
