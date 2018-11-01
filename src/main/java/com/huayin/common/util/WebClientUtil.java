package com.huayin.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * 客户端获取提交页面代码类
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-12-28
 */
public final class WebClientUtil
{
	/**
	 * 生成自动提交页面的内容字符串
	 * @param contentType 内容类型
	 * @param title 页面名称
	 * @param url 提交的页面URL
	 * @param parameters 提交的字段值映射表
	 * @param method 提交的方式,POST或者GET
	 * @return html字符串
	 */
	public static String autoSubmitHtmlForm(String contentType, String title, String url,
			Map<String, String> parameters, String method)
	{
		if (method == null)
		{
			method = "POST";
		}
		StringBuffer sb = new StringBuffer();
		if (method.equalsIgnoreCase("POST"))
		{
			sb.append("<html>\r\n");
			sb.append("<head>\r\n");
			sb.append("<title>" + title + "</title>\r\n");
			sb.append("<meta http-equiv=\"Content-Type\" content=\"" + contentType + "\">\r\n");
			sb.append("<meta http-equiv=\"Cache-Control\" content=\"no-cache\">\r\n");
			sb.append("<meta http-equiv=\"Pragma\" content=\"no-cache\">\r\n");
			sb.append("<meta http-equiv=\"Expires\" content=\"0\">\r\n");
			sb.append("</head>\r\n");
			sb.append("<body>\r\n");
			sb.append("正在提交订单,请稍候......\r\n");
			sb.append("\r\n");
			sb.append("<form name=\"sendOrder\" method=\"" + method + "\" action=\"" + url + "\">\r\n");
			sb.append("<table>\r\n");
			Set<Map.Entry<String, String>> set = parameters.entrySet();
			Iterator<Map.Entry<String, String>> ite = set.iterator();
			while (ite.hasNext())
			{
				Map.Entry<String, String> e = ite.next();
				sb.append("<tr><td><input type=hidden name=\"" + e.getKey() + "\" value=\"" + e.getValue()
						+ "\"></td></tr>\r\n");
			}
			sb.append("</table>\r\n");
			sb.append("</form>\r\n");
			sb.append("\r\n");
			sb.append("<script language=\"javascript\">\r\n");
			sb.append("document.sendOrder.submit();\r\n");
			sb.append("</script>\r\n");
			sb.append("</body>\r\n");
			sb.append("</html>\r\n");
		}
		else
		{
			StringBuffer urlBuf = new StringBuffer(url);
			Set<Map.Entry<String, String>> set = parameters.entrySet();
			Iterator<Map.Entry<String, String>> ite = set.iterator();
			urlBuf.append("?");
			while (ite.hasNext())
			{
				Map.Entry<String, String> e = ite.next();
				urlBuf.append(e.getKey() + "=" + e.getValue() + "&");
			}

			sb.append("<html>\r\n");
			sb.append("<head>\r\n");
			sb.append("<title>" + title + "</title>\r\n");
			sb.append("<meta http-equiv=\"Content-Type\" content=\"" + contentType + "\">\r\n");
			sb.append("<meta http-equiv=\"Cache-Control\" content=\"no-cache\">\r\n");
			sb.append("<meta http-equiv=\"Pragma\" content=\"no-cache\">\r\n");
			sb.append("<meta http-equiv=\"Expires\" content=\"0\">\r\n");
			sb.append("</head>\r\n");
			sb.append("<body>\r\n");
			sb.append("正在提交订单,请稍候......\r\n");
			sb.append("\r\n");
			sb.append("<script language=\"javascript\">\r\n");
			sb.append("window.location = \"" + urlBuf.toString() + "\"\r\n");
			sb.append("</script>\r\n");
			sb.append("</body>\r\n");
			sb.append("</html>\r\n");
		}
		return sb.toString();
	}

	/**
	 * 生成自动提交页面的内容字符串
	 * @param url 提交的页面URL
	 * @param parameters 提交的字段值映射表
	 * @return url字符串
	 */
	public static String autoSubmitWAPForm(String url, Map<String, String> parameters)
	{
		StringBuffer urlBuf = new StringBuffer(url);
		if (parameters.size() > 0)
		{
			Set<Map.Entry<String, String>> set = parameters.entrySet();
			Iterator<Map.Entry<String, String>> ite = set.iterator();
			if (url.contains("?"))
			{
				if (url.indexOf("?") < url.length() - 1)
				{
					urlBuf.append("&");
				}
			}
			else
			{
				urlBuf.append("?");
			}
			while (ite.hasNext())
			{
				Map.Entry<String, String> e = ite.next();
				urlBuf.append(e.getKey() + "=" + e.getValue() + "&");
			}
			urlBuf.deleteCharAt(urlBuf.toString().length() - 1);
		}
		return urlBuf.toString();
	}

