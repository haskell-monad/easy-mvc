package easy.framework.mvc.dispatcher;

import easy.framework.InstanceFactory;
import easy.framework.common.PropertyConfigConstant;
import easy.framework.mvc.HandlerInvoke;
import easy.framework.mvc.HandlerMapping;
import easy.framework.mvc.model.RequestHandler;
import easy.framework.mvc.model.RequestModel;
import easy.framework.utils.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		String requestPath = ServletUtils.requestPath(request);
		logger.debug("requestPath: {}",requestPath);
		logger.debug("uri: {}", request.getRequestURI());
		logger.debug("contextPath: {}",request.getContextPath());
		logger.debug("method: {}",request.getMethod());
		logger.debug("queryString: {}",request.getQueryString());

		HandlerMapping handlerMapping = InstanceFactory.getHandlerMapping();
		HandlerInvoke handlerInvoke = InstanceFactory.getHandlerInvoke();

		RequestHandler requestHandler = handlerMapping.getRequestHandler(requestPath, request.getMethod());
		handlerInvoke.invoke(request,response,requestHandler);

	}
}
