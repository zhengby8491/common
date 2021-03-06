package com.huayin.common.persist.query.projection;

import com.huayin.common.persist.query.Projection;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class CountProjection extends BaseProjection implements Projection
{
	private String field;

	public CountProjection(String field, String alias)
	{
		super(alias);
		this.field = field;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		return "count(" + (field == null ? "*" : field) + ")" + getAliasField();
	}

}
