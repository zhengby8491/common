package com.huayin.common.persist.query.expression;

import com.huayin.common.persist.query.Restriction;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.persist.query.exception.DynamicQueryException;

public class InExpression extends BaseExpression implements Restriction
{

	private Object[] parameters;

	private String field;

	public InExpression(String field, Object... objects)
	{
		this.field = field;
		this.parameters = objects;
	}

	/**
	 * @return field
	 */
	public String getField()
	{
		return field;
	}

	@Override
	public Object[] getParameters()
	{
		return parameters;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		if (parameters == null || parameters.length <= 0)
		{
			throw new DynamicQueryException("动态In字句查询，参数不能为空");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(field + " in (");
		for (int i = 0; i < parameters.length; i++)
		{
			if (i == 0)
			{
				sb.append("?");
			}
			else
			{
				sb.append(",?");
			}
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public Boolean isIgnore()
	{
		if (parameters == null || parameters.length <= 0)
		{
			return true;
		}
		return false;
	}
}
