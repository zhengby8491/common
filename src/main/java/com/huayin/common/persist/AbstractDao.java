/**
 * <pre>
 * Title: 		AbstractDao.java
 * Author:		linriqing
 * Create:	 	2010-7-8 下午05:10:34
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;

/**
 * <pre>
 * DAO抽象类
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-7-8
 */
public abstract class AbstractDao<T extends AbstractEntity> implements NestedSimplyDao<T>
{
	private Class<T> entityType;

	private ThreadLocal<Class<?>> exactType = new ThreadLocal<Class<?>>();

	@Autowired(required = false)
	private PersistProviderBridge persistProviderBridge;

	private Integer providerType = null;

	@SuppressWarnings("unchecked")
	protected AbstractDao()
	{
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		entityType = (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	/**
	 * <pre>
	 * 动态查询记录数
	 * </pre>
	 * @param query 动态查询
	 * @return 记录数
	 */
	public Long countByDynamicQuery(DynamicQuery query)
	{
		try
		{
			return getPersistProviderHolder().countByDynamicQuery(query);
		}
		catch (NoResultException e)
		{
			return 0L;
		}
		catch (EmptyResultDataAccessException e)
		{
			return 0L;
		}
	}

	/**
	 * <pre>
	 * 通过命名查询名称统计记录数
	 * </pre>
	 * @param name 命名查询名称
	 * @param parameters 参数集合
	 * @return 记录数
	 */
	protected long countByNamedQuery(String name, Object... parameters)
	{
		try
		{
			return getPersistProviderHolder().countEntityByNamedQuery(name, parameters);
		}
		catch (NoResultException e)
		{
			return 0L;
		}
		catch (EmptyResultDataAccessException e)
		{
			return 0L;
		}
	}

	@Override
	public void delete(T object)
	{
		getPersistProviderHolder().deleteEntity(object);
	}

	@Override
	public void deleteAll(Collection<T> entitys)
	{
		getPersistProviderHolder().deleteAllEntity(entitys);
	}

	@Override
	public void deleteById(Object... keys)
	{
		for (Object object2 : keys)
		{
			delete(get(object2));
		}
	}

	/**
	 * <pre>
	 * 批量执行命名查询
	 * </pre>
	 * @param name 命名查询名称
	 * @param parameters 参数集合
	 * @return 影响记录数数组
	 */
	protected int[] exeBatchNamedQuery(String name, List<List<Object>> parameters)
	{
		return getPersistProviderHolder().exeBatchNamedQuery(name, parameters);
	}

	/**
	 * <pre>
	 * 执行命名查询
	 * </pre>
	 * @param name 命名查询名称
	 * @param parameters 参数
	 * @return 影响记录数
	 */
	protected int exeNamedQuery(String name, Object... parameters)
	{
		return getPersistProviderHolder().exeNamedQuery(name, parameters);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll()
	{
		return (List<T>) getPersistProviderHolder().findAllEntityByDynamicQuery(getType(), new DynamicQuery(getType()))
				.getResult();
	}

	/**
	 * <pre>
	 * 通过动态查询获取所有实体对象集合
	 * </pre>
	 * @param query 动态查询
	 * @return 所有实体对象集合
	 */
	public List<T> findAllByDynamicQuery(DynamicQuery query)
	{
		return getPersistProviderHolder().findAllEntityByDynamicQuery(entityType, query).getResult();
	}

	protected SearchResult<T> findAllByDynamicQueryPage(DynamicQuery query)
	{
		return getPersistProviderHolder().findAllEntityByDynamicQuery(entityType, query);
	}

	protected <E> SearchResult<E> findAllByDynamicQueryPage(DynamicQuery query, Class<E> clz)
	{
		return getPersistProviderHolder().findAllEntityByDynamicQuery(clz, query);
	}

	/**
	 * <pre>
	 * 使用动态查询语句执行查询，避免类中出现大量的sql语句
	 * </pre>
	 * @param <E> 返回集合元素类型
	 * @param query 动态查询语句对象
	 * @param clz 返回集合元素类型
	 * @return 查询结果集
	 */
	protected <E> List<E> findAllByDynamicQuery(DynamicQuery query, Class<E> clz)
	{
		return getPersistProviderHolder().findAllEntityByDynamicQuery(clz, query).getResult();
	}

	/**
	 * <pre>
	 * 使用动态查询语句执行查询，避免类中出现大量的sql语句
	 * </pre>
	 * @param query 动态查询语句对象
	 * @return 查询结果集
	 */
	protected List<?> findByDynamicQuery(DynamicQuery query)
	{
		return getPersistProviderHolder().findAllEntityByDynamicQuery(query).getResult();
	}

	/**
	 * <pre>
	 * 通过动态查询获取所有实体对象集合
	 * </pre>
	 * @param query 动态查询
	 * @param lockType 锁
	 * @return 所有实体对象集合
	 */
	public List<T> findAllByDynamicQuery(DynamicQuery query, LockType lockType)
	{
		return getPersistProviderHolder().findAllEntityByDynamicQuery(entityType, query, lockType);
	}

	public <E> List<E> findAllByDynamicQuery(DynamicQuery query, LockType lockType, Class<E> clz)
	{
		return getPersistProviderHolder().findAllEntityByDynamicQuery(clz, query, lockType);
	}

	/**
	 * <pre>
	 * 通过实体类型和查询名称获取实体对象集合
	 * </pre>
	 * @param type 实体类型
	 * @param name 查询名称
	 * @param parameters 参数数组
	 * @return 实体对象集合
	 */
	protected <K> List<K> findAllByNamedQuery(Class<K> type, String name, Object... parameters)
	{
		return getPersistProviderHolder().findAllEntityByNamedSql(type, name, parameters);
	}

	/**
	 * <pre>
	 * 通过查询名称获取实体对象集合
	 * </pre>
	 * @param name 查询名称
	 * @param parameters 参数数组
	 * @return 实体对象集合
	 */
	protected List<T> findAllByNamedQuery(String name, Object... parameters)
	{
		return getPersistProviderHolder().findAllEntityByNamedSql(entityType, name, parameters);
	}

	/**
	 * <pre>
	 * 通过查询名称获取实体对象集合
	 * </pre>
	 * @param name 查询名称
	 * @param pageSize 页记录数
	 * @param pageIndex 页码, 从1开始计数
	 * @param parameters 参数数组
	 * @return 实体对象集合
	 */
	protected List<T> findAllByNamedQueryWithPage(String name, int pageSize, int pageIndex, Object... parameters)
	{
		return getPersistProviderHolder().findAllEntityByNamedSql(entityType, name, pageSize, pageIndex, parameters);
	}

	/**
	 * <pre>
	 * 通过实体类型和主键获取实体对象
	 * </pre>
	 * @param <K> 实体类型
	 * @param clz 实体类型
	 * @param id 主键值
	 * @return 实体对象，未找到则返回null
	 */
	protected <K> K get(Class<K> clz, Object id)
	{
		return getPersistProviderHolder().getEntity(clz, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T get(Object id)
	{
		return (T) getPersistProviderHolder().getEntity(getType(), id);
	}

	/**
	 * <pre>
	 * 通过实体类型和查询名称获取单个实体对象
	 * </pre>
	 * @param type 实体类型
	 * @param name 查询名称
	 * @param parameters 参数数组
	 * @return 单个实体对象
	 */
	protected <E> E getByNamedQuery(Class<E> type, String name, Object... parameters)
	{
		List<E> findEntityByNamedSql = getPersistProviderHolder().findAllEntityByNamedSql(type, name, parameters);
		if (findEntityByNamedSql.size() > 0)
		{
			return findEntityByNamedSql.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * <pre>
	 * 通过命名查询获取实体对象
	 * </pre>
	 * @param name 命名查询名称
	 * @param parameters 参数集合
	 * @return 实体对象
	 */
	protected T getByNamedQuery(String name, Object... parameters)
	{
		List<T> findEntityByNamedSql = getPersistProviderHolder().findAllEntityByNamedSql(entityType, name, parameters);
		if (findEntityByNamedSql.size() > 0)
		{
			return findEntityByNamedSql.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * @return 持久化實現連接器，可以獲取數據源等信息
	 */
	protected PersistProviderBridge getPersistProviderBridge()
	{
		return this.persistProviderBridge;
	}

	private PersistProvider getPersistProviderHolder()
	{
		if (providerType == null)
		{
			return persistProviderBridge.getPersistProvider();
		}
		else
		{
			return persistProviderBridge.getPersistProvider(providerType);
		}
	}

	/**
	 * <pre>
	 * 通过动态查询获取单个实体对象
	 * </pre>
	 * @param query 动态查询
	 * @return 单个实体对象
	 */
	protected T getSingleByDynamicQuery(DynamicQuery query)
	{
		List<T> list = findAllByDynamicQuery(query);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}

	private Class<?> getType()
	{
		if (exactType.get() == null)
		{
			return entityType;
		}
		else
		{
			return exactType.get();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public T load(Object id)
	{
		return (T) getPersistProviderHolder().loadEntity(getType(), id);
	}

	/**
	 * <pre>
	 * 通过实体类型和主键锁定实体对象
	 * </pre>
	 * @param <K> 实体类型
	 * @param clz 实体类型
	 * @param id 主键
	 * @return 实体对象
	 */
	protected <K> K lock(Class<K> clz, Object id)
	{
		return getPersistProviderHolder().lockEntity(clz, id);
	}

	/**
	 * <pre>
	 * 通过对象锁定实体
	 * </pre>
	 * @param entity 对象
	 * @return 实体
	 */
	@Override
	public T lock(T entity)
	{
		return getPersistProviderHolder().lockByEntity(entity);
	}

	/**
	 * <pre>
	 * 通过主键锁定实体
	 * </pre>
	 * @param id 主键
	 * @return 实体
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T lockById(Object id)
	{
		return (T) getPersistProviderHolder().lockEntity(getType(), id);
	}

	@Override
	public T save(T object)
	{
		return getPersistProviderHolder().saveEntity(object);
	}

	@Override
	public void saveAll(Collection<T> objects)
	{
		getPersistProviderHolder().saveAllEntity(objects);
	}

	public void saveOrUpdate(Collection<T> collections)
	{
		getPersistProviderHolder().saveOrUpdateAllEntity(collections);
	}

	@Override
	public void saveOrUpdate(T object)
	{
		getPersistProviderHolder().saveOrUpdateEntity(object);
	}

	/**
	 * @param exactType 當前綫程訪問實體類型
	 */
	protected void setExactType(Class<?> exactType)
	{
		this.exactType.set(exactType);
	}

	/**
	 * @param providerType 持久化实现类型，0-自定义JDBC实现，其它值为JPA实现
	 */
	protected void setProviderType(Integer providerType)
	{
		this.providerType = providerType;
	}

	@Override
	public T update(T object)
	{
		return getPersistProviderHolder().updateEntity(object);
	}

	@Override
	public void updateAll(Collection<T> objects)
	{
		getPersistProviderHolder().updateAllEntity(objects);
	}

}
