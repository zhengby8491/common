/**
 * <pre>
 * Title: 		ActionPageInfo.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2007-6-7 上午10:17:58
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.web.core;

import java.io.Serializable;

/**
 * <pre>
 * 页面信息类,存储页面对应的按钮等相关信息
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-6-16
 */
public class ActionPageInfo implements Serializable
{
	private static final long serialVersionUID = 8446378888466289757L;

	private String pageName;

	private String requestMethod;

	/**
	 * 构造函数
	 * @param pageName 页面名称
	 * @param requestMethod 请求方式
	 */
	public ActionPageInfo(String pageName, String requestMethod)
	{
		super();
		this.pageName = pageName;
		this.requestMethod = requestMethod;
	}

	/**
	 * @return 页面名称.
	 */
	public String getPageName()
	{
		return pageName;
	}

	public String getRequestMethod()
	{
		return requestMethod;
	}
}
