/**
 * <pre>
 * Title: 		CheckSessionTag.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.web.tags.CheckSessionTag
 * Author:		linriqing
 * Create:	 	2007-6-28 上午02:10:29
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.web.core.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.acl.vo.Passport;
import com.huayin.common.constant.Constant;

/**
 * <pre>
 * 检查用户会话超时
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-28
 */
public class CheckSessionTag extends TagSupport
{
	private static final Log logger = LogFactory.getLog(CheckSessionTag.class);

	private static final long serialVersionUID = -8297405996347554313L;

	public int doEndTag() throws JspException
	{
		try
		{
			Object attribute = this.pageContext.getSession().getAttribute(Constant.PROFILE_SESSION_NAME);
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			String homepage = request.getContextPath() + "/index.jsp";
			if (attribute == null)
			{
				HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
				response.sendRedirect(homepage);
				response.getOutputStream().close();
				return SKIP_PAGE;
			}
			if (!(attribute instanceof Passport))
			{
				HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
				response.sendRedirect(homepage);
				response.getOutputStream().close();
				return SKIP_PAGE;
			}
		}
		catch (Exception e)
		{
			logger.error("检查用户会话超时异常", e);
			throw new JspException("检查用户会话超时异常");
		}
		return EVAL_PAGE;
	}

}
