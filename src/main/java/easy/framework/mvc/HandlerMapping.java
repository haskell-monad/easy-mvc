package easy.framework.mvc;

import easy.framework.mvc.model.RequestHandler;

/**
 * @author limengyu
 * @create 2017/09/18
 */
public interface HandlerMapping {
	/**
	 * 获取请求对应的handler
	 * @param requestPath
	 * @param requestMethod
	 * @return
	 */
	RequestHandler getRequestHandler(String requestPath, String requestMethod);
}
