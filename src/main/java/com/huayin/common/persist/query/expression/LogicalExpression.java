package com.huayin.common.persist.query.expression;

import java.util.ArrayList;
import java.util.List;

import com.huayin.common.persist.query.Restriction;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

/**
 * <pre>
 * 逻辑查询符号and ，or
 * </pre>
 * @author chenjian
 * @version 1.0, 2011-7-28
 */
public class LogicalExpression extends BaseExpression implements Restriction
{
	private Restriction[] expression;

	/**
	 * @return expression
	 */
	public Restriction[] getExpression()
	{
		return expression;
	}

	/**
	 * @return op
	 */
	public String getOp()
	{
		return op;
	}

	private String op;

	public LogicalExpression(String op, Restriction... expressions)
	{
		this.op = op;
		this.expression = expressions;
	}

	@Override
	public Object[] getParameters()
	{
		if (expression == null || expression.length <= 0)
			return NO_VALUE;
		List<Object> params = new ArrayList<Object>();
		for (int i = 0; i < expression.length; i++)
		{
			Restriction express = expression[i];
			for (int j = 0; j < express.getParameters().length; j++)
			{
				params.add(express.getParameters()[j]);
			}
		}
		return params.toArray(new Object[params.size()]);
	}

	@Override
	public String toSqlString(QueryType type)
	{
		StringBuilder sb = new StringBuilder();
		if (expression == null || expression.length <= 0)
		{
			return "";
		}
		sb.append("(");
		for (int i = 0; i < expression.length; i++)
		{
			Restriction express = expression[i];
			if (i == 0)
			{
				sb.append(express.toSqlString(type));
			}
			else
			{
				sb.append(" " + op + " " + express.toSqlString(type));
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
