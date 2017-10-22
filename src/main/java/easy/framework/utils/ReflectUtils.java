package easy.framework.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * @author limengyu
 * @create 2017/9/20
 */
public class ReflectUtils {
	private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

	public enum DATA_TYPE {
		BYTE("byte"), SHORT("short"), INT("int"), LONG("long"), DOUBLE("double"), FLOAT("float"), BOOLEAN("boolean");
		private String code;

		DATA_TYPE(String code) {
			this.code = code;
		}
		public String getCode() {
			return code;
		}
	}

	/**
	 * 获取方法参数
	 * @param method
	 * @return
	 */
	public static List<String> findMethodParamName(Method method) {
		List<String> paramNameList = new ArrayList<>();
		Class clazz = method.getDeclaringClass();
		String methodName = method.getName();
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(clazz));
		CtClass cc;
		CtMethod cm;
		CtClass[] parameterTypes;
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
			return paramNameList;
		}
		String[] paramNames = new String[parameterTypes.length];
		String[] signatures = new String[parameterTypes.length];
		int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for (int i = 0; i < paramNames.length; i++) {
			paramNames[i] = attr.variableName(i + pos);
			signatures[i] = attr.signature(i + pos);
		}
		for (int i = 0; i < paramNames.length; i++) {
			paramNameList.add(paramNames[i]);
		}
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
			if (DATA_TYPE.BYTE.getCode().equals(paramType.getName())) {
				return Integer.parseInt(param);
			} else if (DATA_TYPE.SHORT.getCode().equals(paramType.getName())) {
				return Short.parseShort(param);
			} else if (DATA_TYPE.INT.getCode().equals(paramType.getName())) {
				return Integer.parseInt(param);
			} else if (DATA_TYPE.LONG.getCode().equals(paramType.getName())) {
				return Long.parseLong(param);
			} else if (DATA_TYPE.DOUBLE.getCode().equals(paramType.getName())) {
				return Double.parseDouble(param);
			} else if (DATA_TYPE.FLOAT.getCode().equals(paramType.getName())) {
				return Float.parseFloat(param);
			} else if (DATA_TYPE.BOOLEAN.getCode().equals(paramType.getName())) {
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
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			logger.error("创建bean实例失败: {}", clazz.getSimpleName());
			throw new RuntimeException("创建bean实例失败", e);
		}
	}

	/**
	 * 动态生成查询类
	 * @param clazz
	 */
	public static void generateQueryClass(Class<?> clazz){

	}
}
