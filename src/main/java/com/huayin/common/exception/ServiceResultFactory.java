/**
 * <pre>
 * Title: 		ExceptionFactory.java
 * Project: 	HY-Common
 * Author:		zhaojitao
 * Create:	 	2007-6-15 下午04:09:24
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.exception;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.huayin.common.configuration.ConfigProvider;
import com.huayin.common.configuration.ConfigProviderFactory;
import com.huayin.common.constant.Constant;

/**
 * <pre>
 * 响应码/异常处理工厂
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2007-6-15
 */
public class ServiceResultFactory
{
	private static Map<String, String> serviceResultMap = new HashMap<String, String>();

	static
	{
		init();
	}

	/**
	 * <pre>
	 * 通过状态码获取响应状态码说明
	 * </pre>
	 * @param code 状态码
	 * @return 响应状态码说明
	 */
	public static String getRspMessage(String code)
	{
		if (ServiceResultFactory.serviceResultMap.containsKey(code))
		{
			return ServiceResultFactory.serviceResultMap.get(code);
		}
		else
		{
			return Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN;
		}
	}

	/**
	 * <pre>
	 * 创建一个默认的返回对象
	 * </pre>
	 * @return 业务返回对象
	 */
	public static ServiceResult<Object> getServiceResult()
	{
		return new ServiceResult<Object>();
	}

	/**
	 * <pre>
	 * 创建一个返回对象
	 * </pre>
	 * @return 业务返回对象
	 */
	public static <T> ServiceResult<T> getServiceResult(T value)
	{
		return new ServiceResult<T>(value);
	}
	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @return 业务返回对象
	 */
	public static <T> ServiceResult<T> getServiceResult(Class<T> type)
	{
		return new ServiceResult<T>();
	}
	/**
	 * <pre>
	 * 通过状态码和信息获取业务返回对象
	 * </pre>
	 * @param code 状态码
	 * @return 业务返回对象
	 */
	public static ServiceResult<Object> getServiceResult(String code,String msg)
	{
		return new ServiceResult<Object>(code, msg);
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param code 状态码
	 * @return 业务返回对象
	 */
	public static ServiceResult<Object> getServiceResult(String code)
	{
		if (ServiceResultFactory.serviceResultMap.containsKey(code))
		{
			return new ServiceResult<Object>(code, ServiceResultFactory.serviceResultMap.get(code));
		}
		else
		{
			return new ServiceResult<Object>(code, Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN);
		}
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param code 状态码
	 * @return 业务返回对象
	 */
	public static <T> ServiceResult<T> getServiceResult(String code, Class<T> type)
	{
		if (ServiceResultFactory.serviceResultMap.containsKey(code))
		{
			return new ServiceResult<T>(code, ServiceResultFactory.serviceResultMap.get(code));
		}
		else
		{
			return new ServiceResult<T>(code, Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN);
		}
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param code 状态码
	 * @return 业务返回对象
	 */
	public static <T> ServiceResult<T> getServiceResult(String code, T value)
	{
		if (ServiceResultFactory.serviceResultMap.containsKey(code))
		{
			return new ServiceResult<T>(code, ServiceResultFactory.serviceResultMap.get(code), value);
		}
		else
		{
			return new ServiceResult<T>(code, Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN, value);
		}
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param code 状态码
	 * @param value 对象
	 * @param msg 业务信息
	 * @return 业务返回对象
	 */
	public static <T> ServiceResult<T> getServiceResult(String code, T value, String msg)
	{
		if (ServiceResultFactory.serviceResultMap.containsKey(code))
		{
			return new ServiceResult<T>(code, ServiceResultFactory.serviceResultMap.get(code), value);
		}
		else
		{
			if (msg != null)
			{
				return new ServiceResult<T>(code, msg, value);
			}
			else
			{
				return new ServiceResult<T>(code, Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN, value);
			}

		}
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param code 状态码
	 * @param cause 原始异常对象
	 * @return 业务返回对象
	 */
	public static ServiceResult<Object> getServiceResult(String code, Throwable cause)
	{
		if (ServiceResultFactory.serviceResultMap.containsKey(code))
		{
			return new ServiceResult<Object>(code, ServiceResultFactory.serviceResultMap.get(code), cause);
		}
		else
		{
			return new ServiceResult<Object>(code, Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN, cause);
		}
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param code 状态码
	 * @param cause 原始异常对象
	 * @return 业务返回对象
	 */
	public static <T> ServiceResult<T> getServiceResult(String code, Throwable cause, Class<T> type)
	{
		if (ServiceResultFactory.serviceResultMap.containsKey(code))
		{
			return new ServiceResult<T>(code, ServiceResultFactory.serviceResultMap.get(code), cause);
		}
		else
		{
			return new ServiceResult<T>(code, Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN, cause);
		}
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param cause 异常对象
	 * @return 业务返回对象
	 */
	public static ServiceResult<Object> getServiceResult(SystemException cause)
	{
		return new ServiceResult<Object>(cause);
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param cause 异常对象
	 * @return 业务返回对象
	 */
	public static <T> ServiceResult<T> getServiceResult(SystemException cause, Class<T> type)
	{
		return new ServiceResult<T>(cause);
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param cause 异常对象
	 * @return 业务返回对象
	 */
	public static ServiceResult<Object> getServiceResult(Throwable cause)
	{
		return new ServiceResult<Object>(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN,
				Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN, cause);
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param cause 异常对象
	 * @return 业务返回对象
	 */
	public static <T> ServiceResult<T> getServiceResult(Throwable cause, Class<T> type)
	{
		return new ServiceResult<T>(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN,
				Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN, cause);
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param cause 异常对象
	 * @return 业务返回对象
	 */
	public static ServiceResult<Object> getServiceResult(TransException cause)
	{
		return new ServiceResult<Object>(cause);
	}

	/**
	 * <pre>
	 * 通过状态码获取业务返回对象
	 * </pre>
	 * @param cause 异常对象
	 * @return 业务返回对象
	 */
	public static <T> ServiceResult<T> getServiceResult(TransException cause, Class<T> type)
	{
		return new ServiceResult<T>(cause);
	}

	@SuppressWarnings("rawtypes")
	private static void init()
	{
		ConfigProvider<ServiceResult> cp = ConfigProviderFactory.getInstance(Constant.RESPONSE_CODE_CONFIG_FILEPATH,
				ServiceResult.class);
		Map<String, ServiceResult> maps = cp.getAllConfig();
		for (Iterator<Entry<String, ServiceResult>> iter = maps.entrySet().iterator(); iter.hasNext();)
		{
			Entry<String, ServiceResult> element = iter.next();
			ServiceResultFactory.serviceResultMap.put(element.getKey(), element.getValue().getMessage());
		}
		cp = null;
	}
}
