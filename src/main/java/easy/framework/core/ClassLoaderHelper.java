package easy.framework.core;

import easy.framework.common.PropertyConfigConstant;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by limengyu on 2017/8/21.
 */
public class ClassLoaderHelper {
	private static final Logger logger = LoggerFactory.getLogger(ClassLoaderHelper.class);
	private static final String FILE = "file";
	private static final String JAR = "jar";

	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static Class<?> loadClass(String name){
		return loadClass(name,true);
	}

	public static Class<?> loadClass(Class<?> clazz){
		return loadClass(clazz.getName(),true);
	}

	public static Class<?> loadClass(String className, boolean init) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className, init, getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("加载类失败,没有找到类[" + className + "]");
		}
		logger.debug("成功加载类[{}]", className);
		return clazz;
	}
	public static Set<Class<?>> scanAllClassByPackageName(String packageName) {
		Set<Class<?>> cacheClass = new HashSet<>();
		Enumeration<URL> resources;
		try {
			resources = getClassLoader().getResources(packageName);
		} catch (IOException e) {
			throw new RuntimeException("扫描包失败[" + packageName + "]");
		}
		String packageNamePrefix = packageName.replaceAll("/", ".");
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			logger.debug("=========: {}",url.getProtocol());
			if (FILE.equals(url.getProtocol())) {
				loadClassFile(cacheClass, packageNamePrefix, url.getPath());
			}
		}
		return cacheClass;
	}
	private static void loadClassFile(Set<Class<?>> cacheClass, String packageNamePrefix, String path) {
		File[] files = new File(path).listFiles(pathname -> pathname.isDirectory() || pathname.getName().endsWith(".class"));
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (file.isDirectory()) {
					loadClassFile(cacheClass, packageNamePrefix + "." + file.getName(), file.getAbsolutePath());
				} else if (file.isFile()) {
					cacheClass.add(loadClass(packageNamePrefix + "." + FilenameUtils.getBaseName(file.getName()), false));
				}
			}
		}
	}
}
