package com.huayin.common.persist.query.projection;

import com.huayin.common.persist.query.Projection;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class SumProjection extends BaseProjection implements Projection
{
	private String field;

	public SumProjection(String field, String alias)
	{
		super(alias);
		this.field = field;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		return "sum(" + field + ")" + getAliasField();
	}

}
