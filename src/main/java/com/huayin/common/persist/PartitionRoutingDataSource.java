/**
 * <pre>
 * Title: 		PartitionRoutingDataSource.java
 * Author:		linriqing
 * Create:	 	2010-6-11 上午09:20:29
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * <pre>
 * 数据库垂直/水平分区路由数据源
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-6-11
 */
public class PartitionRoutingDataSource extends AbstractRoutingDataSource
{
	@Override
	protected Object determineCurrentLookupKey()
	{
		return getDbContextByPassport();
	}

	private Object getDbContextByPassport()
	{
		return ClientContextHolder.getClientAction().getDbLocation().name();
	}
}
