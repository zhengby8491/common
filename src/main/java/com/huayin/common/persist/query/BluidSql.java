package com.huayin.common.persist.query;

import java.util.List;
import java.util.Set;

import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.persist.query.exception.DynamicQueryException;
import com.huayin.common.persist.query.model.Order;

public class BluidSql
{

	public static void bluidTop(DynamicQuery query, List<Projection> projectionList, StringBuilder sb,
			QueryType queryType)
	{
		sb.append("select");
		if (projectionList == null || projectionList.size() <= 0)
		{
			sb.append(" *");
		}
		for (int i = 0; i < projectionList.size(); i++)
		{
			Projection projection = projectionList.get(i);
			String sql = projection.toSqlString(queryType);
			if (sql == null || sql.equals(""))
			{
				continue;
			}
			if (i == 0)
			{
				sb.append(" ");
				sb.append(sql);
			}
			else
			{
				sb.append(",");
				sb.append(sql);
			}
		}
	}

	public static void bluidJPQLTop(DynamicQuery query, List<Projection> projectionList, StringBuilder sb,
			QueryType queryType)
	{
		if (projectionList == null || projectionList.size() <= 0)
			return;
		sb.append("select");
		for (int i = 0; i < projectionList.size(); i++)
		{
			Projection projection = projectionList.get(i);
			String sql = projection.toSqlString(queryType);
			if (sql == null || sql.equals(""))
			{
				continue;
			}
			if (i == 0)
			{
				sb.append(" ");
				sb.append(sql);
			}
			else
			{
				sb.append(",");
				sb.append(sql);
			}
		}
	}

	public static void bluidTable(DynamicQuery query, List<SearchEntity> searchEntity, StringBuilder sb,
			List<Object> paramsList, QueryType queryType)
	{
		if (searchEntity == null || searchEntity.size() < 1)
		{
			throw new DynamicQueryException("构造动态查询异常,不存在查询的对象");
		}
		sb.append(" from");
		for (int i = 0; i < searchEntity.size(); i++)
		{
			SearchEntity entity = searchEntity.get(i);
			sb.append(" ");
			sb.append(entity.toSqlString(queryType));
			Object[] params = entity.getParameters(queryType);
			for (int j = 0; j < params.length; j++)
			{
				paramsList.add(params[j]);
			}
		}
	}

	public static void bluidOn(DynamicQuery query, List<Restriction> onExpressionList, StringBuilder sb,
			List<Object> paramsList, QueryType queryType)
	{
		for (int i = 0; i < onExpressionList.size(); i++)
		{
			Restriction expression = onExpressionList.get(i);
			if (i == 0)
			{
				sb.append(" on ");
				sb.append(expression.toSqlString(queryType));
			}
			else
			{
				sb.append(" and ");
				sb.append(expression.toSqlString(queryType));
			}
			for (Object params : expression.getParameters())
			{
				paramsList.add(params);
			}
		}
	}

	public static void bluidWhere(DynamicQuery query, List<Restriction> expressionList, StringBuilder sb,
			List<Object> paramsList, QueryType queryType)
	{
		if (expressionList != null && expressionList.size() > 0)
		{
			boolean flag = true;
			for (int i = 0; i < expressionList.size(); i++)
			{
				Restriction expression = expressionList.get(i);
				if (query.getIgnore() && expression.isIgnore())
				{
					continue;
				}
				String sql = expression.toSqlString(queryType);
				if (sql == null || sql.equals(""))
				{
					continue;
				}
				if (flag)
				{
					sb.append(" where");
					sb.append(" ");
					sb.append(sql);
					flag = false;
				}
				else
				{
					sb.append(" and ");
					sb.append(sql);
				}
				for (Object params : expression.getParameters())
				{
					paramsList.add(params);
				}
			}
		}
	}

	public static void bluidGroup(DynamicQuery query, Set<String> groupSet, StringBuilder sb, List<Object> paramsList,
			QueryType queryType)
	{
		boolean tr = false;
		for (String field : groupSet)
		{
			if (!tr)
			{
				sb.append(" group by ");
				sb.append(field);
				tr = true;
			}
			else
			{
				sb.append(",");
				sb.append(field);
			}
		}
	}

	public static void bluidHaving(DynamicQuery query, List<Restriction> havingList, StringBuilder sb,
			List<Object> paramsList, QueryType queryType)
	{
		for (int i = 0; i < havingList.size(); i++)
		{
			Restriction expression = havingList.get(i);
			if (i == 0)
			{
				sb.append(" having ");
				sb.append(expression.toSqlString(queryType));
			}
			else
			{
				sb.append(" and ");
				sb.append(expression.toSqlString(queryType));
			}
			for (Object params : expression.getParameters())
			{
				paramsList.add(params);
			}
		}
	}

	public static void bluidOrder(DynamicQuery query, Set<Order> orderSet, StringBuilder sb, List<Object> paramsList,
			QueryType queryType)
	{
		boolean tr = false;
		for (Order order : orderSet)
		{
			if (!tr)
			{
				sb.append(" order by " + order.getField() + " " + order.getType().name());
				tr = true;
			}
			else
			{
				sb.append(" , " + order.getField() + " " + order.getType().name());
			}
		}
	}
}
