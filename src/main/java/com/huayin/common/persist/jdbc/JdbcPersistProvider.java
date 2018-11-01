/**
 * <pre>
 * Title: 		JdbcTemplateDaoProxyImpl.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2007-6-6 下午07:31:16
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.persist.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.huayin.common.exception.DataOperationException;
import com.huayin.common.persist.PersistProvider;
import com.huayin.common.persist.jdbc.SQLUtil.DriverName;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.SqlResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
/**
 * <pre>
 * 基于Spring JdbcTemplate的DAO代理接口实现
 * 使用说明:
 * 在CIS组件中增加common-jdbc-definition.xml的导入.
 * 在claspath路径创建配置文件entity-config.xml, 定义需要映射的实体和数据表.
 * 配置文件示例:
 * &lt;Entity key="entityName"&gt;
 *	&lt;EntityConfig entityName="com.huayin.common.dao.entity.AnteGame" primaryKey="gameId" tableName="AnteGame" /&gt;
 * &lt;/Entity&gt;
 * 调用示例:
 * <code>
 * PersistProvider provider = ComponentContextLoader.getBean(PersistProvider.CONTEXT_BEAN_NAME_PERSISTPROVIDER, PersistProvider.class);
 * provider.saveEntity(Object);
 * ...
 * </code>
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-6
 */
public class JdbcPersistProvider implements PersistProvider
{
	private static final PersistConfig config = SQLUtil.getPersistConfig();

	/**
	 * 预置的最大批处理语句数量
	 */
	public static final int JDBC_BATCH_UPDATE_MAX_COUNT = 3000;

	private static final Map<String, Entity> zzz_entityMap = SQLUtil.getEntityMap(config);

	private DriverName driverName;

	private JdbcTemplate masterJdbcTemplate;

	private Map<String, String> namedSqlMap;

	/**
	 * @param masterJdbcTemplate masterJdbcTemplate
	 */
	public JdbcPersistProvider(JdbcTemplate masterJdbcTemplate)
	{
		this.masterJdbcTemplate = masterJdbcTemplate;
		namedSqlMap = SQLUtil.getNamedQueries(config, this.getDriverName());
	}

	private int[] batchUpdate(List<String> list)
	{
		// 如果需要执行的语句数量大于预置的最大批处理语句数量则拆分,否则数据库可能报内存不足错误
		if (list.size() > JDBC_BATCH_UPDATE_MAX_COUNT)
		{
			// 获取需要循环的次数
			int loopCount = list.size() / JDBC_BATCH_UPDATE_MAX_COUNT;
			int[] results = new int[list.size()];
			int i = 0;
			for (int j = 0; j < loopCount; j++)
			{
				List<String> subList = list.subList(j * JDBC_BATCH_UPDATE_MAX_COUNT, (j + 1)
						* JDBC_BATCH_UPDATE_MAX_COUNT);
				String[] sqlArray = subList.toArray(new String[subList.size()]);
				int[] subInts = getJdbcTemplate().batchUpdate(sqlArray);
				for (int k = 0; k < subInts.length; k++)
				{
					results[i] = subInts[k];
					i++;
				}
			}
			// 如果不是整除还需要取余数进行执行
			if ((list.size() % JDBC_BATCH_UPDATE_MAX_COUNT) != 0)
			{
				List<String> subList = list.subList(loopCount * JDBC_BATCH_UPDATE_MAX_COUNT, list.size());
				String[] sqlArray = subList.toArray(new String[subList.size()]);
				int[] subInts = getJdbcTemplate().batchUpdate(sqlArray);
				for (int k = 0; k < subInts.length; k++)
				{
					results[i] = subInts[k];
					i++;
				}
			}
			return results;
		}
		else
		{
			String[] array = list.toArray(new String[list.size()]);
			return getJdbcTemplate().batchUpdate(array);
		}
	}

	public Long countByDynamicQuery(DynamicQuery query)
	{
		query.setQueryType(QueryType.JDBC);
		SqlResult result = query.getSqlResult();
		try
		{
			if (result.getParamsList() != null && result.getParamsList().size() > 0)
			{
				return getJdbcTemplate().queryForObject(result.getSql(),Long.class, result.getParamsList());
			}
			else
			{
				return getJdbcTemplate().queryForObject(result.getSql(),Long.class);
			}
		}
		catch (EmptyResultDataAccessException e)
		{
			return 0L;
		}
	}

