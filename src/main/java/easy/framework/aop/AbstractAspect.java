package easy.framework.aop;

import easy.framework.aop.model.ProxyChain;

/**
 * @author limengyu
 * @create 2017/9/26
 */
public abstract class AbstractAspect implements Proxy {
	@Override
	public final Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result;
		begin();
		try {
			if (intercept()) {
				before();
				result = proxyChain.doChain();
				after();
			} else {
				result = proxyChain.doChain();
			}
		} catch (Throwable e) {
			throw e;
		} finally {
			end();
		}
		return result;
	}
	protected boolean intercept() {
		return true;
	}
	protected void before() {
	}
	protected void after() {
	}
	protected void begin() {
	}
	protected void end() {
	}
}
