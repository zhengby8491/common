package com.huayin.common.persist.query.expression;

import com.huayin.common.persist.query.Restriction;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class SimpleExpression extends BaseExpression implements Restriction
{
	private String field;

	private Object value;

	private String operate;

	public SimpleExpression(String filed, Object value, String operate)
	{
		this.field = filed;
		this.value = value;
		this.operate = operate;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(field);
		sb.append(" " + operate + " ");
		sb.append("?");
		return sb.toString();
	}

	@Override
	public Object[] getParameters()
	{
		return new Object[] { value };
	}

	/**
	 * @return field
	 */
	public String getField()
	{
		return field;
	}

	/**
	 * @return value
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * @return operate
	 */
	public String getOperate()
	{
		return operate;
	}

	@Override
	public Boolean isIgnore()
	{
		if (value == null)
		{
			return true;
		}
		return false;
	}
}
