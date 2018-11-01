/**
 * <pre>
 * Title: 		ServiceException.java
 * Project: 	HY-Common
 * Author:		zhaojitao
 * Create:	 	2008-8-13 下午03:38:50
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.exception;

/**
 * <pre>
 * 业务交易错误(意料之外的)异常类型
 * 这类异常应尽量通过设计合理的业务逻辑判断来避免！
 * 
 * 适用范围：
 * 1.业务逻辑判断中，未预料到的其它情况，如空指针等;
 * 2.业务方法中未分类的运行时异常都可以适用。
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2008-8-13
 */
public class ServiceException extends SystemException
{

	private static final long serialVersionUID = 4953260375190880876L;

	/**
	 * 构造函数
	 */
	public ServiceException()
	{
		super();
	}

	/**
	 * 构造函数
	 * @param result 业务方法返回对象
	 */
	public ServiceException(ServiceResult<?> result)
	{
		super(result);
	}

	/**
	 * 构造函数
	 * @param message 异常信息
	 */
	public ServiceException(String message)
	{
		super(message);
	}

	/**
	 * 构造函数
	 * @param errCode 异常代码
	 * @param message 异常信息
	 */
	public ServiceException(String errCode, String message)
	{
		super(errCode, message);
	}

	/**
	 * 构造函数
	 * @param errCode 异常代码
	 * @param message 异常信息
	 * @param cause 原始异常对象
	 */
	public ServiceException(String errCode, String message, Throwable cause)
	{
		super(errCode, message, cause);
	}

	/**
	 * 构造函数
	 * @param message 异常信息
	 * @param cause 原始异常对象
	 */
	public ServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * 构造函数
	 * @param cause 原始异常对象
	 */
	public ServiceException(Throwable cause)
	{
		super(cause);
	}
}