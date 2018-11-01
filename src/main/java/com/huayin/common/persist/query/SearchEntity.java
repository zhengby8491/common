package com.huayin.common.persist.query;

import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

public interface SearchEntity
{
	public String toSqlString(QueryType type);
	
	public Object[] getParameters(QueryType queryType);
}
