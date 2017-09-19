package easy.framework.mvc;

import easy.framework.helper.ConfigHelper;
import easy.framework.helper.HelperLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

/**
 * Created by limengyu on 2017/9/18.
 */
@WebListener
public class ContainerListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(ContainerListener.class);
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.debug("初始化ContainerListener...");
		ServletContext servletContext = servletContextEvent.getServletContext();
		HelperLoader.init();
        addServletMapping(servletContext);
	}
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
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
			registration.addMapping(staticDir + "/*");
		}
	}
	private void registerJspServlet(ServletContext servletContext) {
		ServletRegistration registration = servletContext.getServletRegistration("jsp");
		registration.addMapping("/index.jsp");
		String jspDir = ConfigHelper.getJspDir();
		if (StringUtils.isNotBlank(jspDir)) {
			registration.addMapping(jspDir + "/*");
		}
	}
}
