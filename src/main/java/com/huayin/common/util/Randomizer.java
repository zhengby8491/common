/**
 * <pre>
 * Title: 		Randomizer.java
 * Project: 	AnteAgent
 * Type:		com.huayin.common.util.Randomizer
 * Author:		linriqing
 * Create:	 	2006-8-9 13:21:59
 * Copyright: 	Copyright (c) 2006
 * Company:
 * <pre>
 */
package com.huayin.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <pre>
 * 随机数产生器
 * 同步, 保证随机数种子唯一
 * 保证产生的随机数唯一需要通过setUniqueCycle(long uniqueCycle)设置周期, uniqueCycle表示周期的天数
 * 不设置uniqueCycle,则根据所取随机数长度来尽量保持唯一.
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-8-9
 */
public class Randomizer extends Random
{
	private static final long serialVersionUID = -4992356386948299803L;

	/**
	 * 静态变量, 保证随机数种子唯一
	 */
	private static long initSeed = System.currentTimeMillis();

	/**
	 * 保证随机数唯一的天数, 默认则根据所取随机数长度来尽量保持唯一
	 */
	private long uniqueCycle = 0;

	/**
	 * 当前的随机数种子
	 */
	private long useSeed = System.currentTimeMillis();

	/**
	 * 同一微秒内取的尾随机数集合
	 */
	private static Map<String, String> randSeed = new HashMap<String, String>();

	/**
	 * 上次取随机数的时间
	 */
	private static long lastTimeInMillis = System.currentTimeMillis();

	/**
	 * 构造函数
	 */
	public Randomizer()
	{
		super();
		this.setSeed(this.useSeed);
	}

	/**
	 * 构造函数
	 * @param seed 随机数种子
	 */
	public Randomizer(long seed)
	{
		super(seed);
		this.setSeed(seed);
	}

	/**
	 * @return 保证随机数唯一的天数, 默认则根据所取随机数长度来尽量保持唯一
	 */
	public long getUniqueCycle()
	{
		return uniqueCycle;
	}

	/**
	 * <pre>
	 * 根据指定长度获取双精度随机数
	 * 生成各部分随机数使用同一个种子
	 * </pre>
	 * @param length 指定长度
	 * @return 双精度随机数
	 */
	public double nextDouble(int length)
	{
		double rand = 0;
		int maxLength = String.valueOf(Double.MAX_VALUE).length();
		if (length <= maxLength)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("0.").append(this.nextRandom(length - 2));
			rand = Double.parseDouble(sb.toString());
		}
		else
		{
			throw new IllegalArgumentException("随机数长度[" + length + "]超过double数据类型的最大值允许范围[" + Double.MAX_VALUE + "]");
		}

		return rand;
	}

	/**
	 * <pre>
	 * 根据指定长度获取整型随机数
	 * 生成各部分随机数使用同一个种子
	 * </pre>
	 * @param length 指定长度
	 * @return 整型随机数
	 */
	public int nextInteger(int length)
	{
		int rand = 0;
		int maxLength = String.valueOf(Integer.MAX_VALUE).length();
		if (length <= maxLength)
		{
			String randomStr = this.nextRandom(length);
			while (randomStr.startsWith("0"))
			{
				randomStr = this.nextRandom(length);
			}
			rand = Integer.parseInt(randomStr);
		}
		else
		{
			throw new IllegalArgumentException("随机数长度[" + length + "]超过int数据类型的最大值允许范围[" + Integer.MAX_VALUE + "]");
		}

		return rand;
	}

	/**
	 * <pre>
	 * 根据指定长度获取长整型随机数
	 * 生成各部分随机数使用同一个种子
	 * </pre>
	 * @param length 指定长度
	 * @return 长整型随机数
	 */
	public long nextLong(int length)
	{
		long rand = 0;
		int maxLength = String.valueOf(Long.MAX_VALUE).length();
		if (length <= maxLength)
		{
			String randomStr = this.nextRandom(length);
			while (randomStr.startsWith("0"))
			{
				randomStr = this.nextRandom(length);
			}
			rand = Long.parseLong(randomStr);
		}
		else
		{
			throw new IllegalArgumentException("随机数长度[" + length + "]超过long数据类型的最大值允许范围[" + Long.MAX_VALUE + "]");
		}

		return rand;
	}

	/**
	 * <pre>
	 * 根据指定长度获取随机数字符串
	 * 生成各部分随机数使用同一个种子
	 * 根据长度来实现不同时段的唯一随机数
	 * public synchronized String nextRandom(int length)
	 * </pre>
	 * @param length 指定长度
	 * @return 随机数字符串
	 */
	public String nextRandom(int length)
	{
		// 时间部分开始设为当前微秒数
		long randTime = System.currentTimeMillis();
		String timePart = String.valueOf(randTime);

		if (lastTimeInMillis == randTime)
		{
			// 与最后取随机数的微秒数相同, 则创建额外的随机数并检索是否存在HashMap里面,如果在则需重取
			// 如果9000个随机数已用完, 则循环等待一个微秒的周期
			String extrand = String.valueOf(this.nextInt(8999) + 1000);
			while (randSeed.containsKey(extrand))
			{
				randTime = System.currentTimeMillis();
				if (lastTimeInMillis == randTime)
				{
					extrand = String.valueOf(this.nextInt(8999) + 1000);
				}
				else
				{
					// 与最后取随机数的微秒数不同, 则设置最后取随机数的微秒数
					// 并清空HashMap, 再创建额外的随机数放到HashMap里面
					lastTimeInMillis = randTime;
					randSeed.clear();
					extrand = String.valueOf(this.nextInt(8999) + 1000);
				}
			}
			randSeed.put(extrand, "");
			timePart += String.valueOf(extrand);
		}
		else
		{
			// 与最后取随机数的微秒数不同, 则设置最后取随机数的微秒数
			// 并清空HashMap, 再创建额外的随机数放到HashMap里面
			lastTimeInMillis = randTime;
			randSeed.clear();
			String extrand = String.valueOf(this.nextInt(8999) + 1000);
			randSeed.put(extrand, "");
			timePart = String.valueOf(randTime) + String.valueOf(extrand);
		}

		this.setSeed(Long.parseLong(timePart));

		int timeLength = timePart.length();

		if (this.uniqueCycle > 0)
		{
			int cutLength = String.valueOf(this.uniqueCycle * 86400000L).length() + 1;

			if (cutLength < timeLength)
			{
				timePart.substring(timeLength - cutLength);
			}
		}
		if (timeLength >= length)
		{
			return timePart.substring(timeLength - length);
		}
		else
		{
			StringBuffer randPart = new StringBuffer();
			int randLength = length - timeLength;
			while (randLength > randPart.length())
			{
				randPart.append(String.valueOf(Math.abs(this.nextLong())));
			}

			return (timePart + randPart.substring(randPart.length() - randLength));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Random#setSeed(long)
	 */
	public final synchronized void setSeed(long seed)
	{
		// 保证initSee递增唯一, 方法内同步
		initSeed++;
		this.useSeed = initSeed + seed;
		super.setSeed(this.useSeed);
	}

	/**
	 * @param uniqueCycle 保证随机数唯一的天数, 默认则根据所取随机数长度来尽量保持唯一
	 */
	public void setUniqueCycle(long uniqueCycle)
	{
		this.uniqueCycle = uniqueCycle;
	}
}
