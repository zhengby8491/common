package com.huayin.common.persist.query;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.persist.query.constants.QueryConstants.UnionType;
import com.huayin.common.persist.query.exception.DynamicQueryException;
import com.huayin.common.persist.query.model.Order;
import com.huayin.common.persist.query.model.Order.OrderType;
import com.huayin.common.persist.query.model.Union;
import com.huayin.common.persist.query.search.DefaultSearch;
import com.huayin.common.persist.query.search.SubSearch;

/**
 * <pre>
 * 动态查询工具类
 * </pre>
 * @author chenjian
 * @version 1.0, 2011-9-14
 */
public class DynamicQuery
{
	/**
	 * 条件集合
	 */
	private List<Restriction> expressionList = new ArrayList<Restriction>();

	/**
	 * 分组集合
	 */
	private Set<String> groupSet = new LinkedHashSet<String>();

	/**
	 * 分组后筛选
	 */
	private List<Restriction> havingList = new ArrayList<Restriction>();

	/**
	 * 是否查询总行数
	 */
	private Boolean isSearchTotalCount = false;

	/**
	 * 排序集合
	 */
	private Set<Order> orderSet = new LinkedHashSet<Order>();

	/**
	 * 数据开始页,只对最外层DynamicQuery起作用
	 */
	private Integer pageIndex = -1;

	/**
	 * 数据大小,只对最外层DynamicQuery起作用
	 */
	private Integer pageSize = -1;

	/**
	 * 查询的字段
	 */
	private List<Projection> projectionList = new ArrayList<Projection>();

	/**
	 * 脚本类型
	 */
	private QueryType queryType;

	/**
	 * union all查询
	 */
	private List<Union> unionQuery = new ArrayList<Union>();

	/**
	 * 关联实体的集合
	 */
	private List<SearchEntity> unionTypeList = new ArrayList<SearchEntity>();

	/**
	 * 默认忽略为Null的查询条件
	 */
	private boolean ignore = true;

	public DynamicQuery(Class<?> clz)
	{
		unionTypeList.add(new DefaultSearch(clz));
	}

	public DynamicQuery(Class<?> clz, String alias)
	{
		unionTypeList.add(new DefaultSearch(clz, alias, null));
	}

	public DynamicQuery(DynamicQuery query)
	{
		this(query, null);
	}

	public DynamicQuery(DynamicQuery query, String alias)
	{
		unionTypeList.add(new SubSearch(query, alias, null));
	}

	public DynamicQuery(String table)
	{
		unionTypeList.add(new DefaultSearch(table));
	}

	public DynamicQuery(String table, String alias)
	{
		unionTypeList.add(new DefaultSearch(table, alias, null));
	}

	/**
	 * <pre>
	 * 添加查询条件
	 * </pre>
	 * @param expression
	 * @return
	 */
	public DynamicQuery add(Restriction expression)
	{
		if (expression != null)
		{
			expressionList.add(expression);
		}
		return this;
	}

	/**
	 * <pre>
	 * 查询分组
	 * </pre>
	 * @param field
	 * @return
	 */
	public DynamicQuery addGourp(String field)
	{
		if (field != null)
			groupSet.add(field);
		return this;
	}

	/**
	 * <pre>
	 * 分组后筛选
	 * </pre>
	 * @param expression
	 * @return
	 */
	public DynamicQuery addHaving(Restriction expression)
	{
		if (expression != null)
			havingList.add(expression);
		return this;
	}

	/**
	 * <pre>
	 * 增加查询投影
	 * </pre>
	 * @param projection
	 * @return
	 */
	public DynamicQuery addProjection(Projection projection)
	{
		if (projection != null)
			projectionList.add(projection);
		return this;
	}

	/**
	 * <pre>
	 * 升序排列
	 * </pre>
	 * @param field
	 * @return
	 */
	public DynamicQuery asc(String field)
	{
		if (field != null)
		{
			Order order = new Order();
			order.setField(field);
			order.setType(OrderType.ASC);
			orderSet.add(order);
		}
		return this;
	}

	public DynamicQuery between(String propertyName, Object lo, Object hi)
	{
		return add(Restrictions.between(propertyName, lo, hi));
	}

	public DynamicQuery createAlias(Class<?> clz)
	{
		unionTypeList.add(new DefaultSearch(clz, null, JoinType.DEFAULT));
		return this;
	}

	public DynamicQuery createAlias(Class<?> clz, JoinType type, String alias, String... onExpression)
	{
		unionTypeList.add(new DefaultSearch(clz, alias, type, onExpression));
		return this;
	}

	public DynamicQuery createAlias(Class<?> clz, String alias)
	{
		unionTypeList.add(new DefaultSearch(clz, alias, JoinType.DEFAULT));
		return this;
	}

