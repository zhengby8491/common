package com.huayin.common.persist.query.search;

import javax.persistence.Table;

import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.persist.query.exception.DynamicQueryException;

public class BaseSearch
{
	protected static final Object[] NO_VALUE = new Object[0];

	protected static String getTableName(Class<?> clz, String name, String alias, QueryType queryType)
	{
		String tableName = null;
		if (clz != null)
		{
			if (clz.isAnnotationPresent(Table.class) && queryType == QueryType.JDBC)
			{
				Table table = clz.getAnnotation(Table.class);
				if (table.name() != null && !table.name().trim().equals(""))
				{
					tableName = table.name();
				}
				else
				{
					tableName = clz.getSimpleName();
				}
			}
			else
			{
				tableName = clz.getSimpleName();
			}
		}
		else if (name != null)
		{
			tableName = name;
		}
		else
		{
			throw new DynamicQueryException("构造动态查询异常,查询的表名为空");
		}
		return tableName + ((alias == null || alias.trim().equals("")) ? "" : " " + alias);
	}
}
