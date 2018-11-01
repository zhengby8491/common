/**
 * <pre>
 * Title: 		SimpleCrendentialsProvider.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2013-10-24 上午11:21:59
 * Copyright: 	Copyright (c) 2013
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.web.rest;

/**
 * <pre>
 * 简单认证信息提供者
 * </pre>
 * @author linriqing
 * @version 1.0, 2013-10-24
 */
public class SimpleCrendentialsProvider implements CredentialsProvider
{

	private Credentials credentials;

	public SimpleCrendentialsProvider(Credentials credentials)
	{
		this.credentials = credentials;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.common.web.security.CredentialsProvider#getCredentials(java.lang.String)
	 */
	@Override
	public Credentials getCredentials()
	{
		return credentials;
	}

}
