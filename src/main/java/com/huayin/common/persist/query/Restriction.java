package com.huayin.common.persist.query;

import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

/**
 * <pre>
 * 查询的条件表达式
 * </pre>
 * @author chenjian
 * @version 1.0, 2011-7-28
 */
public interface Restriction
{
	public String toSqlString(QueryType type);

	public Object[] getParameters();

	public Boolean isIgnore();
}
