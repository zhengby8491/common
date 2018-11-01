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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.huayin.common.persist.query.DynamicQuery;

/**
 * <pre>
 * 查询接口参数类型
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2012-3-4
 */
public class QueryParameter<T> implements Serializable
{
	private static final long serialVersionUID = 3527685156014291585L;

	private DynamicQuery customQuery;

	private List<Object> parameters = new ArrayList<Object>();

	private Class<T> type;

	public DynamicQuery getCustomQuery()
	{
		return customQuery;
	}

	public Object[] getParameters()
	{
		return parameters.toArray();
	}

	public void addParameter(Object parameter)
	{
		this.parameters.add(parameter);
	}

	public void addParameters(Object[] parameter)
	{
		this.parameters.addAll(Arrays.asList(parameter));
	}

	public Class<T> getType()
	{
		return type;
	}

	public void setCustomQuery(DynamicQuery query)
	{
		this.customQuery = query;
	}

	public void setType(Class<T> type)
	{
		this.type = type;
	}
}
