package easy.framework.database.dao.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import easy.framework.common.FrameworkConfigConstant;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.annotation.Service;
import easy.framework.database.dao.DataAccessor;
import easy.framework.database.helper.DatabaseHelper;

/**
 * @author limengyu
 * @create 2017/10/12
 */
@Service
public class JdbcTemplate implements DataAccessor {
	private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);
	private QueryRunner queryRunner;
	private DataSource dataSource;

	public JdbcTemplate() {
		this.dataSource = DatabaseHelper.getDataSource();
		this.queryRunner = new QueryRunner(dataSource);
	}

	@Override
	public int selectCount(String sql, Object... params) {
		try {
			return ((BigInteger)queryRunner.query(sql, new ScalarHandler(1), params)).intValue();
		} catch (SQLException e) {
			logger.error("查询数据异常", e);
			throw new RuntimeException("查询数据异常");
		}
	}

	@Override
	public <T> T select(String sql, Class<T> clazz, Object... params) {
		try {
			return queryRunner.query(sql, new BeanHandler<>(clazz, new BasicRowProcessor(new GenerousBeanProcessor())), params);
		} catch (SQLException e) {
			logger.error("查询数据异常", e);
			throw new RuntimeException("查询数据异常");
		}
	}
	@Override
	public <T> List<T> selectList(String sql, Class<T> clazz, Object... params) {
		try {
			return queryRunner.query(sql, new BeanListHandler<>(clazz, new BasicRowProcessor(new GenerousBeanProcessor())), params);
		} catch (SQLException e) {
			logger.error("查询数据异常", e);
			throw new RuntimeException("查询数据异常");
		}
	}

	@Override
	public int insertGeneratedKeys(String generateKey, String sql, Object... params) {
		try {
			if(StringUtils.isBlank(generateKey)){
				generateKey = FrameworkConfigConstant.GENERATE_KEY_NAME;
			}
			int n;
			Connection connection = DatabaseHelper.getConnection();
			if(connection != null){
				n = ((BigInteger) queryRunner.insert(connection,sql, new ScalarHandler(generateKey), params)).intValue();
			}else{
				n = ((BigInteger) queryRunner.insert(sql, new ScalarHandler(generateKey), params)).intValue();
			}
			return n;
		} catch (SQLException e) {
			logger.error("插入数据异常", e);
			throw new RuntimeException("插入数据异常");
		}
	}

	@Override
	public int update(String sql,Object... params) {
		try {
			int update;
			Connection connection = DatabaseHelper.getConnection();
			if(connection != null){
				update = queryRunner.update(sql, connection, params);
			}else{
				update = queryRunner.update(sql, params);
			}
			return update;
		} catch (SQLException e) {
			logger.error("更新数据异常", e);
			throw new RuntimeException("更新数据异常");
		}
	}
}
