/**
 * <pre>
 * Title: 		AgentDegist.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.util.AgentDegist
 * Author:		linriqing
 * Create:	 	2007-6-23 下午03:51:09
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.util;

/**
 * <pre>
 * 代理系统密码加解密算法
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-23
 */
public class AgentDegist
{

	/** Key */
	private static char base_64[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '+', '-' };

	/** Reversed Key */
	private static int base_64_reverse[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1,
			63, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8,
			9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29,
			30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

	/**
	 * <pre>
	 * 代理系统密码解密算法
	 * </pre>
	 * @param inStr 原字符串
	 * @return 解密后字符串
	 */
	public static String agentPwdDecrypt(String inStr)
	{
		if (inStr == null)
			return "";

		int l = inStr.length();
		char c[] = new char[l];
		for (int i = 0; i < l; i++)
		{
			c[i] = inStr.charAt(i);
			if (((int) c[i]) == 32)
				c[i] = '+';
		}

		String dec = Decode_6b_8b(c);
		// System.out.println(dec);

		l = dec.length();
		char cc[] = new char[l];
		for (int i = 0; i < l; i++)
			cc[i] = (char) (dec.charAt(i) ^ 0xff);

		// System.out.println(new String(cc));
		return (new String(cc));
	}

	/**
	 * Convert 6 bit character to 8 bit character
	 * @return A 8-bit character representation of the given 6-bit character.
	 */
	private static String Decode_6b_8b(char inbuf[])
	{
		int inlen = inbuf.length;
		int outlen = ((inlen / 4) * 3 - 2) + 4;

		int unit, i, uselen;
		char outbuf[] = new char[outlen];

		if (outlen * 4 > inlen * 3)
		{
			unit = inlen / 4;
			uselen = 0;
		}
		else
		{
			unit = outlen / 3;
			uselen = 1;
		}

		if ((unit < 1) && ((inlen < 1) || (outlen < 1)))
		{
			return ("");
		}

		for (i = 0; i < unit; i++)
		{
			int a1, a2, a3;
			int b1, b2, b3, b4;

			b1 = base_64_reverse[inbuf[i * 4 + 0]];
			b2 = base_64_reverse[inbuf[i * 4 + 1]];
			b3 = base_64_reverse[inbuf[i * 4 + 2]];
			b4 = base_64_reverse[inbuf[i * 4 + 3]];

			if ((b1 < 0) || (b2 < 0) || (b3 < 0) || (b4 < 0))
			{
				if (b1 < 0)
				{
					b1 = 0;
				}
				if (b2 < 0)
				{
					b2 = 0;
				}
				if (b3 < 0)
				{
					b3 = 0;
				}
				if (b4 < 0)
				{
					b4 = 0;
				}
			}

			a1 = (b1 << 2) | (b2 >> 4);
			a2 = ((b2 & 0x0f) << 4) | (b3 >> 2);
			a3 = ((b3 & 0x3) << 6) | b4;
			outbuf[i * 3 + 0] = (char) a1;
			outbuf[i * 3 + 1] = (char) a2;
			outbuf[i * 3 + 2] = (char) a3;
		}

		if ((uselen == 0) && (unit * 4 < inlen) || (uselen == 1) && (unit * 3 < outlen))
		{
			int a1, a2, a3;
			int b1, b2, b3, b4;

			b1 = b2 = b3 = b4 = 0;
			a1 = a2 = a3 = 0;

			if ((inlen - unit * 4) > 2)
			{
				b3 = base_64_reverse[inbuf[unit * 4 + 2]];
			}
			if ((inlen - unit * 4) > 1)
			{
				b2 = base_64_reverse[inbuf[unit * 4 + 1]];
			}
			if ((inlen - unit * 4) > 0)
			{
				b1 = base_64_reverse[inbuf[unit * 4 + 0]];
			}

			if ((b1 < 0) || (b2 < 0) || (b3 < 0))
			{
				if (b1 < 0)
				{
					b1 = 0;
				}
				if (b2 < 0)
				{
					b2 = 0;
				}
				if (b3 < 0)
				{
					b3 = 0;
				}
			}

			a1 = (b1 << 2) | (b2 >> 4);
			a2 = ((b2 & 0x0f) << 4) | (b3 >> 2);
			a3 = ((b3 & 0x3) << 6) | b4;
			if ((outlen - unit * 3) > 0)
				outbuf[unit * 3 + 0] = (char) a1;
			if ((outlen - unit * 3) > 1)
				outbuf[unit * 3 + 1] = (char) a2;
			if ((outlen - unit * 3) > 2)
				outbuf[unit * 3 + 2] = (char) a3;
		}

		String stemp = new String(outbuf);
		return (stemp.trim());
	}

	/**
	 * Convert 8 bit character to 6 bit character.
	 * @return A 6-bit character representation of the given 8-bit character.
	 */
	private static String Encode_8b_6b(char inbuf[])
	{
		int length = inbuf.length;

		if (length < 1)
			return ("");

		char outbuf[] = new char[((int) ((length + 2) / 3)) * 4];
		int unit, i;
		String result;
		unit = length / 3; // 3

		for (i = 0; i < unit; i++)
		{
			int b1, b2, b3, b4;

			b1 = inbuf[i * 3] >> 2;
			b2 = ((inbuf[i * 3] & 3) << 4) | (inbuf[i * 3 + 1] >> 4);
			b3 = ((inbuf[i * 3 + 1] & 0x0f) << 2) | (inbuf[i * 3 + 2] >> 6);
			b4 = inbuf[i * 3 + 2] & 0x3f;
			outbuf[i * 4 + 0] = base_64[b1];
			outbuf[i * 4 + 1] = base_64[b2];
			outbuf[i * 4 + 2] = base_64[b3];
			outbuf[i * 4 + 3] = base_64[b4];
		}

		if (unit * 3 < length)
		{
			int b1, b2, b3, b4, a1, a2;

			b4 = b3 = b2 = b1 = 0;
			a2 = a1 = 0;
			if ((length - unit * 3) > 1)
				a2 = inbuf[unit * 3 + 1];
			if ((length - unit * 3) > 0)
				a1 = inbuf[unit * 3];

			b3 = (a2 & 0x0f) << 2;
			b2 = ((a1 & 3) << 4) | (a2 >> 4);
			b1 = a1 >> 2;

			outbuf[unit * 4 + 0] = base_64[b1];
			outbuf[unit * 4 + 1] = base_64[b2];
			outbuf[unit * 4 + 2] = base_64[b3];
			outbuf[unit * 4 + 3] = base_64[b4];
		}

		result = new String(outbuf);
		return (result);
	}

	/**
	 * <pre>
	 * 代理系统密码加密算法
	 * </pre>
	 * @param inStr 原字符串
	 * @return 加密后的字符串
	 */
	public static String Encrypt(String inStr)
	{
		if (inStr == null)
			return "";

		int l = inStr.length();
		char c[] = new char[l];
		for (int i = 0; i < l; i++)
		{
			c[i] = (char) (inStr.charAt(i) ^ 0xff);
		}
		String enc = Encode_8b_6b(c);

		return enc;
	}
}
