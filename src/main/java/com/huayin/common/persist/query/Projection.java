package com.huayin.common.persist.query;

import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

/**
 * <pre>
 * 查询字段投影
 * </pre>
 * @author chenjian
 * @version 1.0, 2011-7-28
 */
public interface Projection
{
	public String toSqlString(QueryType type);
}
