/**
 * <pre>
 * Title: 		ServiceResult.java
 * Author:		zhaojitao
 * Create:	 	2008-8-12 下午00:38:50
 * Copyright: 	Copyright (c) 2008
 * Company:
 * <pre>
 */
package com.huayin.common.exception;

import com.huayin.common.configuration.Config;
import com.huayin.common.constant.Constant;

/**
 * <pre>
 * 业务层方法调用返回值类型
 * 注：该对象可以替代业务方法中用异常作为返回值的方案，通过异常返回的业务方法往往非常低效！
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2008-8-12
 */
public class ServiceResult<T> extends Config
{
	private static final long serialVersionUID = -1791350429078814535L;

	/**
	 * 返回的代码
	 */
	private String code = Constant.TRANSACTION_RESPONSE_CODE_SUCCESS;

	/**
	 * 返回的异常
	 */
	private Throwable exception;

	/**
	 * 成功失败标识
	 */
	private Boolean isSuccess = true;

	/**
	 * 返回的信息
	 */
	private String message = Constant.TRANSACTION_RESPONSE_MESSAGE_SUCCESS;

	/**
	 * 返回的对象
	 */
	private T returnValue;

	/**
	 * 返回记录条数
	 */
	private int returnListSize;

	/**
	 * 返回的附加信息
	 */
	private Object returnObject;

	/**
	 * <pre>
	 * 构造函数
	 * 默认为成功的返回对象
	 * </pre>
	 */

	public ServiceResult()
	{
	}

	/**
	 * <pre>
	 * 构造函数
	 * 成功的标记默认为失败
	 * </pre>
	 * @param exception 返回的异常
	 */
	public ServiceResult(TransException exception)
	{
		this.code = exception.getExceptionCode();
		this.message = exception.getExceptionMsg();
		this.isSuccess = false;
		this.exception = exception;
	}

	/**
	 * <pre>
	 * 构造函数
	 * 成功的标记默认为失败
	 * </pre>
	 * @param exception 返回的异常
	 */
	public ServiceResult(SystemException exception)
	{
		this.code = exception.getExceptionCode();
		this.message = exception.getExceptionMsg();
		this.isSuccess = false;
		this.exception = exception;
	}

	/**
	 * <pre>
	 * 构造函数 
	 * 该构造将根据参数code是否为'0000'来设置是否成功的标记
	 * </pre>
	 * @param code 返回的代码
	 * @param message 返回的信息
	 */
	public ServiceResult(String code, String message)
	{
		this.code = code;
		this.message = message;
		if (Constant.TRANSACTION_RESPONSE_CODE_SUCCESS.equalsIgnoreCase(code))
		{
			this.isSuccess = true;
		}
		else
		{
			this.isSuccess = false;
		}
	}

	/**
	 * <pre>
	 * 构造函数 
	 * 该构造将根据参数code是否为'0000'来设置是否成功的标记
	 * </pre>
	 * @param code 返回的代码
	 * @param message 返回的信息
	 * @param returnValue 返回的对象
	 */
	public ServiceResult(String code, String message, T returnValue)
	{
		this.code = code;
		this.message = message;
		if (Constant.TRANSACTION_RESPONSE_CODE_SUCCESS.equalsIgnoreCase(code))
		{
			this.isSuccess = true;
		}
		else
		{
			this.isSuccess = false;
		}
		this.returnValue = returnValue;
	}

	/**
	 * <pre>
	 * 构造函数 
	 * 该构造将根据参数code是否为'0000'来设置是否成功的标记
	 * </pre>
	 * @param returnValue 返回的对象
	 */
	public ServiceResult(T returnValue)
	{
		this.returnValue = returnValue;
	}

	/**
	 * <pre>
	 * 构造函数 
	 * 该构造将根据参数code是否为'0000'来设置是否成功的标记
	 * </pre>
	 * @param code 返回的代码
	 * @param message 返回的信息
	 * @param exception 返回的异常
	 */
	public ServiceResult(String code, String message, Throwable exception)
	{
		this.code = code;
		this.message = message;
		if (Constant.TRANSACTION_RESPONSE_CODE_SUCCESS.equalsIgnoreCase(code))
		{
			this.isSuccess = true;
		}
		else
		{
			this.isSuccess = false;
		}
		this.exception = exception;
	}

	/**
	 * <pre>
	 * 构造函数 
	 * 该构造将根据参数code是否为'0000'来设置是否成功的标记
	 * </pre>
	 * @param code 返回的代码
	 * @param message 返回的信息
	 * @param exception 返回的异常
	 * @param returnValue 返回的对象
	 */
	public ServiceResult(String code, String message, Throwable exception, T returnValue)
	{
		this.code = code;
		this.message = message;
		if (Constant.TRANSACTION_RESPONSE_CODE_SUCCESS.equalsIgnoreCase(code))
		{
			this.isSuccess = true;
		}
		else
		{
			this.isSuccess = false;
		}
		this.exception = exception;
		this.returnValue = returnValue;
	}

	/**
	 * <pre>
	 * 构造函数 
	 * 成功的标记默认为失败
	 * </pre>
	 * @param exception 返回的异常
	 */
	public ServiceResult(Throwable exception)
	{
		this.code = Constant.TRANSACTION_RESPONSE_CODE_UNKOWN;
		this.message = Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN;
		this.isSuccess = false;
		this.exception = exception;
	}

	/**
	 * <pre>
	 * 标记错误
	 * </pre>
	 * @param errorMessage
	 */
	public ServiceResult<T> errorMessage(String errorMessage)
	{
		this.code = Constant.TRANSACTION_RESPONSE_CODE_UNKOWN;
		this.message = errorMessage;
		this.isSuccess = false;
		return this;
	}

	/**
	 * <pre>
	 * 标记错误
	 * </pre>
	 * @param errorMessage
	 * @param code
	 * @return
	 */
	public ServiceResult<T> errorMessage(String errorMessage, String code)
	{
		this.code = code;
		this.message = errorMessage;
		this.isSuccess = false;
		return this;
	}

	/**
	 * @return 返回的代码
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @return 返回的异常
	 */
	public Throwable getException()
	{
		return exception;
	}

	/**
	 * @return 成功失败标识
	 */
	public Boolean getIsSuccess()
	{
		return isSuccess;
	}

	/**
	 * @return 返回的信息
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @return 返回的对象
	 */
	public T getReturnValue()
	{
		return returnValue;
	}

	/**
	 * @param code 返回的代码
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	/**
	 * @param exception 返回的异常
	 */
	public void setException(Throwable exception)
	{
		this.exception = exception;
	}

	/**
	 * @param message 返回的信息
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * @param returnValue 返回的对象
	 */
	public void setReturnValue(T returnValue)
	{
		this.returnValue = returnValue;
	}

	public int getReturnListSize()
	{
		return returnListSize;
	}

	public void setReturnListSize(int returnListSize)
	{
		this.returnListSize = returnListSize;
	}

	public void setIsSuccess(Boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public Object getReturnObject()
	{
		return returnObject;
	}

	public void setReturnObject(Object returnObject)
	{
		this.returnObject = returnObject;
	}

}
