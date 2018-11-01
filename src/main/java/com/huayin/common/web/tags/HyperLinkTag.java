/**
 * Create at 2006-5-11 16:37:29
 */
package com.huayin.common.web.tags;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * 超链接标签,纠正了非根目录使用"/"的路径错误问题
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-6-16
 */
public class HyperLinkTag extends BodyTagSupport
{
	private static Log logger = LogFactory.getLog(HyperLinkTag.class);

	private static final long serialVersionUID = 594480971343539082L;

	private String href;

	private String target;

	private String title;

	public HyperLinkTag()
	{
		super();
	}

	public int doAfterBody() throws JspException
	{
		super.doAfterBody();
		BodyContent body = this.getBodyContent();
		String content = body.getString();
		body.clearBody();

		try
		{
			StringBuffer sb = new StringBuffer();
			sb.append("<a ");
			if (this.href != null)
			{
				if (this.href.startsWith("/"))
				{
					HttpServletRequest req = (HttpServletRequest) this.pageContext.getRequest();
					this.href = req.getContextPath() + this.href;
				}
				sb.append("href=\"" + this.href + "\" ");
			}
			if (this.target != null)
			{
				sb.append("target=\"" + this.target + "\" ");
			}
			if (this.title != null)
			{
				sb.append("title=\"" + this.title + "\" ");
			}
			sb.append(">");
			sb.append(content);
			sb.append("</a>");
			this.getPreviousOut().print(sb.toString());
		}
		catch (IOException e)
		{
			logger.error("do HyperLinkTag failed, page:" + ((Servlet) this.pageContext.getPage()).toString()
					+ ", falt detail:" + e.toString());
		}
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException
	{
		return super.doEndTag();
	}

	public int doStartTag() throws JspException
	{
		super.doStartTag();
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * @return href.
	 */
	public String getHref()
	{
		return href;
	}

	/**
	 * @return target.
	 */
	public String getTarget()
	{
		return target;
	}

	/**
	 * @return title.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param href href
	 */
	public void setHref(String href)
	{
		this.href = href;
	}

	/**
	 * @param target target
	 */
	public void setTarget(String target)
	{
		this.target = target;
	}

	/**
	 * @param title title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

}
