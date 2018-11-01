package com.huayin.common.persist.query;

import com.huayin.common.persist.query.expression.BetweenExpression;
import com.huayin.common.persist.query.expression.ExistsExpression;
import com.huayin.common.persist.query.expression.InExpression;
import com.huayin.common.persist.query.expression.IsNotNullExpression;
import com.huayin.common.persist.query.expression.IsNullExpression;
import com.huayin.common.persist.query.expression.LogicalExpression;
import com.huayin.common.persist.query.expression.PropertyExpression;
import com.huayin.common.persist.query.expression.SimpleExpression;
import com.huayin.common.persist.query.expression.SubqueryExpression;

/**
 * <pre>
 * 条件封装工具类
 * </pre>
 * @author chenjian
 * @version 1.0, 2011-7-28
 */
public class Restrictions
{
	public static Restriction eq(String propertyName, Object value)
	{
		return new SimpleExpression(propertyName, value, "=");
	}

	public static Restriction eqProperty(String propertyName1, String propertyName2)
	{
		return new PropertyExpression(propertyName1, propertyName2, "=");
	}

	public static Restriction gtProperty(String propertyName1, String propertyName2)
	{
		return new PropertyExpression(propertyName1, propertyName2, ">");
	}

	public static Restriction geProperty(String propertyName1, String propertyName2)
	{
		return new PropertyExpression(propertyName1, propertyName2, ">=");
	}

	public static Restriction ltProperty(String propertyName1, String propertyName2)
	{
		return new PropertyExpression(propertyName1, propertyName2, "<");
	}

	public static Restriction leProperty(String propertyName1, String propertyName2)
	{
		return new PropertyExpression(propertyName1, propertyName2, "<=");
	}

	public static Restriction ne(String propertyName, Object value)
	{
		return new SimpleExpression(propertyName, value, "<>");
	}

	public static Restriction like(String propertyName, Object value)
	{
		return new SimpleExpression(propertyName, value, "like");
	}

	public static Restriction gt(String propertyName, Object value)
	{
		return new SimpleExpression(propertyName, value, ">");
	}

	public static Restriction lt(String propertyName, Object value)
	{
		return new SimpleExpression(propertyName, value, "<");
	}

	public static Restriction le(String propertyName, Object value)
	{
		return new SimpleExpression(propertyName, value, "<=");
	}

	public static Restriction ge(String propertyName, Object value)
	{
		return new SimpleExpression(propertyName, value, ">=");
	}

	public static Restriction isNull(String propertyName)
	{
		return new IsNullExpression(propertyName);
	}

	public static Restriction isNotNull(String propertyName)
	{
		return new IsNotNullExpression(propertyName);
	}

	public static Restriction in(String propertyName, Object... params)
	{
		return new InExpression(propertyName, params);
	}

	public static Restriction inArray(String propertyName, Object[] params)
	{
		return new InExpression(propertyName, params);
	}

	public static Restriction between(String propertyName, Object lo, Object hi)
	{
		return new BetweenExpression(propertyName, lo, hi);
	}

	public static Restriction and(Restriction... expressions)
	{
		return new LogicalExpression("and", expressions);
	}

	public static Restriction or(Restriction... expressions)
	{
		return new LogicalExpression("or", expressions);
	}

	public static Restriction exists(DynamicQuery query)
	{
		return new ExistsExpression("exists", query);
	}

	public static Restriction notExists(DynamicQuery query)
	{
		return new ExistsExpression("not exists", query);
	}

	public static Restriction in(String field, DynamicQuery query)
	{
		return new SubqueryExpression(field, "in", query);
	}
}
