/**
 * <pre>
 * Title: 		TixUtil.java
 * Project: 	Framework
 * Type:		com.huayin.anteagent.payment.util.TixUtil
 * Author:		ritch
 * Create:	 	2006-8-26 22:28:27
 * Copyright: 	Copyright (c) 2006
 * Company:
 * <pre>
 */
package com.huayin.common.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 三十六进制帮助类，类似十六进制，字母扩展到Z，故为三十六进制。
 * </pre>
 * @author ritch
 * @version 1.0, 2006-8-26
 */
public class TixUtil
{
	private final static Map<Character, Integer> characterMap = new HashMap<Character, Integer>();

	private final static Map<Integer, Character> valueMap = new HashMap<Integer, Character>();

	/**
	 * 长的三十六进制日期表示法各段的长度
	 */
	private final static int[] longTixDatePartLengths = new int[] { 3, 1, 1, 1, 2, 2, 2 };

	/**
	 * 长的十进制日期表示法各段的长度
	 */
	private final static int[] longDecDatePartLengths = new int[] { 4, 2, 2, 2, 2, 2, 3 };

	static
	{
		for (int i = 0; i < 10; i++)
		{
			TixUtil.characterMap.put(new Character((char) (i + 48)), new Integer(i));
			TixUtil.valueMap.put(new Integer(i), new Character((char) (i + 48)));
		}

		for (int i = 'a'; i <= 'z'; i++)
		{
			TixUtil.characterMap.put(new Character((char) i), new Integer(i - 87));
		}

		for (int i = 'A'; i <= 'Z'; i++)
		{
			TixUtil.characterMap.put(new Character((char) i), new Integer(i - 55));
			TixUtil.valueMap.put(new Integer(i - 55), new Character((char) (i)));
		}
	}

	/**
	 * 三十六进制转为十进制
	 * @param tixStr 三十六进制数
	 * @return 十进制数
	 * @throws IllegalArgumentException 不合法的三十六进制字符
	 */
	public static long tix2Dec(String tixStr) throws IllegalArgumentException
	{
		long rtnValue = 0;
		int powCount = tixStr.length() - 1;
		for (int i = 0; i < tixStr.length(); i++)
		{
			int bitValue = 0;
			try
			{
				bitValue = ((Integer) TixUtil.characterMap.get(new Character(tixStr.charAt(i)))).intValue();
			}
			catch (NullPointerException e)
			{
				throw new IllegalArgumentException("不合法的三十六进制字符, index[" + i + "], 字符[" + tixStr.charAt(i) + "]");
			}
			rtnValue += bitValue * Math.pow(36, powCount);
			powCount--;
		}
		return rtnValue;
	}

	/**
	 * 十进制转为三十六进制
	 * @param decValue 十进制数
	 * @return 三十六进制数
	 */
	public static String dec2Tix(long decValue)
	{
		StringBuffer sb = new StringBuffer();
		long divValue = decValue;
		do
		{
			int modValue = (int) (divValue % 36);
			sb.append(TixUtil.valueMap.get(new Integer(modValue)));
			divValue = divValue / 36;
		}
		while (divValue > 0);
		return sb.reverse().toString();
	}

	/**
	 * 十进制转为三十六进制
	 * @param decValue 十进制数
	 * @param fixLength 三十六进制表示的长度, 不足左补齐零
	 * @return 三十六进制数
	 */
	public static String dec2Tix(long decValue, int fixLength)
	{
		StringBuffer sb = new StringBuffer();
		long divValue = decValue;
		do
		{
			int modValue = (int) (divValue % 36);
			sb.append(TixUtil.valueMap.get(new Integer(modValue)));
			divValue = divValue / 36;
		}
		while (divValue > 0);
		int padLength = fixLength - sb.length();
		for (int i = 0; i < padLength; i++)
		{
			sb.append('0');

		}
		return sb.reverse().toString();
	}

	/**
	 * 短十进制日期(yyMMddHH)转为三十六进制表示
	 * @param dateValue 十进制日期(yyMMddHH)
	 * @return 三十六进制表示
	 */
	public static String shortdateDec2Tix(Date dateValue)
	{
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yy,MM,dd,HH");
		String dateStr = sdf.format(dateValue);
		String[] timeParts = dateStr.split(",");
		for (int i = 0; i < timeParts.length; i++)
		{
			sb.append(TixUtil.dec2Tix(Long.parseLong(timeParts[i])));
		}
		return sb.toString();
	}

	/**
	 * 三十六进制日期表示转为十进制(yyMMddHH)表示
	 * @param tixValue 三十六进制日期
	 * @return 十进制表示日期(yyMMddHH)
	 * @throws ParseException
	 */
	public static Date shortdateTix2Dec(String tixValue) throws ParseException
	{
		DecimalFormat df = new DecimalFormat("00");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tixValue.length(); i++)
		{
			sb.append(df.format(TixUtil.tix2Dec(String.valueOf(tixValue.charAt(i)))));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHH");
		Date date = sdf.parse(sb.toString());
		return date;
	}

	/**
	 * 十进制日期(yyyyMMddHHmmssSSS)转为三十六进制表示
	 * @param dateValue 十进制日期(yyyyMMddHHmmssSSS)
	 * @return 三十六进制表示(yyyMdHmmssSS)
	 */
	public static String longdateDec2Tix(Date dateValue)
	{
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss,SSS");
		String dateStr = sdf.format(dateValue);
		String[] timeParts = dateStr.split(",");
		for (int i = 0; i < timeParts.length; i++)
		{
			sb.append(TixUtil.dec2Tix(Long.parseLong(timeParts[i]), longTixDatePartLengths[i]));
		}
		return sb.toString();
	}

	/**
	 * 三十六进制日期表示转为十进制(yyyyMMddHHmmssSSS)表示
	 * @param tixValue 三十六进制日期(yyyMdHmmssSS)
	 * @return 十进制表示日期(yyyyMMddHHmmssSSS)
	 * @throws ParseException
	 */
	public static Date longdateTix2Dec(String tixValue) throws ParseException
	{
		StringBuffer sb = new StringBuffer();
		int position = 0;
		for (int i = 0; i < TixUtil.longTixDatePartLengths.length; i++)
		{
			String part = tixValue.substring(position, position + TixUtil.longTixDatePartLengths[i]);
			StringBuffer pattern = new StringBuffer();
			for (int j = 0; j < TixUtil.longDecDatePartLengths[i]; j++)
			{
				pattern.append("0");
			}
			DecimalFormat df = new DecimalFormat(pattern.toString());
			sb.append(df.format(TixUtil.tix2Dec(part)));
			position += TixUtil.longTixDatePartLengths[i];
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = sdf.parse(sb.toString());
		return date;
	}
}
