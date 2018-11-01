package com.huayin.common.persist.query.projection;

import com.huayin.common.persist.query.Projection;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class SqlProjection extends BaseProjection implements Projection
{
	private String sql;

	public SqlProjection(String sql, String alias)
	{
		super(alias);
		this.sql = sql;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		return sql;
	}

}
