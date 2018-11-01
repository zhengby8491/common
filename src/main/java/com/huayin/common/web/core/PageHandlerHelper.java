/**
 * <pre>
 * Title: 		PageHandlerHelper.java
 * Author:		linriqing
 * Create:	 	2010-8-30 下午01:56:46
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.web.core;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huayin.common.acl.SessionTimeOutFilter;
import com.huayin.common.exception.SystemException;

/**
 * <pre>
 * 页面处理帮助类
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-8-30
 */
public class PageHandlerHelper
{

	/**
	 * <pre>
	 * 重定向到首页
	 * </pre>
	 * @param request 原始ServletRequest参数
	 * @param response 原始ServletResponse参数
	 */
	public static void redirectHomePage(ServletRequest request, ServletResponse response)
	{
		try
		{
			HttpServletRequest httpreq = (HttpServletRequest) request;
			HttpServletResponse httprsp = (HttpServletResponse) response;
			httprsp.sendRedirect(httpreq.getContextPath().concat("/index.jsp"));
			return;
		}
		catch (Exception e)
		{
			SessionTimeOutFilter.logger.info("重定向到JSP错误页面时异常", e);
			throw new SystemException("重定向到JSP错误页面时异常", e);
		}
	}

}
