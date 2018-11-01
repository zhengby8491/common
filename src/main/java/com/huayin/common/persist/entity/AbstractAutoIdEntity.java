/**
 * <pre>
 * Title: 		AbstractAutoIdEntity.java
 * Author:		zhaojitao
 * Create:	 	2010-7-6 上午11:40:21
 * Copyright: 	Copyright (c) 2010
 * <pre>
 */
package com.huayin.common.persist.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * <pre>
 * 基于数据库的自动切换自增策略，移植性差，一般用在操作不频繁的且对移植无要求的模块，如：权限管理模块
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-7-6
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
//@EntityListeners(OperateLogListener.class)
public abstract class AbstractAutoIdEntity extends AbstractEntity
{
	private static final long serialVersionUID = 6757814772463743205L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	/**
	 * 备注，说明，解释，参考等
	 */
	private String memo;
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}
	
}
