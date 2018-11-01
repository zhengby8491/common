/**
 * Create at 2006-5-11 11:02:02
 */
package com.huayin.common.web.tags;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * 替代Html的link标签,主要是解决ContextPath的问题,即相对目录的问题
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-6-16
 */
public class LinkTag extends BodyTagSupport
{
	private static Log logger = LogFactory.getLog(LinkTag.class);

	private static final long serialVersionUID = -1697279864926655717L;

	private String href;

	private String rel;

	private String type;

	public LinkTag()
	{
		super();
		this.rel = null;
		this.type = null;
		this.href = null;
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
			StringBuffer sb = new StringBuffer();
			sb.append("<link ");
			if (this.rel != null)
			{
				sb.append("rel=\"" + this.rel + "\" ");
			}
			if (this.href != null)
			{
				if (this.href.startsWith("/"))
				{
					HttpServletRequest req = (HttpServletRequest) this.pageContext.getRequest();
					this.href = req.getContextPath() + this.href;
				}
				sb.append("href=\"" + this.href + "\" ");
			}
			if (this.type != null)
			{
				sb.append("type=\"" + this.type + "\" ");
			}
			sb.append("/>");
			this.pageContext.getOut().print(sb.toString());
		}
		catch (Exception e)
		{
			logger.error("do LinkTag failed, page:" + ((Servlet) this.pageContext.getPage()).toString()
					+ ", falt detail:" + e.toString());
		}
		return SKIP_BODY;
	}

	/**
	 * @return href.
	 */
	public String getHref()
	{
		return href;
	}

	/**
	 * @return rel.
	 */
	public String getRel()
	{
		return rel;
	}

	/**
	 * @return type.
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param href href
	 */
	public void setHref(String href)
	{
		this.href = href;
	}

	/**
	 * @param rel rel
	 */
	public void setRel(String rel)
	{
		this.rel = rel;
	}

	/**
	 * @param type type
	 */
	public void setType(String type)
	{
		this.type = type;
	}

}
