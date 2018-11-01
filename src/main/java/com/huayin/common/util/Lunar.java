/**
 * <pre>
 * Author:		linriqing
 * Create:	 	2014-8-6 下午01:07:35
 * Copyright: 	Copyright (c) 2014
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * <pre>
 * 农历工具类
 * </pre>
 * @author linriqing
 * @version 1.0, 2014-8-6
 */
public class Lunar
{
	final private static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554,
			0x056a0, 0x09ad0, 0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0,
			0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
			0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550,
			0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0,
			0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263,
			0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0,
			0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5,
			0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960, 0x0d954, 0x0d4a0,
			0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
			0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0,
			0x0d260, 0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520,
			0x0dd45, 0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

	public static final class Solar2Lunar
	{
		public final static String[] nStr1 = new String[] { "", "正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬月",
				"腊月" };

		private final static String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };

		private final static String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };

		private final static String[] Animals = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗",
				"猪" };

		/**
		 * 传回农历 y年的总天数
		 * @param y
		 * @return
		 */
		final private static int lYearDays(int y)
		{
			int i, sum = 348;
			for (i = 0x8000; i > 0x8; i >>= 1)
			{
				if ((lunarInfo[y - 1900] & i) != 0)
					sum += 1;
			}
			return (sum + leapDays(y));
		}

		/**
		 * 传回农历 y年闰月的天数
		 * @param y
		 * @return
		 */
		final private static int leapDays(int y)
		{
			if (leapMonth(y) != 0)
			{
				if ((lunarInfo[y - 1900] & 0x10000) != 0)
					return 30;
				else
					return 29;
			}
			else
				return 0;
		}

		/**
		 * 传回农历 y年闰哪个月 1-12 , 没闰传回 0
		 * @param y
		 * @return
		 */
		final private static int leapMonth(int y)
		{
			return (int) (lunarInfo[y - 1900] & 0xf);
		}

		/**
		 * 传回农历 y年m月的总天数
		 * @param y
		 * @param m
		 * @return
		 */
		final private static int monthDays(int y, int m)
		{
			if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
				return 29;
			else
				return 30;
		}

		/**
		 * 传回农历 y年的生肖
		 * @param y
		 * @return
		 */
		final private static String AnimalsYear(int y)
		{
			return Animals[(y - 4) % 12];
		}

		/**
		 * 传入 月日的offset 传回干支,0=甲子
		 * @param num
		 * @return
		 */
		final private static String cyclicalm(int num)
		{
			return (Gan[num % 10] + Zhi[num % 12]);
		}

		/**
		 * 传入 offset 传回干支, 0=甲子
		 * @param y
		 * @return
		 */
		final private static String cyclical(int y)
		{
			int num = y - 1900 + 36;
			return (cyclicalm(num));
		}

		/**
		 * 传出y年m月d日对应的农历.year0 .month1 .day2 .yearCyl3 .monCyl4 .dayCyl5 .isLeap6
		 * @param y
		 * @param m
		 * @param d
		 * @return
		 */
		final public static long[] calElement(int y, int m, int d)
		{
			long[] nongDate = new long[7];
			int i = 0, temp = 0, leap = 0;
			Date baseDate = new GregorianCalendar(0 + 1900, 0, 31).getTime();
			Date objDate = new GregorianCalendar(y, m - 1, d).getTime();
			long offset = (objDate.getTime() - baseDate.getTime()) / 86400000L;
			nongDate[5] = offset + 40;
			nongDate[4] = 14;
			for (i = 1900; i < 2050 && offset > 0; i++)
			{
				temp = lYearDays(i);
				offset -= temp;
				nongDate[4] += 12;
			}
			if (offset < 0)
			{
				offset += temp;
				i--;
				nongDate[4] -= 12;
			}
			nongDate[0] = i;
			nongDate[3] = i - 1864;
			leap = leapMonth(i); // 闰哪个月
			nongDate[6] = 0;
			for (i = 1; i < 13 && offset > 0; i++)
			{
				// 闰月
				if (leap > 0 && i == (leap + 1) && nongDate[6] == 0)
				{
					--i;
					nongDate[6] = 1;
					temp = leapDays((int) nongDate[0]);
				}
				else
				{
					temp = monthDays((int) nongDate[0], i);
				}
				// 解除闰月
				if (nongDate[6] == 1 && i == (leap + 1))
					nongDate[6] = 0;
				offset -= temp;
				if (nongDate[6] == 0)
					nongDate[4]++;
			}
			if (offset == 0 && leap > 0 && i == leap + 1)
			{
				if (nongDate[6] == 1)
				{
					nongDate[6] = 0;
				}
				else
				{
					nongDate[6] = 1;
					--i;
					--nongDate[4];
				}
			}
			if (offset < 0)
			{
				offset += temp;
				--i;
				--nongDate[4];
			}
			nongDate[1] = i;
			nongDate[2] = offset + 1;
			return nongDate;
		}

		private final static String getChinaDate(int day)
		{
			String a = "";
			if (day == 10)
				return "初十";
			if (day == 20)
				return "二十";
			if (day == 30)
				return "三十";
			int two = (int) ((day) / 10);
			if (two == 0)
				a = "初";
			if (two == 1)
				a = "十";
			if (two == 2)
				a = "廿";
			if (two == 3)
				a = "三";
			int one = (int) (day % 10);
			switch (one)
			{
				case 1:
					a += "一";
					break;
				case 2:
					a += "二";
					break;
				case 3:
					a += "三";
					break;
				case 4:
					a += "四";
					break;
				case 5:
					a += "五";
					break;
				case 6:
					a += "六";
					break;
				case 7:
					a += "七";
					break;
				case 8:
					a += "八";
					break;
				case 9:
					a += "九";
					break;
			}
			return a;
		}

		public static String today()
		{
			Calendar today = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
			int year = today.get(Calendar.YEAR);
			int month = today.get(Calendar.MONTH) + 1;
			int date = today.get(Calendar.DATE);
			long[] l = calElement(year, month, date);
			StringBuffer sToday = new StringBuffer();
			try
			{
				sToday.append(sdf.format(today.getTime()));
				sToday.append(" 农历");
				sToday.append(cyclical(year));
				sToday.append('(');
				sToday.append(AnimalsYear(year));
				sToday.append(")年");
				sToday.append(nStr1[(int) l[1]]);
				sToday.append("月");
				sToday.append(getChinaDate((int) (l[2])));
				return sToday.toString();
			}
			finally
			{
				sToday = null;
			}
		}

		public static String oneDay(int year, int month, int day)
		{
			Calendar today = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
			today.set(Calendar.YEAR, year);
			today.set(Calendar.MONTH, month - 1);
			today.set(Calendar.DATE, day);
			long[] l = calElement(year, month, day);
			StringBuffer sToday = new StringBuffer();
			try
			{
				sToday.append(sdf.format(today.getTime()));
				sToday.append(" 农历");
				sToday.append(cyclical(year));
				sToday.append('(');
				sToday.append(AnimalsYear(year));
				sToday.append(")年");
				sToday.append(nStr1[(int) l[1]]);
				sToday.append("月");
				sToday.append(getChinaDate((int) (l[2])));
				return sToday.toString();
			}
			finally
			{
				sToday = null;
			}
		}

		private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 EEEEE");
	}

	@Deprecated
	public static final class Lunar2SolarOld
	{
		// Array lIntLunarDay is stored in the monthly day information in every year from 1901 to 2100 of the lunar
		// calendar,
		// The lunar calendar can only be 29 or 30 days every month, express with 12(or 13) pieces of binary bit in one
		// year,
		// it is 30 days for 1 form in the corresponding location , otherwise it is 29 days
		private static final int[] iLunarMonthDaysTable = { 0x4ae0, 0xa570, 0x5268, 0xd260, 0xd950, 0x6aa8, 0x56a0,
				0x9ad0, 0x4ae8, 0x4ae0, // 1910
				0xa4d8, 0xa4d0, 0xd250, 0xd548, 0xb550, 0x56a0, 0x96d0, 0x95b0, 0x49b8, 0x49b0, // 1920
				0xa4b0, 0xb258, 0x6a50, 0x6d40, 0xada8, 0x2b60, 0x9570, 0x4978, 0x4970, 0x64b0, // 1930
				0xd4a0, 0xea50, 0x6d48, 0x5ad0, 0x2b60, 0x9370, 0x92e0, 0xc968, 0xc950, 0xd4a0, // 1940
				0xda50, 0xb550, 0x56a0, 0xaad8, 0x25d0, 0x92d0, 0xc958, 0xa950, 0xb4a8, 0x6ca0, // 1950
				0xb550, 0x55a8, 0x4da0, 0xa5b0, 0x52b8, 0x52b0, 0xa950, 0xe950, 0x6aa0, 0xad50, // 1960
				0xab50, 0x4b60, 0xa570, 0xa570, 0x5260, 0xe930, 0xd950, 0x5aa8, 0x56a0, 0x96d0, // 1970
				0x4ae8, 0x4ad0, 0xa4d0, 0xd268, 0xd250, 0xd528, 0xb540, 0xb6a0, 0x96d0, 0x95b0, // 1980
				0x49b0, 0xa4b8, 0xa4b0, 0xb258, 0x6a50, 0x6d40, 0xada0, 0xab60, 0x9370, 0x4978, // 1990
				0x4970, 0x64b0, 0x6a50, 0xea50, 0x6b28, 0x5ac0, 0xab60, 0x9368, 0x92e0, 0xc960, // 2000
				0xd4a8, 0xd4a0, 0xda50, 0x5aa8, 0x56a0, 0xaad8, 0x25d0, 0x92d0, 0xc958, 0xa950, // 2010
				0xb4a0, 0xb550, 0xb550, 0x55a8, 0x4ba0, 0xa5b0, 0x52b8, 0x52b0, 0xa930, 0x74a8, // 2020
				0x6aa0, 0xad50, 0x4da8, 0x4b60, 0x9570, 0xa4e0, 0xd260, 0xe930, 0xd530, 0x5aa0, // 2030
				0x6b50, 0x96d0, 0x4ae8, 0x4ad0, 0xa4d0, 0xd258, 0xd250, 0xd520, 0xdaa0, 0xb5a0, // 2040
				0x56d0, 0x4ad8, 0x49b0, 0xa4b8, 0xa4b0, 0xaa50, 0xb528, 0x6d20, 0xada0, 0x55b0 // 2050
		};

		// Array iLunarLeapMonthTable preserves the lunar calendar leap month from 1901 to 2050,
		// if it is 0 express not to have , every byte was stored for two years
		private static final char[] iLunarLeapMonthTable = { 0x00, 0x50, 0x04, 0x00, 0x20, // 1910
				0x60, 0x05, 0x00, 0x20, 0x70, // 1920
				0x05, 0x00, 0x40, 0x02, 0x06, // 1930
				0x00, 0x50, 0x03, 0x07, 0x00, // 1940
				0x60, 0x04, 0x00, 0x20, 0x70, // 1950
				0x05, 0x00, 0x30, 0x80, 0x06, // 1960
				0x00, 0x40, 0x03, 0x07, 0x00, // 1970
				0x50, 0x04, 0x08, 0x00, 0x60, // 1980
				0x04, 0x0a, 0x00, 0x60, 0x05, // 1990
				0x00, 0x30, 0x80, 0x05, 0x00, // 2000
				0x40, 0x02, 0x07, 0x00, 0x50, // 2010
				0x04, 0x09, 0x00, 0x60, 0x04, // 2020
				0x00, 0x20, 0x60, 0x05, 0x00, // 2030
				0x30, 0xb0, 0x06, 0x00, 0x50, // 2040
				0x02, 0x07, 0x00, 0x50, 0x03 // 2050
		};

		// Array iSolarLunarTable stored the offset days
		// in New Year of solar calendar and lunar calendar from 1901 to 2050;
		private static final char[] iSolarLunarOffsetTable = { 49, 38, 28, 46, 34, 24, 43, 32, 21, 40, // 1910
				29, 48, 36, 25, 44, 34, 22, 41, 31, 50, // 1920
				38, 27, 46, 35, 23, 43, 32, 22, 40, 29, // 1930
				47, 36, 25, 44, 34, 23, 41, 30, 49, 38, // 1940
				26, 45, 35, 24, 43, 32, 21, 40, 28, 47, // 1950
				36, 26, 44, 33, 23, 42, 30, 48, 38, 27, // 1960
				45, 35, 24, 43, 32, 20, 39, 29, 47, 36, // 1970
				26, 45, 33, 22, 41, 30, 48, 37, 27, 46, // 1980
				35, 24, 43, 32, 50, 39, 28, 47, 36, 26, // 1990
				45, 34, 22, 40, 30, 49, 37, 27, 46, 35, // 2000
				23, 42, 31, 21, 39, 28, 48, 37, 25, 44, // 2010
				33, 23, 41, 31, 50, 39, 28, 47, 35, 24, // 2020
				42, 30, 21, 40, 28, 47, 36, 25, 43, 33, // 2030
				22, 41, 30, 49, 37, 26, 44, 33, 23, 42, // 2040
				31, 21, 40, 29, 47, 36, 25, 44, 32, 22, // 2050
		};

		static boolean bIsSolarLeapYear(int iYear)
		{
			return ((iYear % 4 == 0) && (iYear % 100 != 0) || iYear % 400 == 0);
		}

		// The days in the month of solar calendar
		static int iGetSYearMonthDays(int iYear, int iMonth)
		{
			if ((iMonth == 1) || (iMonth == 3) || (iMonth == 5) || (iMonth == 7) || (iMonth == 8) || (iMonth == 10)
					|| (iMonth == 12))
				return 31;
			else if ((iMonth == 4) || (iMonth == 6) || (iMonth == 9) || (iMonth == 11))
				return 30;
			else if (iMonth == 2)
			{
				if (bIsSolarLeapYear(iYear))
					return 29;
				else
					return 28;
			}
			else
				return 0;
		}

		// The offset days from New Year and the day when point out in solar calendar
		static int iGetSNewYearOffsetDays(int iYear, int iMonth, int iDay)
		{
			int iOffsetDays = 0;
			for (int i = 1; i < iMonth; i++)
			{
				iOffsetDays += iGetSYearMonthDays(iYear, i);
			}
			iOffsetDays += iDay - 1;
			return iOffsetDays;
		}

		static int iGetLLeapMonth(int iYear)
		{
			char iMonth = iLunarLeapMonthTable[(iYear - 1901) / 2];
			if (iYear % 2 == 0)
				return (iMonth & 0x0f);
			else
				return (iMonth & 0xf0) >> 4;
		}

		static int iGetLMonthDays(int iYear, int iMonth)
		{
			int iLeapMonth = iGetLLeapMonth(iYear);
			if ((iMonth > 12) && (iMonth - 12 != iLeapMonth) || (iMonth < 0))
			{
				System.out.println("Wrong month, ^_^ , i think you are want a -1, go to death!");
				return -1;
			}
			if (iMonth - 12 == iLeapMonth)
			{
				if ((iLunarMonthDaysTable[iYear - 1901] & (0x8000 >> iLeapMonth)) == 0)
					return 29;
				else
					return 30;
			}
			if ((iLeapMonth > 0) && (iMonth > iLeapMonth))
				iMonth++;
			if ((iLunarMonthDaysTable[iYear - 1901] & (0x8000 >> (iMonth - 1))) == 0)
				return 29;
			else
				return 30;
		}

		// Days in this year of lunar calendar
		static int iGetLYearDays(int iYear)
		{
			int iYearDays = 0;
			int iLeapMonth = iGetLLeapMonth(iYear);
			for (int i = 1; i < 13; i++)
				iYearDays += iGetLMonthDays(iYear, i);
			if (iLeapMonth > 0)
				iYearDays += iGetLMonthDays(iYear, iLeapMonth + 12);
			return iYearDays;
		}

		static int iGetLNewYearOffsetDays(int iYear, int iMonth, int iDay)
		{
			int iOffsetDays = 0;
			int iLeapMonth = iGetLLeapMonth(iYear);
			if ((iLeapMonth > 0) && (iLeapMonth == iMonth - 12))
			{
				iMonth = iLeapMonth;
				iOffsetDays += iGetLMonthDays(iYear, iMonth);
			}
			for (int i = 1; i < iMonth; i++)
			{
				iOffsetDays += iGetLMonthDays(iYear, i);
				if (i == iLeapMonth)
					iOffsetDays += iGetLMonthDays(iYear, iLeapMonth + 12);
			}
			iOffsetDays += iDay - 1;
			return iOffsetDays;
		}

		// The solar calendar is turned into the lunar calendar
		static String sCalendarSolarToLundar(int iYear, int iMonth, int iDay)
		{
			int iLDay, iLMonth, iLYear;
			int iOffsetDays = iGetSNewYearOffsetDays(iYear, iMonth, iDay);
			int iLeapMonth = iGetLLeapMonth(iYear);
			if (iOffsetDays < iSolarLunarOffsetTable[iYear - 1901])
			{
				iLYear = iYear - 1;
				iOffsetDays = iSolarLunarOffsetTable[iYear - 1901] - iOffsetDays;
				iLDay = iOffsetDays;
				for (iLMonth = 12; iOffsetDays > iGetLMonthDays(iLYear, iLMonth); iLMonth--)
				{
					iLDay = iOffsetDays;
					iOffsetDays -= iGetLMonthDays(iLYear, iLMonth);
				}
				if (0 == iLDay)
					iLDay = 1;
				else
					iLDay = iGetLMonthDays(iLYear, iLMonth) - iOffsetDays + 1;
			}
			else
			{
				iLYear = iYear;
				iOffsetDays -= iSolarLunarOffsetTable[iYear - 1901];
				iLDay = iOffsetDays + 1;
				for (iLMonth = 1; iOffsetDays >= 0; iLMonth++)
				{
					iLDay = iOffsetDays + 1;
					iOffsetDays -= iGetLMonthDays(iLYear, iLMonth);
					if ((iLeapMonth == iLMonth) && (iOffsetDays > 0))
					{
						iLDay = iOffsetDays;
						iOffsetDays -= iGetLMonthDays(iLYear, iLMonth + 12);
						if (iOffsetDays <= 0)
						{
							iLMonth += 12 + 1;
							break;
						}
					}
				}
				iLMonth--;
			}
			return "" + iLYear + (iLMonth > 9 ? "" + iLMonth : "0" + iLMonth) + (iLDay > 9 ? "" + iLDay : "0" + iLDay);
		}

		// The lunar calendar is turned into the Solar calendar
		static String sCalendarLundarToSolar(int iYear, int iMonth, int iDay)
		{
			int iSYear, iSMonth, iSDay;
			int iOffsetDays = iGetLNewYearOffsetDays(iYear, iMonth, iDay) + iSolarLunarOffsetTable[iYear - 1901];
			int iYearDays = bIsSolarLeapYear(iYear) ? 366 : 365;
			if (iOffsetDays >= iYearDays)
			{
				iSYear = iYear + 1;
				iOffsetDays -= iYearDays;
			}
			else
			{
				iSYear = iYear;
			}
			iSDay = iOffsetDays + 1;
			for (iSMonth = 1; iOffsetDays >= 0; iSMonth++)
			{
				iSDay = iOffsetDays + 1;
				iOffsetDays -= iGetSYearMonthDays(iSYear, iSMonth);
			}
			iSMonth--;
			return "" + iSYear + (iSMonth > 9 ? iSMonth + "" : "0" + iSMonth) + (iSDay > 9 ? iSDay + "" : "0" + iSDay);
		}

		// 自定义星期类
		static class Week
		{
			int iWeek;

			private String sWeek[] = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

			public Week()
			{
				iWeek = 0;
			}

			public Week(int w)
			{
				if ((w > 6) || (w < 0))
				{
					System.out.println("Week out of range, I think you want Sunday");
					this.iWeek = 0;
				}
				else
					this.iWeek = w;
			}

			public String toString()
			{
				return sWeek[iWeek];
			}
		}

		// 自定义日期类
		static class MyDate
		{
			public int iYear;

			public int iMonth;

			public int iDay;

			private static int checkYear(int iYear)
			{
				if ((iYear > 1901) && (iYear < 2050))
					return iYear;
				else
				{
					System.out.println("The Year out of range, I think you want 1981");
					return 1981;
				}
			}

			public MyDate(int iYear, int iMonth, int iDay)
			{
				this.iYear = checkYear(iYear);
				this.iMonth = iMonth;
				this.iDay = iDay;
			}

			public MyDate(int iYear, int iMonth)
			{
				this.iYear = checkYear(iYear);
				this.iMonth = iMonth;
				this.iDay = 1;
			}

			public MyDate(int iYear)
			{
				this.iYear = checkYear(iYear);
				this.iMonth = 1;
				this.iDay = 1;
			}

			public MyDate()
			{
				this.iYear = 1981;
				this.iMonth = 1;
				this.iDay = 1;
			}

			public String toString()
			{
				return "" + this.iYear + (this.iMonth > 9 ? "" + this.iMonth : "0" + this.iMonth)
						+ (this.iDay > 9 ? "" + this.iDay : "0" + this.iDay);
			}

			public boolean equals(MyDate md)
			{
				return ((md.iDay == this.iDay) && (md.iMonth == this.iMonth) && (md.iYear == this.iYear));
			}
		}

		// 阳历日期类,继承自定义日期
		static class SolarDate extends MyDate
		{
			private static int checkMonth(int iMonth)
			{
				if (iMonth > 12)
				{
					System.out.println("Month out of range, I think you want 12 ");
					return 12;
				}
				else if (iMonth < 1)
				{
					System.out.println("Month out of range, I think you want 1 ");
					return 1;
				}
				else
					return iMonth;
			}

			private static int checkDay(int iYear, int iMonth, int iDay)
			{
				int iMonthDays = Lunar2SolarOld.iGetSYearMonthDays(iYear, iMonth);
				if (iDay > iMonthDays)
				{
					System.out.println("Day out of range, I think you want " + iMonthDays + " ");
					return iMonthDays;
				}
				else if (iDay < 1)
				{
					System.out.println("Day out of range, I think you want 1 ");
					return 1;
				}
				else
					return iDay;
			}

			public SolarDate(int iYear, int iMonth, int iDay)
			{
				super(iYear);
				this.iMonth = checkMonth(iMonth);
				this.iDay = checkDay(this.iYear, this.iMonth, iDay);
			}

			public SolarDate(int iYear, int iMonth)
			{
				super(iYear);
				this.iMonth = checkMonth(iMonth);
			}

			public SolarDate(int iYear)
			{
				super(iYear);
			}

			public SolarDate()
			{
				super();
			}

			public String toString()
			{
				return "" + this.iYear + (this.iMonth > 9 ? "-" + this.iMonth : "-0" + this.iMonth)
						+ (this.iDay > 9 ? "-" + this.iDay : "-0" + this.iDay);
			}

			public Week toWeek()
			{
				int iOffsetDays = 0;
				for (int i = 1901; i < iYear; i++)
				{
					if (Lunar2SolarOld.bIsSolarLeapYear(i))
						iOffsetDays += 366;
					else
						iOffsetDays += 365;
				}
				iOffsetDays += Lunar2SolarOld.iGetSNewYearOffsetDays(iYear, iMonth, iDay);
				return new Week((iOffsetDays + 2) % 7);
			}

			public LunarDate toLunarDate()
			{
				int iYear, iMonth, iDay, iDate;
				LunarDate ld;
				iDate = Integer.parseInt(Lunar2SolarOld.sCalendarSolarToLundar(this.iYear, this.iMonth, this.iDay));
				iYear = iDate / 10000;
				iMonth = iDate % 10000 / 100;
				iDay = iDate % 100;
				ld = new LunarDate(iYear, iMonth, iDay);
				return ld;
			}
		}

		// 阴历日期类,继承自定义日期类
		static class LunarDate extends MyDate
		{
			private String sChineseNum[] = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" };

			private static int checkMonth(int iYear, int iMonth)
			{
				if ((iMonth > 12) && (iMonth == Lunar2SolarOld.iGetLLeapMonth(iYear) + 12))
				{
					return iMonth;
				}
				else if (iMonth > 12)
				{
					System.out.println("Month out of range, I think you want 12 ");
					return 12;
				}
				else if (iMonth < 1)
				{
					System.out.println("Month out of range, I think you want 1 ");
					return 1;
				}
				else
					return iMonth;
			}

			private static int checkDay(int iYear, int iMonth, int iDay)
			{
				int iMonthDays = Lunar2SolarOld.iGetLMonthDays(iYear, iMonth);
				if (iDay > iMonthDays)
				{
					System.out.println("Day out of range, I think you want " + iMonthDays + " ");
					return iMonthDays;
				}
				else if (iDay < 1)
				{
					System.out.println("Day out of range, I think you want 1 ");
					return 1;
				}
				else
					return iDay;
			}

			public LunarDate(int iYear, int iMonth, int iDay)
			{
				super(iYear);
				this.iMonth = checkMonth(this.iYear, iMonth);
				this.iDay = checkDay(this.iYear, this.iMonth, iDay);
			}

			public LunarDate(int iYear, int iMonth)
			{
				super(iYear);
				this.iMonth = checkMonth(this.iYear, iMonth);
			}

			public LunarDate(int iYear)
			{
				super(iYear);
			}

			public LunarDate()
			{
				super();
			}

			public String toString()
			{
				String sCalendar = "农历";
				sCalendar += sChineseNum[iYear / 1000] + sChineseNum[iYear % 1000 / 100]
						+ sChineseNum[iYear % 100 / 10] + sChineseNum[iYear % 10] + "(" + toChineseEra() + ")年";
				if (iMonth > 12)
				{
					iMonth -= 12;
					sCalendar += "闰";
				}
				if (iMonth == 12)
					sCalendar += "腊月";
				else if (iMonth == 11)
					sCalendar += "冬月";
				else if (iMonth == 1)
					sCalendar += "正月";
				else
					sCalendar += sChineseNum[iMonth] + "月";
				if (iDay > 29)
					sCalendar += "三十";
				else if (iDay > 20)
					sCalendar += "二十" + sChineseNum[iDay % 20];
				else if (iDay == 20)
					sCalendar += "二十";
				else if (iDay > 10)
					sCalendar += "十" + sChineseNum[iDay % 10];
				else
					sCalendar += "初" + sChineseNum[iDay];
				return sCalendar;
			}

			public CnWeek toWeek()
			{
				int iOffsetDays = 0;
				for (int i = 1901; i < iYear; i++)
					iOffsetDays += Lunar2SolarOld.iGetLYearDays(i);
				iOffsetDays += Lunar2SolarOld.iGetLNewYearOffsetDays(iYear, iMonth, iDay);
				return new CnWeek((iOffsetDays + 2) % 7);
			}

			public ChineseEra toChineseEra()
			{
				return new ChineseEra(iYear);
			}

			public SolarDate toSolarDate()
			{
				int iYear, iMonth, iDay, iDate;
				SolarDate sd;
				iDate = Integer.parseInt(Lunar2SolarOld.sCalendarLundarToSolar(this.iYear, this.iMonth, this.iDay));
				iYear = iDate / 10000;
				iMonth = iDate % 10000 / 100;
				iDay = iDate % 100;
				sd = new SolarDate(iYear, iMonth, iDay);
				return sd;
			}
		}

		static class CnWeek extends Week
		{
			private String sCnWeek[] = { "日", "一", "二", "三", "四", "五", "六" };

			public CnWeek()
			{
				super();
			}

			public CnWeek(int iWeek)
			{
				super(iWeek);
			}

			public String toString()
			{
				return "星期" + sCnWeek[this.iWeek];
			}
		}

		static class ChineseEra
		{
			int iYear;

			String[] sHeavenlyStems = { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };

			String[] sEarthlyBranches = { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };

			public ChineseEra()
			{
				iYear = 1981;
			}

			public ChineseEra(int iYear)
			{
				if ((iYear < 2050) && (iYear > 1901))
					this.iYear = iYear;
				else
					this.iYear = 1981;
			}

			public String toString()
			{
				int temp;
				temp = Math.abs(iYear - 1924);
				return sHeavenlyStems[temp % 10] + sEarthlyBranches[temp % 12];
			}
		}
	}

	public static class Lunar2Solar
	{
		private final static int MONTH_DAYS[] = { 29, 30 };

		private final static int DAYS_MONTH[][] = { { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
				{ 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } };

		private final static int DATAS[][] = { { 23, 3, 2, 17, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0 },
				{ 41, 0, 4, 23, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1 },
				{ 30, 7, 5, 28, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1 },
				{ 49, 0, 6, 33, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1 },
				{ 38, 0, 0, 38, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1 },
				{ 26, 6, 2, 44, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0 },
				{ 45, 0, 3, 49, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0 },
				{ 35, 0, 4, 54, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1 },
				{ 24, 4, 5, 59, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1 },
				{ 43, 0, 0, 5, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1 },
				{ 32, 0, 1, 10, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1 },
				{ 21, 2, 2, 15, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1 },
				{ 40, 0, 3, 20, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1 },
				{ 28, 7, 5, 26, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1 },
				{ 47, 0, 6, 31, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1 },
				{ 36, 0, 0, 36, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0 },
				{ 26, 5, 1, 41, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1 },
				{ 44, 0, 3, 47, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1 },
				{ 33, 0, 4, 52, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0 },
				{ 23, 3, 5, 57, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1 },
				{ 42, 0, 6, 2, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1 },
				{ 30, 8, 1, 8, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0 },
				{ 48, 0, 2, 13, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0 },
				{ 38, 0, 3, 18, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
				{ 27, 6, 4, 23, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0 },
				{ 45, 0, 6, 29, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0 },
				{ 35, 0, 0, 34, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
				{ 24, 4, 1, 39, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0 },
				{ 43, 0, 2, 44, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0 },
				{ 32, 0, 4, 50, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1 },
				{ 20, 3, 5, 55, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0 },
				{ 39, 0, 6, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0 },
				{ 29, 7, 0, 5, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
				{ 47, 0, 2, 11, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1 },
				{ 36, 0, 3, 16, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0 },
				{ 26, 5, 4, 21, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1 },
				{ 45, 0, 5, 26, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1 },
				{ 33, 0, 0, 32, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1 },
				{ 22, 4, 1, 37, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1 },
				{ 41, 0, 2, 42, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1 },
				{ 30, 8, 3, 47, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1 },
				{ 48, 0, 5, 53, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1 },
				{ 37, 0, 6, 58, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1 },
				{ 27, 6, 0, 3, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0 },
				{ 46, 0, 1, 8, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0 },
				{ 35, 0, 3, 14, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1 },
				{ 24, 4, 4, 19, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1 },
				{ 43, 0, 5, 24, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1 },
				{ 32, 10, 6, 29, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1 },
				{ 50, 0, 1, 35, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0 },
				{ 39, 0, 2, 40, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1 },
				{ 28, 6, 3, 45, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0 },
				{ 47, 0, 4, 50, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
				{ 36, 0, 6, 56, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0 },
				{ 26, 5, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1 },
				{ 45, 0, 1, 6, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0 },
				{ 34, 0, 2, 11, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0 },
				{ 22, 3, 4, 17, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0 },
				{ 40, 0, 5, 22, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0 },
				{ 30, 8, 6, 27, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1 },
				{ 49, 0, 0, 32, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1 },
				{ 37, 0, 2, 38, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
				{ 27, 5, 3, 43, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1 },
				{ 46, 0, 4, 48, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1 },
				{ 35, 0, 5, 53, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1 },
				{ 23, 4, 0, 59, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1 },
				{ 42, 0, 1, 4, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1 },
				{ 31, 0, 2, 9, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0 },
				{ 21, 2, 3, 14, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
				{ 39, 0, 5, 20, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1 },
				{ 28, 7, 6, 25, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1 },
				{ 48, 0, 0, 30, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1 },
				{ 37, 0, 1, 35, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1 },
				{ 25, 5, 3, 41, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1 },
				{ 44, 0, 4, 46, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1 },
				{ 33, 0, 5, 51, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1 },
				{ 22, 4, 6, 56, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0 },
				{ 40, 0, 1, 2, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0 },
				{ 30, 9, 2, 7, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1 },
				{ 49, 0, 3, 12, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1 },
				{ 38, 0, 4, 17, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0 },
				{ 27, 6, 6, 23, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1 },
				{ 46, 0, 0, 28, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0 },
				{ 35, 0, 1, 33, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0 },
				{ 24, 4, 2, 38, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1 },
				{ 42, 0, 4, 44, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
				{ 31, 0, 5, 49, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0 },
				{ 21, 2, 6, 54, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1 },
				{ 40, 0, 0, 59, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
				{ 28, 6, 2, 5, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0 },
				{ 47, 0, 3, 10, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1 },
				{ 36, 0, 4, 15, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1 },
				{ 25, 5, 5, 20, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0 },
				{ 43, 0, 0, 26, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1 },
				{ 32, 0, 1, 31, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0 },
				{ 22, 3, 2, 36, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0 } };

		private static String dateToStr(Date d, int type)
		{
			String s = "";
			SimpleDateFormat sdf = null;
			switch (type)
			{
				case 1:
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				case 2:
					sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				case 3:
					sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			}
			s = sdf.format(d);
			return s;
		}

		/**
		 * 根据阴历取得对应的阳历日期（该日期必须在1936—2028年之间）
		 * @param date 阴历日期（字符串）
		 * @return 返回对应的阳历日期
		 * @throws Exception
		 */
		public static String lunarTosolar(String date)
		{
			Date d = getGregorianCalendar(date);
			String s = dateToStr(d, 1);
			s = s.substring(0, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6, 8);
			return s;

		}

		/**
		 * 根据阳历取得对应的阴历日期（该日期必须在1936—2028年之间）
		 * @param date 阳历日期（字符串）
		 * @return 返回阴历日期
		 * @throws Exception
		 */
		public static String solarTolunar(String date)
		{
			Date d = getLunarCalendar(date);
			String s = dateToStr(d, 1);
			s = s.substring(0, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6, 8);
			return s;

		}

		private static Date getGregorianCalendar(String date)
		{
			Calendar c = getCalendar(date);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			int index = year - 1936;
			int endIndex = month;

			if ((DATAS[index][1] != 0) && (month > DATAS[index][1]))
			{
				endIndex++;
			}

			int dayNum = 0;

			for (int i = 0; i < (endIndex - 1); i++)
			{
				dayNum += MONTH_DAYS[DATAS[index][4 + i]];
			}

			dayNum += day;
			dayNum += DATAS[index][0];

			int year_days = 365;

			if (isLeapYear(year))
			{
				year_days = 366;
			}

			if (dayNum > year_days)
			{
				year++;
				dayNum -= year_days;
			}

			month = 1;

			int dayOfMonth[] = DAYS_MONTH[0];

			if (isLeapYear(year))
			{
				dayOfMonth = DAYS_MONTH[1];
			}

			int i = 0;

			for (; i < 12; i++)
			{
				dayNum -= dayOfMonth[i];

				if (dayNum <= 0)
				{
					break;
				}

				month++;
			}

			day = dayOfMonth[i] + dayNum;

			return getDate(year + "-" + month + "-" + day);
		}

		private static Date getLunarCalendar(String date)
		{
			Calendar calendar = getCalendar(date);
			int year = calendar.get(Calendar.YEAR);
			int month = 1;
			int day;

			if ((year < 1936) || (year > 2028))
			{
				return null;
			}

			int index = year - 1936;
			int l_days = DATAS[index][0];
			int day_year = calendar.get(Calendar.DAY_OF_YEAR);
			int days;

			if (day_year >= l_days)
			{
				days = day_year - l_days;
			}
			else
			{
				index--;
				year--;

				Calendar c = getCalendar(year + "-12-31");
				days = (c.get((Calendar.DAY_OF_YEAR)) + day_year) - DATAS[index][0];
			}

			int i = 0;
			int day_num = 0;

			for (; i < 13; i++)
			{
				day_num += MONTH_DAYS[DATAS[index][i + 4]];

				if (day_num >= days)
				{
					break;
				}

				month++;
			}

			day = MONTH_DAYS[DATAS[index][i + 4]] - (day_num - days);

			if ((DATAS[index][1] != 0) && (month > DATAS[index][1]))
			{
				month--;
			}

			return getDate(year + "-" + month + "-" + day);
		}

		private static Calendar getCalendar(String date)
		{
			Date dd = getDate(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dd);

			return calendar;
		}

		private static Date getDate(String date)
		{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date dd = null;

			try
			{
				dd = format.parse(date);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return dd;
		}

		private static boolean isLeapYear(int year)
		{
			if ((year % 400) == 0)
			{
				return true;
			}
			else if ((year % 100) == 0)
			{
				return false;
			}
			else if ((year % 4) == 0)
			{
				return true;
			}

			return false;
		}
	}

	/**
	 * 农历日历工具使用演示
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println(Solar2Lunar.today());
		System.out.println(Solar2Lunar.oneDay(2022, 2, 1));
		System.out.println(Lunar2Solar.solarTolunar("2022-2-1"));
		System.out.println(Lunar2Solar.lunarTosolar("2022-1-1"));
	}

}
