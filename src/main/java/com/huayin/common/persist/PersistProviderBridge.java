/**
 * <pre>
 * Title: 		PersistProviderBridge.java
 * Author:		zhaojitao
 * Create:	 	2011-7-19 下午06:41:52
 * Copyright: 	Copyright (c) 2011
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist;

/**
 * <pre>
 * 持久化实现提供桥接接口
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2011-7-19
 */
public interface PersistProviderBridge
{
	PersistProvider getPersistProvider();

	PersistProvider getPersistProvider(int providerType);
}
