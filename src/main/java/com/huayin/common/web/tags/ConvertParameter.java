/**
 * Create at 2006-5-11 14:30:47
 */
package com.huayin.common.web.tags;

import java.util.StringTokenizer;

/**
 * <pre>
 * 转换参数
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-6-16
 */
public class ConvertParameter
{

	/**
	 * <pre>
	 * 转换参数
	 * </pre>
	 * @param content 原始内容
	 * @param envP 替换类实例
	 * @return 转换后的内容
	 */
	public static String convert(String content, ConvertProcessor envP)
	{
		StringTokenizer token = new StringTokenizer(content, "@}", true);

		StringBuffer buffer = new StringBuffer(content.length() * 2);

		while (token.hasMoreTokens())
		{
			String tok = token.nextToken();

			if ("@".equals(tok))
			{
				if (token.hasMoreTokens())
				{
					tok = token.nextToken();
					if (tok.startsWith("{"))
					{
						String key = tok.substring(1);
						if (token.hasMoreTokens() && "}".equals(token.nextToken()))
						{
							buffer.append(envP.replace(key));
						}
						else
						{
							buffer.append(tok);
						}
					}
					else
					{
						buffer.append(tok);
					}
				}
				else
				{
					buffer.append(tok);
				}
			}
			else
			{
				buffer.append(tok);
			}
		}
		return buffer.toString();
	}

	public ConvertParameter()
	{
		super();
	}

}
