/**
 * <pre>
 * Title: 		DataOperationException.java
 * Project: 	HP-Common
 * Author:		zhaojitao
 * Create:	 	2006-8-26 下午01:28:00
 * Copyright: 	Copyright (c) 2006
 * Company:
 * <pre>
 */
package com.huayin.common.exception;

/**
 * <pre>
 * 数据持久化异常类型
 * 
 * 适用范围：
 * 1.数据库操作错误。
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2006-8-26
 */
public class DataOperationException extends SystemException
{
	private static final long serialVersionUID = 652408585899519394L;

	/**
	 * 构造函数
	 */
	public DataOperationException()
	{
		super();
	}

	/**
	 * 构造函数
	 * @param message 错误信息
	 */
	public DataOperationException(String message)
	{
		super(message);
	}

	/**
	 * 构造函数
	 * @param message 错误信息
	 * @param cause 原始异常对象
	 */
	public DataOperationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * 构造函数
	 * @param cause 原始异常对象
	 */
	public DataOperationException(Throwable cause)
	{
		super(cause);
	}
}
