package easy.framework.mvc;

import easy.framework.mvc.model.RequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by limengyu on 2017/9/18.
 */
public interface HandlerInvoke {

    public void invoke(HttpServletRequest request,HttpServletResponse response,RequestHandler requestHandler);
}
