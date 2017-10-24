package easy.framework.mvc.listener;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.helper.ConfigHelper;
import easy.framework.helper.HelperLoader;
import easy.framework.plugin.Plugin;
import easy.framework.plugin.PluginHelper;
import easy.framework.plugin.WebPlugin;

/**
 * @author limengyu
 * @create 2017/09/18
 */
@WebListener
public class ContainerListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ContainerListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.debug("[easy-mvc]初始化ContainerListener...");
        ServletContext servletContext = servletContextEvent.getServletContext();
        HelperLoader.init();
        addServletMapping(servletContext);
        registerWebPlugin(servletContext);
    }

    /**
     * 注册web插件
     *
     * @param servletContext
     */
    private void registerWebPlugin(ServletContext servletContext) {
        List<Plugin> pluginList = PluginHelper.getPluginList();
        pluginList.forEach(plugin -> {
            if (plugin instanceof WebPlugin) {
                logger.debug("[easy-mvc-plugin]注册webPlugin>>>[{}]",plugin.getName());
                ((WebPlugin) plugin).register(servletContext);
            }
        });
    }

    /**
     * 销毁插件
     */
    private void destroyPlugin() {
        logger.debug("[easy-mvc-plugin]销毁Plugin");
        List<Plugin> pluginList = PluginHelper.getPluginList();
        pluginList.forEach(Plugin::destroy);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        destroyPlugin();
    }

    private void addServletMapping(ServletContext servletContext) {
        registerDefaultServlet(servletContext);
        registerJspServlet(servletContext);
    }

    private void registerDefaultServlet(ServletContext servletContext) {
        ServletRegistration registration = servletContext.getServletRegistration("default");
        registration.addMapping("/index.html");
        registration.addMapping("/favicon.ico");
        String staticDir = ConfigHelper.getStaticDir();
        if (StringUtils.isNotBlank(staticDir)) {
            staticDir = staticDir.endsWith("/") ? staticDir + "*" : staticDir + "/*";
            registration.addMapping(staticDir);
        }
    }

    private void registerJspServlet(ServletContext servletContext) {
        ServletRegistration registration = servletContext.getServletRegistration("jsp");
        registration.addMapping("/index.jsp");
        String jspDir = ConfigHelper.getJspDir();
        if (StringUtils.isNotBlank(jspDir)) {
            jspDir = jspDir.endsWith("/") ? jspDir + "*" : jspDir + "/*";
            registration.addMapping(jspDir);
        }
    }
}
