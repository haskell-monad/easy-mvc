package easy.framework.orm.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import easy.framework.core.ClassHelper;
import easy.framework.mvc.model.FileModel;
import easy.framework.orm.annotation.Entity;
import easy.framework.orm.annotation.FieldIgnore;
import easy.framework.orm.annotation.Table;

/**
 * @author limengyu
 * @create 2017/10/17
 */
public class OrmHelper {
	/**
	 * <实体类,表名称>
	 */
	private final static Map<Class<?>, String> ENTITY_TABLE_MAP = new HashMap<>();
	/**
	 * <实体类,<字段名,表字段名>>
	 */
	private final static Map<Class<?>, Map<String, String>> ENTITY_FIELD_MAP = new HashMap<>();
	static {
		Set<Class<?>> entityClassList = ClassHelper.findClassByAnnotation(Entity.class);
		Iterator<Class<?>> iterator = entityClassList.iterator();
		while (iterator.hasNext()) {
			Class<?> entityClass = iterator.next();
			initEntity2Table(entityClass);
			initEntity2TableField(entityClass);
		}
	}

	private static void initEntity2Table(Class<?> entityClass) {
		String tableName;
		if (entityClass.isAnnotationPresent(Table.class)) {
			tableName = entityClass.getAnnotation(Table.class).name();
		} else {
			tableName = builderLowercaseTableName(entityClass.getSimpleName());
		}
		ENTITY_TABLE_MAP.put(entityClass, tableName);
	}
	private static void initEntity2TableField(Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return;
		}
		Map<String, String> map = new HashMap<>(16);
		for (Field field : fields) {
			if (field.getModifiers() != Modifier.PRIVATE) {
				continue;
			} else if (field.isAnnotationPresent(FieldIgnore.class)) {
				continue;
			}
			String tableFieldName;
			if (field.isAnnotationPresent(easy.framework.orm.annotation.Field.class)) {
				tableFieldName = field.getAnnotation(easy.framework.orm.annotation.Field.class).name();
			} else {
				tableFieldName = builderUpperTableFieldName(field.getName());
			}
			map.put(field.getName(), tableFieldName);
		}
		if (map.size() > 0) {
			ENTITY_FIELD_MAP.put(entityClass, map);
		}
	}
	/**
	 * 将实体类名称转换成小写数据库表名称 如：UserRole -> user_role
	 * @param tableName
	 * @return
	 */
	private static String builderLowercaseTableName(String tableName) {
		tableName = tableName.replaceAll("_", "");
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
	/**
	 * 将实体类字段名转换成大写的数据库字段名 如：userName -> USER_NAME,age -> AGE, firstUserName -> FIRST_USER_NAME
	 * @param classFieldName
	 * @return
	 */
	private static String builderUpperTableFieldName(String classFieldName) {
		StringBuffer subBuffer = new StringBuffer();
		for (int i = 0; i < classFieldName.length(); i++) {
			if (Character.isUpperCase(classFieldName.charAt(i))) {
				subBuffer.append("_");
				subBuffer.append(classFieldName.charAt(i));
			} else {
				subBuffer.append(Character.toUpperCase(classFieldName.charAt(i)));
			}
		}
		return subBuffer.toString();
	}
	public static Map<Class<?>, String> getEntityTableMap() {
		return ENTITY_TABLE_MAP;
	}
	public static Map<Class<?>, Map<String, String>> getEntityFieldMap() {
		return ENTITY_FIELD_MAP;
	}
	/**
	 * 获取实体类对应的数据库表名称
	 * @param clazz
	 * @return
	 */
	public static String getEntityTableName(Class<?> clazz) {
		String tableName = ENTITY_TABLE_MAP.get(clazz);
		if (StringUtils.isBlank(tableName)) {
			if (!clazz.isAnnotationPresent(Entity.class)) {
				throw new RuntimeException("获取数据库表名异常,实体类[" + clazz.getName() + "]缺少@Entity注解");
			}
			throw new RuntimeException("没有获取到数据库表名[" + clazz.getName() + "]");
		}
		return tableName;
	}
	/**
	 * 获取实体类字段与数据库表字段的映射关系
	 * @param clazz
	 * @return
	 */
	public static Map<String, String> getEntityTableFieldMap(Class<?> clazz) {
		Map<String, String> map = ENTITY_FIELD_MAP.get(clazz);
		if (map == null) {
			return new HashMap<>(1);
		}
		return map;
	}

	/**
	 * 获取实体类字段与数据库表字段的映射关系
	 * @param clazz
	 * @return
	 */
	public static String getEntityTableFieldName(Class<?> clazz,String fieldName) {
		Map<String, String> map = ENTITY_FIELD_MAP.get(clazz);
		if (map == null) {
			return fieldName;
		}
		return StringUtils.isBlank(map.get(fieldName)) ? fieldName : map.get(fieldName);
	}

	public static void main(String[] args) {
		initEntity2TableField(FileModel.class);
	}
}
