/**
 * <pre>
 * Title: 		Credentials.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2013-10-24 上午11:12:03
 * Copyright: 	Copyright (c) 2013
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.web.rest;

/**
 * <pre>
 * 认证信息
 * </pre>
 * @author linriqing
 * @version 1.0, 2013-10-24
 */
public class Credentials
{
	public Credentials(String userName, String password)
	{
		super();
		this.password = password;
		this.userName = userName;
	}

	protected String password;

	protected String userName;

	public String getPassword()
	{
		return password;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
}
