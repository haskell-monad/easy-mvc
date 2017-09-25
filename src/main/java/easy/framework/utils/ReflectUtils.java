package easy.framework.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import easy.framework.mvc.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * Created by limengyu on 2017/9/20.
 */
public class ReflectUtils {
	private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

	public static List<String> findMethodParamName(Method method){
		List<String> paramNameList = new ArrayList<>();
		Class clazz = method.getDeclaringClass();
		String methodName = method.getName();
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(clazz));
        CtClass cc = null;
        CtMethod cm = null;
        CtClass[] parameterTypes = null;
        try {
            cc = pool.get(clazz.getName());
            cm = cc.getDeclaredMethod(methodName);
            parameterTypes = cm.getParameterTypes();
        } catch (NotFoundException e) {
            return paramNameList;
        }
		MethodInfo methodInfo = cm.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
		if (attr == null) {
			logger.debug("method [{}] no params", method.getName());
			return paramNameList;
		}
		String[] paramNames = new String[parameterTypes.length];
		String[] signatures = new String[parameterTypes.length];
		int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for (int i = 0; i < paramNames.length; i++) {
			paramNames[i] = attr.variableName(i + pos);
			signatures[i] = attr.signature(i + pos);
			logger.debug("signatures: {}", signatures[i]);
		}
		for (int i = 0; i < paramNames.length; i++) {
			paramNameList.add(paramNames[i]);
		}
		logger.debug("method [{}] params list: {}", method.getName(), paramNameList);
		return paramNameList;
	}

	/**
	 * 转换参数类型
	 * @param paramType
	 * @param paramValue
	 * @return Object
	 */
	public static Object convertValue(Class<?> paramType, Object paramValue) {
		if (paramValue == null) {
			return paramValue;
		}
		if (paramType.isInstance(paramValue)) {
			return paramValue;
		}
		String param = null;
		if (paramValue instanceof String) {
			param = (String) paramValue;
		}
		if (paramType.isPrimitive()) {
			if (paramType.getName().equals("byte")) {
				return Integer.parseInt(param);
			} else if (paramType.getName().equals("short")) {
				return Short.parseShort(param);
			} else if (paramType.getName().equals("int")) {
				return Integer.parseInt(param);
			} else if (paramType.getName().equals("long")) {
				return Long.parseLong(param);
			} else if (paramType.getName().equals("double")) {
				return Double.parseDouble(param);
			} else if (paramType.getName().equals("float")) {
				return Float.parseFloat(param);
			} else if (paramType.getName().equals("bool")) {
				return Boolean.parseBoolean(param);
			} else {
				logger.error("paramValue: {},paramType: {}", paramValue, paramType);
				throw new RuntimeException("类型转换异常");
			}
		} else {
			if (paramType.isAssignableFrom(Byte.class)) {
				return Byte.valueOf(param);
			} else if (paramType.isAssignableFrom(Short.class)) {
				return Short.valueOf(param);
			} else if (paramType.isAssignableFrom(Integer.class)) {
				return Integer.valueOf(param);
			} else if (paramType.isAssignableFrom(Long.class)) {
				return Long.valueOf(param);
			} else if (paramType.isAssignableFrom(Float.class)) {
				return Float.valueOf(param);
			} else if (paramType.isAssignableFrom(Double.class)) {
				return Double.valueOf(param);
			} else if (paramType.isAssignableFrom(Boolean.class)) {
				return Boolean.valueOf(param);
			} else {
				return paramType.cast(paramValue);
			}
		}
	}
}
