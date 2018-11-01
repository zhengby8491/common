package com.huayin.common.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * <pre>
 * 日期操作工具类
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2011-3-10
 */
public class DateTimeUtil
{
	public static final String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";

	public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";

	public static final String DATE_FORMAT_HMS = "HH:mm:ss";

	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

	/**
	 * <pre>
	 * 日期对象转字符串
	 * </pre>
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatToStr(Date date, String format)
	{
		if (date == null)
		{
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * <pre>
	 * 字符串转日期对象
	 * </pre>
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date formatToDate(String dateStr, String format)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		ParsePosition pos = new ParsePosition(0);// 从0开始解析
		Date strtodate = formatter.parse(dateStr, pos);
		return strtodate;
	}

	/**
	 * <pre>
	 * 格式化日期字符串
	 * </pre>
	 * @param dateStr
	 * @param oldformat
	 * @param newformat
	 * @return
	 */
	public static String formatStrToStr(String dateStr, String oldformat, String newformat)
	{
		return formatToStr(formatToDate(dateStr, oldformat), newformat);
	}

	/**
	 * <pre>
	 * 格式化日期（简短型）
	 * </pre>
	 * @param date
	 * @return 返回格式 yyyy-MM-dd
	 */
	public static String formatShortStr(Date date)
	{
		if (date == null)
		{
			return null;
		}
		return formatToStr(date, DATE_FORMAT_YMD);
	}

	/**
	 * <pre>
	 * 格式化日期（全格式型）
	 * </pre>
	 * @param date
	 * @return 返回格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String formatLongStr(Date date)
	{
		if (date == null)
		{
			return null;
		}
		return formatToStr(date, DATE_FORMAT_YMDHMS);
	}

	/**
	 * <pre>
	 *  格式化日期（时间型）
	 * </pre>
	 * @param date
	 * @return 返回格式 HH:mm:ss
	 */
	public static String formatTimeStr(Date date)
	{
		if (date == null)
		{
			return null;
		}
		return formatToStr(date, DATE_FORMAT_HMS);
	}

