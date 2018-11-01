/**
 * <pre>
 * Title: 		WindowTag.java
 * Author:		linriqing
 * Create:	 	2011-10-11 下午02:48:36
 * Copyright: 	Copyright (c) 2011
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * <pre>
 * Window窗口样式标签
 * </pre>
 * @author linriqing
 * @version 1.0, 2011-10-11
 */
public class WindowTag extends BodyTagSupport
{
	private static final long serialVersionUID = 630802118373220764L;

	private boolean showCloseButton;

	private String title;

	private int width;

	@Override
	public int doAfterBody() throws JspException
	{
		super.doAfterBody();
		BodyContent body = this.getBodyContent();
		String content = body.getString();
		body.clearBody();
		if (!"".equals(content))
		{
			try
			{
				StringBuffer sb = new StringBuffer();
				sb.append("	<div	style=\"margin-top: 50px; margin-left: auto; margin-right: auto; ");
				if (width > 0)
				{
					sb.append(" width: " + width + "px; ");
				}
				else
				{
					sb.append(" width: 350px; ");
				}
				sb.append("z-index: 999; clear: both; overflow: hidden;\" id=\"t_div\">");
				sb.append("		<u class=\"f1\"></u>");
				sb.append("		<u class=\"f2\"></u>");
				sb.append("		<u class=\"f3\"></u>");
				sb.append("		<div class=\"d_top\">");
				if ((this.title != null) && (this.title.length() > 0))
				{
					sb.append("				<span>" + this.title + "</span>");
				}
				else
				{
					sb.append("				<span>&nbsp;</span>");
				}
				if (showCloseButton)
				{
					sb.append("				<a href=\"javascript:void(0)\" onClick=\"javascript:history.back();\">X</a>");
				}
				sb.append("		</div>");
				sb.append("		<div class=\"d_body\">");
				sb.append(content);
				sb.append("		</div>");
				sb.append("		<div class=\"d_foot\"></div>");
				sb.append("		<u class=\"f3\"></u>");
				sb.append("		<u class=\"f2\"></u>");
				sb.append("		<u class=\"f1\"></u>");
				sb.append("	</div>");
				getPreviousOut().print(sb.toString());
			}
			catch (IOException e)
			{
				throw new JspException(e);
			}
		}
		return SKIP_BODY;
	}

	public String getTitle()
	{
		return title;
	}

	public int getWidth()
	{
		return width;
	}

	public boolean getShowCloseButton()
	{
		return showCloseButton;
	}

	public void setShowCloseButton(boolean showCloseButton)
	{
		this.showCloseButton = showCloseButton;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}
}
