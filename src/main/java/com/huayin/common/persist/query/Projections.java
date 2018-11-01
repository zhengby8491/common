package com.huayin.common.persist.query;

import com.huayin.common.persist.query.projection.AvgProjection;
import com.huayin.common.persist.query.projection.CountProjection;
import com.huayin.common.persist.query.projection.MaxProjection;
import com.huayin.common.persist.query.projection.MinProjection;
import com.huayin.common.persist.query.projection.PropertyProjection;
import com.huayin.common.persist.query.projection.SumProjection;

/**
 * <pre>
 * 查询字段工具类
 * </pre>
 * @author chenjian
 * @version 1.0, 2011-7-29
 */
public class Projections
{
	public static Projection property(String property)
	{
		return new PropertyProjection(property, null);
	}
	public static Projection property(String property, String alias)
	{
		return new PropertyProjection(property, alias);
	}

	public static Projection sum(String property)
	{
		return new SumProjection(property, null);
	}

	public static Projection sum(String property, String alias)
	{
		return new SumProjection(property, alias);
	}

	public static Projection min(String property)
	{
		return new MinProjection(property, null);
	}

	public static Projection min(String property, String alias)
	{
		return new MinProjection(property, alias);
	}

	public static Projection max(String property)
	{
		return new MaxProjection(property, null);
	}

	public static Projection max(String property, String alias)
	{
		return new MaxProjection(property, alias);
	}

	public static Projection count()
	{
		return new CountProjection(null, null);
	}

	public static Projection count(String property)
	{
		return new CountProjection(property, null);
	}

	public static Projection count(String property, String alias)
	{
		return new CountProjection(property, alias);
	}

	public static Projection avg(String property)
	{
		return new AvgProjection(property, null);
	}

	public static Projection avg(String property, String alias)
	{
		return new AvgProjection(property, alias);
	}
}
