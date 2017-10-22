package easy.framework.database.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
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
	public int insert(String sql, Object... obj) {
		try {
			return queryRunner.update(sql, obj);
		} catch (SQLException e) {
			logger.error("插入数据异常", e);
			throw new RuntimeException("插入数据异常");
		}
	}
	@Override
	public int insertBatch(String sql, Object[][]... obj) {
		try {
			return queryRunner.update(sql, obj);
		} catch (SQLException e) {
			logger.error("批量插入数据异常", e);
			throw new RuntimeException("批量插入数据异常");
		}
	}
	@Override
	public int update(String sql, Object... obj) {
		try {
			return queryRunner.update(sql, obj);
		} catch (SQLException e) {
			logger.error("更新数据异常", e);
			throw new RuntimeException("更新数据异常");
		}
	}
	@Override
	public int delete(String sql, Object... obj) {
		return update(sql, obj);
	}
	@Override
	public <T> T select(String sql, Class<T> clazz, Object... obj) {
		try {
			return queryRunner.query(sql, new BeanHandler<>(clazz, new BasicRowProcessor(new GenerousBeanProcessor())), obj);
		} catch (SQLException e) {
			logger.error("查询数据异常", e);
			throw new RuntimeException("查询数据异常");
		}
	}
	@Override
	public <T> List<T> selectList(String sql, Class<T> clazz, Object... obj) {
		try {
			return queryRunner.query(sql, new BeanListHandler<>(clazz, new BasicRowProcessor(new GenerousBeanProcessor())), obj);
		} catch (SQLException e) {
			logger.error("查询数据异常", e);
			throw new RuntimeException("查询数据异常");
		}
	}
}
