package com.huayin.common.persist.query;

import java.util.ArrayList;
import java.util.List;

import com.huayin.common.persist.query.constants.QueryConstants.QueryType;

/**
 * <pre>
 * 最终构造成的sql以及参数列表
 * </pre>
 * @author chenjian
 * @version 1.0, 2011-7-29
 */
public class SqlResult
{
	private String sql;

	private String recordCountSql;

	private QueryType queryType;

	private Integer pageIndex = -1;

	private Integer pageSize = -1;

	private List<Object> paramsList = new ArrayList<Object>();

	private List<Object> recordParamsList = new ArrayList<Object>();

	public String getSql()
	{
		return sql;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	public List<Object> getParamsList()
	{
		return paramsList;
	}

	public void setParamsList(List<Object> paramsList)
	{
		this.paramsList = paramsList;
	}

	public Integer getPageIndex()
	{
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex)
	{
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	public QueryType getQueryType()
	{
		return queryType;
	}

	public void setQueryType(QueryType queryType)
	{
		this.queryType = queryType;
	}

	public String getRecordCountSql()
	{
		return recordCountSql;
	}

	public void setRecordCountSql(String recordCountSql)
	{
		this.recordCountSql = recordCountSql;
	}

	public List<Object> getRecordParamsList()
	{
		return recordParamsList;
	}

	public void setRecordParamsList(List<Object> recordParamsList)
	{
		this.recordParamsList = recordParamsList;
	}
}
