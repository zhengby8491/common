/**
 * <pre>
 * Title: 		OperateLog.java
 * Author:		zhaojitao
 * Create:	 	2010-7-6 上午11:33:29
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ExcludeDefaultListeners;
import javax.persistence.ExcludeSuperclassListeners;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <pre>
 * 操作日志信息
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-7-6
 */
@Entity
@NamedQueries({
		// 命名查询，根据实体查询与其关联的审计日志记录，注意这里审计日志和被审计实体并没有物理关联（数据库约束），即使实体被删除，仍然可以根据实体id访问审计日志。
		@NamedQuery(name = "OperateLog.findByEntity", query = "select h from OperateLog h where entityName = :entityName and entityId = :entityId order by operateTime asc"),
		@NamedQuery(name = "OperateLog.testSQL", query = "select count(h) from OperateLog h") })
@ExcludeSuperclassListeners
@ExcludeDefaultListeners
public class OperateLog extends AbstractAutoIdEntity
{
	public static enum OperationType
	{
		CREATE, DELETE, UPDATE
	}

	private static final long serialVersionUID = -3587350041313020540L;

	/**
	 * 操作员的用户编号
	 */
	@Column(length = 20, nullable = false)
	private String userId;

	/**
	 * 操作的对象
	 */
	@Column(length = 20, nullable = false)
	private String entityName;

	/**
	 * 被操作实体的主键id
	 */
	@Column(length = 50)
	private String entityId;

	/**
	 * 操作的时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date operateTime;

	/**
	 * 操作的类型（枚举型：新建、更新、删除）
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	@Column(length = 10, nullable = false)
	private OperationType operationType;

	/**
	 * 操作描述
	 */
	@Column(length = 1000)
	private String description;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

	public String getEntityId()
	{
		return entityId;
	}

	public void setEntityId(String entityId)
	{
		this.entityId = entityId;
	}

	public Date getOperateTime()
	{
		return operateTime;
	}

	public void setOperateTime(Date operateTime)
	{
		this.operateTime = operateTime;
	}

	public OperationType getOperationType()
	{
		return operationType;
	}

	public void setOperationType(OperationType operationType)
	{
		this.operationType = operationType;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@Override
	public String toString()
	{
		return "OperateLog["
				+ (operateTime == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(operateTime))
				+ ", " + operationType + ", " + entityName + "," + entityId + "]";
	}
}
