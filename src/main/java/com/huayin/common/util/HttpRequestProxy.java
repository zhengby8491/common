/**
 * <pre>
 * Title: 		HttpRequestProxy.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.web.HttpRequestProxy
 * Author:		linriqing
 * Create:	 	2007-7-3 上午03:07:07
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.huayin.common.configuration.ConfigProvider;
import com.huayin.common.configuration.ConfigProviderFactory;
import com.huayin.common.constant.Constant;
import com.huayin.common.exception.SystemException;

/**
 * <pre>
 * HTTP请求代理类
 * 连接超时/读取数据超时/请求编码可以通过hp-common.properties文件进行设置.
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-7-3
 */
public class HttpRequestProxy
{
	public final static String HTTP_REQUEST_CONTENTTYPE_APPLICATION_GZIP = "application/x-gzip";

	/**
	 * 连接超时
	 */
	private static int connectTimeOut = 30000;

	private static final String HTTP_REQUEST_PROXY_CONNECT_TIME_OUT = "HttpRequestProxy.connectTimeOut";

	private static final String HTTP_REQUEST_PROXY_READ_TIME_OUT = "HttpRequestProxy.readTimeOut";

	private final static Log logger = LogFactory.getLog(HttpRequestProxy.class);

	/**
	 * 读取数据超时
	 */
	private static int readTimeOut = 60000;

	static
	{
		try
		{
			ConfigProvider<String> cp = ConfigProviderFactory.getInstance(Constant.HY_COMMON_PROPERTIES_FILEPATH);
			HttpRequestProxy.connectTimeOut = Integer.parseInt(cp.getConfigByPrimaryKey(
					HTTP_REQUEST_PROXY_CONNECT_TIME_OUT).toString());
			HttpRequestProxy.readTimeOut = Integer.parseInt(cp.getConfigByPrimaryKey(HTTP_REQUEST_PROXY_READ_TIME_OUT)
					.toString());
		}
		catch (Exception e)
		{
			logger.info("读取HttpReqeustProxy参数配置文件时错误, 配置值将使用默认值. 错误信息{" + e.getMessage() + "}");

		}
	}

	/**
	 * <pre>
	 * 发送HTTP请求
	 * </pre>
	 * @param requestURL 请求地址
	 * @param requestContent 请求内容
	 * @param requestMethod 请求方式:POST或者GET
	 * @param connectTimeout 连接超时时间限制
	 * @param readTimeout 读取数据超时时间限制
	 * @param useGZip 是否使用GZip进行压缩
	 * @param requestEncoding TODO
	 * @return 响应内容
	 */
	private static String connect(String requestURL, String requestContent, String requestMethod, int connectTimeout,
			int readTimeout, boolean useGZip, String requestEncoding)
	{
		HttpURLConnection url_con = null;
		String responseContent = null;
		URL url = null;
		BufferedWriter bw = null;
		BufferedReader br = null;
		OutputStream out = null;
		try
		{
			url = new URL(requestURL);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestProperty("User-Agent", "Mozilla/4.0");
			url_con.setConnectTimeout(connectTimeout);
			url_con.setReadTimeout(readTimeout);
			url_con.setRequestMethod(requestMethod);
			if ((requestContent != null) && (!requestContent.equalsIgnoreCase("")))
			{
				if (useGZip)
				{
					url_con.setRequestProperty("Content-Type", HTTP_REQUEST_CONTENTTYPE_APPLICATION_GZIP);
					url_con.setDoOutput(true);
					out = url_con.getOutputStream();
					GZIPOutputStream gzip = new GZIPOutputStream(out);
					bw = new BufferedWriter(new OutputStreamWriter(gzip));
					bw.write(requestContent);
				}
				else
				{
					url_con.setDoOutput(true);
					out = url_con.getOutputStream();
					bw = new BufferedWriter(new OutputStreamWriter(out));
					bw.write(requestContent);
				}

				bw.flush();
				bw.close();
				out.close();
			}

			url_con.connect();
			if (HTTP_REQUEST_CONTENTTYPE_APPLICATION_GZIP.equalsIgnoreCase(url_con.getContentType()))
			{
				br = new BufferedReader(new InputStreamReader(new GZIPInputStream(url_con.getInputStream())));
			}
			else
			{
				if (requestEncoding != null)
				{
					br = new BufferedReader(new InputStreamReader(url_con.getInputStream(), requestEncoding));
				}
				else
				{
					br = new BufferedReader(new InputStreamReader(url_con.getInputStream()));
				}
			}

			StringBuffer sb = new StringBuffer();
			char[] re = new char[1024];
			int len = 0;
			while ((len = br.read(re)) != -1)
			{
				sb.append(re, 0, len);
			}
			responseContent = sb.toString();
			br.close();
		}
		catch (IOException e)
		{
			logger.error("连接URL{" + requestURL + "}时遇到网络故障, 异常消息{" + e.getMessage() + "}");
		}
		finally
		{
			if (url_con != null)
			{
				url_con.disconnect();
			}
			bw = null;
			out = null;
			br = null;
			url_con = null;
			url = null;
		}
		return responseContent;
	}

