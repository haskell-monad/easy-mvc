import easy.framework.database.helper.DatabaseHelper;
import easy.framework.orm.Condition;
import easy.framework.orm.helper.SqlHelper;
import easy.framework.test.User;
import easy.framework.transaction.annotation.Transaction;
import easy.framework.utils.DateUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * @author limengyu
 * @create 2017/10/23
 */
public class OrmTest {
	@Test
	public void testSelect() {
        List<User> userList = SqlHelper.builder(User.class)
                .and(Condition.notEquals("userName", "Giggs"))
                .and(Condition.multiAnd(
                                Condition.equals("gender", "1"),
                                Condition.before("createDate", new Date()),
                                Condition.in("age", "10", "11", "12", "13"))
                )
                .asc("createDate").desc("age")
                .groupBy("userPhone")
                .field("userName", "userPhone", "gender", "age", "createDate")
                .selectList();
	}

    @Test
    public void testInsert(){
        SqlHelper.builder(User.class)
                .field("userName","userPhone","userStatus","department","createDate")
                .values("limengyu","15603827777","1","技术部", DateUtils.formatDate(new Date()))
                .insertTable();
    }

    @Test
    public void testInsertGenerateKey(){
        int i = SqlHelper.builder(easy.framework.test.Test.class)
                .field("name")
                .values("limengyu")
                .insertTable();
        System.out.println("i:==="+i);
    }

    @Test
    public void testUpdate(){
        SqlHelper.builder(User.class)
                .set("userName", "mengyu.li")
                .set("department", "app组")
                .and(Condition.equals("userPhone", "15603827777"))
                .updateTable();
        SqlHelper.builder(User.class)
                .and(Condition.equals("userPhone", "15603827777"))
                .select();
    }

    @Test
    public void testDelete(){
        SqlHelper.builder(User.class)
                .and(Condition.equals("userPhone", "15603827777"))
                .deleteTable();
    }

}
