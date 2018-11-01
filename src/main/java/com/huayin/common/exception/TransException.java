/**
 * <pre>
 * Title: 		TransException.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2007-6-15 下午04:28:00
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.exception;

import com.huayin.common.constant.Constant;

/**
 * <pre>
 * 业务交易错误(意料之中的)异常类型
 * 
 * 适用范围：
 * 1.业务逻辑判断中，断定交易存在错误的情况，如提交参数不符等;
 * 2.业务方法中未分类的非运行时异常都可以适用。
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-15
 */
public class TransException extends Exception
{
	private static final long serialVersionUID = -1996558415579592755L;

	/**
	 * 异常代码
	 */
	private String exceptionCode = Constant.TRANSACTION_RESPONSE_CODE_UNKOWN;

	/**
	 * 异常信息
	 */
	private String exceptionMsg = Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN;

	/**
	 * 原始异常对象
	 */
	private Throwable incException;

	/**
	 * 构造函数
	 */
	public TransException()
	{
		super(Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN);
	}

	public TransException(SystemException cause)
	{
		super(cause.getMessage(), cause.getIncException());
		this.incException = cause.getIncException();
		this.exceptionCode = cause.getExceptionCode();
		this.exceptionMsg = cause.getExceptionMsg();
	}

	/**
	 * 构造函数
	 * @param result 业务方法返回对象
	 */
	public TransException(ServiceResult<?> result)
	{
		super(result.getMessage(), result.getException());
		this.incException = result.getException();
		this.exceptionCode = result.getCode();
		this.exceptionMsg = result.getMessage();
	}

	/**
	 * 构造函数
	 * @param message 异常信息
	 */
	public TransException(String message)
	{
		super(message);
		this.exceptionMsg = message;
	}

	/**
	 * 构造函数
	 * @param errCode 异常代码
	 * @param message 异常信息
	 */
	public TransException(String errCode, String message)
	{
		super(message);
		this.exceptionCode = errCode;
		this.exceptionMsg = message;
	}

	/**
	 * 构造函数
	 * @param errCode 异常代码
	 * @param message 异常信息
	 * @param e 原始异常对象
	 */
	public TransException(String errCode, String message, Throwable e)
	{
		super(message, e);
		this.exceptionCode = errCode;
		this.exceptionMsg = message;
		this.incException = e;
	}

	/**
	 * 构造函数
	 * @param message 异常信息
	 * @param e 原始异常对象
	 */
	public TransException(String message, Throwable e)
	{
		super(message, e);
		this.exceptionMsg = message;
		this.incException = e;
	}

	/**
	 * 构造函数
	 * @param cause 原始异常对象
	 */
	public TransException(Throwable cause)
	{
		super(Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN, cause);
		this.incException = cause;
	}

	/**
	 * @return 异常代码
	 */
	public String getExceptionCode()
	{
		return this.exceptionCode;
	}

	/**
	 * @return 异常信息
	 */
	public String getExceptionMsg()
	{
		return this.exceptionMsg;
	}

	/**
	 * @return 原始异常对象
	 */
	public Throwable getIncException()
	{
		return this.incException;
	}

}
