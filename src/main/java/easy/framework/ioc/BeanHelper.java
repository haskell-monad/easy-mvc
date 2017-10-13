package easy.framework.ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import easy.framework.annotation.Autowired;
import easy.framework.annotation.Controller;
import easy.framework.annotation.Inject;
import easy.framework.annotation.Service;
import easy.framework.core.ClassHelper;
import easy.framework.utils.ReflectUtils;

/**
 * Created by limengyu on 2017/9/14.
 */
public class BeanHelper {
	private static final Map<Class<?>, Object> beanMap = new HashMap<>();
	static {
		Set<Class<?>> allClass = ClassHelper.getAllClass();
		for (Class<?> clazz : allClass) {
			if (clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Inject.class) || clazz.isAnnotationPresent(Autowired.class)) {
				beanMap.put(clazz, ReflectUtils.newInstance(clazz));
			}
		}
	}

	public static Map<Class<?>, Object> getBeanMap() {
		return beanMap;
	}
	public static <T> T getBeanInstance(Class<T> clazz) {
		if (!beanMap.containsKey(clazz)) {
			throw new RuntimeException("实例化Bean异常");
		}
		return (T) beanMap.get(clazz);
	}
	public static void putBeanInstance(Class<?> beanClass) {
		beanMap.put(beanClass, ReflectUtils.newInstance(beanClass));
	}
	public static void putBeanInstance(Class<?> beanClass, Object beanInstance) {
		// logger.debug("putBeanInstance: {},{}",beanClass,beanInstance);
		beanMap.put(beanClass, beanInstance);
	}
}
