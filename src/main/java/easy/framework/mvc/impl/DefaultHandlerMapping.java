package easy.framework.mvc.impl;

import easy.framework.mvc.ControllerHelper;
import easy.framework.mvc.HandlerMapping;
import easy.framework.mvc.model.RequestHandler;
import easy.framework.mvc.model.RequestModel;

/**
 * Created by limengyu on 2017/9/19.
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
