package com.huayin.common.persist.query.projection;

public abstract class BaseProjection
{
	private String alias;

	public BaseProjection(String alias)
	{
		this.alias = alias;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getAliasField()
	{
		return (this.alias == null || this.alias.trim().equals("")) ? "" : " as " + this.alias;
	}
}
