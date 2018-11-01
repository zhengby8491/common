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
 * 图片标签, ,纠正了非根目录使用"/"的路径错误问题
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-6-16
 */
public class ImageTag extends BodyTagSupport
{
	private static Log logger = LogFactory.getLog(ImageTag.class);

	private static final long serialVersionUID = -1551490822491431975L;

	private String activeimg;

	private String id;

	private String src;

	private String stdimg;

	private String title;

	public ImageTag()
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
			HttpServletRequest req = (HttpServletRequest) this.pageContext.getRequest();
			StringBuffer sb = new StringBuffer();
			sb.append("<img ");
			if (this.src != null)
			{
				if (this.src.startsWith("/"))
				{
					this.src = req.getContextPath() + this.src;
				}
				sb.append("src=\"" + this.src + "\" ");
			}
			if (this.stdimg != null)
			{

				sb.append("stdimg=\"" + req.getContextPath() + this.stdimg + "\" ");
			}
			if (this.activeimg != null)
			{
				sb.append("activeimg=\"" + req.getContextPath() + this.activeimg + "\" ");
			}
			if (this.id != null)
			{
				sb.append("id=\"" + this.id + "\" ");
			}
			if (this.title != null)
			{
				sb.append("title=\"" + this.title + "\"");
			}
			sb.append("/>");
			this.pageContext.getOut().print(sb.toString());
		}
		catch (IOException e)
		{
			logger.error("do ImageTag failed, page:" + ((Servlet) this.pageContext.getPage()).toString()
					+ ", falt detail:" + e.toString());
		}
		return SKIP_BODY;
	}

	/**
	 * @return activeimg.
	 */
	public String getActiveimg()
	{
		return activeimg;
	}

	/**
	 * @return id.
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @return src.
	 */
	public String getSrc()
	{
		return src;
	}

	/**
	 * @return stdimg.
	 */
	public String getStdimg()
	{
		return stdimg;
	}

	/**
	 * @return title.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param activeimg activeimg
	 */
	public void setActiveimg(String activeimg)
	{
		this.activeimg = activeimg;
	}

	/**
	 * @param id id
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @param src src
	 */
	public void setSrc(String src)
	{
		this.src = src;
	}

	/**
	 * @param stdimg stdimg
	 */
	public void setStdimg(String stdimg)
	{
		this.stdimg = stdimg;
	}

	/**
	 * @param title title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

}
