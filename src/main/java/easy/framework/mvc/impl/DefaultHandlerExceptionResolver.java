package easy.framework.mvc.impl;

import easy.framework.mvc.HandlerExceptionResolver;
import easy.framework.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author limengyu
 * @create 2017/10/22
 */
public class DefaultHandlerExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(DefaultHandlerExceptionResolver.class);

    @Override
    public void resolveHandlerException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        Throwable cause = e.getCause();
        if(cause == null){
           logger.error("resolveHandlerException: {}",e);
           return;
        }else{
            ServletUtils.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,cause.getMessage(),response);
        }
    }
}
