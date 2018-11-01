/**
 * <pre>
 * Title: 		Counter.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2009-5-15 上午11:16:51
 * Copyright: 	Copyright (c) 2009
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.thread;

/**
 * <pre>
 * 计数器
 * </pre>
 * @author linriqing
 * @version 1.0, 2009-5-15
 */
public class Counter implements Comparable<Counter>
{
	private int count;

	private String name;

	public Counter()
	{
		super();
	}

	/**
	 * 构造函数
	 * @param name 计数器名称
	 */
	public Counter(String name)
	{
		this.name = name;
	}

	public int compareTo(Counter o)
	{
		return this.name.compareTo(o.name);
	}

	/**
	 * <pre>
	 * 递减
	 * </pre>
	 */
	public void decrease()
	{
		synchronized (this)
		{
			count--;
		}
	}

	/**
	 * @return 计数器名称
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * <pre>
	 * 递增
	 * </pre>
	 */
	public void increase()
	{
		synchronized (this)
		{
			count++;
		}
	}

	/**
	 * <pre>
	 * 计数器当前值
	 * </pre>
	 * @return 返回计数器当前值
	 */
	public int value()
	{
		return count;
	}
}
