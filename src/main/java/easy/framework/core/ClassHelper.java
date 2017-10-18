package easy.framework.core;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.helper.ConfigHelper;

/**
 * @author limengyu
 * @create 2017/9/13
 */
public class ClassHelper {
	private static final Logger logger = LoggerFactory.getLogger(ClassHelper.class);
	public static final Set<Class<?>> ALL_CLASS;
	static {
		logger.debug("=========开始加载class文件==========");
		ALL_CLASS = ClassLoaderHelper.scanAllClassByPackageName(ConfigHelper.getAppBasePackage());
	}

	/**
	 * 获取所有类实例
	 * @return
	 */
	public static Set<Class<?>> getAllClass() {
		return ALL_CLASS;
	}
	/**
	 * 根据注解获取相关类实例
	 * @param annotation
	 * @return
	 */
	public static Set<Class<?>> findClassByAnnotation(Class<? extends Annotation> annotation) {
		Set<Class<?>> result = ALL_CLASS.stream().filter(clazz -> clazz.isAnnotationPresent(annotation)).collect(Collectors.toSet());
		return result;
	}
	/**
	 * 根据父类或者接口获取相关类实例
	 * @param superClazz
	 * @return
	 */
	public static Set<Class<?>> findClassBySuperClass(Class<?> superClazz) {
		// isAssignableFrom 用来判断一个类superClazz和另一个类clazz是否相同或是另一个类的超类或接口
		Set<Class<?>> result = ALL_CLASS.stream().filter(clazz -> superClazz.isAssignableFrom(clazz) && !superClazz.equals(clazz)).collect(Collectors.toSet());
		return result;
	}
	public static void main(String[] args) {
		ClassHelper.getAllClass();
	}
}
