package com.huayin.common.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * <pre>
 * 数字工具类
 * </pre>
 * @author chenjian
 * @version 1.0, 2010-11-2
 */
public class NumberUtil
{
	/**
	 * <pre>
	 * 将小数转化为百分比（默认保留两位小数）
	 * </pre>
	 * @param number 要转换的小数
	 * @return 百分比形式
	 */
	public static String percentFormat(double number)
	{
		return percentFormat(number, 2);
	}

	/**
	 * <pre>
	 * 将小数转化为百分比
	 * </pre>
	 * @param number 要转换的小数
	 * @param newValue 精确的位数
	 * @return 百分比形式
	 */
	public static String percentFormat(double number, int newValue)
	{
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(newValue);
		return nf.format(number);
	}

	/**
	 * <pre>
	 * 数字格式化为字符串
	 * </pre>
	 * @param num
	 * @param format
	 * @return
	 */
	public static String formatToStr(Long num, String format)
	{
		DecimalFormat df = new DecimalFormat(format);
		return df.format(num);
	}

	public static void main(String args[])
	{
		System.out.println(formatToStr(912l, "00000"));
	}
}
