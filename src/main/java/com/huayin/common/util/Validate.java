package com.huayin.common.util;

import java.util.Collection;

public class Validate
{
	public static boolean validateArrayNullOrEmpty(Object[]... array)
	{
		if (array == null)
			return true;
		for (Object o : array)
		{
			boolean tr;
			if (o instanceof String[])
			{
				String[] s = (String[]) o;
				tr = s.length > 0;
			}
			else if (o instanceof Number[])
			{
				Number[] n = (Number[]) o;
				tr = n.length > 0;
			}
			else
			{
				tr = o != null;
			}
			if (!tr)
				return true;
		}
		return false;
	}

	public static boolean validateObjectsNullOrEmpty(Object... obj)
	{
		if (obj == null)
			return true;
		for (Object o : obj)
		{
			boolean isEmpty;
			if (o instanceof String)
			{
				isEmpty = o.toString().trim().equals("");
			}
			else if (o instanceof Collection<?>)
			{
				Collection<?> list = (Collection<?>) o;
				isEmpty = list.size() == 0;
			}
			else
			{
				isEmpty = (o == null);
			}
			if (isEmpty)
				return true;
		}
		return false;
	}
}
