package com.huayin.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtil
{
	public static String getHostName()
	{
		try
		{
			return InetAddress.getLocalHost().getCanonicalHostName();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
