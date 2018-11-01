package com.huayin.common.persist.query.expression;

import com.huayin.common.persist.query.Restriction;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class PropertyExpression extends BaseExpression implements Restriction
{
	private String field;

	private String other;

	private String op;

	public PropertyExpression(String field, String other, String op)
	{
		this.field = field;
		this.other = other;
		this.op = op;
	}

	/**
	 * @return field
	 */
	public String getField()
	{
		return field;
	}

	/**
	 * @return other
	 */
	public String getOther()
	{
		return other;
	}

	/**
	 * @return op
	 */
	public String getOp()
	{
		return op;
	}

	@Override
	public Object[] getParameters()
	{
		return NO_VALUE;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(field);
		sb.append(" " + op + " ");
		sb.append(other);
		return sb.toString();
	}
}
