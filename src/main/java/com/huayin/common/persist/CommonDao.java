/**
 * <pre>
 * Title: 		VoteDao.java
 * Author:		linriqing
 * Create:	 	2010-8-5 上午10:52:35
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist;

import java.util.Collection;
import java.util.List;

import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;

/**
 * <pre>
 * 通用DAO接口
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-8-5
 */
public interface CommonDao
{
	<T extends AbstractEntity> int countByDynamicQuery(DynamicQuery dynamicQuery, Class<T> type);

	/**
	 * <pre>
	 * 通过命名查询进行分页查询时获取记录数
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param name 命名查询名称
	 * @param pageSize 每页记录数
	 * @param pageIndex 页码
	 * @param parameters 参数列表
	 * @return 实体对象集合记录数
	 */
	<T extends AbstractEntity> int countByNamedQuery(Class<T> type, String name, Object... parameters);

	<T extends AbstractEntity> void deleteByIds(Class<T> type, Object... ids);

	/**
	 * <pre>
	 * 根据实体类型和主键删除实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param id 实体主键
	 */
	<T extends AbstractEntity> void deleteEntity(T entity);

	<T extends AbstractEntity> void deleteEntity(Class<T> type, Object id);
	
	<T extends AbstractEntity> void deleteAllEntity(Collection<T> entitys);
	
	<T extends AbstractEntity> List<T> findEntityByDynamicQuery(DynamicQuery dynamicQuery, Class<T> type);

	<T extends AbstractEntity> SearchResult<T> findEntityByDynamicQueryPage(DynamicQuery dynamicQuery, Class<T> type);
	
	<M> SearchResult<M> findCustomEntityByDynamicQueryPage(DynamicQuery dynamicQuery, Class<M> type);

	/**
	 * <pre>
	 * 根据命名查询获取实体对象集合
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param name 命名查询名称
	 * @param parameters 参数列表
	 * @return 实体对象集合
	 */
	<T extends AbstractEntity> List<T> findEntityByNamedQuery(Class<T> type, String name, Object... parameters);

	<T extends AbstractEntity> T getByDynamicQuery(DynamicQuery dynamicQuery, Class<T> type);

	/**
	 * <pre>
	 * 获取实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param id 实体主键
	 * @return 返回实体对象
	 */
	<T extends AbstractEntity> T getEntity(Class<T> type, Object id);

	/**
	 * <pre>
	 * 根据命名查询获取实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param name 命名查询名称
	 * @param parameters 参数列表
	 * @return 实体对象
	 */
	<T extends AbstractEntity> T getEntityByNamedQuery(Class<T> type, String name, Object... parameters);

	/**
	 * <pre>
	 * 根据类型查询所有的实体对象集合
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @return 实体对象集合
	 */
	<T extends AbstractEntity> List<T> listAllEntity(Class<T> type);

	/**
	 * <pre>
	 * 通过命名查询进行分页查询
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param name 命名查询名称
	 * @param pageSize 每页记录数
	 * @param pageIndex 页码
	 * @param parameters 参数列表
	 * @return 实体对象集合
	 */
	<T extends AbstractEntity> List<T> listEntityByNamedQuery(Class<T> type, String name, int pageSize, int pageIndex,
			Object... parameters);

	/**
	 * <pre>
	 * 锁定实体对象直到事务提交
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param id 实体主键
	 * @return 返回实体对象
	 */
	<T extends AbstractEntity> T lockObject(Class<T> type, Object id);

	/**
	 * <pre>
	 *  批量保存实体
	 * </pre>
	 * @param <T> 实体类型
	 * @param entitys 实体对象集合
	 */
	<T extends AbstractEntity> void saveAllEntity(Collection<T> entitys);

	/**
	 * <pre>
	 * 保存实体
	 * </pre>
	 * @param <T> 实体类型
	 * @param entity 实体对象
	 * @return 返回保存后的实体对象
	 */
	<T extends AbstractEntity> T saveEntity(T entity);

	/**
	 * <pre>
	 * 保存或更新实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param entity 实体对象
	 */
	<T extends AbstractEntity> void saveOrUpdateEntity(T entity);

	/**
	 * <pre>
	 *  批量更新实体
	 * </pre>
	 * @param <T> 实体类型
	 * @param entitys 实体对象集合
	 * @return 返回更新后的实体对象集合
	 */
	<T extends AbstractEntity> void updateAllEntity(Collection<T> entitys);

	/**
	 * <pre>
	 * 更新实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param entity 实体对象
	 * @return 返回更新后的实体对象
	 */
	<T extends AbstractEntity> T updateEntity(T entity);

	public <T extends AbstractEntity> List<T> lockByDynamicQuery(DynamicQuery query, Class<T> type, LockType lockType);
	
	public void execBatchNamedQuery(String name,List<List<Object>> parameters);
	
	public int execNamedQuery(String name, Object... parameters);
}
