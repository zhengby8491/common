package com.huayin.common.util;

/**
 * <pre>
 * Title: 		ResourcePathHelper.java
 * Project: 	AnteAgent
 * Type:		.ResourcePathHelper
 * Author:		linriqing
 * Create:	 	2006-12-29 15:43:39
 * Copyright: 	Copyright (c) 2006
 * Company:		
 * <pre>
 */
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * <pre>
 * 获取类的绝对路径
 * 注意：慎用此类的绝对路径方法，如果目标应用打包成为jar单个文件时将会导致无法找到文件的问题。
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-12-29
 */
public class ResourcePathHelper
{
	/**
	 * 获取类的class文件位置的URL。这个方法是本类最基础的方法，供其它方法调用。
	 */
	private static URL getClassLocationURL(final Class<?> cls)
	{
		if (cls == null)
		{
			throw new IllegalArgumentException("null input: cls");
		}
		URL result = null;
		final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
		final ProtectionDomain pd = cls.getProtectionDomain();
		if (pd != null)
		{
			final CodeSource cs = pd.getCodeSource();
			if (cs != null)
			{
				result = cs.getLocation();
			}

			if (result != null)
			{
				if ("file".equals(result.getProtocol()))
				{
					try
					{
						if (result.toExternalForm().endsWith(".jar") || result.toExternalForm().endsWith(".zip"))
						{
							result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
						}
						else if (new File(result.getFile()).isDirectory())
						{
							result = new URL(result, clsAsResource);
						}
					}
					catch (MalformedURLException ignore)
					{
					}
				}
			}
		}

		if (result == null)
		{
			final ClassLoader clsLoader = cls.getClassLoader();
			result = clsLoader != null ? clsLoader.getResource(clsAsResource) : ClassLoader
					.getSystemResource(clsAsResource);
		}
		return result;
	}

	/**
	 * 这个方法可以通过与某个类的class文件的相对路径来获取文件或目录的绝对路径。 通常在程序中很难定位某个相对路径，特别是在B/S应用中。 通过这个方法，我们可以根据我们程序自身的类文件的位置来定位某个相对路径。
	 * 比如：某个txt文件相对于程序的Test类文件的路径是../../resource/test.txt，
	 * 那么使用本方法Path.getFullPathRelateClass("../../resource/test.txt",Test.class) 得到的结果是txt文件的在系统中的绝对路径。
	 * @param relatedPath 相对路径
	 * @param cls 用来定位的类
	 * @return 相对路径所对应的绝对路径
	 * @throws IOException 因为本方法将查询文件系统，所以可能抛出IO异常
	 */
	public static String getFullPathRelateClass(String relatedPath, Class<?> cls) throws IOException
	{
		String path = null;
		if (relatedPath == null)
		{
			throw new NullPointerException();
		}
		String clsPath = getPathFromClass(cls);
		File clsFile = new File(clsPath);
		String tempPath = clsFile.getParent() + File.separator + relatedPath;
		File file = new File(tempPath);
		path = file.getCanonicalPath();
		return path;
	}

	/**
	 * 获取一个类的class文件所在的绝对路径。 这个类可以是JDK自身的类，也可以是用户自定义的类，或者是第三方开发包里的类。 只要是在本程序中可以被加载的类，都可以定位到它的class文件的绝对路径。
	 * @param cls 一个对象的Class属性
	 * @return 这个类的class文件位置的绝对路径。 如果没有这个类的定义，则返回null。
	 */
	public static String getPathFromClass(Class<?> cls) throws IOException
	{
		String path = null;
		if (cls == null)
		{
			throw new NullPointerException();
		}
		URL url = getClassLocationURL(cls);
		if (url != null)
		{
			path = url.getPath();
			if ("jar".equalsIgnoreCase(url.getProtocol()))
			{
				try
				{
					path = new URL(path).getPath();
				}
				catch (MalformedURLException e)
				{
				}
				int location = path.indexOf("!/");
				if (location != -1)
				{
					path = path.substring(0, location);
				}
			}
			File file = new File(path);
			path = file.getCanonicalPath();
		}
		return path;
	}

	/**
	 * <pre>
	 * 获取当前类加载的classes目录绝对路径
	 * </pre>
	 * @return classes目录绝对路径
	 * @throws IOException
	 */
	public static String getClassRootPath() throws IOException
	{
		String path = null;
		int pathLevel = ResourcePathHelper.class.getName().split("\\.").length;
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < pathLevel; i++)
		{
			sb.append("../");
		}
		path = getFullPathRelateClass(sb.toString(), ResourcePathHelper.class);
		return path;
	}

	/**
	 * <pre>
	 * 通过类名获取类的绝对路径
	 * </pre>
	 * @param className 类名
	 * @return 类的绝对路径
	 * @throws IOException
	 */
	public static String getPathFromClassName(String className) throws IOException
	{
		return ResourcePathHelper.getClassRootPath() + File.separator + className.replaceAll("\\.", "/");
	}

	public static void main(String[] args)
	{
		try
		{
			System.out.println(getPathFromClass(ResourcePathHelper.class));
			System.out.println(getFullPathRelateClass("/test", ResourcePathHelper.class));
			System.out.println(ResourcePathHelper.getClassRootPath());
			System.out.println(ResourcePathHelper.getPathFromClassName("chinapay.Des"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
