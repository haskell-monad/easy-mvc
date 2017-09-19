package easy.framework.mvc.model;

import java.lang.reflect.Method;

/**
 * Created by limengyu on 2017/9/18.
 */
public class RequestHandler {

    private Class<?> controllerClass;
    private Method method;

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
