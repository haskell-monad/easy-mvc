package easy.framework.aop;

import easy.framework.aop.model.ProxyChain;

/**
 * Created by limengyu on 2017/9/26.
 */
public interface Proxy {
	public Object doProxy(ProxyChain proxyChain);
}
