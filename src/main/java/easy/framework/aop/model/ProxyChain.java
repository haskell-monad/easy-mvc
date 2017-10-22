package easy.framework.aop.model;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.aop.Proxy;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author limengyu
 * @create 2017/9/26
 */
public class ProxyChain {
	private static final Logger logger = LoggerFactory.getLogger(ProxyChain.class);
	private Object targetObj;
	private Method method;
	private Object[] args;
	private List<Proxy> aspectClassList;
	private MethodProxy methodProxy;
	private int chainIndex;
	private int chainCount;

	public ProxyChain(Object targetObj, Method method, Object[] args, List<Proxy> aspectClassList, MethodProxy methodProxy) {
		this.targetObj = targetObj;
		this.method = method;
		this.args = args;
		this.aspectClassList = aspectClassList;
		this.methodProxy = methodProxy;
		this.chainIndex = 0;
		this.chainCount = aspectClassList.size();
	}
	public Object doChain() throws Throwable {
		Object result;
		int nextIndex = this.nextAspectChain();
		if (nextIndex <= chainCount) {
			result = aspectClassList.get(nextIndex - 1).doProxy(this);
		} else {
			result = methodProxy.invokeSuper(targetObj, args);
		}
		return result;
	}
	private int nextAspectChain() {
		chainIndex += 1;
		return chainIndex;
	}
	public Method getMethod() {
		return method;
	}
}
