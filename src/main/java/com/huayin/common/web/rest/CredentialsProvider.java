/**
 * <pre>
 * Title: 		CredentialsProvider.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2013-10-24 上午11:12:35
 * Copyright: 	Copyright (c) 2013
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.web.rest;

/**
 * <pre>
 * 认证信息提供者
 * </pre>
 * @author linriqing
 * @version 1.0, 2013-10-24
 */
public interface CredentialsProvider
{
	Credentials getCredentials();
}
