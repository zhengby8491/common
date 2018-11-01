/**
 * <pre>
 * Title: 		PrintStackTrace.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2009-6-25 上午11:33:34
 * Copyright: 	Copyright (c) 2009
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.util;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;

/**
 * <pre>
 * 自动打印堆栈信息
 * 代码示例:
 * <code>
 * PrintStackTrace.printStackTrace(System.out);
 * PrintStackTrace.printStackTrace(org.apache.commons.logging.Log);
 * </code>
 * 上面的代码会自动打印堆栈信息到错误输出流
 * </pre>
 * @author linriqing
 * @version 1.0, 2009-6-25
 */
public class PrintStackTrace
{

	/**
	 * <pre>
	 * 输出当前调用堆栈到打印流中
	 * </pre>
	 * @param writer 打印流对象
	 */
	public static void printStackTrace(PrintStream writer)
	{
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		writer.println(stackTraceElements[2].getClassName() + " stacktrace info:");
		for (int i = 2; i < stackTraceElements.length; i++)
		{
			StackTraceElement stackTraceElement = stackTraceElements[i];
			writer.println("\t " + stackTraceElement.toString());
		}
	}

	/**
	 * <pre>
	 * 输出当前调用堆栈到日志中
	 * </pre>
	 * @param logger 日志对象
	 * @throws IOException IO异常
	 */
	public static void printStackTrace(Log logger)
	{
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		StringWriter sw = new StringWriter();
		try
		{
			sw.write(stackTraceElements[2].getClassName() + " stacktrace info:" + StringHelper.getLineSeparator());
			for (int i = 2; i < stackTraceElements.length; i++)
			{
				StackTraceElement stackTraceElement = stackTraceElements[i];
				sw.write("\t " + stackTraceElement.toString() + StringHelper.getLineSeparator());
			}
		}
		finally
		{
			try
			{
				sw.close();
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		logger.info(sw.toString());
	}

	/**
	 * <pre>
	 * 输出异常到字符串
	 * </pre>
	 * @param throwable
	 * @return
	 * @throws IOException IO异常
	 */
	public static String getStackTrace(Throwable throwable)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = null;
		try
		{
			pw = new PrintWriter(sw);
			throwable.printStackTrace(new PrintWriter(sw));
			return sw.toString();
		}
		finally
		{
			if (pw != null)
			{
				try
				{
					pw.close();
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
			}
		}
	}
}