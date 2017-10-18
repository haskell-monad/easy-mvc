package easy.framework.mvc.dispatcher;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import easy.framework.mvc.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.InstanceFactory;
import easy.framework.mvc.HandlerInvoke;
import easy.framework.mvc.HandlerMapping;
import easy.framework.mvc.helper.FileUploadHelper;
import easy.framework.mvc.model.RequestHandler;
import easy.framework.utils.ServletUtils;

/**
 * @author limengyu
 * @create 2017/09/18
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		FileUploadHelper.init();
	}
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding(Constant.DEFAULT_ENCODE);
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String requestPath = ServletUtils.requestPath(request);
		HandlerMapping handlerMapping = InstanceFactory.getHandlerMapping();
		HandlerInvoke handlerInvoke = InstanceFactory.getHandlerInvoke();
		RequestHandler requestHandler = handlerMapping.getRequestHandler(requestPath, request.getMethod());
		handlerInvoke.invoke(request, response, requestHandler);
	}
}
