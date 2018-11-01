package com.huayin.common.persist.query.expression;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Restriction;
import com.huayin.common.persist.query.SqlResult;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class SubqueryExpression extends BaseExpression implements Restriction
{
	private String field;

	private DynamicQuery query;

	/**
	 * @return field
	 */
	public String getField()
	{
		return field;
	}

	/**
	 * @return query
	 */
	public DynamicQuery getQuery()
	{
		return query;
	}

	/**
	 * @return operate
	 */
	public String getOperate()
	{
		return operate;
	}

	private String operate;

	private Object[] parameters;

	public SubqueryExpression(String field, String operate, DynamicQuery query)
	{
		this.field = field;
		this.operate = operate;
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
		StringBuilder sb = new StringBuilder();
		sb.append(field);
		sb.append(" " + operate + " (");
		query.setQueryType(type);

		SqlResult result = query.getSqlResult();
		sb.append(result.getSql());
		parameters = result.getParamsList().toArray();
		sb.append(")");
		return sb.toString();
	}
}
