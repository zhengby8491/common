/**
 * <pre>
 * Title: 		AbstractTypeReference.java
 * Author:		zhaojitao
 * Create:	 	2010-11-18 上午09:43:37
 * Copyright: 	Copyright (c) 2010
 * <pre>
 */
package com.huayin.common.exception;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <pre>
 * 获取泛型抽象类
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-11-18
 */
public abstract class GenericType<T>
{
	final Type _type;

	protected GenericType()
	{
		Type superClass = getClass().getGenericSuperclass();
		if (superClass instanceof Class)
		{
			throw new IllegalArgumentException(
					"Internal error: TypeReference constructed without actual type information");
		}
		_type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
	}

	public Type getType()
	{
		return _type;
	}
}
