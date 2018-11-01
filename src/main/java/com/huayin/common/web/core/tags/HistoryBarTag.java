/**
 * <pre>
 * Title: 		HistoryBarTag.java
 * Type:		com.huayin.common.web.core.tags
 * Author:		linriqing
 * Create:	 	2010-8-28 上午02:10:29
 * Copyright: 	Copyright (c) 2010
 * Company:
 * <pre>
 */
package com.huayin.common.web.core.tags;

import java.util.Stack;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.acl.vo.ActionObject;
import com.huayin.common.acl.vo.Permission;
import com.huayin.common.constant.Constant;

/**
 * <pre>
 * 页面导航栏
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-28
 */
public class HistoryBarTag extends TagSupport
{
	private static final Log logger = LogFactory.getLog(HistoryBarTag.class);

	private static final long serialVersionUID = -8297405996347554313L;

	@Override
	public int doStartTag() throws JspException
	{
		super.doStartTag();
		try
		{
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			Object attribute = request.getAttribute(Constant.CURRENT_PAGE_INFO_NAME);
			if (attribute != null)
			{
				if (attribute instanceof Permission)
				{
					Stack<Permission> tree = new Stack<Permission>();
					Permission current = (Permission) attribute;
					tree.add(current);
					while (current.getParent() != null)
					{
						tree.add(current.getParent());
						current = current.getParent();
					}

					StringBuffer sb = new StringBuffer();
					while (!tree.empty())
					{
						Permission permission = tree.pop();
						if (permission instanceof ActionObject)
						{
							ActionObject action = (ActionObject) permission;
							if ((action.getDestination() != null) && (action.getDestination().length() > 0))
							{
								sb.append("<span>");
								sb.append(action.getShowText());
								sb.append("</span>");
							}
							else
							{
								sb.append(action.getShowText());
							}
						}
						else
						{
							sb.append(permission.getShowText());
						}
						sb.append(">>");
					}
					if (sb.length() > 0)
					{
						sb.deleteCharAt(sb.length() - 1);
						sb.deleteCharAt(sb.length() - 1);
					}
					pageContext.getOut().print(sb.toString());
				}
			}
		}
		catch (Exception e)
		{
			logger.error("do HistoryBarTag failed, page:" + ((Servlet) this.pageContext.getPage()).toString()
					+ ", falt detail:" + e.toString(), e);
		}
		return SKIP_BODY;
	}
}
