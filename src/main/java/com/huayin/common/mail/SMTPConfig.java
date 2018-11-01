/**
 * <pre>
 * Title: 		SMTPlConfig.java
 * Author:		zhaojitao
 * Create:	 	2009-1-19 下午05:06:35
 * Copyright: 	Copyright (c) 2009
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.mail;

import java.io.Serializable;

/**
 * <pre>
 * 邮件发件者的配置信息
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2009-1-19
 */
public class SMTPConfig implements Serializable
{
	private static final long serialVersionUID = 4096859842126982678L;

	/**
	 * 发件人邮箱账户
	 */
	private String sendUser;

	/**
	 * 发件人邮箱密码
	 */
	private String sendUserPwd;

	/**
	 * 邮箱SMTP服务器
	 */
	private String smtp;

	/**
	 * 发件人邮箱地址
	 */
	private String mailfrom;

	/**
	 * 构造函数
	 * @param sendUser 发件人邮箱账户
	 * @param sendUserPwd 发件人邮箱密码
	 * @param smtp 邮箱SMTP服务器
	 * @param mailfrom 发件人邮箱地址
	 */
	public SMTPConfig(String sendUser, String sendUserPwd, String smtp, String mailfrom)
	{
		super();
		this.sendUser = sendUser;
		this.sendUserPwd = sendUserPwd;
		this.smtp = smtp;
		this.mailfrom = mailfrom;
	}

	/**
	 * @return 发件人邮箱账户
	 */
	public String getSendUser()
	{
		return sendUser;
	}

	/**
	 * @param sendUser 发件人邮箱账户
	 */
	public void setSendUser(String sendUser)
	{
		this.sendUser = sendUser;
	}

	/**
	 * @return 发件人邮箱密码
	 */
	public String getSendUserPwd()
	{
		return sendUserPwd;
	}

	/**
	 * @param sendUserPwd 发件人邮箱密码
	 */
	public void setSendUserPwd(String sendUserPwd)
	{
		this.sendUserPwd = sendUserPwd;
	}

	/**
	 * @return 邮箱SMTP服务器
	 */
	public String getSmtp()
	{
		return smtp;
	}

	/**
	 * @param smtp 邮箱SMTP服务器
	 */
	public void setSmtp(String smtp)
	{
		this.smtp = smtp;
	}

	/**
	 * @return 发件人邮箱地址
	 */
	public String getMailfrom()
	{
		return mailfrom;
	}

	/**
	 * @param mailfrom 发件人邮箱地址
	 */
	public void setMailfrom(String mailfrom)
	{
		this.mailfrom = mailfrom;
	}
}
