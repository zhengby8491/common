package com.huayin.common.persist.query.expression;

public abstract class BaseExpression
{
	protected static final Object[] NO_VALUE = new Object[0];

	/**
	 * <pre>
	 * 默认不忽略查询条件
	 * </pre>
	 * @return
	 */
	public Boolean isIgnore()
	{
		return false;
	}
}
