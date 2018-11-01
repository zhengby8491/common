/**
 * <pre>
 * Title: 		ClassLoaderUtil.java
 * Author:		linriqing
 * Create:	 	2010-8-18 下午03:39:20
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.util;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.huayin.common.exception.SystemException;

/**
 * <pre>
 * TODO 输入类型说明
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-8-18
 */
public class ClassLoaderUtil
{
	private static Method addURL;

	private static Field classes;

	private static URLClassLoader ext = (URLClassLoader) getExtClassLoader();

	private static URLClassLoader system = (URLClassLoader) getSystemClassLoader();

	static
	{
		try
		{
			classes = ClassLoader.class.getDeclaredField("classes");
			addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
		}
		catch (Exception e)
		{
			// won't happen ,but remain it
			throw new SystemException(e);
		}
		classes.setAccessible(true);
		addURL.setAccessible(true);
	}

	public static void addClassPath(File dirOrJar)
	{
		try
		{
			addURL2SystemClassLoader(dirOrJar.toURI().toURL());
		}
		catch (MalformedURLException e)
		{
			throw new SystemException(e);
		}
	}

	public static void addClassPath(String path)
	{
		addClassPath(new File(path));
	}

	public static void addExtClassPath(File dirOrJar)
	{
		try
		{
			addURL2ExtClassLoader(dirOrJar.toURI().toURL());
		}
		catch (MalformedURLException e)
		{
			throw new SystemException(e);
		}
	}

	public static void addExtClassPath(String path)
	{
		addExtClassPath(new File(path));
	}

	public static void addURL2ExtClassLoader(URL url)
	{
		try
		{
			addURL.invoke(ext, new Object[] { url });
		}
		catch (Exception e)
		{
			throw new SystemException(e);
		}
	}

	public static void addURL2SystemClassLoader(URL url)
	{
		try
		{
			addURL.invoke(system, new Object[] { url });
		}
		catch (Exception e)
		{
			throw new SystemException(e);
		}
	}

	public static URL getClassPath()
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return classLoader.getResource("");
	}

	public static String getLibPath()
	{
		File classRootFile = new File(getClassPath().getFile());
		String weblib = classRootFile.getParent() + File.separator + "lib";
		return weblib;
	}

	public static List<?> getClassesLoadedByClassLoader(ClassLoader cl)
	{
		try
		{
			return (List<?>) classes.get(cl);
		}
		catch (Exception e)
		{
			throw new SystemException(e);
		}
	}

	public static List<?> getClassesLoadedByExtClassLoader()
	{
		return getClassesLoadedByClassLoader(getExtClassLoader());
	}

	/**
	 * 获得加载的类
	 * @return
	 */
	public static List<?> getClassesLoadedBySystemClassLoader()
	{
		return getClassesLoadedByClassLoader(getSystemClassLoader());
	}

	public static ClassLoader getExtClassLoader()
	{
		return getSystemClassLoader().getParent();
	}

	public static URL[] getExtClassPath()
	{
		return getExtURLs();
	}

	public static URL[] getExtURLs()
	{
		return ext.getURLs();
	}

	public static ClassLoader getSystemClassLoader()
	{
		return ClassLoader.getSystemClassLoader();
	}

	public static URL[] getSystemClassPath()
	{
		return getSystemURLs();
	}

	public static URL[] getSystemURLs()
	{
		return system.getURLs();
	}

	public static void listExtClassPath()
	{
		listExtClassPath(System.out);
	}

	public static void listClassPath(PrintStream ps)
	{
		ps.println("ClassPath:");
		ps.println(getClassPath().getFile());
	}

	public static void listLibPath(PrintStream ps)
	{
		ps.println("LibPath:");
		ps.println(getLibPath());
	}

	public static void listExtClassPath(PrintStream ps)
	{
		ps.println("ExtClassPath:");
		list(ps, getExtClassPath());
	}

	public static void listSystemClassPath()
	{
		listSystemClassPath(System.out);
	}

	public static void listSystemClassPath(PrintStream ps)
	{
		ps.println("SystemClassPath:");
		list(ps, getSystemClassPath());
	}

	private static void list(PrintStream ps, URL[] classPath)
	{
		for (int i = 0; i < classPath.length; i++)
		{
			ps.println(classPath[i]);
		}
	}
}
