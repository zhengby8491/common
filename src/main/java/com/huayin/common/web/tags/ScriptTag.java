/**
 * Create at 2006-5-11 10:11:53
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
 * 替代Html的script标签,主要是解决ContextPath的问题,即相对目录的问题
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-6-16
 */
public class ScriptTag extends BodyTagSupport
{
	private static Log logger = LogFactory.getLog(ScriptTag.class);

	private static final long serialVersionUID = 6773158482507011495L;

	private String language;

	private String src;

	public ScriptTag()
	{
		super();
		this.src = null;
		this.language = null;
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
			sb.append("<script ");
			if (this.language != null)
			{
				sb.append("language=\"" + this.language + "\" ");
			}
			if (this.src != null)
			{
				if (this.src.startsWith("/"))
				{
					HttpServletRequest req = (HttpServletRequest) this.pageContext.getRequest();
					this.src = req.getContextPath() + this.src;
				}
				sb.append("src=\"" + this.src + "\" ");
			}
			sb.append("></script>");
			this.pageContext.getOut().print(sb.toString());
		}
		catch (IOException e)
		{
			logger.error("do ScriptTag failed, page:" + ((Servlet) this.pageContext.getPage()).toString()
					+ ", falt detail:" + e.toString());
		}
		return SKIP_BODY;
	}

	/**
	 * @return language.
	 */
	public String getLanguage()
	{
		return language;
	}

	/**
	 * @return src.
	 */
	public String getSrc()
	{
		return src;
	}

	/**
	 * @param language language
	 */
	public void setLanguage(String language)
	{
		this.language = language;
	}

	/**
	 * @param src src
	 */
	public void setSrc(String src)
	{
		this.src = src;
	}

}
