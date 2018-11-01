/**
 * <pre>
 * Author:		zhaojitao
 * Create:	 	2010-6-24 下午05:12:37
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.eds;

import java.io.Serializable;

/**
 * <pre>
 * 通用事件，基类，其它事件都要继承该类
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-6-24
 */
public class Event implements Serializable
{
	/**
	 * <pre>
	 * 事件动作状态
	 * </pre>
	 * @author zhaojitao
	 * @version 1.0, 2011-6-24
	 */
	public enum ActionState
	{
		/**
		 * 动作完成
		 */
		Ended,
		/**
		 * 错误
		 */
		Error,
		/**
		 * 异常
		 */
		Exception,
		/**
		 * 进行中
		 */
		Starting;
	}

	private static final long serialVersionUID = 8110221699060036977L;

	/**
	 * 事件动作状态
	 */
	private ActionState actionState;

	/**
	 * 如果关心监听者处理进程的话,那么必须指定回调对象
	 */
	private EventCallback callback;

	/**
	 * 如果是异常事件，则附带异常信息
	 */
	private Throwable exception;

	/**
	 * 事件调用者
	 */
	private Object source;

	/**
	 * 事件描述
	 */
	private String summary;

	/**
	 * 构造函数
	 * @param source 事件发布者
	 * @param summary 事件描述
	 * @param actionState 事件动作状态
	 */
	public Event(Object source, String summary, ActionState actionState)
	{
		super();
		this.source = source;
		this.summary = summary;
		this.actionState = actionState;
	}

	/**
	 * 构造函数
	 * @param source 事件发布者
	 * @param summary 事件描述
	 * @param actionState 事件动作状态
	 * @param callback 异步调用回调方法,由容器调用,监听者不需要重复调用
	 */
	public Event(Object source, String summary, ActionState actionState, EventCallback callback)
	{
		this.source = source;
		this.callback = callback;
		this.summary = summary;
		this.actionState = actionState;
	}

	/**
	 * 构造函数
	 * @param source 事件发布者
	 * @param summary 事件描述
	 * @param actionState 事件动作状态
	 * @param exception 异常信息
	 */
	public Event(Object source, String summary, ActionState actionState, Throwable exception)
	{
		super();
		this.source = source;
		this.summary = summary;
		this.setException(exception);
		this.actionState = actionState;
	}

	/**
	 * @return 事件动作状态
	 */
	public ActionState getActionState()
	{
		return actionState;
	}

	/**
	 * @return callback 异步调用回调方法,由容器调用,监听者不需要重复调用 
	 */
	protected EventCallback getCallback()
	{
		return callback;
	}

	/**
	 * @return 事件触发的异常信息
	 */
	public Throwable getException()
	{
		return exception;
	}

	/**
	 * @return source 事件发布者
	 */
	public Object getSource()
	{
		return source;
	}

	/**
	 * @return summary 获取事件描述
	 */
	public String getSummary()
	{
		return summary;
	}

	/**
	 * @param exception 事件触发的异常信息
	 */
	public void setException(Throwable exception)
	{
		this.exception = exception;
	}
}
