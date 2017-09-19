package easy.framework.mvc;

import easy.framework.mvc.model.RequestHandler;

/**
 * Created by limengyu on 2017/9/18.
 */
public interface HandlerMapping {
	public RequestHandler getRequestHandler(String requestPath, String requestMethod);
}
