/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.common.persist.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * <pre>
 * 框架 - 基类
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月26日
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
// @EntityListeners(OperateLogListener.class)
public abstract class AbstractEntity implements Serializable
{
	private static final long serialVersionUID = 6757814772463743205L;

	@Column(nullable = false)
	private Long version = 0L;

	public abstract Object getId();

	public Long getVersion()
	{
		return version;
	}

	public void setVersion(Long version)
	{
		this.version = version;
	}

	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
