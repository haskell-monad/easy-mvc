package easy.framework.helper;

import easy.framework.aop.helper.AopHelper;
import easy.framework.core.ClassHelper;
import easy.framework.core.ClassLoaderHelper;
import easy.framework.ioc.BeanHelper;
import easy.framework.ioc.IocHelper;
import easy.framework.mvc.helper.ControllerHelper;

/**
 * Created by limengyu on 2017/9/18.
 */
public class HelperLoader {
    public static void init() {
        Class[] helper = {ClassHelper.class,BeanHelper.class,ControllerHelper.class,AopHelper.class,IocHelper.class};
        for (Class clazz : helper) {
            ClassLoaderHelper.loadClass(clazz);
        }
    }
}
