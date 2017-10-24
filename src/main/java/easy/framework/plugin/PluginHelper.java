package easy.framework.plugin;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import easy.framework.core.ClassHelper;
import easy.framework.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author limengyu
 * @create 2017/10/23
 */
public class PluginHelper {
	private static final Logger logger = LoggerFactory.getLogger(PluginHelper.class);
	private static final List<Plugin> PLUGIN_LIST = new ArrayList<>();
	static {
		Set<Class<?>> pluginClass = ClassHelper.findClassBySuperClass(Plugin.class);
		pluginClass.forEach(clazz -> {
			Plugin plugin = (Plugin) ReflectUtils.newInstance(clazz);
			logger.debug("[easy-mvc-plugin]初始化插件>>>[{}]",plugin.getName());
			plugin.init();
			PLUGIN_LIST.add(plugin);
		});
	}

	public static List<Plugin> getPluginList() {
		return PLUGIN_LIST;
	}
}
