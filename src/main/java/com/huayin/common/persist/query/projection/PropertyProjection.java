package com.huayin.common.persist.query.projection;

import com.huayin.common.persist.query.Projection;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public class PropertyProjection extends BaseProjection implements Projection
{
	private String field;

	public PropertyProjection(String filed, String alias)
	{
		super(alias);
		this.field = filed;
	}

	@Override
	public String toSqlString(QueryType type)
	{
		return this.field + getAliasField();
	}

}
