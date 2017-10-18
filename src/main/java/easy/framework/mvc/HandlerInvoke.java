package easy.framework.mvc;

import easy.framework.mvc.model.RequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author limengyu
 * @create 2017/09/18
 */
public interface HandlerInvoke {

    /**
     * 执行方法
     * @param request
     * @param response
     * @param requestHandler
     */
    void invoke(HttpServletRequest request,HttpServletResponse response,RequestHandler requestHandler);
}
