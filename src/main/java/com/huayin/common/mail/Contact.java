/**
 * <pre>
 * Title: 		Contract.java
 * Author:		zhaojitao
 * Create:	 	2009-1-19 下午06:02:19
 * Copyright: 	Copyright (c) 2009
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.mail;

import java.io.Serializable;

/**
 * <pre>
 * 联系人配置信息
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2009-1-19
 */
public class Contact implements Serializable
{
	private static final long serialVersionUID = -1887992706266767546L;

	/**
	 * 联系人姓名
	 */
	private String name;

	/**
	 * 联系人邮箱
	 */
	private String email;

	/**
	 * 电话号码
	 */
	private String phone;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * msn账号
	 */
	private String msn;

	/**
	 * qq账号
	 */
	private String qq;

	/**
	 * 构造函数
	 * @param name 联系人姓名
	 * @param email 联系人邮箱
	 */
	public Contact(String name, String email)
	{
		super();
		this.name = name;
		this.email = email;
	}

	/**
	 * 构造函数
	 * @param name 联系人姓名
	 * @param email 联系人邮箱
	 * @param phone 电话号码
	 * @param mobile 手机号码
	 * @param msn msn账号
	 * @param qq qq账号
	 */
	public Contact(String name, String email, String phone, String mobile, String msn, String qq)
	{
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.mobile = mobile;
		this.msn = msn;
		this.qq = qq;
	}

	/**
	 * @return 联系人邮箱
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @return 手机号码
	 */
	public String getMobile()
	{
		return mobile;
	}

	/**
	 * @return msn账号
	 */
	public String getMsn()
	{
		return msn;
	}

	/**
	 * @return 联系人姓名
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return 电话号码
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @return qq账号
	 */
	public String getQq()
	{
		return qq;
	}

	/**
	 * @param email 联系人邮箱
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * @param mobile 手机号码
	 */
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	/**
	 * @param msn msn账号
	 */
	public void setMsn(String msn)
	{
		this.msn = msn;
	}

	/**
	 * @param name 联系人姓名
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param phone 电话号码
	 */
	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	/**
	 * @param qq qq账号
	 */
	public void setQq(String qq)
	{
		this.qq = qq;
	}
}