	public DynamicQuery createAlias(DynamicQuery query, JoinType type, String alias, String... onExpression)
	{
		unionTypeList.add(new SubSearch(query, alias, type, onExpression));
		return this;
	}

	public DynamicQuery createAlias(DynamicQuery query, String alias)
	{
		unionTypeList.add(new SubSearch(query, alias, JoinType.DEFAULT));
		return this;
	}

	public DynamicQuery createAlias(String entityName)
	{
		unionTypeList.add(new DefaultSearch(entityName, null, JoinType.DEFAULT));
		return this;
	}

	public DynamicQuery createAlias(String entityName, JoinType type, String alias, String... onExpression)
	{
		unionTypeList.add(new DefaultSearch(entityName, alias, type, onExpression));
		return this;
	}

	public DynamicQuery createAlias(String entityName, String alias)
	{
		unionTypeList.add(new DefaultSearch(entityName, alias, JoinType.DEFAULT));
		return this;
	}

	/**
	 * <pre>
	 * 降序排列
	 * </pre>
	 * @param field
	 * @return
	 */
	public DynamicQuery desc(String field)
	{
		if (field != null)
		{
			Order order = new Order();
			order.setField(field);
			order.setType(OrderType.DESC);
			orderSet.add(order);
		}
		return this;
	}

	public DynamicQuery eq(String propertyName, Object value)
	{
		return add(Restrictions.eq(propertyName, value));
	}

	public DynamicQuery eqProperty(String propertyName1, String propertyName2)
	{
		return add(Restrictions.eqProperty(propertyName1, propertyName2));
	}

	public DynamicQuery exists(DynamicQuery query)
	{
		return add(Restrictions.exists(query));
	}

	public DynamicQuery ge(String propertyName, Object value)
	{
		return add(Restrictions.ge(propertyName, value));
	}

	/**
	 * @return expressionList
	 */
	public List<Restriction> getExpressionList()
	{
		return expressionList;
	}

	/**
	 * @return groupSet
	 */
	public Set<String> getGroupSet()
	{
		return groupSet;
	}

	/**
	 * @return havingList
	 */
	public List<Restriction> getHavingList()
	{
		return havingList;
	}

	public Boolean getIsSearchTotalCount()
	{
		return isSearchTotalCount;
	}

	/**
	 * @return orderSet
	 */
	public Set<Order> getOrderSet()
	{
		return orderSet;
	}

	public Integer getPageIndex()
	{
		return pageIndex;
	}

	public Integer getPageSize()
	{
		return pageSize;
	}

	/**
	 * @return projectionList
	 */
	public List<Projection> getProjectionList()
	{
		return projectionList;
	}

	/**
	 * <pre>
	 * 默认构造JPQL语句
	 * </pre>
	 * @return
	 */
	public QueryType getQueryType()
	{
		return queryType == null ? QueryType.JPQL : queryType;
	}

	public SqlResult getSqlResult()
	{
		SqlResult result = new SqlResult();
		// 拼装查询sql语句
		StringBuilder sb = new StringBuilder("");
		QueryType type = getQueryType();
		if (type == QueryType.JDBC)
		{
			BluidSql.bluidTop(this, projectionList, sb, type);
			BluidSql.bluidTable(this, unionTypeList, sb, result.getParamsList(), type);
			// BluidSql.bluidOn(this, onExpressionList, sb, result.getParamsList(), type);
			BluidSql.bluidWhere(this, expressionList, sb, result.getParamsList(), type);
			BluidSql.bluidGroup(this, groupSet, sb, result.getParamsList(), type);
			BluidSql.bluidHaving(this, havingList, sb, result.getParamsList(), type);
			BluidSql.bluidOrder(this, orderSet, sb, result.getParamsList(), type);
			for (int i = 0; i < unionQuery.size(); i++)
			{
				Union union = unionQuery.get(i);
				DynamicQuery query = union.getDynamicQuery();
				UnionType _type = union.getUnionType();
				query.setQueryType(type);
				sb.append(" " + _type.getValue() + " ");
				SqlResult _result = query.getSqlResult();
				sb.append(_result.getSql());
				if (_result.getParamsList().size() > 0)
				{
					result.getParamsList().addAll(_result.getParamsList());
				}
			}
		}
		else if (type == QueryType.JPQL)
		{
			BluidSql.bluidJPQLTop(this, projectionList, sb, type);
			BluidSql.bluidTable(this, unionTypeList, sb, result.getParamsList(), type);
			BluidSql.bluidWhere(this, expressionList, sb, result.getParamsList(), type);
			BluidSql.bluidGroup(this, groupSet, sb, result.getParamsList(), type);
			BluidSql.bluidHaving(this, havingList, sb, result.getParamsList(), type);
			BluidSql.bluidOrder(this, orderSet, sb, result.getParamsList(), type);
			if (unionQuery.size() > 0)
			{
				throw new DynamicQueryException("JPQL查询不支持Union连接查询");
			}
		}
		if (this.getIsSearchTotalCount() && unionQuery.size() > 0)
		{
			throw new DynamicQueryException("Union连接查询不支持自动查询记录数");
		}
		if (this.getIsSearchTotalCount())
		{
			// 拼装查询记录数的sql语句
			StringBuilder recordCountSql = new StringBuilder("");
			List<Projection> projections = new ArrayList<Projection>();
			projections.add(Projections.count("*"));
			BluidSql.bluidTop(this, projections, recordCountSql, type);
			BluidSql.bluidTable(this, unionTypeList, recordCountSql, result.getRecordParamsList(), type);
			BluidSql.bluidWhere(this, expressionList, recordCountSql, result.getRecordParamsList(), type);
			result.setRecordCountSql(convertSql(recordCountSql.toString()));
		}
		result.setSql(convertSql(sb.toString()));
		result.setPageIndex(getPageIndex());
		result.setPageSize(getPageSize());
		result.setQueryType(type);
		return result;
	}

