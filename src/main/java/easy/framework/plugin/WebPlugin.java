package easy.framework.plugin;

import javax.servlet.ServletContext;

/**
 * @author limengyu
 * @create 2017/10/24
 */
public abstract class WebPlugin implements Plugin{

    /**
     * 注册servlet/过滤器/监听器等
     * @param servletContext
     */
    public abstract void register(ServletContext servletContext);
}
