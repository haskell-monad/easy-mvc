package easy.framework.helper;

import easy.framework.core.*;
import easy.framework.ioc.BeanHelper;
import easy.framework.ioc.IocHelper;
import easy.framework.mvc.ControllerHelper;

/**
 * Created by limengyu on 2017/9/18.
 */
public class HelperLoader {
	public static void init() {
		Class[] helper = { ClassHelper.class, BeanHelper.class, IocHelper.class, ControllerHelper.class };
	    for (Class clazz : helper){
            ClassLoaderHelper.loadClass(clazz);
        }
    }
}
