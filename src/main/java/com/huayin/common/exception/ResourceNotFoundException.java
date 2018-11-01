/**
 * <pre>
 * Title: 		ResourceNotFoundException.java
 * Author:		zhaojitao
 * Create:	 	2010-5-28 下午02:20:13
 * Copyright: 	Copyright (c) 2010
 * <pre>
 */
package com.huayin.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <pre>
 * 资源未找到异常
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-6-1
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException
{

	private static final long serialVersionUID = -691317075874866288L;

	private Object resourceId;

	public ResourceNotFoundException(Object resourceId)
	{
		this.resourceId = resourceId;
	}

	public Object getResourceId()
	{
		return resourceId;
	}
}
