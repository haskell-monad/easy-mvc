package easy.framework.aop.helper;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.aop.Proxy;
import easy.framework.aop.ProxyManager;
import easy.framework.aop.annotation.Aspect;
import easy.framework.core.ClassHelper;
import easy.framework.ioc.BeanHelper;

/**
 * Created by limengyu on 2017/9/26.
 */
public class AopHelper {
	private static final Logger logger = LoggerFactory.getLogger(AopHelper.class);
	static {
		Set<Class<?>> targetAspectClassList = ClassHelper.findClassByAnnotation(Aspect.class);
		if (targetAspectClassList != null && targetAspectClassList.size() > 0) {
			for (Class<?> targetClass : targetAspectClassList) {
				Class<? extends Proxy>[] aspectList = targetClass.getAnnotation(Aspect.class).value();
				List<Proxy> aspectClassList = new ArrayList<>();
				for (Class<? extends Proxy> aspectClass : aspectList) {
					try {
						aspectClassList.add(aspectClass.newInstance());
					} catch (Exception e) {
						throw new RuntimeException("创建切面类实例异常",e);
					}
				}
				Object proxyClass = ProxyManager.createProxy(targetClass, aspectClassList);
				BeanHelper.putBeanInstance(targetClass,proxyClass);
			}
		}
	}
}
