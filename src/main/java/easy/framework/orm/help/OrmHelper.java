package easy.framework.orm.help;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import easy.framework.core.ClassHelper;
import easy.framework.mvc.model.FileModel;
import easy.framework.orm.annotation.Entity;
import easy.framework.orm.annotation.Table;

/**
 * @author limengyu
 * @create 2017/10/17
 */
public class OrmHelper {
	/**
	 * 实体类，表名称
	 */
	private final static Map<Class<?>, String> ENTITY_TABLE_MAP = new HashMap<>();
	private final static Map<Class<?>, Map<String, String>> ENTITY_FIELD_MAP = new HashMap<>();
//	static {
//		// 加载@Entity
//		Set<Class<?>> entityClassList = ClassHelper.findClassByAnnotation(Entity.class);
//		Iterator<Class<?>> iterator = entityClassList.iterator();
//		while (iterator.hasNext()) {
//			Class<?> entityClass = iterator.next();
//			initEntity2Table(entityClass);
//			initEntity2TableField(entityClass);
//		}
//	}

	private static void initEntity2Table(Class<?> entityClass) {
		String tableName = getLowercaseTableName(entityClass.getSimpleName());
		if (entityClass.isAnnotationPresent(Table.class)) {
			tableName = entityClass.getAnnotation(Table.class).name();
		}
		ENTITY_TABLE_MAP.put(entityClass, tableName);
	}
	private static void initEntity2TableField(Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
                //private=2
                System.out.println(field.getName()+"=="+field.getModifiers());
//                if (field.isEnumConstant()) {
//					continue;
//				} else if (field.isSynthetic()) {
//				}
			}
		}
//		ENTITY_FIELD_MAP.put();
	}
	/**
	 * 将实体类转换成小写数据库表名称 如：UserRole -> user_role
	 * @param tableName
	 * @return
	 */
	private static String getLowercaseTableName(String tableName) {
		tableName = tableName.replaceAll("_", "").replaceAll("-", "");
		StringBuffer subBuffer = new StringBuffer();
		for (int i = 0; i < tableName.length(); i++) {
			if (i > 0 && Character.isUpperCase(tableName.charAt(i))) {
				subBuffer.append("_");
				subBuffer.append(tableName.charAt(i));
			} else {
				subBuffer.append(tableName.charAt(i));
			}
		}
		return subBuffer.toString().toLowerCase();
	}
	public static void main(String[] args) {
//		System.out.println(getLowercaseTableName("UserComboInfo"));
        initEntity2TableField(FileModel.class);
	}
}
