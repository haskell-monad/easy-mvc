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

import easy.framework.helper.ConfigHelper;
import easy.framework.mvc.HandlerExceptionResolver;
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
		if(Constant.ROOT_PATH.equals(requestPath)){
			ServletUtils.redirect(ConfigHelper.getHomePage(),request,response);
			return;
		}
		HandlerMapping handlerMapping = InstanceFactory.getHandlerMapping();
		HandlerInvoke handlerInvoke = InstanceFactory.getHandlerInvoke();
		HandlerExceptionResolver handlerExceptionResolver = InstanceFactory.getHandlerExceptionResolver();
		try {
			RequestHandler requestHandler = handlerMapping.getRequestHandler(requestPath, request.getMethod());
			if (requestHandler == null) {
				ServletUtils.sendError(HttpServletResponse.SC_NOT_FOUND,"资源映射不存在",response);
				return;
			}
			handlerInvoke.invoke(request, response, requestHandler);
		}catch (Exception e){
			handlerExceptionResolver.resolveHandlerException(request,response,e);
		}finally {

		}

	}
}
