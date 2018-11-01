/**
 * <pre>
 * Title: 		ClassUtil.java
 * Project: 	AnteAgent
 * Type:		com.huayin.util.ClassUtil
 * Author:		linriqing
 * Create:	 	2007-2-1 上午09:26:07
 * Copyright: 	Copyright (c) 2007
 * Company:		
 * <pre>
 */
package com.huayin.common.util;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * <pre>
 * 针对类的一些帮助方法
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-2-1
 */
public class ClassHelper
{
	private static final Log logger = LogFactory.getLog(ClassHelper.class);

	/**
	 * <pre>
	 * 通过类全称 获取类型对象
	 * </pre>
	 * @param className 类全称
	 * @return 类型对象
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClassForName(String className) throws ClassNotFoundException
	{
		return Class.forName(className);
	}

	/**
	 * <pre>
	 * 获取指定包下所有类名称
	 * </pre>
	 * @param basePackage
	 * @return
	 */
	public static Set<MetadataReader> getClassInPackageBySpring(String basePackage)
	{
		Set<MetadataReader> candidates = new LinkedHashSet<MetadataReader>();
		try
		{
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))
					+ "/" + "**/*.class";
			PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = pathMatchingResourcePatternResolver.getResources(packageSearchPath);
			boolean traceEnabled = logger.isTraceEnabled();
			for (Resource resource : resources)
			{
				if (traceEnabled)
				{
					logger.trace("Scanning " + resource);
				}
				if (resource.isReadable())
				{
					try
					{
						MetadataReader metadataReader = new CachingMetadataReaderFactory(
								pathMatchingResourcePatternResolver).getMetadataReader(resource);
						candidates.add(metadataReader);
					}
					catch (Throwable ex)
					{
						throw new BeanDefinitionStoreException("Failed to read candidate component class: " + resource,
								ex);
					}
				}
				else
				{
					if (traceEnabled)
					{
						logger.trace("Ignored because not readable: " + resource);
					}
				}
			}
		}
		catch (IOException ex)
		{
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		}
		return candidates;
	}

	/**
	 * <pre>
	 * 通过类全称获取不包括包名的类名
	 * </pre>
	 * @param classname 类全称
	 * @return 不包括包名的类名
	 */
	public static String getShortTypeName(String classname)
	{
		return classname.substring(classname.lastIndexOf(".") + 1);
	}

	public static void list(String pkgName, Set<String> classList, File dir)
	{
		if ((pkgName != null) && (pkgName.length() > 0))
		{
			pkgName = pkgName + ".";
		}
		File[] listFiles = dir.listFiles();
		for (File file : listFiles)
		{
			if (file.isFile())
			{
				String className = file.getName();
				if (className.endsWith(".class"))
				{
					className = pkgName + className.substring(0, className.length() - 6);
					classList.add(className);
				}
			}
			else
			{
				list(pkgName + file.getName(), classList, file);
			}
		}
	}

	public static boolean isPrimitive4Number(Class<?> valueType)
	{
		return ((valueType.isPrimitive()) || (valueType.equals(Integer.class)) || (valueType.equals(Long.class))
				|| (valueType.equals(Float.class)) || (valueType.equals(Boolean.class))
				|| (valueType.equals(Short.class)) || (valueType.equals(Byte.class)) || (valueType.equals(Double.class)));
	}

	public static boolean isPrimitive4String(Class<?> valueType)
	{
		return ((valueType.equals(String.class)) || (valueType.equals(Character.class)));
	}

	public static boolean isPrimitive4Date(Class<?> valueType)
	{
		return ((valueType.equals(Date.class)) || (valueType.equals(Time.class))
				|| (valueType.equals(java.sql.Date.class)) || (valueType.equals(java.sql.Timestamp.class)));
	}

	public static boolean isPrimitive(Class<?> valueType)
	{
		return (valueType.isPrimitive()) || (valueType.equals(String.class)) || (valueType.equals(Date.class))
				|| (valueType.equals(Integer.class)) || (valueType.equals(Long.class))
				|| (valueType.equals(Float.class)) || (valueType.equals(Boolean.class))
				|| (valueType.equals(Short.class)) || (valueType.equals(Byte.class))
				|| (valueType.equals(Character.class)) || (valueType.equals(Double.class))
				|| (valueType.equals(Void.class)) || (valueType.equals(Time.class))
				|| (valueType.equals(java.sql.Date.class)) || (valueType.equals(java.sql.Timestamp.class));
	}

}
