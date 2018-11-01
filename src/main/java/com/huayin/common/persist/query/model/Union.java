package com.huayin.common.persist.query.model;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.constants.QueryConstants.UnionType;

public class Union
{
	private UnionType unionType;

	private DynamicQuery dynamicQuery;

	public UnionType getUnionType()
	{
		return unionType;
	}

	public void setUnionType(UnionType unionType)
	{
		this.unionType = unionType;
	}

	public DynamicQuery getDynamicQuery()
	{
		return dynamicQuery;
	}

	public void setDynamicQuery(DynamicQuery dynamicQuery)
	{
		this.dynamicQuery = dynamicQuery;
	}
}
