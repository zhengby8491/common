package com.huayin.common.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * 页面显示字符长度处理，精确到字节数,按照GBK编码一个汉字占两个字节
 * </pre>
 * @author chenjian
 * @version 1.0, 2011-1-17
 */
public class SubStrTag extends BodyTagSupport
{

	private static final long serialVersionUID = 1L;

	private Log logger = LogFactory.getLog(this.getClass());

	/**
	 * 要截取的字符串
	 */
	private String text;

	/**
	 * 要截取字符传长度的字节数（按照GBK中文编码,汉字占有2个字节，数字和字母为一个字节）
	 */
	private int byteLength = -1;

	/**
	 * 截取后填充的省略字符
	 */
	private String omit = "..";

	@Override
	public int doStartTag() throws JspException
	{
		super.doStartTag();
		JspWriter jw = pageContext.getOut();
		try
		{
			if (text == null || byteLength == 0)
			{
				return EVAL_PAGE;
			}
			else if (byteLength < 0)
			{
				jw.print(text);
				return EVAL_PAGE;
			}
			else
			{
				byte[] bytes = text.getBytes("gbk");
				if (bytes.length <= byteLength)
				{
					jw.print(text);
					return EVAL_PAGE;
				}
				else
				{
					int count = 0;
					for (int i = 0; i < byteLength; i++)
					{
						if (bytes[i] < 0)
						{
							count++;
						}
					}
					if (count % 2 == 0)
					{
						jw.println(new String(bytes, 0, byteLength, "gbk") + omit);
					}
					else
					{
						jw.println(new String(bytes, 0, byteLength - 1, "gbk") + omit);
					}
				}
			}
		}
		catch (Exception ex)
		{
			logger.error("截取字符串发生异常，源字符{" + text + "}，截取字节数{" + byteLength + "}");
		}
		return EVAL_PAGE;
	}

	@Override
	public int doEndTag() throws JspException
	{

		return super.doEndTag();
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public int getByteLength()
	{
		return byteLength;
	}

	public void setByteLength(int byteLength)
	{
		this.byteLength = byteLength;
	}

	public String getOmit()
	{
		return omit;
	}

	public void setOmit(String omit)
	{
		this.omit = omit;
	}
}
