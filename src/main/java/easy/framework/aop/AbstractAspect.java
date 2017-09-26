package easy.framework.aop;

import easy.framework.aop.model.ProxyChain;

/**
 * Created by limengyu on 2017/9/26.
 */
public abstract class AbstractAspect implements Proxy {
	@Override
	public final Object doProxy(ProxyChain proxyChain) {
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
		} catch (Exception e) {
			throw new RuntimeException("aop方法调用异常", e);
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
	private void begin() {
	}
	protected void end() {
	}
}
