/**
 * <pre>
 * Title: 		Entity.java
 * Author:		linriqing
 * Create:	 	2011-7-10 上午02:03:31
 * Copyright: 	Copyright (c) 2011
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist.jdbc;

import java.lang.reflect.Field;

/**
 * <pre>
 * 实体类型
 * </pre>
 * @author linriqing
 * @version 1.0, 2011-7-10
 */
public class Entity
{
	/**
	 * 是否允许缓存
	 */
	private boolean cacheable;

	/**
	 * 实体类名
	 */
	private String entityName;

	/**
	 * 所有属性
	 */
	private Field[] fields = new Field[0];

	/**
	 * 主键属性
	 */
	private Field keyField;

	/**
	 * 主键名
	 */
	private String primaryKey;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 用Oracle获取序列时,Oracle主键序列名
	 */
	private String keySeqName;

	/**
	 * @return 实体类名
	 */
	public String getEntityName()
	{
		return entityName;
	}

	/**
	 * @return fields
	 */
	public Field[] getFields()
	{
		return fields;
	}

	/**
	 * @return keyField
	 */
	public Field getKeyField()
	{
		return keyField;
	}

	public String getKeySeqName()
	{
		return keySeqName;
	}

	/**
	 * @return 主键名
	 */
	public String getPrimaryKey()
	{
		return primaryKey;
	}

	/**
	 * @return 表名
	 */
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * @return 是否允许缓存
	 */
	public boolean isCacheable()
	{
		return cacheable;
	}

	/**
	 * @param cacheable 是否允许缓存
	 */
	public void setCacheable(boolean cacheable)
	{
		this.cacheable = cacheable;
	}

	/**
	 * @param entityName 实体类名
	 */
	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

	/**
	 * @param fields fields
	 */
	public void setFields(Field[] fields)
	{
		this.fields = fields;
	}

	/**
	 * @param keyField keyField
	 */
	public void setKeyField(Field keyField)
	{
		this.keyField = keyField;
	}

	public void setKeySeqName(String keySeqName)
	{
		this.keySeqName = keySeqName;
	}

	/**
	 * @param primaryKey 主键名
	 */
	public void setPrimaryKey(String primaryKey)
	{
		this.primaryKey = primaryKey;
	}

	/**
	 * @param tableName 表名
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}
}
