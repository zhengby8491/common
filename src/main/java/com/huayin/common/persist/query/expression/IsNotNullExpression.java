package com.huayin.common.persist.query.expression;

import com.huayin.common.persist.query.Restriction;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class IsNotNullExpression extends BaseExpression implements Restriction
{
	private String filed;

	public IsNotNullExpression(String filed)
	{
		this.filed = filed;
	}

	@Override
	public Object[] getParameters()
	{
		return NO_VALUE;
	}

	/**
	 * @return filed
	 */
	public String getFiled()
	{
		return filed;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		return filed + " is not null";
	}

}
