/**
 * <pre>
 * Title: 		EventListenerContainer.java
 * Project: 	HP-Common
 * Author:		zhaojitao
 * Create:	 	2008-4-25 上午03:39:08
 * Copyright: 	Copyright (c) 2008
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.eds;

import java.io.Serializable;

/**
 * <pre>
 * 监听器实例容器
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2008-4-25
 */
public class EventListenerContainer implements Comparable<EventListenerContainer>, Serializable
{
	private static final long serialVersionUID = 41112379288385499L;

	/**
	 * 监听器索引值, 决定优先级
	 */
	private int index;

	/**
	 * 监听器实例
	 */
	private EventListener listener;

	/**
	 * 事件类型
	 */
	private String eventType;

	/**
	 * 事件名称
	 */
	private String eventName;

	public int compareTo(EventListenerContainer o)
	{
		return new Integer(this.getIndex()).compareTo(o.getIndex());
	}

	/**
	 * @return 事件类型
	 */
	public String getEventType()
	{
		return eventType;
	}

	/**
	 * @return 事件名称
	 */
	public String getEventName()
	{
		return eventName;
	}

	/**
	 * @return 监听器实例
	 */
	public EventListener getListener()
	{
		return listener;
	}

	/**
	 * @return 监听器索引值, 决定优先级
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @param eventType 事件类型
	 */
	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}

	/**
	 * @param eventName 事件名称
	 */
	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}

	/**
	 * @param listener 监听器实例
	 */
	public void setListener(EventListener listener)
	{
		this.listener = listener;
	}

	/**
	 * @param index 监听器索引值, 决定优先级
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}
}
