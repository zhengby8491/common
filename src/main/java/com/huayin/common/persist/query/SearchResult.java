package com.huayin.common.persist.query;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * <pre>
 * 分页查询返回的结果集
 * </pre>
 * @author chenjian
 * @version 1.0, 2012-5-8
 */
public class SearchResult<T>
{
	/**
	 * 查询的记录集
	 */
	private List<T> result;

	/**
	 * 查询总行数
	 */
	private Integer count;

	public List<T> getResult()
	{
		return result;
	}

	public void setResult(List<T> result)
	{
		this.result = result;
	}

	public Integer getCount()
	{
		return count;
	}

	public void setCount(Integer count)
	{
		this.count = count;
	}
	
	/**
	 * <pre>
	 * 构造一个空对象
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月28日 下午7:22:09, think
	 */
	public static <T> SearchResult<T> genEmptyResult()
	{
		SearchResult<T> emptyResult = new SearchResult<>();
		List<T> resultList = Lists.newArrayList();
		emptyResult.setResult(resultList);
		return emptyResult;
	}
}