	/**
	 * 生成自动提交页面的内容字符串
	 * @param contentType 内容类型
	 * @param title 页面名称
	 * @param content 页面内容
	 * @param url 提交的页面URL
	 * @param parameters 提交的字段值映射表
	 * @param method 提交的方式,POST或者GET
	 * @return html字符串
	 */
	public static String autoSubmitHtmlForm(String contentType, String title, String content, String url,
			Map<String, String> parameters, String method)
	{
		if (method == null)
		{
			method = "POST";
		}
		StringBuffer sb = new StringBuffer();
		if (method.equalsIgnoreCase("POST"))
		{
			sb.append("<html>\r\n");
			sb.append("<head>\r\n");
			sb.append("<title>" + title + "</title>\r\n");
			sb.append("<meta http-equiv=\"Content-Type\" content=\"" + contentType + "\">\r\n");
			sb.append("<meta http-equiv=\"Cache-Control\" content=\"no-cache\">\r\n");
			sb.append("<meta http-equiv=\"Pragma\" content=\"no-cache\">\r\n");
			sb.append("<meta http-equiv=\"Expires\" content=\"0\">\r\n");
			sb.append("</head>\r\n");
			sb.append("<body>\r\n");
			sb.append(content);
			sb.append("\r\n");
			sb.append("<form name=\"sendOrder\" method=\"" + method + "\" action=\"" + url + "\">\r\n");
			sb.append("<table>\r\n");
			Set<Map.Entry<String, String>> set = parameters.entrySet();
			Iterator<Map.Entry<String, String>> ite = set.iterator();
			while (ite.hasNext())
			{
				Map.Entry<String, String> e = ite.next();
				sb.append("<tr><td><input type=hidden name=\"" + e.getKey() + "\" value=\"" + e.getValue()
						+ "\"></td></tr>\r\n");
			}
			sb.append("</table>\r\n");
			sb.append("</form>\r\n");
			sb.append("\r\n");
			sb.append("<script language=\"javascript\">\r\n");
			sb.append("document.sendOrder.submit();\r\n");
			sb.append("</script>\r\n");
			sb.append("</body>\r\n");
			sb.append("</html>\r\n");
		}
		else
		{
			StringBuffer urlBuf = new StringBuffer(url);
			Set<Map.Entry<String, String>> set = parameters.entrySet();
			Iterator<Map.Entry<String, String>> ite = set.iterator();
			urlBuf.append("?");
			while (ite.hasNext())
			{
				Map.Entry<String, String> e = ite.next();
				urlBuf.append(e.getKey() + "=" + e.getValue() + "&");
			}

			sb.append("<html>\r\n");
			sb.append("<head>\r\n");
			sb.append("<title>" + title + "</title>\r\n");
			sb.append("<meta http-equiv=\"Content-Type\" content=\"" + contentType + "\">\r\n");
			sb.append("<meta http-equiv=\"Cache-Control\" content=\"no-cache\">\r\n");
			sb.append("<meta http-equiv=\"Pragma\" content=\"no-cache\">\r\n");
			sb.append("<meta http-equiv=\"Expires\" content=\"0\">\r\n");
			sb.append("</head>\r\n");
			sb.append("<body>\r\n");
			sb.append(content);
			sb.append("\r\n");
			sb.append("<script language=\"javascript\">\r\n");
			sb.append("window.location = \"" + urlBuf.toString() + "\"\r\n");
			sb.append("</script>\r\n");
			sb.append("</body>\r\n");
			sb.append("</html>\r\n");
		}
		return sb.toString();
	}

	/**
	 * 解析request.getReader().readLine()，变成正常格式
	 * @param s request.getReader().readLine()读出来的字符串
	 * @return 解析出的字符串
	 * @throws Exception
	 */
	public static String decode(String s) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			switch (c)
			{
				case '+':
					sb.append(' ');
					break;
				case '%':
					sb.append((char) Integer.parseInt(s.substring(i + 1, i + 3), 16));
					i += 2;
					break;
				default:
					sb.append(c);
					break;
			}
		}
		String result = sb.toString();
		byte[] inputBytes = null;
		inputBytes = result.getBytes("ISO-8859-1");
		return new String(inputBytes);
	}

	/**
	 * <pre>
	 * 从url参数后缀转为参数值Map映射表
	 * </pre>
	 * @param queryString url参数后缀
	 * @return 参数值Map映射表
	 * @throws Exception
	 */
	public static Map<String, String> readUrlParameterToMap(String queryString) throws Exception
	{
		Map<String, String> map = new HashMap<String, String>();
		String[] parameters = queryString.split("[&]");
		for (int i = 0; i < parameters.length; i++)
		{
			String pair = parameters[i];
			if (pair.indexOf("=") != -1)
			{
				String key = pair.substring(0, pair.indexOf("="));
				String value = pair.substring(pair.indexOf("=") + 1, pair.length());
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * 将给定的中文字符串转换为UTF码,在JAVA类中调用
	 * @param gbString 字符串
	 * @return UTF码字符串
	 */
	public static String getJavaUTFString(final String gbString)
	{
		if ((gbString == null) && ("".equals(gbString)))
		{
			return "";
		}
		char[] utfBytes = gbString.toCharArray();
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++)
		{
			String hexB = "";
			if (utfBytes[byteIndex] > '!')
			{
				hexB = Integer.toHexString(utfBytes[byteIndex]);
				if (hexB.length() <= 2)
				{
					hexB = "00" + hexB;
				}
				unicodeBytes = unicodeBytes + "&#x" + hexB + ";";
			}
			else
			{
				unicodeBytes += utfBytes[byteIndex];
			}
		}
		return unicodeBytes;
	}
}
