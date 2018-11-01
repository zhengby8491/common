/**
 * <pre>
 * Title: 		RestClient.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2013-10-24 上午11:14:55
 * Copyright: 	Copyright (c) 2013
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.web.rest;

import org.springframework.web.client.RestTemplate;

import com.huayin.common.constant.Constant;
import com.huayin.common.exception.ServiceResult;

/**
 * <pre>
 * REST服务接口的客户端调用工具类
 * 基于RestTemplate和commons.httpclient实现，支持HTTP Basic和HTTP Degist两种认证模式。
 * 为了提高性能，尽量建议单例模式运行。
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-01-27
 */
public class RestClient
{
	private RestTemplate restTemplate;

	/**
	 * 构造函数，默认配置为：每个主机的最大并行链接数=1000，客户端总并行链接最大数=1024，连接超时=10秒，读超时=30秒
	 * @param restTemplate RestTemplate实例
	 * @param userName REST服务接口认证的用户名
	 * @param password REST服务接口认证的密码
	 */
	public RestClient(RestTemplate restTemplate, String userName, String password)
	{
		this.restTemplate = restTemplate;
		
		AuthHttpComponentsClientHttpRequestFactory requestFactory = new AuthHttpComponentsClientHttpRequestFactory(1000, 1024,
				10000, 30000);
		if ((userName != null) || (password != null))
		{
			requestFactory.setUserName(userName);
			requestFactory.setPassword(password);
		}
		this.restTemplate.setRequestFactory(requestFactory);
	}

	/**
	 * 构造函数
	 * @param restTemplate RestTemplate实例
	 * @param userName REST服务接口认证的用户名
	 * @param password REST服务接口认证的密码
	 * @param defaultMaxConnectionsPerHost 每个主机的最大并行链接数
	 * @param maxTotalConnections 客户端总并行链接最大数
	 * @param connTimeout 连接超时，单位毫秒
	 * @param readTimeout 读超时，单位毫秒
	 */
	public RestClient(RestTemplate restTemplate, String userName, String password, int defaultMaxConnectionsPerHost,
			int maxTotalConnections, int connTimeout, int readTimeout)
	{
		this.restTemplate = restTemplate;
		AuthHttpComponentsClientHttpRequestFactory requestFactory = new AuthHttpComponentsClientHttpRequestFactory(
				defaultMaxConnectionsPerHost, maxTotalConnections, connTimeout, readTimeout);
		if ((userName != null) || (password != null))
		{
			requestFactory.setUserName(userName);
			requestFactory.setPassword(password);
		}
		this.restTemplate.setRequestFactory(requestFactory);
	}

	/**
	 * <pre>
	 * 通过GET方式调用REST服务
	 * </pre>
	 * @param <T> 返回的对象类型
	 * @param url 服务URL地址
	 * @param returnType 返回的对象类型
	 * @param pathValue 路径参数，比如url为：http://example.com/hotels/{hotel}/bookings/{booking}, 需要传递的值为：12,13
	 * @return 业务返回对象，通过ServiceResult.getIsSuccess()判断是否成功调用，如果成功的话，通过ServiceResult.getReturnValue获取返回对象
	 */
	public <T> ServiceResult<T> execGet(String url, Class<T> returnType, Object... pathValue)
	{
		ServiceResult<T> forObject = new ServiceResult<T>();
		try
		{
			forObject.setReturnValue(restTemplate.getForEntity(url, returnType, pathValue).getBody());
		}
		catch (Exception e)
		{
			forObject.setCode(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN);
			forObject.setMessage(Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN);
			forObject.setException(e);
			forObject.setIsSuccess(false);
		}
		return forObject;
	}

	/**
	 * <pre>
	 * 通过POST方式调用REST服务
	 * </pre>
	 * @param <T> 返回的对象类型
	 * @param url 服务URL地址
	 * @param returnType 返回的对象类型
	 * @param parameter 请求的对象
	 * @return 业务返回对象，通过ServiceResult.getIsSuccess()判断是否成功调用，如果成功的话，通过ServiceResult.getReturnValue获取返回对象
	 */
	public <T> ServiceResult<T> execPost(String url, Class<T> returnType, Object parameter)
	{
		ServiceResult<T> forObject = new ServiceResult<T>();
		try
		{
			forObject.setReturnValue(restTemplate.postForEntity(url, parameter, returnType).getBody());
		}
		catch (Exception e)
		{
			forObject.setCode(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN);
			forObject.setMessage(Constant.TRANSACTION_RESPONSE_MESSAGE_UNKOWN);
			forObject.setException(e);
			forObject.setIsSuccess(false);
		}
		return forObject;
	}
}