	/**
	 * <pre>
	 * 增加天数
	 * </pre>
	 * @param date 原始日期
	 * @param days 增加的天数
	 * @return 增加days天后的日期
	 */
	public static Date addDate(Date date, int days)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}

	/**
	 * <pre>
	 *  得到日期的小时(24小时制)
	 * </pre>
	 * @param date
	 * @return
	 */
	public static int getHour(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * <pre>
	 * 得到日期的分钟
	 * </pre>
	 * @param date
	 * @return
	 */
	public static int getMinute(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}

	/**
	 * <pre>
	 * 增加num秒后的时间
	 * </pre>
	 * @param date
	 * @param second
	 * @return
	 */
	public static Date addSecond(Date date, long num)
	{
		long time = date.getTime() + num * 1000;
		return new Date(time);
	}

	/**
	 * 判断是否润年
	 * @param ddate
	 * @return
	 */
	public static boolean isLeapYear(Date date)
	{
		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年 3.能被4整除同时能被100整除则不是闰年
		 */
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		if ((year % 400) == 0)
		{
			return true;
		}
		else if ((year % 4) == 0)
		{
			if ((year % 100) == 0)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * 获取年得第一天
	 * @param year
	 * @return
	 */
	public static Date getFristDateForYear(int year)
	{
		String timeStr = year + "-01-01 00:00:00";
		return formatToDate(timeStr, DATE_FORMAT_YMDHMS);
	}

	/**
	 * 获取年得最后一天
	 */
	public static Date getLastDateForYear(int year)
	{
		String timeStr = year + "-12-31 23:59:59";
		return formatToDate(timeStr, DATE_FORMAT_YMDHMS);
	}

	/**
	 * 获取一个月的第一天
	 * @param date
	 * @return
	 */
	public static Date getFristDateForMonth(int year, int month)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 00);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取一个月的第一天
	 * @param date
	 * @return
	 */
	public static Date getFristDateForMonth(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	/**
	 * 获取一个月的最后一天
	 * @param date
	 * @return
	 */
	public static Date getEndDateOfMonth(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int curMonth = c.get(Calendar.MONTH);
		c.set(Calendar.MONTH, curMonth + 1);
		c.set(Calendar.DAY_OF_MONTH, 0);
		return c.getTime();
	}

	/**
	 * 判断二个时间是否在同一个周
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeekDates(Date date1, Date date2)
	{
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		if (c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR))
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断二个时间是否在同一个月
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameMonthDates(Date date1, Date date2)
	{
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断二个时间是否在同一个年
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameYearDates(Date date1, Date date2)
	{
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
		{
			return true;
		}
		return false;
	}

	/**
	 * <pre>
	 * 获取日期的年份
	 * </pre>
	 * @param date
	 * @return
	 */
	public static int getYear(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	/**
	 * <pre>
	 * 获取日期的月份(特殊是月份要加1)
	 * </pre>
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * <pre>
	 * 获取日期在一年中的第几周
	 * </pre>
	 * @param date
	 * @return
	 */
	public static int getWeekForYear(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * <pre>
	 * 获取日期在当前月中的第几周
	 * </pre>
	 * @param date
	 * @return
	 */
	public static int getWeekForMonth(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * <pre>
	 * 判断当前日期是周几
	 * 1-7 分别表示周日,周一,周二,周三,周四,周五,周六
	 * 1:SUNDAY, 2:MONDAY, 3:TUESDAY, 4:WEDNESDAY, 5:THURSDAY, 6:FRIDAY, 7:SATURDAY
	 * </pre>
	 * @param date
	 * @return
	 */
	public static int getDayForWeek(Date date)
	{

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * <pre>
	 * w
	 * 获得一个日期所在周的周几的日期
	 * 例：获得当前周的周一：getFristForWeek(new Date(),Calendar.MONDAY)
	 * </pre>
	 * @param date 日期
	 * @param weekday 周数 1-7 分别表示周日,周一,周二,周三,周四,周五,周六
	 * @return 所在日期
	 */
	public static Date getDayForWeek(Date date, int weekday)
	{
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, weekday);
		return c.getTime();
	}

	public static Date getMonDayForYear(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int between = cal.get(Calendar.DAY_OF_WEEK) - 1;
		int subMonday = 0;
		if (between >= 1 && between <= 6)
		{
			subMonday = 1 - between;
		}
		else if (between == 0)
		{
			subMonday = -6;
		}
		cal.add(Calendar.DAY_OF_MONTH, subMonday);
		return cal.getTime();

	}

	public static Date getSunDayForYear(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int between = cal.get(Calendar.DAY_OF_WEEK) - 1;
		int subMonday = 0;
		if (between >= 1 && between <= 6)
		{
			subMonday = 1 - between;
		}
		else if (between == 0)
		{
			subMonday = -6;
		}
		cal.add(Calendar.DAY_OF_MONTH, subMonday);
		cal.add(Calendar.DAY_OF_MONTH, 6);
		return cal.getTime();
	}

	/**
	 * 两个时间之间的天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDayNum(Date start, Date end)
	{
		return Integer.parseInt(String.valueOf((end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000))) + 1;
	}

	/**
	 * <pre>
	 * 获取两个时间之间的日期数组
	 * </pre>
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Date> getDayArray(Date start, Date end)
	{
		List<Date> dates = new ArrayList<Date>();
		int count = getDayNum(start, end);
		for (int i = 0; i < count; i++)
		{
			Calendar c = Calendar.getInstance();
			c.setTime(start);
			c.add(Calendar.DAY_OF_YEAR, i);
			dates.add(c.getTime());
		}
		return dates;
	}

	/**
	 * 获得昨天
	 * @param date
	 * @return Date
	 */
	public static Date getPreviousDate()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * 获得昨天开始时间
	 * @param date
	 * @return Date
	 */
	public static Date getPreviousStartDate()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获得昨天结束时间
	 * @param date
	 * @return Date
	 */
	public static Date getPreviousEndDate()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	/**
	 * 获得昨天结束时间
	 * @param date
	 * @return Date
	 */
	public static Date getPreviousEndDateInMin()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取某天的开始时间
	 * @return
	 */
	public static Date getSomeDayStartTimes(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取某天的结束时间
	 * @return
	 */
	public static Date getSomeDayEndTimes(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	/**
	 * 计算某年某周的开始日期
	 * @param yearNum 格式 yyyy ，必须大于1900年度 小于9999年
	 * @param weekNum 1到52或者53
	 * @return 日期
	 */
	public static Date getYearWeekFirstDay(int yearNum, int weekNum)
	{
		if (yearNum < 1900 || yearNum > 9999)
		{
			throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
		}

		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, yearNum);
		c.set(Calendar.WEEK_OF_YEAR, weekNum);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		return c.getTime();
	}

	/**
	 * 计算某年某周的结束日期
	 * @param yearNum 格式 yyyy ，必须大于1900年度 小于9999年
	 * @param weekNum 1到52或者53
	 * @return 日期，格式为yyyy-MM-dd
	 */
	public static Date getYearWeekEndDay(int yearNum, int weekNum)
	{
		if (yearNum < 1900 || yearNum > 9999)
		{
			throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
		}

		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, yearNum);
		c.set(Calendar.WEEK_OF_YEAR, weekNum);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		c.roll(Calendar.DAY_OF_WEEK, 6);

		return c.getTime();
	}

	/**
	 * 获得本周星期一的日期
	 */
	public static Date getCurrentWeekMonday()
	{
		Calendar cal = Calendar.getInstance();
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek)
		{
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		return cal.getTime();
	}

	/**
	 * 获得本周星期日的日期
	 * @return
	 */
	public static Date getCurrentWeekSunday()
	{
		Calendar cal = Calendar.getInstance();
		// 判断要计算的日期是否是周日，如果是直接返回该时间
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek)
		{
			return cal.getTime();
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, 6);
		return cal.getTime();
	}

	/**
	 * 获得上周星期一的日期
	 */
	public static Date getPreviousWeekMonday()
	{
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * -1);
		Date monday = currentDate.getTime();
		return monday;
	}

	/**
	 * 获得上周星期日的日期
	 * @return
	 */
	public static Date getPreviousWeekSunday()
	{
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus - 1);
		return currentDate.getTime();
	}

	// 获得当前日期与上周日相差的天数
	private static int getMondayPlus()
	{
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1)
		{
			return 0;
		}
		else
		{
			return 1 - dayOfWeek;
		}
	}

	public static void main(String args[])
	{/*
		String formatStr = "yyyy-MM-dd";
		DateFormat format = new SimpleDateFormat(formatStr);
		try
		{
			int n=DateTimeUtil.getDayNum(format.parse("2015-03-28"),format.parse(DateTimeUtil.formatToStr(new Date(), formatStr)));
			System.out.println(n);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		System.out.println(formatShortStr(getCurrentWeekMonday()));

		System.out.println(formatShortStr(getCurrentWeekSunday()));
		if (true)
			return;
		String dateStrEx1 = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
		String dateStrEx2 = "[0-9]{4}[0-9]{2}[0-9]{2}";
		String dateStr = "201201";

		if (Pattern.matches(dateStrEx1, dateStr))
		{
			System.out.println(formatToStr(formatToDate(dateStr, DATE_FORMAT_YMD), DATE_FORMAT_YMDHMS));
		}
		else if (Pattern.matches(dateStrEx2, dateStr))
		{
			System.out.println(formatToStr(formatToDate(dateStr, DATE_FORMAT_YYYYMMDD), DATE_FORMAT_YMDHMS));
		}
		else
		{
			System.out.println(formatToStr(addDate(new Date(), -1), DATE_FORMAT_YMDHMS));
		}
		
		Date a = getFristDateForYear(2011);
		Date b = getLastDateForYear(2011);
		System.out.println("a:-->" + formatShortStr(a));
		System.out.println("b:-->" + formatShortStr(b));
		DateTimeUtil d = new DateTimeUtil();
		System.out.println(formatShortStr(getPreviousWeekMonday()));
		System.out.println(formatShortStr(getPreviousWeekSunday()));
	*/}
}
