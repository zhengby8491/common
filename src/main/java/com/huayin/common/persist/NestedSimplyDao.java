/**
 * <pre>
 * Title: 		PersistDao.java
 * Author:		linriqing
 * Create:	 	2011-7-20 下午02:08:39
 * Copyright: 	Copyright (c) 2011
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist;

import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;

/**
 * <pre>
 * 该接口仅提供基本的持久化操作接口
 * DAO接口可以继承该接口：
 * <code>
 * public interface AnteSysParaDao<T> extends NestedSimplyDao<T>{};
 * </code>
 * 这样DAO实现就包括了基本的持久化操作接口。
 * 
 * 當DAO不需要實現特定的操作時可以以内部类形式實現該接口：
 * <code>
 * public static final class AnteSysParaDaoImpl extends AbstractDao<AnteSysPara>{};
 * </code>
 * </pre>
 * @author linriqing
 * @version 1.0, 2011-7-20
 */
public interface NestedSimplyDao<T>
{
	/**
	 * <pre>
	 * 删除实体对象
	 * </pre>
	 * @param object 实体对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	void delete(T object);

	/**
	 * <pre>
	 * 删除实体对象集合
	 * </pre>
	 * @param entitys 实体对象集合
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	void deleteAll(Collection<T> entitys);

	/**
	 * <pre>
	 * 通过主键集合删除实体
	 * </pre>
	 * @param keys 主键集合
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	void deleteById(Object... keys);

	/**
	 * <pre>
	 * 查找所有实体集合
	 * </pre>
	 * @return 所有实体集合
	 */
	List<T> findAll();

	/**
	 * <pre>
	 * 通过主键获取实体对象
	 * </pre>
	 * @param id 主键
	 * @return 实体对象
	 */
	T get(Object id);

	/**
	 * <pre>
	 * 通过主键装载实体对象，不存在则抛出异常
	 * </pre>
	 * @param id 主键
	 * @return 实体对象
	 */
	T load(Object id);

	/**
	 * <pre>
	 * 通过实体对象
	 * </pre>
	 * @param entity 实体对象
	 * @return 实体对象
	 */
	T lock(T entity);

	/**
	 * <pre>
	 * 通过主键获取实体对象
	 * </pre>
	 * @param id 主键
	 * @return 实体对象
	 */
	T lockById(Object id);

	/**
	 * <pre>
	 * 保存实体对象
	 * </pre>
	 * @param object 实体对象
	 * @return 实体对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	T save(T object);

	/**
	 * <pre>
	 * 保存实体对象集合
	 * </pre>
	 * @param objects 实体对象集合
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	void saveAll(Collection<T> objects);

	/**
	 * <pre>
	 * 保存更新实体
	 * </pre>
	 * @param object 实体对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	void saveOrUpdate(T object);

	/**
	 * <pre>
	 * 更新实体
	 * </pre>
	 * @param object 实体对象
	 * @return 实体对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	T update(T object);

	/**
	 * <pre>
	 * 更新实体对象集合
	 * </pre>
	 * @param objects 实体对象集合
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	void updateAll(Collection<T> objects);

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveOrUpdate(Collection<T> collections);

	/**
	 * <pre>
	 * 动态查询记录数
	 * </pre>
	 * @param query 动态查询
	 * @return 记录数
	 */
	Long countByDynamicQuery(DynamicQuery query);

	/**
	 * <pre>
	 * 通过动态查询获取所有实体对象集合
	 * </pre>
	 * @param query 动态查询
	 * @return 所有实体对象集合
	 */
	List<T> findAllByDynamicQuery(DynamicQuery query);
}
