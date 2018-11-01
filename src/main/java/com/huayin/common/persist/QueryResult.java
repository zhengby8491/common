/**
 * <pre>
 * Title: 		QueryParameter.java
 * Author:		zhaojitao
 * Create:	 	2012-3-4 下午02:52:32
 * Copyright: 	Copyright (c) 2012
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist;

import java.io.Serializable;
import java.util.List;

import com.huayin.common.constant.Constant;

/**
 * <pre>
 * 查询接口结果类型
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2012-3-4
 */
public class QueryResult<T> implements Serializable
{
	private static final long serialVersionUID = 3527685156014291585L;

	/**
	 * 返回的代码
	 */
	private String code = Constant.TRANSACTION_RESPONSE_CODE_SUCCESS;

	/**
	 * 成功失败标识
	 */
	private boolean isSuccess = true;

	/**
	 * 返回的信息
	 */
	private String message = Constant.TRANSACTION_RESPONSE_MESSAGE_SUCCESS;

	private int pageCount;

	private int pageIndex;

	private int pageSize;

	private List<T> results;

	private long totalCount;

	public String getCode()
	{
		return code;
	}

	public boolean getIsSuccess()
	{
		return isSuccess;
	}

	public String getMessage()
	{
		return message;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public int getPageIndex()
	{
		return pageIndex;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public List<T> getResults()
	{
		return results;
	}

	public long getTotalCount()
	{
		return totalCount;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public void setIsSuccess(boolean isSuccess)
	{
		this.isSuccess = isSuccess;
		if (!isSuccess)
		{
			this.code = Constant.TRANSACTION_RESPONSE_CODE_UNKOWN;
			this.message = Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN;
		}
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
	}

	public void setPageIndex(int pageIndex)
	{
		this.pageIndex = pageIndex;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public void setResults(List<T> parameters)
	{
		this.results = parameters;
	}

	public void setTotalCount(long totalCount)
	{
		this.totalCount = totalCount;
	}
}
