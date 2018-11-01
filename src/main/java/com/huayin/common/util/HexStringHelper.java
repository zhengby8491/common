/**
 * <pre>
 * Title: 		Hex2BytesHelper.java
 * Project: 	HappyLottery
 * Author:		linriqing
 * Create:	 	2013-3-28 下午12:44:00
 * Copyright: 	Copyright (c) 2013
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.util;

/**
 * <pre>
 * 16进制字符串和字节转换工具类
 * </pre>
 * @author linriqing
 * @version 1.0, 2013-3-28
 */
public class HexStringHelper
{
	private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	/**
	 * 将字节数组转换为16进制字符串的形式.
	 */
	public static final String bytesToHexStr(byte[] bcd)
	{
		StringBuffer s = new StringBuffer(bcd.length * 2);

		for (int i = 0; i < bcd.length; i++)
		{
			s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
			s.append(bcdLookup[bcd[i] & 0x0f]);
		}

		return s.toString();
	}

	/**
	 * 将16进制字符串还原为字节数组.
	 */
	public static final byte[] hexStrToBytes(String s)
	{
		byte[] bytes;

		bytes = new byte[s.length() / 2];

		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
		}

		return bytes;
	}
}
