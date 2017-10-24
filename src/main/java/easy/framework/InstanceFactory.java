package easy.framework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import easy.framework.common.FrameworkConfigConstant;
import easy.framework.core.ClassLoaderHelper;
import easy.framework.database.dao.DataAccessor;
import easy.framework.database.dao.impl.JdbcTemplate;
import easy.framework.database.ds.AbstractDataSourceFactory;
import easy.framework.database.ds.pool.DbcpDataSourceFactory;
import easy.framework.helper.ConfigHelper;
import easy.framework.ioc.BeanHelper;
import easy.framework.mvc.HandlerExceptionResolver;
import easy.framework.mvc.HandlerInvoke;
import easy.framework.mvc.HandlerMapping;
import easy.framework.mvc.HandlerViewResolver;
import easy.framework.mvc.impl.DefaultHandlerExceptionResolver;
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
		return getInstance(FrameworkConfigConstant.HANDLER_MAPPING_KEY, DefaultHandlerMapping.class);
	}
	public static HandlerInvoke getHandlerInvoke() {
		return getInstance(FrameworkConfigConstant.HANDLER_INVOKE_KEY, DefaultHandlerInvoke.class);
	}
	public static HandlerViewResolver getHandlerViewResolver() {
		return getInstance(FrameworkConfigConstant.HANDLER_VIEW_RESOLVER_KEY, DefaultHandlerViewResolver.class);
	}
	public static HandlerExceptionResolver getHandlerExceptionResolver() {
		return getInstance(FrameworkConfigConstant.HANDLER_EXCEPTION_RESOLVER_KEY, DefaultHandlerExceptionResolver.class);
	}
	public static AbstractDataSourceFactory getDataSourceFactory() {
		return getInstance(FrameworkConfigConstant.DATASOURCE_KEY, DbcpDataSourceFactory.class);
	}
	public static DataAccessor getDataAccessor() {
		return getInstance(FrameworkConfigConstant.DATA_ACCESSOR_KEY, JdbcTemplate.class);
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
				if (BeanHelper.containBean(clazz)) {
					t = BeanHelper.getBeanInstance(clazz);
				} else {
					t = clazz.newInstance();
				}
			}
			CACHE_BEAN_MAP.put(cacheKey, t);
			return t;
		} catch (Exception e) {
			throw new RuntimeException("获取实例异常", e);
		}
	}
}
