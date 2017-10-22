package easy.framework.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author limengyu
 * @create 2017/10/22
 */
public interface HandlerExceptionResolver {

    void resolveHandlerException(HttpServletRequest request,HttpServletResponse response,Exception e);
}
