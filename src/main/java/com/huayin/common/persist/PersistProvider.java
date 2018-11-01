/**
 * <pre>
 * Title: 		DaoProxy.java
 * Author:		zhaojitao
 * Create:	 	2007-4-18 上午11:53:49
 * Copyright: 	Copyright (c) 2007
 * Company:		
 * <pre>
 */
package com.huayin.common.persist;

import java.util.Collection;
import java.util.List;

import com.huayin.common.exception.DataOperationException;
import com.huayin.common.persist.jdbc.SQLUtil.DriverName;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;

/**
 * <pre>
 * DAO代理接口
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2007-4-18
 */
public interface PersistProvider
{
	/**
	 * jdbc持久化实现的上下文名称
	 */
	public static final String CONTEXT_BEAN_NAME_PERSISTPROVIDER_JDBC = "jdbcPersistProvider";

	/**
	 * jpa持久化实现的上下文名称
	 */
	public static final String CONTEXT_BEAN_NAME_PERSISTPROVIDER_JPA = "jpaPersistProvider";

	/**
	 * <pre>
	 * 通过命名查询获取实体对象数量
	 * </pre>
	 * @param name 命名查询名称
	 * @param parameters 参数
	 * @return 记录数
	 */
	public long countEntityByNamedQuery(String name, Object... parameters);

	/**
	 * <pre>
	 * 删除持久化数据集合
	 * </pre>
	 * @param entitys 实体对象集合
	 * @throws DataOperationException 数据库处理异常
	 */
	void deleteAllEntity(Collection<?> entitys) throws DataOperationException;

	/**
	 * <pre>
	 * 删除持久化数据
	 * </pre>
	 * @param entity 实体对象
	 * @throws DataOperationException 数据库处理异常
	 */
	void deleteEntity(Object entity) throws DataOperationException;

	/**
	 * <pre>
	 * 批量执行命名查询
	 * </pre>
	 * @param name 命名查询名称
	 * @param parameters 参数集合
	 * @return 影响记录数数组
	 */
	int[] exeBatchNamedQuery(String name, List<List<Object>> parameters);

	/**
	 * <pre>
	 * 批量执行查询语句
	 * </pre>
	 * @param qls 查询语句数组
	 * @return 影响的记录数数组
	 * @throws DataOperationException 数据库处理异常
	 */
	public int[] exeBatchNativeSql(String[] qls) throws DataOperationException;

	/**
	 * <pre>
	 * 执行命名查询
	 * </pre>
	 * @param name 命名查询名称
	 * @param parameters 参数
	 * @return 影响记录数
	 */
	public int exeNamedQuery(String name, Object... parameters);

	/**
	 * <pre>
	 * 使用动态查询语句执行查询，避免类中出现大量的sql语句
	 * </pre>
	 * @param <T> 返回集合元素类型
	 * @param type 返回集合元素类型
	 * @param query 动态查询语句对象
	 * @return 查询结果集
	 */
	public <T> SearchResult<T> findAllEntityByDynamicQuery(Class<T> type, DynamicQuery query);

	public SearchResult<?> findAllEntityByDynamicQuery(DynamicQuery query);

	/**
	 * <pre>
	 * 使用动态查询语句执行查询，避免类中出现大量的sql语句
	 * </pre>
	 * @param <T> 返回集合元素类型
	 * @param type 返回集合元素类型
	 * @param query 动态查询语句对象
	 * @param lockType 锁类型
	 * @return 查询结果集
	 */
	public <T> List<T> findAllEntityByDynamicQuery(Class<T> type, DynamicQuery query, LockType lockType);

	/**
	 * <pre>
	 * 通过实体类型和查询名称获取实体对象集合
	 * </pre>
	 * @param type 实体类型
	 * @param name 查询名称
	 * @param pageSize 页记录数
	 * @param pageNum 页码, 从1开始计数
	 * @param parameters 参数数组
	 * @return 实体对象集合
	 * @throws DataOperationException 数据库处理异常
	 */
	<T> List<T> findAllEntityByNamedSql(Class<T> type, String name, int pageSize, int pageNum, Object... parameters)
			throws DataOperationException;

