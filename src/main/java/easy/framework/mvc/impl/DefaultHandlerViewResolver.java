package easy.framework.mvc.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import easy.framework.mvc.common.Constant;
import easy.framework.utils.JsonUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.helper.ConfigHelper;
import easy.framework.mvc.HandlerViewResolver;
import easy.framework.mvc.model.Result;
import easy.framework.mvc.model.View;

/**
 * @author limengyu
 * @create 2017/09/19
 */
public class DefaultHandlerViewResolver implements HandlerViewResolver {
	private static final Logger logger = LoggerFactory.getLogger(DefaultHandlerViewResolver.class);

	private static final String REDIRECT_PREFIX = "/";

	@Override
	public void resolver(HttpServletRequest request, HttpServletResponse response, Object resultObj) {
		if (resultObj == null) {
			logger.debug("resultObj为空,没有resolver");
			return;
		}
		if (resultObj instanceof View) {
			// redirect/jsp
			View view = (View) resultObj;
			if (view.getPath().startsWith(REDIRECT_PREFIX)) {
				this.redirect(view.getPath(), request, response);
			} else {
				if (view.getParams() != null && view.getParams().size() > 0) {
					view.getParams().forEach((key, value) -> request.setAttribute(key, value));
				}
				String jsp = ConfigHelper.getJspDir().endsWith("/") ? ConfigHelper.getJspDir() + view.getPath() : ConfigHelper.getJspDir() + "/" + view.getPath();
				if(!jsp.endsWith(Constant.JSP_FILE_SUFFIX)){
					jsp += Constant.JSP_FILE_SUFFIX;
				}
				this.forward(jsp, request, response);
			}
		} else if (resultObj instanceof Result) {
			// json/upload
			Result result = (Result) resultObj;
			boolean multipart = ServletFileUpload.isMultipartContent(request);
			if(multipart){

			}else{
				this.sendJson(response,result);
			}
		}
	}
	private void redirect(String viewPath, HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.debug("redirect: {}", request.getContextPath() + viewPath);
			response.sendRedirect(request.getContextPath() + viewPath);
		} catch (Exception e) {
			throw new RuntimeException("重定向异常",e);
		}
	}
	private void forward(String viewPath, HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher(viewPath).forward(request, response);
		} catch (Exception e) {
			throw new RuntimeException("转发异常",e);
		}
	}
	private void sendJson(HttpServletResponse response,Result result){

		response.setContentType("application/json");
		response.setCharacterEncoding(Constant.DEFAULT_ENCODE);
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(JsonUtils.toJson(result));
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException("响应数据发生异常",e);
		}finally {
			writer.close();
		}

	}
}
