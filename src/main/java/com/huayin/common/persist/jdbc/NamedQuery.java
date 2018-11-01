/**
 * <pre>
 * Title: 		NamedQuery.java
 * Author:		linriqing
 * Create:	 	2011-7-10 上午02:09:03
 * Copyright: 	Copyright (c) 2011
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist.jdbc;

import com.huayin.common.configuration.Config;

/**
 * <pre>
 * 命名查询
 * </pre>
 * @author linriqing
 * @version 1.0, 2011-7-10
 */
public class NamedQuery extends Config
{
	private static final long serialVersionUID = 4158652935714912859L;

	/**
	 * 数据库类型
	 */
	private String dbType;
	
	/**
	 * 导入定义文件
	 */
	private String importFile;

	/**
	 * 命名
	 */
	private String name;
	
	/**
	 * sql语句
	 */
	private String sql;

	/**
	 * @return 数据库类型 
	 */
	public String getDbType()
	{
		return dbType;
	}

	/**
	 * @return 导入定义文件 
	 */
	public String getImportFile()
	{
		return importFile;
	}
	
	/**
	 * @return 命名 
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return sql语句 
	 */
	public String getSql()
	{
		return sql;
	}

	/**
	 * @param dbType 数据库类型
	 */
	public void setDbType(String dbType)
	{
		this.dbType = dbType;
	}

	/**
	 * @param importFile 导入定义文件
	 */
	public void setImportFile(String importFile)
	{
		this.importFile = importFile;
	}

	/**
	 * @param name 命名
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param sql sql语句
	 */
	public void setSql(String sql)
	{
		this.sql = sql;
	}

}
