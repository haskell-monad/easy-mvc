package easy.framework.aop;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.aop.model.ProxyChain;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author limengyu
 * @create 2017/9/26
 */
public class ProxyManager {
	private static final Logger logger = LoggerFactory.getLogger(ProxyManager.class);

	public static <T> T createProxy(Class<?> targetClass, List<Proxy> aspectClassList) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetClass);
		enhancer.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(Object targetObj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
				return new ProxyChain(targetObj,method, args, aspectClassList, methodProxy).doChain();
			}
		});
		T proxyClass = (T) enhancer.create();
		return proxyClass;
	}
}