	/**
	 * @return unionQuery
	 */
	public List<Union> getUnionQuery()
	{
		return unionQuery;
	}

	/**
	 * @return unionTypeList
	 */
	public List<SearchEntity> getUnionTypeList()
	{
		return unionTypeList;
	}

	public DynamicQuery gt(String propertyName, Object value)
	{
		return add(Restrictions.gt(propertyName, value));
	}

	public DynamicQuery in(String field, DynamicQuery query)
	{
		return add(Restrictions.in(field, query));
	}

	public DynamicQuery in(String propertyName, Object... params)
	{
		return add(Restrictions.in(propertyName, params));
	}

	public DynamicQuery inArray(String propertyName, Object[] params)
	{
		return add(Restrictions.inArray(propertyName, params));
	}

	public DynamicQuery isNotNull(String propertyName)
	{
		return add(Restrictions.isNotNull(propertyName));
	}

	public DynamicQuery isNull(String propertyName)
	{
		return add(Restrictions.isNull(propertyName));
	}

	public DynamicQuery le(String propertyName, Object value)
	{
		return add(Restrictions.le(propertyName, value));
	}

	public DynamicQuery like(String propertyName, Object value)
	{
		return add(Restrictions.like(propertyName, value));
	}

	public DynamicQuery lt(String propertyName, Object value)
	{
		return add(Restrictions.lt(propertyName, value));
	}

	public DynamicQuery ne(String propertyName, Object value)
	{
		return add(Restrictions.ne(propertyName, value));
	}

	public DynamicQuery notExists(DynamicQuery query)
	{
		return add(Restrictions.notExists(query));
	}

	/*
	 * public DynamicQuery on(Restriction expression) { if (expression != null) onExpressionList.add(expression); return
	 * this; }
	 */
	/**
	 * <pre>
	 * 删除条件
	 * </pre>
	 * @param expression
	 */
	public void remove(Restriction expression)
	{
		if (expression != null)
			expressionList.remove(expression);
	}

	public void setIsSearchTotalCount(Boolean isSearchTotalCount)
	{
		this.isSearchTotalCount = isSearchTotalCount;
	}

	public void setPageIndex(Integer pageIndex)
	{
		this.pageIndex = pageIndex;
	}

	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	public void setQueryType(QueryType queryType)
	{
		this.queryType = queryType;
	}

	public DynamicQuery union(DynamicQuery query)
	{
		Union union = new Union();
		union.setDynamicQuery(query);
		union.setUnionType(UnionType.UNION);
		unionQuery.add(union);
		return this;
	}

	public DynamicQuery unionAll(DynamicQuery query)
	{
		Union union = new Union();
		union.setDynamicQuery(query);
		union.setUnionType(UnionType.UNION_ALL);
		unionQuery.add(union);
		return this;
	}

	public void ignoreEmpty()
	{
		ignore = true;
	}

	public void unignoreEmpty()
	{
		ignore = false;
	}

	public boolean getIgnore()
	{
		return this.ignore;
	}

	public static String convertSql(String sql)
	{
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotEmpty(sql))
		{
			try
			{
				Pattern p = Pattern.compile("(\\?\\d*)");
				Matcher m = p.matcher(sql);

				int i = 0;
				while (m.find())
				{
					m.appendReplacement(sb, "?" + (++i));
				}
				m.appendTail(sb);
				//System.out.println(sb);

			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/*public static void main(String[] args)
	{
		System.out.println(convertSql("select ?2?4"));
	}*/
}
