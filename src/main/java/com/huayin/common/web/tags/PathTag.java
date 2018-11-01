/**
 * Create at 2006-5-11 16:37:29
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
 * 路径标签, ,纠正了非根目录使用"/"的路径错误问题
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-6-16
 */
public class PathTag extends BodyTagSupport
{
	private static Log logger = LogFactory.getLog(ImageTag.class);

	private static final long serialVersionUID = -6916871149290361831L;

	private String path;

	/**
	 * 构造函数
	 */
	public PathTag()
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
			StringBuffer sb = new StringBuffer();
			if (this.path != null)
			{
				if (this.path.startsWith("/"))
				{
					HttpServletRequest req = (HttpServletRequest) this.pageContext.getRequest();
					this.path = req.getContextPath() + this.path;
				}
				sb.append(this.path);
			}
			this.pageContext.getOut().print(sb.toString());
		}
		catch (IOException e)
		{
			logger.error("do PathTag failed, page:" + ((Servlet) this.pageContext.getPage()).toString()
					+ ", falt detail:" + e.toString());
		}
		return SKIP_BODY;
	}

	/**
	 * @return src.
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * @param src src
	 */
	public void setPath(String src)
	{
		this.path = src;
	}

}
