package easy.framework.aop;

import easy.framework.aop.model.ProxyChain;

/**
 * @author limengyu
 * @create 2017/9/26
 */
public interface Proxy {
	/**
	 * 执行代理
	 * @param proxyChain
	 * @return
	 */
	public Object doProxy(ProxyChain proxyChain);
}