	/**
	 * <pre>
	 * 动态查询记录数
	 * </pre>
	 * @param query 动态查询
	 * @return 记录数
	 */
	public Long countByDynamicQuery(DynamicQuery query);

	/**
	 * <pre>
	 * 通过实体类型和查询名称获取实体对象集合
	 * </pre>
	 * @param type 实体类型
	 * @param name 查询名称
	 * @param parameters 参数数组
	 * @return 实体对象集合
	 * @throws DataOperationException 数据库处理异常
	 */
	<T> List<T> findAllEntityByNamedSql(Class<T> type, String name, Object... parameters) throws DataOperationException;

	/**
	 * <pre>
	 * 通过实体类型和主键获取实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param id 主键值
	 * @return 实体对象，未找到则返回null
	 * @throws DataOperationException 数据库处理异常
	 */
	<T> T getEntity(Class<T> type, Object id) throws DataOperationException;

	/**
	 * <pre>
	 * 通过查询语句取单个对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 对象类型
	 * @param name 查询语句
	 * @param parameters 参数数组
	 * @return 单个对象
	 */
	public <T> T getEntityByNamedQuery(Class<T> type, String name, Object... parameters);

	/**
	 * <pre>
	 * 通过实体类型和主键获取实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param id 主键值
	 * @return 实体对象，未找到则抛异常
	 * @throws DataOperationException 数据库处理异常
	 */
	<T> T loadEntity(Class<T> type, Object id) throws DataOperationException;

	/**
	 * <pre>
	 * 通过实体类型和实体对象锁定实体对象
	 * </pre>
	 * @param entity 实体对象
	 * @param <T> 实体类型
	 * @return 锁定后的实体对象
	 * @throws DataOperationException 数据库处理异常
	 */
	<T> T lockByEntity(T entity) throws DataOperationException;

	/**
	 * <pre>
	 * 通过实体类型和主键锁定实体对象
	 * </pre>
	 * @param <T> 实体类型
	 * @param type 实体类型
	 * @param id 主键值
	 * @return 实体对象，未找到则返回null
	 * @throws DataOperationException 数据库处理异常
	 */
	<T> T lockEntity(Class<T> type, Object id) throws DataOperationException;

	/**
	 * <pre>
	 * 保存实体对象集合到持久化数据
	 * </pre>
	 * @param entitys 实体对象集合
	 * @throws DataOperationException 数据库处理异常
	 */
	public void saveAllEntity(Collection<?> entitys) throws DataOperationException;

	/**
	 * <pre>
	 * 保存或修改实体对象集合到持久化数据
	 * </pre>
	 * @param entitys
	 * @throws DataOperationException
	 */
	public void saveOrUpdateAllEntity(Collection<?> entitys) throws DataOperationException;

	/**
	 * <pre>
	 * 保存实体对象到持久化数据
	 * </pre>
	 * @param entity 实体对象
	 * @return 持久化数据
	 * @throws DataOperationException 数据库处理异常
	 */
	public <T> T saveEntity(T entity) throws DataOperationException;

	/**
	 * <pre>
	 * 保存或更新实体对象到持久化数据
	 * </pre>
	 * @param entity 实体对象
	 * @throws DataOperationException 数据库处理异常
	 */
	public void saveOrUpdateEntity(Object entity) throws DataOperationException;

	/**
	 * <pre>
	 * 更新实体对象集合到持久化数据
	 * </pre>
	 * @param entitys 实体对象集合
	 * @throws DataOperationException 数据库处理异常
	 */
	void updateAllEntity(Collection<?> entitys) throws DataOperationException;

	/**
	 * <pre>
	 * 更新实体对象到持久化数据
	 * </pre>
	 * @param entity 实体对象
	 * @throws DataOperationException 数据库处理异常
	 */
	<T> T updateEntity(T entity) throws DataOperationException;

	/**
	 * 不传类型的原SQL查询
	 * @param sql
	 * @param values
	 * @return
	 * @throws DataOperationException
	 */
	List<Object> exeNativeSqlNotType(String sql, Object... values) throws DataOperationException;

	/**
	 * <pre>
	 * 获取数据库驱动名称
	 * </pre>
	 * @return 数据库驱动名称
	 */
	public DriverName getDriverName();
}
