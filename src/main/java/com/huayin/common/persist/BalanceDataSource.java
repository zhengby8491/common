/**
 * <pre>
 * Title: 		BalanceDataSource.java
 * Author:		linriqing
 * Create:	 	2010-6-11 上午09:38:28
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.util.Assert;

/**
 * <pre>
 * 负载均衡自动路由数据源
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-6-11
 */
public class BalanceDataSource extends AbstractRoutingDataSource
{
	private Object[] targetDataSourcesKeys = null;

	private static final ThreadLocal<Integer> contextHolder = new ThreadLocal<Integer>();

	public static void setCurrentLookupKeyIndex(Integer currentLookupKeyIndex)
	{
		Assert.notNull(currentLookupKeyIndex, "current LookupKey cannot be null");
		contextHolder.set(currentLookupKeyIndex);
	}

	public static Integer getCurrentLookupKeyIndex()
	{
		Integer i = (Integer) contextHolder.get();
		if (i == null)
		{
			setCurrentLookupKeyIndex(0);
			return new Integer(0);
		}
		else
		{
			return i;
		}
	}

	public static void clearCurrentLookupKeyIndex()
	{
		contextHolder.remove();
	}

	@Override
	protected Object determineCurrentLookupKey()
	{
		return targetDataSourcesKeys[getCurrentLookupKeyIndex().intValue()];
	}

	@Override
	public Connection getConnection() throws SQLException
	{
		setCurrentLookupKeyIndex(0);
		for (int i = 0; i < targetDataSourcesKeys.length;)
		{
			try
			{
				Connection conn = determineTargetDataSource().getConnection();
				// 获取连接成功
				return conn;
			}
			catch (Exception e)
			{
				// 获取连接失败
				setCurrentLookupKeyIndex(i);
				i++;
			}
		}
		return determineTargetDataSource().getConnection();
	}

	@Override
	public void setDataSourceLookup(DataSourceLookup dataSourceLookup)
	{
		super.setDataSourceLookup(dataSourceLookup);
	}

	@Override
	public void setDefaultTargetDataSource(Object defaultTargetDataSource)
	{
		super.setDefaultTargetDataSource(defaultTargetDataSource);
	}

	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources)
	{
		this.targetDataSourcesKeys = targetDataSources.keySet().toArray();
		setCurrentLookupKeyIndex(0);
		super.setTargetDataSources(targetDataSources);
	}

}
