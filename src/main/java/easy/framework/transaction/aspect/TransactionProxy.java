package easy.framework.transaction.aspect;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easy.framework.aop.Proxy;
import easy.framework.aop.model.ProxyChain;
import easy.framework.database.helper.DatabaseHelper;
import easy.framework.transaction.annotation.Transaction;

/**
 * @author limengyu
 * @create 2017/10/12
 */
public class TransactionProxy implements Proxy {
	private static final Logger logger = LoggerFactory.getLogger(TransactionProxy.class);
	private static final ThreadLocal<Boolean> TRANSACTION_START_FLAG = ThreadLocal.withInitial(() -> Boolean.FALSE);

	@Override
	public Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result = null;
		Method method = proxyChain.getMethod();
		if (method.isAnnotationPresent(Transaction.class) && Boolean.FALSE.equals(TRANSACTION_START_FLAG.get())) {
			// 启用了事务
			TRANSACTION_START_FLAG.set(Boolean.TRUE);
			try {
				DatabaseHelper.beginTransaction();
				logger.info("[easy-mvc]启动事务");
				result = proxyChain.doChain();
				DatabaseHelper.commitTransaction();
				logger.info("[easy-mvc]提交事务");
			} catch (Throwable e) {
				DatabaseHelper.rollbackTransaction();
				logger.info("[easy-mvc]回滚事务");
				throw e;
			} finally {
				TRANSACTION_START_FLAG.remove();
			}
		} else {
			result = proxyChain.doChain();
		}
		return result;
	}
}
