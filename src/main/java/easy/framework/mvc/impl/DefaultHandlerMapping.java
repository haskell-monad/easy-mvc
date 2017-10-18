package easy.framework.mvc.impl;

import easy.framework.mvc.helper.ControllerHelper;
import easy.framework.mvc.HandlerMapping;
import easy.framework.mvc.model.RequestHandler;
import easy.framework.mvc.model.RequestModel;

/**
 * @author limengyu
 * @create 2017/09/19
 */
public class DefaultHandlerMapping implements HandlerMapping {

    @Override
    public RequestHandler getRequestHandler(String requestPath, String requestMethod) {
        RequestModel model = new RequestModel();
        model.setPath(requestPath);
        model.setMethod(requestMethod);
        return ControllerHelper.getRequestHandler(model);
    }
}
