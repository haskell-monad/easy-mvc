package easy.framework.core;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.common.PropertyConfigConstant;
import easy.framework.utils.FileUtils;

/**
 * @author limengyu
 */
public class ClassLoaderHelper {
	private static final Logger logger = LoggerFactory.getLogger(ClassLoaderHelper.class);

	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	public static Class<?> loadClass(String name) {
		return loadClass(name, true);
	}
	public static Class<?> loadClass(Class<?> clazz) {
		return loadClass(clazz.getName(), true);
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
	public static Set<Class<?>> scanAllClassByPackageName(String basePackageName) {
		Set<Class<?>> cacheClass = new HashSet<>();
		Enumeration<URL> resources;
		try {
			resources = getClassLoader().getResources(basePackageName.replaceAll(".", FileUtils.SYSTEM_SEPARATOR));
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				// [/D:/myapp/tomcat8/webapps/demo/WEB-INF/classes/]
				// [file:/D:/myapp/tomcat8/webapps/demo/WEB-INF/lib/easy-mvc-1.0-SNAPSHOT.jar!/]
				if (FileUtils.FILE.equals(url.getProtocol())) {
					loadClassFile(cacheClass, "", url.getPath());
				} else if (FileUtils.JAR.equals(url.getProtocol())) {
					JarURLConnection connection = (JarURLConnection) url.openConnection();
					Enumeration<JarEntry> entries = connection.getJarFile().entries();
					while (entries.hasMoreElements()) {
						String jarName = entries.nextElement().getName();
						if (jarName.endsWith(FileUtils.CLASS_FILE_SUFFIX)) {
							String baseName = FileUtils.getBaseName(jarName);
							if (baseName.startsWith(PropertyConfigConstant.FRAMEWORK_BASE_PACKAGE) || baseName.startsWith(basePackageName)) {
								cacheClass.add(loadClass(baseName, false));
							}
						}
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("扫描包失败[" + basePackageName + "]");
		}
		return cacheClass;
	}
	private static void loadClassFile(Set<Class<?>> cacheClass, String packageName, String path) {
		File[] files = new File(path).listFiles(pathname -> pathname.isDirectory() || pathname.getName().endsWith(FileUtils.CLASS_FILE_SUFFIX));
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (file.isDirectory()) {
					loadClassFile(cacheClass, FileUtils.builderFullPackageName(packageName, file.getName()), file.getAbsolutePath());
				} else if (file.isFile()) {
					cacheClass.add(loadClass(FileUtils.builderFullClassName(packageName, file.getName()), false));
				}
			}
		}
	}
}
