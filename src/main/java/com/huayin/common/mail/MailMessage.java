/**
 * <pre>
 * Title: 		MailMessage.java
 * Author:		linriqing
 * Create:	 	2009-1-19 下午05:06:35
 * Copyright: 	Copyright (c) 2009
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.mail;

/**
 * <pre>
 * 邮件消息
 * </pre>
 * @author linriqing
 * @version 1.0, 2009-1-19
 */
public class MailMessage
{
	/**
	 * 主题
	 */
	private String subject;

	/**
	 * 内容
	 */
	private String content;

	public MailMessage()
	{
	}

	public MailMessage(String subject, String content)
	{
		this.subject = subject;
		this.content = content;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}
}
