package easy.framework.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by limengyu on 2017/9/20.
 */
public class ServletUtils {

    /**
     * 获取请求的url，取消contextPath
     * @param request
     * @return
     */
    public static String requestPath(HttpServletRequest request){
        String requestPath = request.getRequestURI();
        if(StringUtils.isNotBlank(request.getContextPath())){
            requestPath = requestPath.substring(request.getContextPath().length(),requestPath.length());
        }
        return requestPath;
    }
}
