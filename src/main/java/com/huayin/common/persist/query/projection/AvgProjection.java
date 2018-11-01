package com.huayin.common.persist.query.projection;

import com.huayin.common.persist.query.Projection;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class AvgProjection extends BaseProjection implements Projection
{
	private String field;

	public AvgProjection(String field, String alias)
	{
		super(alias);
		this.field = field;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		return "avg(" + field + ")" + getAliasField();
	}

}
