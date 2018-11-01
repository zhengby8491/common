package com.huayin.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <pre>
 * 金额运算工具类
 * </pre>
 * @author chenjian
 * @version 1.0, 2010-10-9
 */
public class MoneyUtil
{
	private MoneyUtil()
	{
	}

	/**
	 * <pre>
	 * 元转换为分
	 * </pre>
	 * @param money
	 * @return int
	 */
	public static int convertYuanToFen(Double money)
	{
		if (money == null)
		{
			return 0;
		}
		double amount = mul(money, 100);
		return (int) amount;
	}

	/**
	 * <pre>
	 * 元转换为分
	 * </pre>
	 * @param money
	 * @return Long
	 */
	public static long convertLongYuanToFen(Double money)
	{
		if (money == null)
		{
			return 0;
		}
		double amount = mul(money, 100);
		return (long) amount;
	}

	/**
	 * <pre>
	 * 元转换为分
	 * </pre>
	 * @param money
	 * @return
	 */
	public static long convertLongYuanToFen(Long money)
	{
		if (money == null)
		{
			return 0;
		}
		double amount = mul(money, 100);
		return (long) amount;
	}

	/**
	 * <pre>
	 * 元转换为分
	 * </pre>
	 * @param money
	 * @return
	 */
	public static Integer convertYuanToFen(Integer money)
	{
		if (money == null)
		{
			return null;
		}
		double amount = mul(money, 100);
		return (int) amount;
	}

	/**
	 * <pre>
	 * 分转换为元
	 * </pre>
	 * @param money
	 * @return
	 */
	public static String convertFenToYuan(Integer money)
	{
		if (money == null)
			return "0.00";
		double amount = div(money, 100, 2);
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(amount);
	}

	/**
	 * <pre>
	 * 分转换为元
	 * </pre>
	 * @param money
	 * @return
	 */
	public static String convertFenToYuan(Long money)
	{
		if (money == null)
			return "0.00";
		double amount = div(money, 100, 2);
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(amount);
	}

	/**
	 * <pre>
	 * 分转换为元
	 * </pre>
	 * @param money
	 * @return
	 */
	public static Integer convertIntFenToYuan(Integer money)
	{
		return money / 100;
	}

	/**
	 * <pre>
	 * 分转换为元
	 * </pre>
	 * @param money
	 * @return
	 */
	public static Long convertLongFenToYuan(Long money)
	{
		return money / 100;
	}

	public static Double convertFenToYuanNum(Integer money)
	{
		double amount = div(money, 100, 2);
		return amount;
	}

	/**
	 * <pre>
	 * 金额相加
	 * </pre>
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static double add(double num1, double num2)
	{
		BigDecimal n1 = new BigDecimal(Double.toString(num1));
		BigDecimal n2 = new BigDecimal(Double.toString(num2));
		return n1.add(n2).doubleValue();
	}

	/**
	 * <pre>
	 * 金额相减
	 * </pre>
	 * @param num1 减数
	 * @param num2 被减数
	 * @return 差值
	 */
	public static double sub(double num1, double num2)
	{
		BigDecimal n1 = new BigDecimal(Double.toString(num1));
		BigDecimal n2 = new BigDecimal(Double.toString(num2));
		return n1.subtract(n2).doubleValue();
	}

	/**
	 * <pre>
	 * 金额相乘
	 * </pre>
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static double mul(double num1, double num2)
	{
		BigDecimal n1 = new BigDecimal(Double.toString(num1));
		BigDecimal n2 = new BigDecimal(Double.toString(num2));
		return n1.multiply(n2).doubleValue();
	}

	/**
	 * <pre>
	 * 金额相除
	 * </pre>
	 * @param num1 除数
	 * @param num2 被除数
	 * @param scale 小数精确位数
	 * @return
	 */
	public static double div(double num1, double num2, int scale)
	{
		BigDecimal n1 = new BigDecimal(Double.toString(num1));
		BigDecimal n2 = new BigDecimal(Double.toString(num2));
		return n1.divide(n2, scale, BigDecimal.ROUND_FLOOR).doubleValue();
	}

	public static void main(String args[])
	{
		System.out.println(convertFenToYuan(2000000l));
		System.out.println(convertLongYuanToFen(0.11d));
		System.out.println(convertYuanToFen(0.00d));
		System.out.println(convertFenToYuanNum(1));
		System.out.println((convertYuanToFen(0.10d) * 1) / 1000);
	}
}
