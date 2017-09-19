package easy.framework.mvc.dispatcher;

import easy.framework.common.PropertyConfigConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Created by limengyu on 2017/9/18.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		// req.setCharacterEncoding(PropertyConfigConstant.DEFAULT_ENCODE);
		logger.debug("============req=========" + req);
	}
}
