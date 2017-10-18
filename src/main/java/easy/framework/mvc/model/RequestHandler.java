package easy.framework.mvc.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author limengyu
 * @create 2017/09/18
 */
public class RequestHandler {
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
	private Class<?> controllerClass;
	private Method method;
	private List<String> pathParams;
	private Pattern pattern;

	public Pattern getPattern() {
		return pattern;
	}
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	public boolean match(String subPath) {
		return pattern != null && pattern.matcher(subPath).matches();
	}
    public List<String> matchGroup(String subPath) {
        List<String> result = new ArrayList<>();
        if(pattern == null){
            return result;
        }
        Matcher matcher = pattern.matcher(subPath);
		int count = matcher.groupCount();
		if(matcher.matches() && count > 0){
			for (int i = 1; i <= count; i++) {
				result.add(matcher.group(i));
			}
		}
        return result;
    }
	public List<String> getPathParams() {
		return pathParams;
	}
	public void setPathParams(List<String> pathParams) {
		this.pathParams = pathParams;
	}
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
