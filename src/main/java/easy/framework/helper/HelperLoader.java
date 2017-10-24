package easy.framework.helper;

import easy.framework.aop.helper.AopHelper;
import easy.framework.core.ClassHelper;
import easy.framework.core.ClassLoaderHelper;
import easy.framework.database.helper.DatabaseHelper;
import easy.framework.ioc.BeanHelper;
import easy.framework.ioc.IocHelper;
import easy.framework.mvc.helper.ControllerHelper;
import easy.framework.orm.helper.OrmHelper;
import easy.framework.plugin.PluginHelper;

/**
 * @author limengyu
 * @create 2017/09/18
 */
public class HelperLoader {
    public static void init() {
        Class[] helper = {
                ClassHelper.class,
                DatabaseHelper.class,
                OrmHelper.class,
                BeanHelper.class,
                ControllerHelper.class,
                AopHelper.class,
                IocHelper.class,
                PluginHelper.class};
        for (Class clazz : helper) {
            ClassLoaderHelper.loadClass(clazz);
        }
    }
}
