package com.huayin.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类型转换
 */
public class ConverterUtils
{

	/*
	 *	list 转map ,必须制定fieldName,切 fieldName不能为空
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> Map<K, V> list2Map(List<V> list, String fieldName)
	{
		Map<K, V> map = new HashMap<K, V>();
		if (list != null)
		{
			for (V object : list)
			{
				map.put((K)(Reflections.getFieldValue(object, fieldName)), object);
			}

		}
		return map;
	}
}
