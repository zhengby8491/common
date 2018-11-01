/**
 * <pre>
 * Title: 		SessionTimeOutFilter.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2007-9-16 上午09:38:03
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.acl;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.acl.impl.PermissionProviderImpl;
import com.huayin.common.acl.vo.Passport;
import com.huayin.common.constant.Constant;
import com.huayin.common.web.core.PageHandlerHelper;

/**
 * <pre>
 * 检查用户会话超时过滤器
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-9-16
 */
public class SessionTimeOutFilter implements Filter
{
	public static final Log logger = LogFactory.getLog(SessionTimeOutFilter.class);

	@Override
	public void destroy()
	{
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		try
		{
			HttpServletRequest request = (HttpServletRequest) req;
			String url = request.getServletPath();
			int index = url.indexOf("?");
			if (index > 0)
			{
				url = url.substring(0, index);
			}
			boolean passFlag = false;
			for (String exclude : PermissionProviderImpl.getInstance().getExcludeActions())
			{
				if (url.endsWith(exclude))
				{
					passFlag = true;
					break;
				}
			}
			if (passFlag)
			{
				chain.doFilter(req, res);
			}
			else
			{
				Passport passport = (Passport) request.getSession().getAttribute(Constant.PROFILE_SESSION_NAME);

				if (passport == null)
				{
					PageHandlerHelper.redirectHomePage(req, res);
					return;
				}
				else
				{
					chain.doFilter(req, res);
				}
			}
		}
		catch (Exception e)
		{
			String errMsg = "检查用户会话超时异常";
			logger.error(errMsg, e);
			PageHandlerHelper.redirectHomePage(req, res);
			return;
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
	}
}