	/**
	 * <pre>
	 * 发送不带参数的GET的HTTP请求
	 * </pre>
	 * @param reqUrl HTTP请求URL
	 * @return HTTP响应的字符串
	 */
	public static String doGet(String reqUrl)
	{
		int paramIndex = reqUrl.indexOf("?");
		if (paramIndex != -1)
		{
			return HttpRequestProxy.doGet(reqUrl.substring(0, paramIndex),
					reqUrl.substring(paramIndex + 1, reqUrl.length()));
		}
		else
		{
			String requestContent = null;
			return HttpRequestProxy.doGet(reqUrl, requestContent);
		}
	}

	/**
	 * <pre>
	 * 发送带参数键值对的GET的HTTP请求
	 * </pre>
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @param requestEncoding 请求编码
	 * @return HTTP响应的字符串
	 */
	public static String doGet(String reqUrl, Map<String, String> parameters, String requestEncoding)
	{
		try
		{
			StringBuffer params = new StringBuffer();
			for (Iterator<Entry<String, String>> iter = parameters.entrySet().iterator(); iter.hasNext();)
			{
				Entry<String, String> element = iter.next();
				params.append(element.getKey());
				params.append("=");
				params.append(URLEncoder.encode(element.getValue().toString(), requestEncoding));
				params.append("&");
			}

			if (params.length() > 0)
			{
				params = params.deleteCharAt(params.length() - 1);
			}
			return HttpRequestProxy.doGet(reqUrl, params.toString());
		}
		catch (UnsupportedEncodingException e)
		{
			throw new SystemException(e);
		}
	}

	/**
	 * <pre>
	 * 发送只有参数值无参数名的GET的HTTP请求
	 * </pre>
	 * @param reqUrl HTTP请求URL
	 * @param parameter 参数值
	 * @return HTTP响应的字符串
	 */
	public static String doGet(String reqUrl, String parameter)
	{
		return HttpRequestProxy.connect(reqUrl, parameter, "GET", HttpRequestProxy.connectTimeOut,
				HttpRequestProxy.readTimeOut, false, null);
	}

	/**
	 * <pre>
	 * 发送带参数键值对的POST的HTTP请求
	 * </pre>
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @param requestEncoding 请求编码类型, 响应时使用相同编码
	 * @return HTTP响应的字符串
	 */
	public static String doPost(String reqUrl, Map<String, String> parameters, String requestEncoding)
	{
		return HttpRequestProxy.doPost(reqUrl, parameters, requestEncoding, false);
	}

	/**
	 * <pre>
	 * 发送带参数键值对的POST的HTTP请求
	 * </pre>
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @param requestEncoding 请求编码类型
	 * @param useGZip 是否使用GZip进行压缩
	 * @return HTTP响应的字符串
	 */
	public static String doPost(String reqUrl, Map<String, String> parameters, String requestEncoding, boolean useGZip)
	{
		try
		{
			StringBuffer params = new StringBuffer();
			if (useGZip)
			{
				for (Iterator<Entry<String, String>> iter = parameters.entrySet().iterator(); iter.hasNext();)
				{
					Entry<String, String> element = iter.next();
					params.append(element.getKey());
					params.append("=");
					params.append(element.getValue().toString());
					params.append("&");
				}
			}
			else
			{
				for (Iterator<Entry<String, String>> iter = parameters.entrySet().iterator(); iter.hasNext();)
				{
					Entry<String, String> element = iter.next();
					params.append(element.getKey());
					params.append("=");
					params.append(URLEncoder.encode(element.getValue().toString(), requestEncoding));
					params.append("&");
				}
			}

			if (params.length() > 0)
			{
				params = params.deleteCharAt(params.length() - 1);
			}
			return HttpRequestProxy.connect(reqUrl, params.toString(), "POST", HttpRequestProxy.connectTimeOut,
					HttpRequestProxy.readTimeOut, useGZip, requestEncoding);

		}
		catch (UnsupportedEncodingException e)
		{
			throw new SystemException(e);
		}
	}

	/**
	 * <pre>
	 * 发送只有参数值无参数名的POST的HTTP请求
	 * </pre>
	 * @param reqUrl HTTP请求URL
	 * @param parameter 参数值
	 * @return HTTP响应的字符串
	 */
	public static String doPost(String reqUrl, String parameter)
	{
		return HttpRequestProxy.connect(reqUrl, parameter, "POST", HttpRequestProxy.connectTimeOut,
				HttpRequestProxy.readTimeOut, false, null);
	}
	
	/**
	 * get请求 返回Map
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> doGetStr(String url) throws ParseException, IOException{
		Map<String, Object> map=null;
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		HttpGet httpget = new HttpGet(url);  
		CloseableHttpResponse response = httpclient.execute(httpget);  
		try
		{
			HttpEntity entity = response.getEntity();
			if(entity!=null){
				String result = EntityUtils.toString(entity,"UTF-8");
				map=(Map<String, Object>) JsonUtils.jsonToMap(result);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.close();
		}
		return map;
	}
	
	/**
	 * post请求 返回Map
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> doPostStr(String url) throws ParseException, IOException{
		Map<String, Object> map=null;
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		HttpPost httppost = new HttpPost(url);  
		CloseableHttpResponse response = httpclient.execute(httppost);  
		try
		{
			HttpEntity entity = response.getEntity();
			if(entity!=null){
				String result = EntityUtils.toString(entity,"UTF-8");
				map=(Map<String, Object>) JsonUtils.jsonToMap(result);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.close();
		}
		return map;
	}
}
