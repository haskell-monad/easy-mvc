package easy.framework.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by limengyu on 2017/9/18.
 */
public interface HandlerViewResolver {
	public void resolver(HttpServletRequest request, HttpServletResponse response, Object resultObj);
}
