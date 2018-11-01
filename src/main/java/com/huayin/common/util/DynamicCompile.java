/**
 * <pre>
 * Title: 		DynamicCompile.java
 * Author:		linriqing
 * Create:	 	2010-7-2 下午04:22:39
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import com.sun.tools.javac.Main;

/**
 * <pre>
 * 动态编译类
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-7-2
 */
public class DynamicCompile
{
	private static ClassLoader loader;

	public class ClassSourceDescriptor
	{
		Set<String> imports;

		Set<MethodSourceDescriptor> methods;

		Set<PropertySourceDescriptor> properties;

		public Set<String> getImports()
		{
			return imports;
		}

		public void setImports(Set<String> imports)
		{
			this.imports = imports;
		}

		public Set<MethodSourceDescriptor> getMethods()
		{
			return methods;
		}

		public void setMethods(Set<MethodSourceDescriptor> methods)
		{
			this.methods = methods;
		}

		public Set<PropertySourceDescriptor> getProperties()
		{
			return properties;
		}

		public void setProperties(Set<PropertySourceDescriptor> properties)
		{
			this.properties = properties;
		}
	}

	public class MethodSourceDescriptor
	{
		Set<String> sourceLine;

		String name;

		public Set<String> getSourceLine()
		{
			return sourceLine;
		}

		public void setSourceLine(Set<String> sourceLine)
		{
			this.sourceLine = sourceLine;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}
	}

	public class PropertySourceDescriptor
	{
		Class<?> type;

		String name;

		Object value;

		public Class<?> getType()
		{
			return type;
		}

		public void setType(Class<?> type)
		{
			this.type = type;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public Object getValue()
		{
			return value;
		}

		public void setValue(Object value)
		{
			this.value = value;
		}
	}

	public static void compileTempCode(String className, String code)
	{
		try
		{
			String filename = className + ".java";
			String path = System.getProperty("java.io.tmpdir") + "/";
			String filePath = path + filename;
			FileHelper.writeTextFile(filePath, code, "UTF-8");
			String[] arg = new String[] { "-d", path, filePath };
			Main.compile(arg);

			URL[] us = { new URL("file://" + path) };
			loader = new URLClassLoader(us);
			Class<?> cls = loader.loadClass(className);
			Method main = cls.getMethod("main", new Class[] { String[].class });
			main.invoke(null, new Object[] { new String[0] });
			File file = new File(filePath);
			file.delete();
			file = new File(path + className + ".class");
			file.delete();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
