package com.huayin.common.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * 属性拷贝工具,支持继承，要拷贝的属性必须有get，set的公用方法
 * </pre>
 * @author chenjian
 * @version 1.0, 2010-10-13
 */
public class PropertyClone
{

	private static final Log logger = LogFactory.getLog(PropertyClone.class);
	private final static String SET_METHOD_FIX = "set";

	private final static String GET_METHOD_FIX = "get";

	private PropertyClone()
	{

	}

	private static <T> Map<String, Method> findPublicMethod(T t, String fix)
	{
		Map<String, Method> methodMap = new HashMap<String, Method>();
		Class<?> clz = t.getClass();
		while (!clz.equals(Object.class))
		{
			Method[] methods = clz.getDeclaredMethods();
			for (Method method : methods)
			{
				int status = method.getModifiers();
				if (!Modifier.isPublic(status))
				{
					continue;
				}
				if (method.getName().startsWith(fix))
				{
					String field = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
					if (methodMap.keySet().contains(field))
					{
						continue;
					}
					methodMap.put(field, method);
				}
			}
			clz = clz.getSuperclass();
		}
		return methodMap;
	}

	/**
	 * <pre>
	 * 属性拷贝工具方法(不COPY 值为null的属性)
	 * </pre>
	 * @param <T> 对象类型
	 * @param dest 目标对象
	 * @param orig 源对象
	 */
	public static <T, K> void copyProperties(T dest, K orig)
	{
		copyProperties(dest, orig, false);
	}
	/**
	 * <pre>
	 * 属性拷贝工具方法
	 * </pre>
	 * @param <T> 对象类型
	 * @param dest 目标对象
	 * @param orig 源对象
	 * @param flag 当属性值为null时，是否赋值. true:赋值,false:不赋值
	 */
	public static <T, K> void copyProperties(T dest, K orig, Boolean copyNullFlag)
	{
		copyProperties(dest, orig, copyNullFlag, null);
	}

	public static <T, K> void copyProperties(T dest, K orig, Boolean copyNullFlag, String[] uncope)
	{
		Map<String, Method> destSetMethodMap = findPublicMethod(dest, SET_METHOD_FIX);
		// 源对象中的所有get方法集合
		Map<String, Method> origGetMethodMap = findPublicMethod(orig, GET_METHOD_FIX);
		Set<String> fields = origGetMethodMap.keySet();
		boolean flag = (uncope == null || uncope.length<= 0) ? false : true;
		for (String field : fields)
		{
			if (flag &&  Arrays.asList(uncope).contains(field))
			{
				continue;
			}
			Method getMethod = origGetMethodMap.get(field);
			try
			{
				Object value = getMethod.invoke(orig, new Object[] {});
				if (!copyNullFlag)
				{
					if (value == null)
					{
						continue;
					}
				}
				Method setMethod = destSetMethodMap.get(field);
				if (setMethod != null)
					setMethod.invoke(dest, value);
			}
			catch (Exception e)
			{
				logger.warn("实体属性克隆["+orig.getClass().getName()+":"+field+"]发生异常");
				continue;
			}
		}
	}
	/**
	 * @param <T>
	 * @param <K>
	 * @param dest
	 * @param orig
	 * @param copyNullFlag
	 * @param uncope 非克隆属性名
	 * @param forceCope 强制克隆属性名
	 */
	public static <T, K> void copyProperties(T dest, K orig, Boolean copyNullFlag, String[] uncope,String[] forceCope)
	{
		Map<String, Method> destSetMethodMap = findPublicMethod(dest, SET_METHOD_FIX);
		// 源对象中的所有get方法集合
		Map<String, Method> origGetMethodMap = findPublicMethod(orig, GET_METHOD_FIX);
		Set<String> fields = origGetMethodMap.keySet();
		boolean flag = (uncope == null || uncope.length <= 0) ? false : true;
		for (String field : fields)
		{
			if (flag && Arrays.asList(uncope).contains(field))
			{
				continue;
			}
			Method getMethod = origGetMethodMap.get(field);
			try
			{
				Object value = getMethod.invoke(orig, new Object[] {});
				if (!copyNullFlag)
				{
					if (value == null)
					{
						if(forceCope==null||!Arrays.asList(forceCope).contains(field))
						{
							continue;
						}
					}
				}
				Method setMethod = destSetMethodMap.get(field);
				if (setMethod != null)
					setMethod.invoke(dest, value);
			}
			catch (Exception e)
			{
				logger.warn("实体属性克隆["+orig.getClass().getName()+":"+field+"]发生异常");
				continue;
			}
		}
	}
}
