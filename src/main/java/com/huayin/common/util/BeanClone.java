package com.huayin.common.util;

import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

/**
 * <pre>
 * 对象属性克隆类
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-5-6
 */
public class BeanClone
{

	/**
	 * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
	 */
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	/**
	 * <pre>
	 *  基于Dozer转换对象的类型
	 * </pre>
	 * @param source
	 * @param destinationClass
	 * @return
	 */
	public static <T> T clone(Object source, Class<T> destinationClass)
	{
		return dozer.map(source, destinationClass);
	}

	/**
	 * 基于Dozer转换Collection中对象的类型.
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> clone(Collection sourceList, Class<T> destinationClass)
	{
		List<T> destinationList = Lists.newArrayList();
		for (Object sourceObject : sourceList)
		{
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}

	/**
	 * 基于Dozer将对象A的值拷贝到对象B中.
	 */
	public static void copy(Object source, Object destinationObject)
	{
		dozer.map(source, destinationObject);
	}
}
