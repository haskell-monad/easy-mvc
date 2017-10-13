package easy.framework.aop.helper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.aop.Proxy;
import easy.framework.aop.ProxyManager;
import easy.framework.aop.annotation.Aspect;
import easy.framework.core.ClassHelper;
import easy.framework.ioc.BeanHelper;
import easy.framework.transaction.annotation.Transaction;
import easy.framework.transaction.aspect.TransactionProxy;
import easy.framework.utils.ReflectUtils;

/**
 * Created by limengyu on 2017/9/26.
 */
public class AopHelper {
	private static final Logger logger = LoggerFactory.getLogger(AopHelper.class);
	private static final Map<Class<?>, List<Proxy>> targetClassProxy = new HashMap<>();
	static {
		findAbstractProxy();
		findTransactionProxy();
		loadTargetClassProxy();
	}

	private static void findAbstractProxy() {
		Set<Class<?>> targetAspectClassList = ClassHelper.findClassByAnnotation(Aspect.class);
		if (targetAspectClassList != null && targetAspectClassList.size() > 0) {
			for (Class<?> targetClass : targetAspectClassList) {
				Class<? extends Proxy>[] aspectList = targetClass.getAnnotation(Aspect.class).value();
				List<Proxy> aspectClassList = new ArrayList<>();
				for (Class<? extends Proxy> aspectClass : aspectList) {
					aspectClassList.add(ReflectUtils.newInstance(aspectClass));
				}
				targetClassProxy.put(targetClass, aspectClassList);
			}
		}
	}
	private static void findTransactionProxy() {
		Set<Class<?>> allClass = ClassHelper.getAllClass();
		for (Class<?> targetClass : allClass) {
			Method[] methods = targetClass.getMethods();
			if (methods == null || methods.length == 0) {
				continue;
			}
			for (Method method : methods) {
				if (method.isAnnotationPresent(Transaction.class)) {
					List<Proxy> targetClassProxyList;
					if (targetClassProxy.containsKey(targetClass)) {
						targetClassProxyList = targetClassProxy.get(targetClass);
					} else {
						targetClassProxyList = new ArrayList<>();
					}
					targetClassProxyList.add(ReflectUtils.newInstance(TransactionProxy.class));
					targetClassProxy.put(targetClass, targetClassProxyList);
				}
			}
		}
	}
	private static void loadTargetClassProxy() {
		if (targetClassProxy != null && targetClassProxy.size() > 0) {
			targetClassProxy.forEach((targetClass, aspectClassList) -> {
				Object proxyClass = ProxyManager.createProxy(targetClass, aspectClassList);
				BeanHelper.putBeanInstance(targetClass, proxyClass);
			});
		}
	}
}
