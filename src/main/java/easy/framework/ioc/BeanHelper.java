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
 * @author limengyu
 * @create 2017/09/14
 */
public class BeanHelper {
	private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();
	static {
		Set<Class<?>> allClass = ClassHelper.getAllClass();
		for (Class<?> clazz : allClass) {
			if (clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Inject.class) || clazz.isAnnotationPresent(Autowired.class)) {
				BEAN_MAP.put(clazz, ReflectUtils.newInstance(clazz));
			}
		}
	}

	public static Map<Class<?>, Object> getBeanMap() {
		return BEAN_MAP;
	}
	public static <T> T getBeanInstance(Class<T> clazz) {
		if (!BEAN_MAP.containsKey(clazz)) {
			throw new RuntimeException("Bean[{"+clazz.getName()+"}]不存在");
		}
		return (T) BEAN_MAP.get(clazz);
	}
	public static void putBeanInstance(Class<?> beanClass) {
		BEAN_MAP.put(beanClass, ReflectUtils.newInstance(beanClass));
	}
	public static void putBeanInstance(Class<?> beanClass, Object beanInstance) {
		BEAN_MAP.put(beanClass, beanInstance);
	}
}
