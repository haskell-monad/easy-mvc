package easy.framework.mvc.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.annotation.Controller;
import easy.framework.core.ClassHelper;
import easy.framework.mvc.annotation.Action;
import easy.framework.mvc.common.Constant;
import easy.framework.mvc.common.RequestMethod;
import easy.framework.mvc.model.RequestHandler;
import easy.framework.mvc.model.RequestModel;

/**
 * @author limengyu
 * @create 2017/09/13
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
			if (controllerClass.isAnnotationPresent(Action.class)) {
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
				builderHandler(rootPath, paths, requestMethods, controllerClass, method);
			}
		}
	}

	private static void builderHandler(String rootPath, String[] paths, RequestMethod[] requestMethods, Class<?> controllerClass, Method method) {
		for (String path : paths) {
			for (RequestMethod requestMethod : requestMethods) {
				RequestHandler controllerModel = new RequestHandler();
				controllerModel.setControllerClass(controllerClass);
				controllerModel.setMethod(method);
				RequestModel requestModel = new RequestModel();
				requestModel.setPath(rootPath + "/" + path);
				requestModel.setMethod(requestMethod.name());
				if (requestModel.isRegex()) {
					String pattern = requestModel.getPath().replaceAll(Constant.PATH_PARAM_TAG, Constant.PATH_PARAM_REGEX);
					controllerModel.setPattern(Pattern.compile(pattern));
					controllerModel.setPathParams(requestModel.pathParams());
				}
//				logger.debug("请求映射: {}\t{}\t{}", requestModel.getMethod(), requestModel.getPath(), controllerModel.getPattern() == null ? null : controllerModel.getPattern().pattern())
				handlerMap.put(requestModel, controllerModel);
			}
		}
	}
	public static Map<RequestModel, RequestHandler> getHandlerMap() {
		return handlerMap;
	}
	public static RequestHandler getRequestHandler(RequestModel requestModel) {
		Set<Map.Entry<RequestModel, RequestHandler>> entries = handlerMap.entrySet();
		for (Map.Entry<RequestModel, RequestHandler> entry : entries) {
			RequestModel model = entry.getKey();
			RequestHandler handler = entry.getValue();
			if (!model.getMethod().equalsIgnoreCase(requestModel.getMethod())) {
				continue;
			}
			if (isMatchMethodPath(model.getPath(), requestModel.getPath()) || (isMatchMethodRegex(handler, model.isRegex(), requestModel.getPath()))) {
				return handler;
			}
		}
		return null;
	}
	/**
	 * 请求地址是否匹配方法的路由地址
	 * @param methodPath
	 * @param requestPath
	 * @return
	 */
	private static boolean isMatchMethodPath(String methodPath, String requestPath) {
		return methodPath.equals(requestPath);
	}
	/**
	 * 方法是正则路由 并且 正则匹配请求地址
	 * @param handler
	 * @param isRegex
	 * @param requestPath
	 * @return
	 */
	private static boolean isMatchMethodRegex(RequestHandler handler, boolean isRegex, String requestPath) {
		return isRegex && handler.match(requestPath);
	}
}
