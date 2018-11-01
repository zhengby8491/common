package com.huayin.common.persist.query.expression;

import com.huayin.common.persist.query.Restriction;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

/**
 * <pre>
 * 原生sql条件支持
 * </pre>
 * @author chenjian
 * @version 1.0, 2011-7-28
 */
public class SqlExpression extends BaseExpression implements Restriction
{
	private String sql;

	private Object[] parameters;

	public SqlExpression(String sql, Object... objs)
	{
		this.sql = sql;
		this.parameters = objs;
	}

	@Override
	public Object[] getParameters()
	{
		return parameters;
	}

	/**
	 * @return sql
	 */
	public String getSql()
	{
		return sql;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		return sql;
	}
}
