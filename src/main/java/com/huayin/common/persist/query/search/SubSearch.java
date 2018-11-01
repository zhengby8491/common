package com.huayin.common.persist.query.search;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchEntity;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.persist.query.exception.DynamicQueryException;

/**
 * <pre>
 * 嵌套查询
 * </pre>
 * @author chenjian
 * @version 1.0, 2012-4-18
 */
public class SubSearch extends BaseSearch implements SearchEntity
{
	private DynamicQuery query;

	private JoinType type;

	private String alias;

	/**
	 * 连接查询on条件
	 */
	private String[] onExpression= {};
	public SubSearch(DynamicQuery query)
	{
		this.query = query;
	}

	public SubSearch(DynamicQuery query, String alias, JoinType type,String... onExpression)
	{
		this(query);
		this.type = type;
		this.alias = alias;
		this.onExpression=onExpression;
	}

	@Override
	public Object[] getParameters(QueryType queryType)
	{
		query.setQueryType(queryType);
		return query.getSqlResult().getParamsList().toArray();
	}

	@Override
	public String toSqlString(QueryType queryType)
	{
		if (queryType == QueryType.JPQL)
		{
			throw new DynamicQueryException("构造动态查询JPQL异常,JPQL不支持嵌套查询");
		}
		query.setQueryType(queryType);
		String ql = "(" + query.getSqlResult().getSql() + ")";
		String searchName = ((alias == null || alias.trim().equals("")) ? ql : ql + alias);
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
