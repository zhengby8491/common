/**
 * <pre>
 * Title: 		PaginationButtonTag.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.web.tags.PaginationButtonTag
 * Author:		linriqing
 * Create:	 	2007-9-12 上午10:10:29
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.web.tags;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * 分页查询的页码按钮标签
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-9-12
 */
public class PaginationButtonTag extends BodyTagSupport
{
	private static Log logger = LogFactory.getLog(ImageTag.class);

	private static final long serialVersionUID = -355485638237637543L;

	/**
	 * 构造函数
	 */
	public PaginationButtonTag()
	{
		super();
	}

	public int doEndTag() throws JspException
	{
		return super.doEndTag();
	}

	public int doStartTag() throws JspException
	{
		super.doStartTag();
		try
		{
			HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
			String currentPage = (String) request
					.getAttribute(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_CURRENTPAGEINDEX);
			String pageCount = (String) request.getAttribute(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_PAGECOUNT);
			String pageSize = (String) request.getAttribute(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_PAGESIZE);
			String entityCount = (String) request
					.getAttribute(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_ENTITYCOUNT);
			if (pageSize == null)
			{
				pageSize = "10";
			}
			if (pageCount == null)
			{
				pageCount = "0";
			}
			if (currentPage == null)
			{
				currentPage = "0";
			}
			if (entityCount == null)
			{
				entityCount = "0";
			}
			this.pageContext.getOut().print(this.showPaginationButton(pageCount, pageSize, currentPage, entityCount));
		}
		catch (IOException e)
		{
			logger.error("do PaginationButtonTag failed, page:" + ((Servlet) this.pageContext.getPage()).toString()
					+ ", falt detail:", e);
		}
		return SKIP_BODY;
	}

	/**
	 * <pre>
	 * 根据总页码、当前页码显示分页导航按钮。
	 * </pre>
	 * @param pageCount 总页码
	 * @param pageSize 单页显示记录数
	 * @param currentPage 当前页码
	 * @return 分页导航按钮
	 */
	public String showPaginationButton(String pageCount, String pageSize, String currentPage, String entityCount)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<table width='100%' align='center' border='0' cellpadding='0' cellspacing='0'>\n");
		sb.append("<tr><td align='right'>\n");
		sb.append("总共<B>");
		sb.append(entityCount);
		sb.append("</B>条记录，");
		sb.append("分<B>");
		sb.append(pageCount);
		sb.append("</B>页，");
		sb.append("当前第<B>");
		sb.append(currentPage);
		sb.append("</B>页，每页显示");
		sb.append("<input type='text' name='");
		sb.append(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_PAGESIZE);
		sb.append("'  size='2' maxlength='3' value='");
		sb.append(pageSize);
		sb.append("'>条记录\n");
		sb.append("<input type='button' value='首页' onclick=\"javascript:PaginationButton_Navigate(this, 1, ");
		sb.append(pageCount);
		sb.append(");\">\n");
		sb.append("<input type='button' value='上页' onclick=\"javascript:PaginationButton_Navigate(this, 2, ");
		sb.append(pageCount);
		sb.append(");\">\n");
		sb.append("<input type='button' value='下页' onclick=\"javascript:PaginationButton_Navigate(this, 3, ");
		sb.append(pageCount);
		sb.append(");\">\n");
		sb.append("<input type='button' value='末页' onclick=\"javascript:PaginationButton_Navigate(this, 4, ");
		sb.append(pageCount);
		sb.append(");\">\n");
		sb.append("到<input type='text' size='3' name='");
		sb.append(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_CURRENTPAGEINDEX);
		sb.append("' value='");
		sb.append(currentPage);
		sb.append("'/>页\n");
		sb.append("<input type='button' value='转到' onclick=\"javascript:PaginationButton_Navigate(this, 5, ");
		sb.append(pageCount);
		sb.append(");\" />\n");
		sb.append("</td></tr></table>\n");
		return sb.toString();
	}

}
