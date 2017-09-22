package easy.framework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import easy.framework.common.PropertyConfigConstant;
import easy.framework.core.ClassLoaderHelper;
import easy.framework.helper.ConfigHelper;
import easy.framework.mvc.HandlerInvoke;
import easy.framework.mvc.HandlerMapping;
import easy.framework.mvc.HandlerViewResolver;
import easy.framework.mvc.impl.DefaultHandlerInvoke;
import easy.framework.mvc.impl.DefaultHandlerMapping;
import easy.framework.mvc.impl.DefaultHandlerViewResolver;

/**
 * Created by limengyu on 2017/9/19.
 */
public class InstanceFactory {
	private static final Map<String, Object> cacheBeanMap = new ConcurrentHashMap<>();

	public static HandlerMapping getHandlerMapping() {
		return getInstance(PropertyConfigConstant.HANDLER_MAPPING_KEY, DefaultHandlerMapping.class);
	}
	public static HandlerInvoke getHandlerInvoke() {
		return getInstance(PropertyConfigConstant.HANDLER_INVOKE_KEY, DefaultHandlerInvoke.class);
	}
	public static HandlerViewResolver getHandlerViewResolver() {
		return getInstance(PropertyConfigConstant.HANDLER_VIEW_RESOLVER_KEY, DefaultHandlerViewResolver.class);
	}
	private static <T> T getInstance(String cacheKey, Class<T> clazz) {
		try {
			if (cacheBeanMap.containsKey(cacheKey)) {
				return (T) cacheBeanMap.get(cacheKey);
			}
			String configValue = ConfigHelper.getConfigValue(cacheKey);
			T t;
			if (StringUtils.isNotBlank(configValue)) {
				Class<?> loadClass = ClassLoaderHelper.loadClass(configValue);
				t = (T) loadClass.newInstance();
			} else {
				t = clazz.newInstance();
			}
			cacheBeanMap.put(cacheKey, t);
			return t;
		} catch (Exception e) {
			throw new RuntimeException("获取实例异常");
		}
	}
}
