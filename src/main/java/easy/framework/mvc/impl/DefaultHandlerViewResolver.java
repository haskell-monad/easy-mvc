package easy.framework.mvc.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import easy.framework.utils.ServletUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.helper.ConfigHelper;
import easy.framework.mvc.HandlerViewResolver;
import easy.framework.mvc.common.Constant;
import easy.framework.mvc.model.Result;
import easy.framework.mvc.model.View;
import easy.framework.utils.JsonUtils;

/**
 * @author limengyu
 * @create 2017/09/19
 */
public class DefaultHandlerViewResolver implements HandlerViewResolver {
	private static final Logger logger = LoggerFactory.getLogger(DefaultHandlerViewResolver.class);
	private static final String REDIRECT_PREFIX = "/";

	@Override
	public void resolver(HttpServletRequest request, HttpServletResponse response, Object resultObj){
		if (resultObj == null) {
			logger.debug("resultObj为空,没有resolver");
			return;
		}
		if (resultObj instanceof View) {
			// redirect/jsp
			View view = (View) resultObj;
			if (view.getPath().startsWith(REDIRECT_PREFIX)) {
				ServletUtils.redirect(view.getPath(), request, response);
			} else {
				if (view.getParams() != null && view.getParams().size() > 0) {
					view.getParams().forEach((key, value) -> request.setAttribute(key, value));
				}
				String jsp = ConfigHelper.getJspDir().endsWith("/") ? ConfigHelper.getJspDir() + view.getPath() : ConfigHelper.getJspDir() + "/" + view.getPath();
				if (!jsp.endsWith(Constant.JSP_FILE_SUFFIX)) {
					jsp += Constant.JSP_FILE_SUFFIX;
				}
				ServletUtils.forward(jsp, request, response);
			}
		} else if (resultObj instanceof Result) {
			// json/upload
			Result result = (Result) resultObj;
			boolean multipart = ServletFileUpload.isMultipartContent(request);
			if (multipart) {

			} else {
				ServletUtils.sendJson(response, result);
			}
		}
	}
}
