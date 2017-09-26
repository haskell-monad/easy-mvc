package easy.framework.ioc;

import easy.framework.annotation.Autowired;
import easy.framework.annotation.Controller;
import easy.framework.annotation.Inject;
import easy.framework.annotation.Service;
import easy.framework.core.ClassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by limengyu on 2017/9/14.
 */
public class BeanHelper {
	private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);
	private static final Map<Class<?>, Object> beanMap = new HashMap<>();
	static {
		Set<Class<?>> allClass = ClassHelper.getAllClass();
		for (Class<?> clazz : allClass) {
			if (clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Inject.class) || clazz.isAnnotationPresent(Autowired.class)) {
				try {
					beanMap.put(clazz, clazz.newInstance());
				} catch (Exception e) {
					throw new RuntimeException("创建bean实例失败");
				}
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
	public static void putBeanInstance(Class<?> beanClass){
		try {
			beanMap.put(beanClass,beanClass.newInstance());
		} catch (Exception e) {
			throw new RuntimeException("创建bean实例异常",e);
		}
	}
	public static void putBeanInstance(Class<?> beanClass,Object beanInstance){
//		logger.debug("putBeanInstance: {},{}",beanClass,beanInstance);
		beanMap.put(beanClass,beanInstance);
	}
}
