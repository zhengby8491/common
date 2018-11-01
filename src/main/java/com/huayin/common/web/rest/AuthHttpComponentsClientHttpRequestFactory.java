/**
 * <pre>
 * Title: 		SecureCommonsClientHttpRequestFactory.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2013-10-24 上午10:03:01
 * Copyright: 	Copyright (c) 2013
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.web.rest;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * <pre>
 * 基于HttpClient的SpringSecurity客户端安全认证请求工厂
 * </pre>
 * @author linriqing
 * @version 1.0, 2013-10-24
 */
public class AuthHttpComponentsClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory
{
	protected HttpHost host;

	protected String userName;

	protected String password;

	public AuthHttpComponentsClientHttpRequestFactory(int defaultMaxPerRoute, int maxTotalConnections, int connTimeout,
			int readTimeout)
	{
		super();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
		connManager.setMaxTotal(maxTotalConnections);
		super.setHttpClient(HttpClientBuilder.create().setConnectionManager(connManager).build());
		super.setConnectTimeout(connTimeout);
		super.setReadTimeout(readTimeout);
	}

	@Override
	protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri)
	{
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);

		// Add AuthCache to the execution context
		HttpClientContext localcontext = HttpClientContext.create();
		localcontext.setAuthCache(authCache);

		if (userName != null)
		{
			BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(host), new UsernamePasswordCredentials(userName, password));
			localcontext.setCredentialsProvider(credsProvider);
		}
		return localcontext;
	}

	public HttpHost getHost()
	{
		return host;
	}

	public void setHost(HttpHost host)
	{
		this.host = host;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

}
