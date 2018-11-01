package com.huayin.common.persist.jpa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;

import com.huayin.common.exception.DataOperationException;
import com.huayin.common.persist.PersistProvider;
import com.huayin.common.persist.jdbc.SQLUtil;
import com.huayin.common.persist.jdbc.SQLUtil.DriverName;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Restriction;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.SqlResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

/**
 * <pre>
 * 基于JPA的DAO代理接口实现
 * 使用说明:
 * 在CIS组件中增加common-jpa-definition.xml的导入.
 * 在claspath路径中定义META-INF/persistence.xml, 具体参照JPA规范.
 * 调用示例:
 * <code>
 * PersistProvider provider = ComponentContextLoader.getBean(PersistProvider.CONTEXT_BEAN_NAME_PERSISTPROVIDER, PersistProvider.class);
 * provider.saveEntity(Object);
 * ...
 * </code>
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2007-8-24
 */
public class JpaPersistProvider implements PersistProvider
{
	private DriverName driverName;

	private EntityManager entityManager;

	public JpaPersistProvider(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	@Override
	public Long countByDynamicQuery(DynamicQuery query)
	{
		SqlResult result = query.getSqlResult();
		Query jpaquery = null;
		if (query.getQueryType() == QueryType.JDBC)
		{
			jpaquery = getEntityManager().createNativeQuery(result.getSql());
		}
		else
		{
			jpaquery = getEntityManager().createQuery(result.getSql());
		}
		int index = 1;
		for (Object object : result.getParamsList())
		{
			if (query.getQueryType() == QueryType.JDBC && object instanceof Enum)
			{
				object = object.toString();
			}
			jpaquery.setParameter(index++, object);
		}
		Object count = jpaquery.getSingleResult();
		if (count == null)
		{
			return 0L;
		}
		else
		{
			return Long.valueOf(count.toString());
		}
	}

	@Override
	public long countEntityByNamedQuery(String name, Object... parameters)
	{
		Query query = getEntityManager().createNamedQuery(name);
		int index = 1;
		for (Object object : parameters)
		{
			query.setParameter(index++, object);
		}
		Object count = query.getSingleResult();
		return count == null ? 0l : (Long) count;
	}

	@Override
	public void deleteAllEntity(Collection<?> entitys) throws DataOperationException
	{
		for (Iterator<?> iterator = entitys.iterator(); iterator.hasNext();)
		{
			Object object = (Object) iterator.next();

			Object o = getEntityManager().merge(object);
			getEntityManager().remove(o);
		}
	}

	@Override
	public void deleteEntity(Object entity) throws DataOperationException
	{
		getEntityManager().remove(getEntityManager().merge(entity));
	}

	@Override
	public int[] exeBatchNamedQuery(String name, List<List<Object>> param)
	{
		int[] results = new int[param.size()];
		Query query = getEntityManager().createNamedQuery(name);
		int row = 0;
		for (List<Object> list : param)
		{
			int index = 1;
			for (Object object : list)
			{
				query.setParameter(index++, object);
			}
			results[row++] = query.executeUpdate();
			if ((row % 3000) == 0)
			{
				getEntityManager().flush();
			}
		}
		return results;
	}

	@Override
	public int[] exeBatchNativeSql(String[] qls) throws DataOperationException
	{
		int[] returns = new int[qls.length];
		for (int i = 0; i < qls.length; i++)
		{
			Query query = getEntityManager().createQuery(qls[i]);
			returns[i] = query.executeUpdate();
		}
		return returns;
	}

	@Override
	public int exeNamedQuery(String name, Object... parameters)
	{
		Query query = getEntityManager().createNamedQuery(name);
		int index = 1;
		for (Object object : parameters)
		{
			query.setParameter(index++, object);
		}
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> exeNativeSqlNotType(String sql, Object... values) throws DataOperationException
	{
		Query query = getEntityManager().createNativeQuery(sql);
		int index = 1;
		for (Object object : values)
		{
			if (object instanceof Enum)
			{
				query.setParameter(index++, ((Enum<?>) object).name());
			}
			else
			{
				query.setParameter(index++, object);
			}
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public <T> SearchResult<T> findAllEntityByDynamicQuery(Class<T> type, DynamicQuery query)
	{
		return (SearchResult<T>) findByDynamicQuery(query, type);
	}

	public SearchResult<?> findAllEntityByDynamicQuery(DynamicQuery query)
	{
		return findByDynamicQuery(query, null);
	}

	@SuppressWarnings("unchecked")
	private SearchResult<?> findByDynamicQuery(DynamicQuery query, Class<?> clz)
	{
		SqlResult result = query.getSqlResult();
		Query inquery = null;
		Query searchCountQuery = null;
		if (query.getQueryType() == QueryType.JDBC)
		{
			if (clz != null)
			{
				if(clz.getAnnotation(Entity.class)!=null)
				{//表示是持久层映射类
					inquery = getEntityManager().createNativeQuery(result.getSql(), clz);
				}else
				{
					inquery = getEntityManager().createNativeQuery(result.getSql());
					if(clz.isAssignableFrom(Map.class))
					{
						inquery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP); 
					}else
					{
						inquery.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(clz));  
					}
				}
			}
			else
			{
				inquery = getEntityManager().createNativeQuery(result.getSql());
			}
			if (query.getIsSearchTotalCount())
			{
				searchCountQuery = getEntityManager().createNativeQuery(result.getRecordCountSql());
			}
		}
		else
		{
			inquery = getEntityManager().createQuery(result.getSql());
			if (query.getIsSearchTotalCount())
			{
				searchCountQuery = getEntityManager().createQuery(result.getRecordCountSql());
			}
		}
		Integer pageIndex = result.getPageIndex();
		Integer pageSize = result.getPageSize();
		if (pageIndex != null && pageIndex > 0)
		{
			inquery.setFirstResult((pageIndex - 1) * pageSize);
		}
		if (pageSize != null && pageSize > 0)
		{
			inquery.setMaxResults(pageSize);
		}
		int index = 1;
		for (Object object : result.getParamsList())
		{
			if (query.getQueryType() == QueryType.JDBC && object instanceof Enum)
			{
				object = object.toString();
			}
			inquery.setParameter(index++, object);
		}
		SearchResult<Object> sresult = new SearchResult<Object>();
		List<Object> resltList = inquery.getResultList();
		sresult.setResult(resltList);
		if (searchCountQuery != null)
		{
			index = 1;
			for (Object object : result.getRecordParamsList())
			{
				if (query.getQueryType() == QueryType.JDBC && object instanceof Enum)
				{
					object = object.toString();
				}
				searchCountQuery.setParameter(index++, object);
			}
			Integer count = Integer.valueOf(searchCountQuery.getSingleResult().toString());
			sresult.setCount(count);
		}
		return sresult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAllEntityByNamedSql(Class<T> type, String name, int pageSize, int pageNum,
			Object... parameters) throws DataOperationException
	{
		Query query = getEntityManager().createNamedQuery(name);
		int index = 1;
		for (Object object : parameters)
		{
			query.setParameter(index++, object);
		}
		query.setFirstResult((pageNum - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAllEntityByNamedSql(Class<T> type, String name, Object... parameters)
			throws DataOperationException
	{
		Query query = getEntityManager().createNamedQuery(name);
		int index = 1;
		for (Object object : parameters)
		{
			query.setParameter(index++, object);
		}
		return query.getResultList();
	}

	public DriverName getDriverName()
	{
		if (this.driverName == null)
		{
			final StringBuffer buffer = new StringBuffer();
			Object delegate = getEntityManager().getDelegate();
			if (delegate instanceof Session)
			{
				Session tempSession = null;
				try
				{
					tempSession = ((Session) delegate).getSessionFactory().openSession();
					tempSession.doWork(new Work()
					{
						@Override
						public void execute(Connection connection) throws SQLException
						{
							buffer.append(connection.getMetaData().getDatabaseProductName());
						}
					});
				}
				finally
				{
					if (tempSession != null)
					{
						tempSession.close();
					}
				}
			}
			this.driverName = DriverName.parseDriverName(buffer.toString());
		}
		return this.driverName;
	}

	@Override
	public <T> T getEntity(Class<T> type, Object id) throws DataOperationException
	{
		return getEntityManager().find(type, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getEntityByNamedQuery(Class<T> type, String name, Object... parameters)
	{
		Query query = getEntityManager().createQuery(name);
		int index = 1;
		for (Object object : parameters)
		{
			query.setParameter(index++, object);
		}
		return (T) query.getSingleResult();
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}

	@Override
	public <T> T loadEntity(Class<T> type, Object id) throws DataOperationException
	{
		T object = getEntityManager().find(type, id);
		if (object != null)
		{
			return object;
		}
		else
		{
			throw new DataOperationException(type.getName() + "未找到记录{" + id + "}.");
		}
	}

	@Override
	public <T> T lockByEntity(T entity) throws DataOperationException
	{
		if (entity != null)
		{
			getEntityManager().lock(entity, LockModeType.PESSIMISTIC_READ);
			getEntityManager().refresh(entity);
			return entity;
		}
		else
		{
			return null;
		}
	}

	@Override
	public <T> T lockEntity(Class<T> type, Object id) throws DataOperationException
	{
		T entity = getEntityManager().find(type, id, LockModeType.PESSIMISTIC_READ);
		if (entity != null)
			getEntityManager().refresh(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findAllEntityByDynamicQuery(Class<T> clz, DynamicQuery query, LockType lockType)
	{
		if (lockType == null)
		{
			return ((SearchResult<T>) findByDynamicQuery(query, clz)).getResult();
		}
		else
		{
			query.setQueryType(QueryType.JDBC);
			if (query.getPageIndex() > 0)
			{
				throw new DataOperationException("加锁查询不支持分页操作");
			}
			SqlResult result = null;
			String sql = "";
			if (query.getPageSize() > 0)
			{
				// 暂时只支持oracle
				if (getDriverName() == DriverName.Oracle)
				{
					Restriction restriction = Restrictions.le("rownum", query.getPageSize());
					query.add(restriction);
					result = query.getSqlResult();
					sql = result.getSql();
					query.remove(restriction);
				}
				else if(getDriverName() == DriverName.MySQL)
				{
					result = query.getSqlResult();
					sql = result.getSql();
					sql = sql + " limit " + query.getPageSize() + " ";
				}
				else if(getDriverName() == DriverName.HSQL)
				{
					result = query.getSqlResult();
					sql = result.getSql();
					SQLUtil.getLimitString(getDriverName(), sql, query.getPageSize(), 1);
				}
				else
				{
					throw new DataOperationException("加锁查询不支持分页操作");
				}
			}
			else
			{
				result = query.getSqlResult();
				sql = result.getSql();
			}
			if (lockType == LockType.LOCK_PASS)
			{
				if (getDriverName() == DriverName.MySQL)
				{
					sql = sql + " for update";
				}
				else if (getDriverName() == DriverName.Oracle)
				{
					sql = sql + " for update skip locked";
				}
				else if (getDriverName() == DriverName.SQLServer)
				{
					String newStr = sql.toLowerCase();
					int indexOf = newStr.indexOf("where");
					sql = sql.substring(0, indexOf) + " WITH(UPDLOCK, ROWLOCK, READPAST) "
							+ sql.substring(indexOf, sql.length());
				}
			}
			else if (lockType == LockType.LOCK_WAIT)
			{
				if (getDriverName() == DriverName.MySQL || getDriverName() == DriverName.Oracle)
				{
					sql = sql + " for update";
				}
				else if (getDriverName() == DriverName.SQLServer)
				{
					String newStr = sql.toLowerCase();
					int indexOf = newStr.indexOf("where");
					sql = sql.substring(0, indexOf) + " WITH(UPDLOCK, ROWLOCK) " + sql.substring(indexOf, sql.length());
				}
			}
			else
			{
				throw new DataOperationException("不支持的加锁类型{" + lockType + "}");
			}
			Query inquery = getEntityManager().createNativeQuery(sql, clz);
			int index = 1;
			for (Object object : result.getParamsList())
			{
				if (query.getQueryType() == QueryType.JDBC && object instanceof Enum)
				{
					object = object.toString();
				}
				inquery.setParameter(index++, object);
			}
			List<T> list = inquery.getResultList();
			// if (list != null)
			// {
			// for (T t : list)
			// {
			// getEntityManager().refresh(t);
			// }
			// }
			return list;
		}
	}

	@Override
	public void saveAllEntity(Collection<?> entitys) throws DataOperationException
	{
		for (Object entity : entitys)
		{
			getEntityManager().persist(entity);
		}
	}

	@Override
	public <T> T saveEntity(T entity) throws DataOperationException
	{
		getEntityManager().persist(entity);
		return entity;
	}

	@Override
	public void saveOrUpdateAllEntity(Collection<?> entitys) throws DataOperationException
	{
		for (Object entity : entitys)
		{
			getEntityManager().merge(entity);
		}
	}

	@Override
	public void saveOrUpdateEntity(Object entity) throws DataOperationException
	{
		EntityManager entityManager2 = getEntityManager();
		if (entityManager2.contains(entity))
		{
			entityManager2.merge(entity);
		}
		else
		{
			entityManager2.persist(entity);
		}
	}

	@Override
	public void updateAllEntity(Collection<?> entitys) throws DataOperationException
	{
		for (Object entity : entitys)
		{
			getEntityManager().merge(entity);
		}
	}

	@Override
	public <T> T updateEntity(T entity) throws DataOperationException
	{
		return getEntityManager().merge(entity);
	}
}