	@Override
	public long countEntityByNamedQuery(String name, Object... parameters) throws DataOperationException
	{
		try
		{
			return getJdbcTemplate().queryForObject(getNameSql(name),Long.class, parameters);
		}
		catch (EmptyResultDataAccessException e)
		{
			return 0L;
		}
	}

	@Override
	public void deleteAllEntity(Collection<?> entitys) throws DataOperationException
	{
		try
		{
			List<String> list = new ArrayList<String>();
			for (Iterator<?> iter = entitys.iterator(); iter.hasNext();)
			{
				Object element = iter.next();
				Entity dc = getEntityConfig(element.getClass());
				String sql = SQLUtil.createStatement(dc, element, SQLUtil.ENTITY_DELETE, getDriverName());
				printSQL(config, sql);
				list.add(sql);
			}
			if (list.size() > 0)
			{
				this.batchUpdate(list);
			}
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public void deleteEntity(Object entity) throws DataOperationException
	{
		try
		{
			Entity dc = getEntityConfig(entity.getClass());
			String sql = SQLUtil.createStatement(dc, entity, SQLUtil.ENTITY_DELETE, getDriverName());
			printSQL(config, sql);
			getJdbcTemplate().execute(sql);
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public int[] exeBatchNamedQuery(String name, final List<List<Object>> param)
	{
		return getJdbcTemplate().batchUpdate(getNameSql(name), new BatchPreparedStatementSetter()
		{
			@Override
			public int getBatchSize()
			{
				return param.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException
			{
				List<Object> list = param.get(index);
				int columnIndex = 1;
				for (Object list2 : list)
				{
					ps.setObject(columnIndex, list2);
					columnIndex++;
				}
			}
		});
	}

	@Override
	public int[] exeBatchNativeSql(String[] qls) throws DataOperationException
	{
		try
		{
			List<String> list = Arrays.asList(qls);
			// 如果需要执行的语句数量大于预置的最大批处理语句数量则拆分,否则数据库可能报内存不足错误
			if (list.size() > JDBC_BATCH_UPDATE_MAX_COUNT)
			{
				// 获取需要循环的次数
				int loopCount = list.size() / JDBC_BATCH_UPDATE_MAX_COUNT;
				int[] results = new int[list.size()];
				int i = 0;
				for (int j = 0; j < loopCount; j++)
				{
					List<String> subList = list.subList(j * JDBC_BATCH_UPDATE_MAX_COUNT, (j + 1)
							* JDBC_BATCH_UPDATE_MAX_COUNT);
					String[] sqlArray = subList.toArray(new String[subList.size()]);
					printSQL(config, sqlArray);
					int[] subInts = getJdbcTemplate().batchUpdate(sqlArray);
					for (int k = 0; k < subInts.length; k++)
					{
						results[i] = subInts[k];
						i++;
					}
				}
				// 如果不是整除还需要取余数进行执行
				if ((list.size() % JDBC_BATCH_UPDATE_MAX_COUNT) != 0)
				{
					List<String> subList = list.subList(loopCount * JDBC_BATCH_UPDATE_MAX_COUNT, list.size());
					String[] sqlArray = subList.toArray(new String[subList.size()]);
					printSQL(config, sqlArray);
					int[] subInts = getJdbcTemplate().batchUpdate(sqlArray);
					for (int k = 0; k < subInts.length; k++)
					{
						results[i] = subInts[k];
						i++;
					}
				}
				return results;
			}
			else
			{
				printSQL(config, qls);
				return getJdbcTemplate().batchUpdate(qls);
			}
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public int exeNamedQuery(String name, Object... parameters)
	{
		try
		{
			return getJdbcTemplate().update(getNameSql(name), parameters);
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public List<Object> exeNativeSqlNotType(String sql, Object... values) throws DataOperationException
	{
		return null;
	}

	@Override
	public <T> SearchResult<T> findAllEntityByDynamicQuery(Class<T> type, DynamicQuery query)
	{
		try
		{
			query.setQueryType(QueryType.JDBC);
			SqlResult result = query.getSqlResult();
			Entity dc = getEntityConfig(type);
			String queryString = result.getSql();
			int pageIndex = 0;
			if (result.getPageIndex() == null || result.getPageIndex() <= 0)
			{
				pageIndex = 1;
			}
			else
			{
				pageIndex = query.getPageIndex();
			}
			if (result.getPageSize() != null && result.getPageSize() > 0)
			{
				queryString = SQLUtil.getLimitString(driverName, result.getSql(), result.getPageSize(), pageIndex);
			}
			printSQL(config, queryString);
			SqlRowSet rowset = getJdbcTemplate().queryForRowSet(queryString, result.getParamsList().toArray());
			List<T> resultList = SQLUtil.SqlRowSet2List(rowset, type, dc);
			SearchResult<T> searchResult = new SearchResult<T>();
			searchResult.setResult(resultList);
			if (query.getIsSearchTotalCount())
			{
				if (result.getRecordParamsList() != null && result.getRecordParamsList().size() > 0)
				{
					searchResult.setCount(getJdbcTemplate().queryForObject(result.getRecordCountSql(),Integer.class,
							result.getParamsList()));
				}
				else
				{
					searchResult.setCount(getJdbcTemplate().queryForObject(result.getRecordCountSql(),Integer.class));
				}
			}
			return searchResult;
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public SearchResult<?> findAllEntityByDynamicQuery(DynamicQuery query)
	{
		throw new DataOperationException("JDBC实现暂时不支持findAllEntityByDynamicQuery(DynamicQuery query)方法");
	}

	@Override
	public <T> List<T> findAllEntityByDynamicQuery(Class<T> type, DynamicQuery query, LockType lockType)
	{
		try
		{
			query.setQueryType(QueryType.JDBC);
			SqlResult result = query.getSqlResult();
			Entity dc = getEntityConfig(type);
			String queryString = result.getSql();
			switch (driverName)
			{
				case MySQL:
				case Oracle:
					break;
				case SQLServer:
					queryString = getSQLServerLockString(queryString, lockType);
					break;
				default:
					break;
			}
			int pageIndex = 0;
			if (result.getPageIndex() == null || result.getPageIndex() <= 0)
			{
				pageIndex = 1;
			}
			else
			{
				pageIndex = query.getPageIndex();
			}
			if (result.getPageSize() != null && result.getPageSize() > 0)
			{
				queryString = SQLUtil.getLimitString(driverName, queryString, result.getPageSize(), pageIndex);
			}
			printSQL(config, queryString);
			SqlRowSet rowset = getJdbcTemplate().queryForRowSet(queryString, result.getParamsList().toArray());
			return SQLUtil.SqlRowSet2List(rowset, type, dc);
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public <T> List<T> findAllEntityByNamedSql(Class<T> type, String name, int pageSize, int pageNum,
			Object... parameters) throws DataOperationException
	{
		try
		{
			Entity dc = getEntityConfig(type);
			String nameSql = SQLUtil.getLimitString(driverName, getNameSql(name), pageSize, pageNum);
			SqlRowSet rowset = getJdbcTemplate().queryForRowSet(nameSql, parameters);
			return SQLUtil.SqlRowSet2List(rowset, type, dc);
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public <T> List<T> findAllEntityByNamedSql(Class<T> type, String name, Object... parameters)
			throws DataOperationException
	{
		try
		{
			Entity dc = getEntityConfig(type);
			SqlRowSet rowset = getJdbcTemplate().queryForRowSet(getNameSql(name), parameters);
			return SQLUtil.SqlRowSet2List(rowset, type, dc);
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	public DriverName getDriverName()
	{
		if (this.driverName == null)
		{
			try
			{
				DataSource dataSource = getJdbcTemplate().getDataSource();
				DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
				String databaseProductName = metaData.getDatabaseProductName();
				driverName = DriverName.parseDriverName(databaseProductName);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return driverName;
	}

	@Override
	public <T> T getEntity(Class<T> type, Object entityId) throws DataOperationException
	{
		try
		{
			Entity dc = getEntityConfig(type);
			String sql = SQLUtil.createStatement(dc, type, entityId.toString(), SQLUtil.ENTITY_SELECT, getDriverName());
			printSQL(config, sql);
			SqlRowSet rowset = getJdbcTemplate().queryForRowSet(sql);
			List<T> list = SQLUtil.SqlRowSet2List(rowset, type, dc);
			if (list.size() > 0)
			{
				return list.get(0);
			}
			else
			{
				return null;
			}
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public <T> T getEntityByNamedQuery(Class<T> type, String name, Object... parameters)
	{
		List<T> list = this.findAllEntityByNamedSql(type, name, parameters);
		if (list.size() > 0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}

	private <T> Entity getEntityConfig(Class<T> type)
	{
		String name = type.getName();
		Entity dc = JdbcPersistProvider.zzz_entityMap.get(name);
		if (dc == null)
		{
			dc = SQLUtil.parseEntity(config, type);
			JdbcPersistProvider.zzz_entityMap.put(name, dc);
		}
		return dc;
	}

	/**
	 * @return jdbcTemplate
	 */
	private JdbcTemplate getJdbcTemplate()
	{
		return masterJdbcTemplate;
	}

	private String getNameSql(String name)
	{
		String string = namedSqlMap.get(name);
		printSQL(config, string);
		return string;
	}

	public String getSQLServerLockString(String queryString, LockType lockType)
	{
		String newStr = queryString.toLowerCase();
		int indexOf = newStr.indexOf("where");
		switch (lockType)
		{
			case LOCK_WAIT:
				newStr = newStr.substring(0, indexOf) + " WITH(UPDLOCK, ROWLOCK) "
						+ newStr.substring(indexOf, newStr.length());
				break;
			case LOCK_PASS:
				newStr = newStr.substring(0, indexOf) + " WITH(UPDLOCK, ROWLOCK, READPAST) "
						+ newStr.substring(indexOf, newStr.length());
				break;
			case UNLOCK_READPAST:
				newStr = newStr.substring(0, indexOf) + " WITH(READPAST) " + newStr.substring(indexOf, newStr.length());
				break;
			case LOCK_NO:
				newStr = newStr.substring(0, indexOf) + " WITH(NOLOCK) " + newStr.substring(indexOf, newStr.length());
				break;
		}
		return newStr;
	}

	@Override
	public <T> T loadEntity(Class<T> type, Object id) throws DataOperationException
	{
		try
		{
			Entity dc = getEntityConfig(type);
			String sql = SQLUtil.createStatement(dc, type, String.valueOf(id), SQLUtil.ENTITY_SELECT, getDriverName());
			printSQL(config, sql);
			SqlRowSet rowset = getJdbcTemplate().queryForRowSet(sql);
			List<T> list = SQLUtil.SqlRowSet2List(rowset, type, dc);
			if (list.size() > 0)
			{
				return list.get(0);
			}
			else
			{
				throw new DataOperationException(type.getName() + "未找到记录{" + id + "}.");
			}
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T lockByEntity(T entity) throws DataOperationException
	{
		try
		{
			Entity dc = getEntityConfig(entity.getClass());
			String sql = SQLUtil.createStatement(dc, entity, SQLUtil.ENTITY_SELECT_FOR_UPDATE, getDriverName());
			printSQL(config, sql);
			SqlRowSet rowset = getJdbcTemplate().queryForRowSet(sql);
			List<T> list = (List<T>) SQLUtil.SqlRowSet2List(rowset, entity.getClass(), dc);
			if (list.size() > 0)
			{
				return list.get(0);
			}
			else
			{
				return null;
			}
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public <T> T lockEntity(Class<T> type, Object entityId) throws DataOperationException
	{
		try
		{
			Entity dc = getEntityConfig(type);
			String sql = SQLUtil.createStatement(dc, type, String.valueOf(entityId), SQLUtil.ENTITY_SELECT_FOR_UPDATE,
					getDriverName());
			printSQL(config, sql);
			SqlRowSet rowset = getJdbcTemplate().queryForRowSet(sql);
			List<T> list = SQLUtil.SqlRowSet2List(rowset, type, dc);
			if (list.size() > 0)
			{
				return list.get(0);
			}
			else
			{
				return null;
			}
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	public void printSQL(PersistConfig config, String... sql)
	{
		SQLUtil.printSQL(true, config, sql);
	}

	@Override
	public void saveAllEntity(Collection<?> entitys) throws DataOperationException
	{
		try
		{
			List<String> list = new ArrayList<String>();
			for (Iterator<?> iter = entitys.iterator(); iter.hasNext();)
			{
				Object element = iter.next();
				Entity dc = getEntityConfig(element.getClass());
				String sql = SQLUtil.createStatement(dc, element, SQLUtil.ENTITY_INSERT, getDriverName());
				printSQL(config, sql);
				list.add(sql);
			}
			if (list.size() > 0)
			{
				this.batchUpdate(list);
			}
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public <T> T saveEntity(T entity) throws DataOperationException
	{
		try
		{
			final Entity dc = getEntityConfig(entity.getClass());
			final String sql = SQLUtil.createStatement(dc, entity, SQLUtil.ENTITY_INSERT, getDriverName());
			printSQL(config, sql);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator()
			{
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException
				{
					PreparedStatement prepareStatement = null;
					if (driverName.equals(DriverName.SQLite))
					{
						// SQLite未实现PrepareStatement执行update时返回值的方法
						prepareStatement = conn.prepareStatement(sql);
					}
					else
					{
						// 通过主键名获取更安全，兼容SQLServer, MySQL, Oracle
						prepareStatement = conn.prepareStatement(sql, new String[] { dc.getPrimaryKey() });
					}
					// else
					// {
					// prepareStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					// }
					return prepareStatement;
				}
			}, keyHolder);
			Object primaryKey = null;
			try
			{
				primaryKey = keyHolder.getKey();
				if (primaryKey != null)
				{
					SQLUtil.setEntityPrimaryKeyValue(dc, entity, primaryKey);
				}
			}
			catch (Exception e)
			{
				System.err.println(e.getMessage());
			}
			return entity;
		}
		catch (RuntimeException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public void saveOrUpdateAllEntity(Collection<?> entitys) throws DataOperationException
	{
		try
		{
			List<String> list = new ArrayList<String>();
			for (Iterator<?> iter = entitys.iterator(); iter.hasNext();)
			{
				Object element = iter.next();
				Entity dc = getEntityConfig(element.getClass());
				String sql = SQLUtil.createStatement(dc, element, SQLUtil.ENTITY_SAVE_OR_UPDATE, getDriverName());
				printSQL(config, sql);
				list.add(sql);
			}
			if (list.size() > 0)
			{
				this.batchUpdate(list);
			}
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public void saveOrUpdateEntity(Object entity) throws DataOperationException
	{
		try
		{
			Entity dc = getEntityConfig(entity.getClass());
			String sql = SQLUtil.createStatement(dc, entity, SQLUtil.ENTITY_SAVE_OR_UPDATE, getDriverName());
			printSQL(config, sql);
			getJdbcTemplate().execute(sql);
		}
		catch (RuntimeException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public void updateAllEntity(Collection<?> entitys) throws DataOperationException
	{
		try
		{
			List<String> list = new ArrayList<String>();
			for (Iterator<?> iter = entitys.iterator(); iter.hasNext();)
			{
				Object element = iter.next();
				Entity dc = getEntityConfig(element.getClass());
				String sql = SQLUtil.createStatement(dc, element, SQLUtil.ENTITY_UPDATE, getDriverName());
				printSQL(config, sql);
				list.add(sql);
			}
			if (list.size() > 0)
			{
				this.batchUpdate(list);
			}
		}
		catch (DataAccessException e)
		{
			throw new DataOperationException(e);
		}
	}

	@Override
	public <T> T updateEntity(T entity) throws DataOperationException
	{
		try
		{
			Entity dc = getEntityConfig(entity.getClass());
			String sql = SQLUtil.createStatement(dc, entity, SQLUtil.ENTITY_UPDATE, getDriverName());
			printSQL(config, sql);
			getJdbcTemplate().execute(sql);
			return entity;
		}
		catch (RuntimeException e)
		{
			throw new DataOperationException(e);
		}
	}
}
