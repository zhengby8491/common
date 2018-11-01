/**
 * <pre>
 * Title: 		ProcessResult.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2007-6-22 上午09:38:03
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.web.core;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.exception.ServiceResultFactory;
import com.huayin.common.exception.TransException;

/**
 * <pre>
 * 封装成功/失败信息
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-22
 */
public class ProcessResult implements Serializable
{
	private static final Log logger = LogFactory.getLog(ProcessResult.class);

	private static final long serialVersionUID = 144705876921194103L;

	/**
	 * <pre>
	 * 根据错误信息构造处理结果描述对象
	 * </pre>
	 * @param errMsg 错误信息
	 * @return 处理结果描述对象
	 */
	public static ProcessResult createErrorMsgResult(String errMsg)
	{
		TransException exception = new TransException(errMsg);
		return new ProcessResult(exception, false);
	}

	/**
	 * <pre>
	 * 通过应用程序异常构造处理结果描述对象
	 * </pre>
	 * @param e 应用程序异常
	 * @return 处理结果描述对象
	 */
	public static ProcessResult createErrorResult(TransException e)
	{
		return new ProcessResult(e);
	}

	/**
	 * <pre>
	 * 根据错误代码构造处理结果描述对象
	 * </pre>
	 * @param errCode 错误代码
	 * @return 处理结果描述对象
	 */
	public static ProcessResult createErrorResult(String errCode)
	{
		TransException exception = new TransException(ServiceResultFactory.getServiceResult(errCode));
		return new ProcessResult(exception);
	}

	/**
	 * <pre>
	 * 根据错误代码错误信息/原始异常构造处理结果描述对象
	 * </pre>
	 * @param errCode 错误代码
	 * @param errMsg 错误信息
	 * @return 处理结果描述对象
	 */
	public static ProcessResult createErrorResult(String errCode, String errMsg)
	{
		TransException exception = new TransException(errCode, errMsg);
		return new ProcessResult(exception);
	}

	/**
	 * <pre>
	 * 根据错误代码/错误信息/原始异常构造处理结果描述对象
	 * </pre>
	 * @param errCode 错误代码
	 * @param errMsg 错误信息
	 * @param e 原始异常
	 * @return 处理结果描述对象
	 */
	public static ProcessResult createErrorResult(String errCode, String errMsg, Throwable e)
	{
		TransException exception = new TransException(errCode, errMsg, e);
		return new ProcessResult(exception);
	}

	/**
	 * <pre>
	 * 根据错误代码/原始异常构造处理结果描述对象
	 * </pre>
	 * @param errCode 错误代码
	 * @param e 原始异常
	 * @return 处理结果描述对象
	 */
	public static ProcessResult createErrorResult(String errCode, Throwable e)
	{
		TransException exception = new TransException(ServiceResultFactory.getServiceResult(errCode,e));
		return new ProcessResult(exception);
	}

	/**
	 * <pre>
	 * 根据响应消息构造处理结果描述对象
	 * </pre>
	 * @param rspMsg 响应消息
	 * @param returnAction 返回Action
	 * @param returnOldAction 是否返回原来的Action
	 * @return 处理结果描述对象
	 */
	public static ProcessResult createInfoResult(String rspMsg, String returnAction, boolean returnOldAction)
	{
		TransException exception = new TransException(rspMsg);
		return new ProcessResult(exception, returnAction, returnOldAction);
	}

	public boolean errorFlag = true;

	private TransException appException = new TransException();

	private String deliverParameters = null;

	private String returnAction = null;

	/**
	 * 是否返回原来的Action
	 */
	private boolean returnOldAction = true;

	/**
	 * 构造函数
	 */
	public ProcessResult()
	{
		logger.debug("默认构造函数构造ProcessResult:" + this);
	}

	/**
	 * 构造函数
	 * @param e 应用程序异常
	 */
	private ProcessResult(TransException e)
	{
		this.appException = e;
		this.errorFlag = true;
		logger.debug("构造函数ProcessResult(TransException e)构造ProcessResult:" + this);
	}

	/**
	 * 构造函数
	 * @param e 应用程序异常
	 * @param errorFlag 异常标记
	 */
	private ProcessResult(TransException e, boolean errorFlag)
	{
		this.appException = e;
		this.errorFlag = errorFlag;
		logger.debug("构造函数ProcessResult(TransException e, boolean errorFlag)构造ProcessResult:" + this);
	}

	/**
	 * 构造函数
	 * @param e 应用程序异常
	 * @param returnAction 返回Action
	 * @param returnOldAction 是否返回原来的Action
	 * @param errorFlag 异常标记
	 */
	private ProcessResult(TransException e, String returnAction, boolean returnOldAction)
	{
		this.appException = e;
		this.errorFlag = false;
		this.returnAction = returnAction;
		this.returnOldAction = returnOldAction;
		logger.debug("构造函数ProcessResult(TransException e, String returnAction)构造ProcessResult:" + this);
	}

	/**
	 * @return deliverParameters
	 */
	public String getDeliverParameters()
	{
		return deliverParameters;
	}

	/**
	 * <pre>
	 * 获取处理结果描述信息
	 * </pre>
	 * @return 处理结果描述信息
	 */
	public String getDesc()
	{
		String errorDesc = this.appException.getExceptionMsg();

		if (errorDesc == null)
		{
			errorDesc = this.appException.getMessage();
		}

		if (errorDesc == null)
		{
			if (this.appException.getIncException() != null)
			{
				errorDesc = this.appException.getIncException().getMessage();
			}
		}
		return errorDesc;
	}

	/**
	 * <pre>
	 * 获取详细异常信息
	 * </pre>
	 * @return 详细异常信息
	 */
	public String getErrorInfo()
	{
		String errorinfo = "";
		if (errorFlag && this.appException != null)
		{
			if (this.appException.getIncException() != null)
			{
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				this.appException.getIncException().printStackTrace(pw);
				errorinfo = sw.toString();
			}
		}
		return errorinfo;
	}

	/**
	 * @return 返回页面
	 */
	public String getReturnAction()
	{
		return returnAction;
	}

	/**
	 * @return 是否返回原来的Action
	 */
	public boolean getReturnOldAction()
	{
		return returnOldAction;
	}

	/**
	 * @param deliverParameters deliverParameters
	 */
	public void setDeliverParameters(String deliverParameters)
	{
		this.deliverParameters = deliverParameters;
	}

	/**
	 * @param returnAction 返回Action
	 */
	public void setReturnAction(String returnAction)
	{
		this.returnAction = returnAction;
	}

	/**
	 * @param returnOldAction 是否返回原来的Action
	 */
	public void setReturnOldAction(boolean returnOldAction)
	{
		this.returnOldAction = returnOldAction;
	}

}