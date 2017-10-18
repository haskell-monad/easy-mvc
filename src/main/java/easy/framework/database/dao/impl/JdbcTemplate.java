package easy.framework.database.dao.impl;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
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
	public int insert(String sql, Object... obj) {
		try {
			return queryRunner.update(sql, obj);
		} catch (SQLException e) {
			throw new RuntimeException("插入数据异常", e);
		}
	}
	public int insertBatch(String sql, Object[][]... obj) {
		try {
			return queryRunner.update(sql, obj);
		} catch (SQLException e) {
			throw new RuntimeException("插入数据异常", e);
		}
	}
	public int update(String sql) {
		try {
			return queryRunner.update(sql);
		} catch (SQLException e) {
			throw new RuntimeException("更新数据异常", e);
		}
	}
	public int update(String sql, Object... obj) {
		try {
			return queryRunner.update(sql, obj);
		} catch (SQLException e) {
			throw new RuntimeException("更新数据异常", e);
		}
	}
	public int delete(String sql) {
		return update(sql);
	}
	public int delete(String sql, Object... obj) {
		return update(sql, obj);
	}
	public <T> T select(String sql, ResultSetHandler<T> rsh) {
		try {
			return queryRunner.query(sql, rsh);
		} catch (SQLException e) {
			throw new RuntimeException("查询数据异常", e);
		}
	}
	public <T> T select(String sql, ResultSetHandler<T> rsh, Object... obj) {
		try {
			return queryRunner.query(sql, rsh, obj);
		} catch (SQLException e) {
			throw new RuntimeException("查询数据异常", e);
		}
	}
}
