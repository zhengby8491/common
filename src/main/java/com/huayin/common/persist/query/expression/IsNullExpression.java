package com.huayin.common.persist.query.expression;

import com.huayin.common.persist.query.Restriction;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class IsNullExpression extends BaseExpression implements Restriction
{
	private String filed;

	public IsNullExpression(String filed)
	{
		this.filed = filed;
	}

	/**
	 * @return filed
	 */
	public String getFiled()
	{
		return filed;
	}

	@Override
	public Object[] getParameters()
	{
		return NO_VALUE;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		return filed + " is null";
	}
}
