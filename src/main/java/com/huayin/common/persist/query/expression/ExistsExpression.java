package com.huayin.common.persist.query.expression;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Restriction;
import com.huayin.common.persist.query.SqlResult;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class ExistsExpression extends BaseExpression implements Restriction
{
	private DynamicQuery query;

	private Object[] parameters;

	private String op;

	/**
	 * @return query
	 */
	public DynamicQuery getQuery()
	{
		return query;
	}

	/**
	 * @return op
	 */
	public String getOp()
	{
		return op;
	}

	public ExistsExpression(String op, DynamicQuery query)
	{
		this.op = op;
		this.query = query;
	}

	@Override
	public Object[] getParameters()
	{
		return parameters;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		StringBuilder sb = new StringBuilder("");
		if (query != null)
		{
			query.setQueryType(type);
			SqlResult result = query.getSqlResult();
			sb.append(op).append(" (");
			sb.append(result.getSql());
			parameters = result.getParamsList().toArray();
			sb.append(")");
		}
		return sb.toString();
	}
}
