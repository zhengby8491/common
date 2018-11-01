/**
 * <pre>
 * Title: 		SystemException.java
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
 * 系统异常类型
 * 
 * 适用范围：
 * 1.文件系统故障;
 * 2.网络系统故障;
 * 3.系统配置错误;
 * 4.系统初始化错误;
 * 5.应用系统设计存在缺陷;
 * 6.大部分未分类的运行时异常都可以适用。
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-15
 */
public class SystemException extends RuntimeException
{
	private static final long serialVersionUID = 8622950699332177171L;

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
	 * 附加信息
	 */
	private String extMessage;

	/**
	 * @return 附加信息
	 */
	public String getExtMessage()
	{
		return extMessage;
	}

	/**
	 * @param extMessage 附加信息
	 */
	public void setExtMessage(String extMessage)
	{
		this.extMessage = extMessage;
	}

	/**
	 * 构造函数
	 */
	public SystemException()
	{
		super(Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN);
	}

	/**
	 * 构造函数
	 * @param result 业务方法返回对象
	 */
	public SystemException(ServiceResult<?> result)
	{
		super(result.getMessage(), result.getException());
		this.incException = result.getException();
		this.exceptionCode = result.getCode();
		this.exceptionMsg = result.getMessage();
	}

	/**
	 * 构造函数
	 * @param message 错误消息
	 */
	public SystemException(String message)
	{
		super(message);
		this.exceptionMsg = message;
	}

	/**
	 * 构造函数
	 * @param errCode 异常代码
	 * @param message 异常信息
	 */
	public SystemException(String errCode, String message)
	{
		super(message);
		this.exceptionCode = errCode;
		this.exceptionMsg = message;
	}

	/**
	 * 构造函数
	 * @param errCode 异常代码
	 * @param message 异常信息
	 * @param extMessage 附加信息
	 */
	public SystemException(String errCode, String message, String extMessage)
	{
		super(message);
		this.exceptionCode = errCode;
		this.exceptionMsg = message;
		this.extMessage = extMessage;
	}

	/**
	 * 构造函数
	 * @param errCode 异常代码
	 * @param message 异常信息
	 * @param cause 原始异常对象
	 */
	public SystemException(String errCode, String message, Throwable cause)
	{
		super(message);
		this.exceptionCode = errCode;
		this.exceptionMsg = message;
		this.incException = cause;
	}

	/**
	 * 构造函数
	 * @param errCode 异常代码
	 * @param message 异常信息
	 * @param extMessage 附加信息
	 * @param cause 原始异常对象
	 */
	public SystemException(String errCode, String message, String extMessage, Throwable cause)
	{
		super(message);
		this.exceptionCode = errCode;
		this.exceptionMsg = message;
		this.incException = cause;
		this.extMessage = extMessage;
	}

	/**
	 * 构造函数
	 * @param message 错误消息
	 * @param cause 原始异常对象
	 */
	public SystemException(String message, Throwable cause)
	{
		super(message, cause);
		this.exceptionMsg = message;
		this.incException = cause;
	}

	/**
	 * 构造函数
	 * @param cause 原始异常对象
	 */
	public SystemException(Throwable cause)
	{
		super(Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN, cause);
		this.incException = cause;
	}

	/**
	 * 构造函数
	 * @param cause 原始异常对象
	 */
	public SystemException(TransException cause)
	{
		super(cause.getExceptionMsg(), cause.getIncException());
		this.incException = cause.getIncException();
		this.exceptionCode = cause.getExceptionCode();
		this.exceptionMsg = cause.getExceptionMsg();
	}

	/**
	 * @return 异常代码
	 */
	public String getExceptionCode()
	{
		return exceptionCode;
	}

	/**
	 * @return 异常信息
	 */
	public String getExceptionMsg()
	{
		return exceptionMsg;
	}

	/**
	 * @return 原始异常对象
	 */
	public Throwable getIncException()
	{
		return incException;
	}

}
