package easy.framework.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author limengyu
 * @create 2017/09/18
 */
public interface HandlerViewResolver {
	/**
	 * 处理请求响应
	 * @param request
	 * @param response
	 * @param resultObj
	 */
	void resolver(HttpServletRequest request, HttpServletResponse response, Object resultObj);
}
