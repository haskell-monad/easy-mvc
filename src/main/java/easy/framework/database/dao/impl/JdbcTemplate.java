package easy.framework.database.dao.impl;

import java.sql.SQLException;

import javax.sql.DataSource;

import easy.framework.annotation.Service;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.database.dao.DataAccessor;
import easy.framework.database.helper.DatabaseHelper;

/**
 * Created by limengyu on 2017/10/12.
 */
@Service
public class JdbcTemplate implements DataAccessor {
	private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);
	private QueryRunner queryRunner;
	private DataSource dataSource;

	private JdbcTemplate() {
		this.dataSource = DatabaseHelper.getDataSource();
		this.queryRunner = new QueryRunner(dataSource);
	}
	public <T> T insert(String sql, ResultSetHandler<T> rsh, Object... obj) throws SQLException {
		return queryRunner.insert(sql, rsh, obj);
	}
	public <T> T insertBatch(String sql, ResultSetHandler<T> rsh, Object[][]... obj) throws SQLException {
		return queryRunner.insertBatch(sql, rsh, obj);
	}
	public int update(String sql) throws SQLException {
		return queryRunner.update(sql);
	}
	public int update(String sql, Object... obj) throws SQLException {
		return queryRunner.update(sql, obj);
	}
	public int delete(String sql) throws SQLException {
		return update(sql);
	}
	public int delete(String sql, Object... obj) throws SQLException {
		return update(sql, obj);
	}
	public <T> T select(String sql, ResultSetHandler<T> rsh) throws SQLException {
		return queryRunner.query(sql, rsh);
	}
	public <T> T select(String sql, ResultSetHandler<T> rsh, Object... obj) throws SQLException {
		return queryRunner.query(sql, rsh, obj);
	}
}
