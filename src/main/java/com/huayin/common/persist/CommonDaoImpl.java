/**
 * <pre>
 * Title: 		VoteDaoImpl.java
 * Author:		linriqing
 * Create:	 	2010-8-5 上午10:56:16
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;

/**
 * <pre>
 * 通用DAO接口实现
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-8-5
 */
@Repository("masterCommonDao")
public class CommonDaoImpl extends AbstractDao<AbstractEntity> implements CommonDao
{
	@Override
	public void delete(AbstractEntity entity)
	{
		super.delete(entity);
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntity> void deleteAllEntity(Collection<T> entitys)
	{
		super.deleteAll((Collection<AbstractEntity>)entitys);
	}

	@Override
	public <T extends AbstractEntity> void deleteEntity(T entity)
	{
		super.delete(entity);
	}
	@Override
	public <T extends AbstractEntity> void deleteEntity(Class<T> type, Object id)
	{
		super.setExactType(type);
		super.delete(super.get(id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntity> T getEntity(Class<T> type, Object id)
	{
		super.setExactType(type);
		return (T) super.get(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntity> List<T> listAllEntity(Class<T> type)
	{
		super.setExactType(type);
		return (List<T>) super.findAll();
	}

	@Override
	public <T extends AbstractEntity> T saveEntity(T entity)
	{
		super.save(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntity> T updateEntity(T entity)
	{
		return (T) super.update(entity);
	}

	@Override
	public <T extends AbstractEntity> void saveOrUpdateEntity(T entity)
	{
		super.saveOrUpdate(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntity> List<T> listEntityByNamedQuery(Class<T> type, String name, int pageSize,
			int pageIndex, Object... parameters)
	{
		super.setExactType(type);
		return (List<T>) super.findAllByNamedQueryWithPage(name, pageSize, pageIndex, parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntity> List<T> findEntityByNamedQuery(Class<T> type, String name, Object... parameters)
	{
		super.setExactType(type);
		return (List<T>) super.findAllByNamedQuery(name, parameters);
	}

	@Override
	public <T extends AbstractEntity> int countByNamedQuery(Class<T> type, String name, Object... parameters)
	{
		super.setExactType(type);
		return (int) super.countByNamedQuery(name, parameters);
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractEntity> T lockObject(Class<T> type, Object id)
	{
		super.setExactType(type);
		return (T) super.lockById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntity> T getEntityByNamedQuery(Class<T> type, String name, Object... parameters)
	{
		super.setExactType(type);
		return (T) super.getByNamedQuery(name, parameters);
	}

	@Override
	public <T extends AbstractEntity> List<T> findEntityByDynamicQuery(DynamicQuery dynamicQuery, Class<T> type)
	{
		super.setExactType(type);
		return super.findAllByDynamicQuery(dynamicQuery, type);
	}

	@Override
	public <T extends AbstractEntity> SearchResult<T> findEntityByDynamicQueryPage(DynamicQuery dynamicQuery,
			Class<T> type)
	{
		super.setExactType(type);
		return super.findAllByDynamicQueryPage(dynamicQuery, type);
	}

	@Override
	public <M> SearchResult<M> findCustomEntityByDynamicQueryPage(DynamicQuery dynamicQuery,
			Class<M> type)
	{
		super.setExactType(type);
		return super.findAllByDynamicQueryPage(dynamicQuery, type);
	}

	public <T extends AbstractEntity> T getByDynamicQuery(DynamicQuery dynamicQuery, Class<T> type)
	{
		List<T> list = super.findAllByDynamicQuery(dynamicQuery, type);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public <T extends AbstractEntity> int countByDynamicQuery(DynamicQuery dynamicQuery, Class<T> type)
	{
		super.setExactType(type);
		return (int) super.countByDynamicQuery(dynamicQuery).longValue();
	}

	@Override
	public <T extends AbstractEntity> void deleteByIds(Class<T> type, Object... ids)
	{
		super.setExactType(type);
		super.deleteById(ids);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntity> void saveAllEntity(Collection<T> entitys)
	{
		super.saveAll((Collection<AbstractEntity>) entitys);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntity> void updateAllEntity(Collection<T> entitys)
	{
		super.updateAll((Collection<AbstractEntity>) entitys);
	}

	public <T extends AbstractEntity> List<T> lockByDynamicQuery(DynamicQuery query, Class<T> type, LockType lockType)
	{
		return super.findAllByDynamicQuery(query, lockType, type);
	}
	@Override
	public void execBatchNamedQuery(String name, List<List<Object>> parameters)
	{
		super.exeBatchNamedQuery(name, parameters);
	}
	@Override
	public int execNamedQuery(String name, Object... parameters)
	{
		return super.exeNamedQuery(name, parameters);
	}
}
