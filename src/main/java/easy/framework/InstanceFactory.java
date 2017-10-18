package easy.framework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import easy.framework.common.PropertyConfigConstant;
import easy.framework.core.ClassLoaderHelper;
import easy.framework.database.ds.AbstractDataSourceFactory;
import easy.framework.database.ds.pool.DbcpDataSourceFactory;
import easy.framework.helper.ConfigHelper;
import easy.framework.mvc.HandlerInvoke;
import easy.framework.mvc.HandlerMapping;
import easy.framework.mvc.HandlerViewResolver;
import easy.framework.mvc.impl.DefaultHandlerInvoke;
import easy.framework.mvc.impl.DefaultHandlerMapping;
import easy.framework.mvc.impl.DefaultHandlerViewResolver;

/**
 * @author limengyu
 * @create 2017/09/19
 */
public class InstanceFactory {
	private static final Map<String, Object> CACHE_BEAN_MAP = new ConcurrentHashMap<>();

	public static HandlerMapping getHandlerMapping() {
		return getInstance(PropertyConfigConstant.HANDLER_MAPPING_KEY, DefaultHandlerMapping.class);
	}
	public static HandlerInvoke getHandlerInvoke() {
		return getInstance(PropertyConfigConstant.HANDLER_INVOKE_KEY, DefaultHandlerInvoke.class);
	}
	public static HandlerViewResolver getHandlerViewResolver() {
		return getInstance(PropertyConfigConstant.HANDLER_VIEW_RESOLVER_KEY, DefaultHandlerViewResolver.class);
	}
	public static AbstractDataSourceFactory getDataSourceFactory() {
		return getInstance(PropertyConfigConstant.DATASOURCE_KEY, DbcpDataSourceFactory.class);
	}
	private static <T> T getInstance(String cacheKey, Class<T> clazz) {
		try {
			if (CACHE_BEAN_MAP.containsKey(cacheKey)) {
				return (T) CACHE_BEAN_MAP.get(cacheKey);
			}
			String configValue = ConfigHelper.getConfigValue(cacheKey);
			T t;
			if (StringUtils.isNotBlank(configValue)) {
				Class<?> loadClass = ClassLoaderHelper.loadClass(configValue);
				t = (T) loadClass.newInstance();
			} else {
				t = clazz.newInstance();
			}
			CACHE_BEAN_MAP.put(cacheKey, t);
			return t;
		} catch (Exception e) {
			throw new RuntimeException("获取实例异常",e);
		}
	}
}
