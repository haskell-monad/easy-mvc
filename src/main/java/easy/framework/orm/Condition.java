package easy.framework.orm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import easy.framework.database.helper.DatabaseHelper;
import org.apache.commons.lang3.StringUtils;

import easy.framework.orm.helper.OrmHelper;
import easy.framework.utils.DateUtils;
import easy.framework.utils.StringExtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author limengyu
 * @create 2017/10/19
 */
public class Condition<T> {
	private static final Logger logger = LoggerFactory.getLogger(Condition.class);
	private List<String> andList;
	private List<String> orList;
	private List<String> sortList;
	private List<String> fieldList;
	private List<String> groupList;
	private String tableName;
	private Class<T> clazz;
	private Map<String, String> fieldNameMap;

	public Condition(Class<T> clazz) {
		this.clazz = clazz;
		this.tableName = OrmHelper.getEntityTableName(clazz);
		this.fieldNameMap = OrmHelper.getEntityTableFieldMap(clazz);
		this.andList = new ArrayList<>();
		this.orList = new ArrayList<>();
		this.sortList = new ArrayList<>();
		this.fieldList = new ArrayList<>();
		this.groupList = new ArrayList<>();
	}
	private String getTableFieldName(String classFieldName) {
		return StringUtils.isBlank(fieldNameMap.get(classFieldName)) ? classFieldName : fieldNameMap.get(classFieldName);
	}
	private String builderSql(Pair pair) {
		return this.getTableFieldName(pair.getClassFieldName()) + pair.getConditionSql();
	}
	public Condition and(Pair pair) {
		andList.add(this.builderSql(pair));
		return this;
	}
	public Condition and(Pair[] pairs) {
		if (pairs != null && pairs.length > 0) {
			List<String> sqlList = new ArrayList<>();
			for (Pair pair : pairs) {
				sqlList.add(this.builderSql(pair));
			}
			String sql = StringExtUtils.prettyJoinValue("(", ")",StringExtUtils.prettyValueWithTab("and"), sqlList);
			andList.add(sql);
		}
		return this;
	}
	public Condition or(Pair pair) {
		orList.add(this.builderSql(pair));
		return this;
	}
	public Condition or(Pair[] pairs) {
		if (pairs != null && pairs.length > 0) {
			List<String> sqlList = new ArrayList<>();
			for (Pair pair : pairs) {
				sqlList.add(this.builderSql(pair));
			}
			String sql = StringExtUtils.prettyJoinValue("(", ")", StringExtUtils.prettyValueWithTab("or"), sqlList);
			orList.add(sql);
		}
		return this;
	}
	public Condition asc(String fieldName) {
		fieldName = this.getTableFieldName((fieldName));
		sortList.add(fieldName + "\tasc");
		return this;
	}
	public Condition desc(String fieldName) {
		fieldName = this.getTableFieldName(fieldName);
		sortList.add(fieldName + "\tdesc");
		return this;
	}
	public Condition groupBy(String... fieldNameList) {
		if (fieldNameList != null && fieldNameList.length > 0) {
			for (String fieldName : fieldNameList) {
				groupList.add(this.getTableFieldName(fieldName));
			}
		}
		return this;
	}
	public Condition field(String... fieldNameList) {
		if (fieldNameList != null && fieldNameList.length > 0) {
			for (String fieldName : fieldNameList) {
				fieldList.add(this.getTableFieldName(fieldName));
			}
		}
		return this;
	}
	private String builder() {
		StringBuffer sql = new StringBuffer();
		sql.append("select\t");
		if (fieldList.size() > 0) {
			sql.append(String.join(",", fieldList));
		} else {
			sql.append("*");
		}
		sql.append(StringExtUtils.prettyValueWithTab("from") + tableName);
		if (andList.size() > 0) {
			sql.append(StringExtUtils.prettyValueWithTab("where"));
			sql.append(String.join(StringExtUtils.prettyValueWithTab("and"), andList));
		}
		if (orList.size() > 0) {
			sql.append(StringExtUtils.prettyValueWithTab("or"));
			sql.append(String.join(StringExtUtils.prettyValueWithTab("or"), orList));
		}
		if (groupList.size() > 0) {
			sql.append(StringExtUtils.prettyValueWithTab("group by"));
			sql.append(String.join(",", groupList));
		}
		if (sortList.size() > 0) {
			sql.append(StringExtUtils.prettyValueWithTab("order by"));
			sql.append(String.join(",", sortList));
		}
		sql.append(";");
		logger.debug("[easy-orm:sql]: {}", sql.toString());
		return sql.toString();
	}
	public <T> T select(){
		String sql = this.builder();
		return (T) DatabaseHelper.select(sql, clazz);
	}
	public <T> List<T> selectList(){
		String sql = this.builder();
		return (List<T>) DatabaseHelper.selectList(sql, clazz);
	}

