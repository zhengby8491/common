package com.huayin.common.persist.query.search;

import com.huayin.common.persist.query.SearchEntity;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class DefaultSearch extends BaseSearch implements SearchEntity
{
	private JoinType type;

	private Class<?> clz;

	private String tableName;

	private String alias;

	/**
	 * 连接查询on条件
	 */
	private String[] onExpression= {};
	
	public DefaultSearch(Class<?> clz)
	{
		this.clz = clz;
	}

	public DefaultSearch(String tableName)
	{
		this.tableName = tableName;
	}

	public DefaultSearch(Class<?> clz, String alias, JoinType type,String... onExpression)
	{
		this(clz);
		this.alias = alias;
		this.type = type;
		this.onExpression=onExpression;
	}

	public DefaultSearch(String tableName, String alias, JoinType type,String... onExpression)
	{
		this(tableName);
		this.alias = alias;
		this.type = type;
		this.onExpression=onExpression;
	}

	@Override
	public Object[] getParameters(QueryType queryType)
	{
		return NO_VALUE;
	}

	@Override
	public String toSqlString(QueryType queryType)
	{
		String searchName = super.getTableName(clz, tableName, alias, queryType);
		/*if (queryType == QueryType.JPQL && type != null && type != JoinType.DEFAULT)
		{
			throw new DynamicQueryException("构造动态查询JPQL异常,不支持的连接查询{" + type + "}");
		}*/
		if (type == null)
		{
			return searchName;
		}
		else
		{
			StringBuffer sb=new StringBuffer();
			boolean flag = true;
			for(String exp:onExpression)
			{
				if(flag)
				{
					sb.append(" on ");
					sb.append(exp);
					flag=false;
				}else
				{
					sb.append(" and ");
					sb.append(exp);
				}
			}
			return type.getValue() + " " + searchName + sb.toString();
		}
	}
}
