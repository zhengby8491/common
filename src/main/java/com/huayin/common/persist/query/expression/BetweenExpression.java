package com.huayin.common.persist.query.expression;

import com.huayin.common.persist.query.Restriction;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class BetweenExpression extends BaseExpression implements Restriction
{
	private String field;

	private Object lo;

	private Object hi;

	/**
	 * @return field
	 */
	public String getField()
	{
		return field;
	}

	/**
	 * @return lo
	 */
	public Object getLo()
	{
		return lo;
	}

	/**
	 * @return hi
	 */
	public Object getHi()
	{
		return hi;
	}

	public BetweenExpression(String field, Object lo, Object hi)
	{
		this.field = field;
		this.lo = lo;
		this.hi = hi;
	}

	@Override
	public Object[] getParameters()
	{
		return new Object[] { lo, hi };
	}

	@Override
	public String toSqlString(QueryType type)
	{
		return field + " between ? and ?";
	}

}