	public static <T> T[] multiAnd(T... t) {
		return t;
	}
	public static  <T> T[] multiOr(T... t) {
		return t;
	}
	public static Pair equals(String fieldName, String value) {
		return new Pair(fieldName, StringExtUtils.prettyValueWithTab("=") + StringExtUtils.prettyValueWithDoubleQuot(value));
	}
	public static Pair notEquals(String fieldName, String value) {
		return new Pair(fieldName, StringExtUtils.prettyValueWithTab("!=") + StringExtUtils.prettyValueWithDoubleQuot(value));
	}
	public static Pair like(String fieldName, String value) {
		return new Pair(fieldName, StringExtUtils.prettyValueWithTab("like") + "(" + StringExtUtils.builderLeftRightValue("%", value) + ")");
	}
	public static Pair isStartsWith(String fieldName, String value) {
		return new Pair(fieldName, StringExtUtils.prettyValueWithTab("like") + "(" + StringExtUtils.builderLeftValue("%", value) + ")");
	}
	public static Pair isEndWith(String fieldName, String value) {
		return new Pair(fieldName, StringExtUtils.prettyValueWithTab("like") + "(" + StringExtUtils.builderRightValue("%", value) + ")");
	}
	public static Pair lessThan(String fieldName, Date date) {
		return new Pair(fieldName, StringExtUtils.prettyValueWithTab("<") + StringExtUtils.prettyValueWithDoubleQuot(DateUtils.formatDate(date)));
	}
	public static Pair greaterThan(String fieldName, Date date) {
		return new Pair(fieldName, StringExtUtils.prettyValueWithTab(">") + StringExtUtils.prettyValueWithDoubleQuot(DateUtils.formatDate(date)));
	}
	public static Pair before(String fieldName, Date date) {
		return new Pair(fieldName, StringExtUtils.prettyValueWithTab("<=") + StringExtUtils.prettyValueWithDoubleQuot(DateUtils.formatDate(date)));
	}
	public static Pair after(String fieldName, Date date) {
		return new Pair(fieldName, StringExtUtils.prettyValueWithTab(">=") + StringExtUtils.prettyValueWithDoubleQuot(DateUtils.formatDate(date)));
	}
	public static Pair in(String fieldName, String... values) {
		if (values != null && values.length > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append(StringExtUtils.prettyValueWithTab("in"));
			sb.append(StringExtUtils.prettyJoinValue("(", ")", ",", values));
			return new Pair(fieldName, sb.toString());
		}
		return null;
	}
	public static Pair notIn(String fieldName, String... values) {
		if (values != null && values.length > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append(StringExtUtils.prettyValue("\t", "not in"));
			sb.append(StringExtUtils.prettyJoinValue("(", ")", ",", values));
			return new Pair(fieldName, sb.toString());
		}
		return null;
	}

	public static class Pair {
		private String classFieldName;
		private String conditionSql;

		public Pair(String classFieldName, String conditionSql) {
			this.classFieldName = classFieldName;
			this.conditionSql = conditionSql;
		}
		public String getClassFieldName() {
			return classFieldName;
		}
		public void setClassFieldName(String classFieldName) {
			this.classFieldName = classFieldName;
		}
		public String getConditionSql() {
			return conditionSql;
		}
		public void setConditionSql(String conditionSql) {
			this.conditionSql = conditionSql;
		}
	}
}
