package easy.framework.plugin;

/**
 * @author limengyu
 * @create 2017/10/23
 */
public interface Plugin {
	/**
	 * 初始化插件
	 */
	void init();
	/**
	 * 销毁插件
	 */
	void destroy();

	/**
	 * 插件名称
	 * @return
	 */
	String getName();
}
