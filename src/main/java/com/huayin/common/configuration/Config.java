/**
 * <pre>
 * Title: 		Config.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2007-1-17 13:53:43
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.configuration;

import java.io.Serializable;

/**
 * <pre>
 * 配置信息基类
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-1-17
 */
public class Config implements Serializable
{
	private static final long serialVersionUID = -5873771354661578000L;

	/**
	 * 主键
	 */
	private String key;

	/**
	 * @return 主键
	 * @see com.huayin.common.configuration.Config#key
	 */
	public final String getKey()
	{
		return key;
	}

	/**
	 * @param key 主键
	 * @see com.huayin.common.configuration.Config#key
	 */
	public final void setKey(String key)
	{
		this.key = key;
	}
}
