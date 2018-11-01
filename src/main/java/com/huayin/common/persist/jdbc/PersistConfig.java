/**
 * <pre>
 * Title: 		EntityConfig.java
 * Author:		zhaojitao
 * Create:	 	2007-4-24 上午09:48:09
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.persist.jdbc;

import com.huayin.common.configuration.Config;

/**
 * <pre>
 * 持久化配置信息类
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2007-4-24
 */
public class PersistConfig extends Config
{
	private static final long serialVersionUID = 4507269861894018832L;
	
	/**
	 * 实体配置信息
	 */
	private Entity[] entitys;
	
	/**
	 * 持久化名
	 */
	private String name;
	
	/**
	 * 命名查询
	 */
	private NamedQuery[] namedQueries;

	/**
	 * 是否显示SQL语句
	 */
	private boolean showSQL;

	/**
	 * @return 实体配置信息 
	 */
	public Entity[] getEntitys()
	{
		return entitys;
	}

	/**
	 * @return 持久化名
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @return 命名查询 
	 */
	public NamedQuery[] getNamedQueries()
	{
		return namedQueries;
	}

	/**
	 * @return 是否显示SQL语句 
	 */
	public boolean getShowSQL()
	{
		return showSQL;
	}

	/**
	 * @param entitys 实体配置信息
	 */
	public void setEntitys(Entity[] entitys)
	{
		this.entitys = entitys;
	}

	/**
	 * @param name 持久化名
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param namedQueries 命名查询
	 */
	public void setNamedQueries(NamedQuery[] namedQueries)
	{
		this.namedQueries = namedQueries;
	}

	/**
	 * @param showSQL 是否显示SQL语句
	 */
	public void setShowSQL(boolean showSQL)
	{
		this.showSQL = showSQL;
	}
}
