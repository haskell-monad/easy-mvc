package easy.framework.orm.helper;

import easy.framework.orm.Condition;
import easy.framework.test.User;

import java.util.Date;
import java.util.List;

/**
 * @author limengyu
 * @create 2017/10/19
 */
public class SqlHelper {
	public static <T> Condition builder(Class<T> clazz) {
		return new Condition(clazz);
	}

	public static void main(String[] args) {
		//select * from user where createDate = ? and userName like '%xx%'
		List<User> userList = SqlHelper.builder(User.class)
				.and(Condition.notEquals("userName","Giggs"))
				.and(Condition.multiAnd(
								Condition.equals("gender","1"),
								Condition.before("createDate",new Date()),
								Condition.in("age","10","11","12","13"))
				)
				.asc("createDate").desc("age")
				.groupBy("userPhone")
				.field("userName", "userPhone", "gender", "age", "createDate")
				.selectList();
	}
}
