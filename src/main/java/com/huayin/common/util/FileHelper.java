/**
 * <pre>
 * Title: 		FileHelper.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2008-3-17 下午03:18:51
 * Copyright: 	Copyright (c) 2008
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.huayin.common.exception.SystemException;

/**
 * <pre>
 * 系统文件帮助类, 当编码为"ISO-8859-1"可以读写二进制文件
 * </pre>
 * @author linriqing
 * @version 1.0, 2008-3-17
 */
public class FileHelper
{
	/**
	 * <pre>
	 * 从文件读出文本字符串
	 * </pre>
	 * @param filePath 文件绝对路径
	 * @param encoding 读取的编码, 设置编码为"ISO-8859-1"可以读取二进制文件
	 * @return 文本字符串
	 */
	public static String readFileAsText(String filePath, String encoding)
	{
		StringBuffer content = new StringBuffer();
		BufferedReader reader = null;
		try
		{
			File f = new File(filePath);
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding));
			char[] re = new char[1024];
			int len = 0;
			while ((len = reader.read(re)) != -1)
			{
				content.append(re, 0, len);
			}
		}
		catch (IOException e)
		{
			throw new SystemException(e);
		}
		finally
		{
			try
			{
				if (reader != null)
				{
					reader.close();
				}
			}
			catch (IOException e)
			{
				throw new SystemException(e);
			}
		}
		return content.toString();
	}

	/**
	 * <pre>
	 * 将文本字符串写入到文件
	 * </pre>
	 * @param filePath 文件绝对路径
	 * @param content 文本字符串
	 * @param encoding 写入的编码, 设置编码为"ISO-8859-1"可以写入二进制文件
	 */
	public static void writeTextFile(String filePath, String content, String encoding)
	{
		BufferedWriter writer = null;
		try
		{
			File f = new File(filePath);
			if (f.exists())
			{
				throw new IOException("File is exist!" + f.getPath());
			}
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), encoding));
			writer.write(content);
			writer.flush();
		}
		catch (Exception e)
		{
			throw new SystemException(e);
		}
		finally
		{
			try
			{
				if (writer != null)
				{
					writer.close();
				}
			}
			catch (IOException e)
			{
				throw new SystemException(e);
			}
		}
	}
}
