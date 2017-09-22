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
}
