package easy.framework.aop.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.aop.Proxy;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Created by limengyu on 2017/9/26.
 */
public class ProxyChain {
	private static final Logger logger = LoggerFactory.getLogger(ProxyChain.class);
	private Object targetObj;
	private Object[] args;
	private List<Proxy> aspectClassList;
	private MethodProxy methodProxy;
	private int chainIndex;
	private int chainCount;

	public ProxyChain(Object targetObj, Object[] args, List<Proxy> aspectClassList, MethodProxy methodProxy) {
		this.targetObj = targetObj;
		this.args = args;
		this.aspectClassList = aspectClassList;
		this.methodProxy = methodProxy;
		this.chainIndex = 0;
		this.chainCount = aspectClassList.size();
	}
	private int nextAspectChain() {
		chainIndex += 1;
		return chainIndex;
	}
	public Object doChain() {
		Object result;
		int nextIndex = nextAspectChain();
		if (nextIndex <= chainCount) {
//			logger.debug("一共有{}个ProxyChain.开始执行第{}个Chain", chainCount, nextIndex);
			result = aspectClassList.get(nextIndex - 1).doProxy(this);
		} else {
			try {
				result = methodProxy.invokeSuper(targetObj, args);
			} catch (Throwable e) {
				throw new RuntimeException("方法调用异常", e);
			}
		}
		return result;
	}
}
