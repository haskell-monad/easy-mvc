package easy.framework.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import easy.framework.annotation.Controller;
import easy.framework.mvc.annotation.Action;
import easy.framework.mvc.common.RequestMethod;
import easy.framework.mvc.model.RequestHandler;
import easy.framework.mvc.model.RequestModel;
import easy.framework.core.ClassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by limengyu on 2017/9/13.
 */
public class ControllerHelper {
	private static final Logger logger = LoggerFactory.getLogger(ControllerHelper.class);
	private static Map<RequestModel, RequestHandler> handlerMap = new HashMap<>();
	static {
		Set<Class<?>> classSet = ClassHelper.findClassByAnnotation(Controller.class);
		Iterator<Class<?>> iterator = classSet.iterator();
		while (iterator.hasNext()) {
			Class<?> controllerClass = iterator.next();
			Method[] methods = controllerClass.getDeclaredMethods();
			String rootPath = "";
			if(controllerClass.isAnnotationPresent(Action.class)){
				String[] paths = controllerClass.getAnnotation(Action.class).value();
				rootPath = (paths == null || paths.length < 1) ? rootPath : paths[0];
			}
			for (Method method : methods) {
				if (!method.isAnnotationPresent(Action.class)) {
					continue;
				}
				Action annotation = method.getAnnotation(Action.class);
				String[] paths = annotation.value();
				RequestMethod[] requestMethods = annotation.method();
				if (paths == null || paths.length == 0 || requestMethods == null || requestMethods.length == 0) {
					throw new RuntimeException(controllerClass.getName() + "." + method.getName() + "路由配置异常");
				}
				builderHandler(rootPath,paths, requestMethods, controllerClass, method);
			}
		}
	}

	private static void builderHandler(String rootPath,String[] paths, RequestMethod[] requestMethods, Class<?> controllerClass, Method method) {
		for (String path : paths) {
			for (RequestMethod requestMethod : requestMethods) {
				RequestModel requestModel = new RequestModel();
				logger.debug("request-path: {}",rootPath+"/"+path);
				requestModel.setPath(rootPath+"/"+path);
				requestModel.setMethod(requestMethod);
				RequestHandler controllerModel = new RequestHandler();
				controllerModel.setControllerClass(controllerClass);
				controllerModel.setMethod(method);
				handlerMap.put(requestModel, controllerModel);
			}
		}
	}
	public static Map<RequestModel, RequestHandler> getHandlerMap() {
		return handlerMap;
	}
	public static RequestHandler getRequestHandler(RequestModel requestModel) {
		return handlerMap.get(requestModel) == null ? null : handlerMap.get(requestModel);
	}
}
