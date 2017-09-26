package easy.framework.ioc;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.annotation.Autowired;
import easy.framework.annotation.Impl;
import easy.framework.annotation.Inject;
import easy.framework.core.ClassHelper;

/**
 * Created by limengyu on 2017/9/13.
 */
public class IocHelper {
	private static final Logger logger = LoggerFactory.getLogger(IocHelper.class);
	static {
		Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
		if (beanMap != null && beanMap.size() > 0) {
			Iterator<Class<?>> iterator = beanMap.keySet().iterator();
			while (iterator.hasNext()) {
				Class<?> clazz = iterator.next();
				Object mainInstance = beanMap.get(clazz);
				Field[] fields = clazz.getDeclaredFields();
				if (fields == null || fields.length == 0) {
					continue;
				}
				for (Field field : fields) {
					if (field.isAnnotationPresent(Inject.class) || field.isAnnotationPresent(Autowired.class)) {
						Class<?> implClass;
						if (field.isAnnotationPresent(Impl.class)) {
							implClass = field.getAnnotation(Impl.class).value();
						} else {
							Set<Class<?>> superClassList = ClassHelper.findClassBySuperClass(field.getType());
							if (superClassList == null || superClassList.size() == 0) {
								throw new RuntimeException("没有获取可用的实现类[" + field.getType() + "]");
							}
							implClass = superClassList.iterator().next();
						}
						Object subInstance = BeanHelper.getBeanInstance(implClass);
						try {
							field.setAccessible(true);
							field.set(mainInstance, subInstance);
						} catch (IllegalAccessException e) {
							throw new RuntimeException("注入失败");
						}
					}
				}
			}
		}
	}
}
