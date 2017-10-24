package easy.framework.plugin;

import java.util.List;

import easy.framework.aop.Proxy;

/**
 * @author limengyu
 * @create 2017/10/23
 */
public abstract class AbstractPluginProxy implements Proxy {
	/**
	 * 获取代理类对应的目标类列表
	 * @return
	 */
	public abstract List<Class<?>> getTargetClassList();
}
