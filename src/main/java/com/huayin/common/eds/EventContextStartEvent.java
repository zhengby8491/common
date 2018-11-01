package com.huayin.common.eds;

import org.springframework.context.ApplicationContext;

/**
 * <pre>
 * 事件服务启动事件
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2011-6-24
 */
public class EventContextStartEvent extends Event
{
	private static final long serialVersionUID = 5046807191508945063L;

	private ApplicationContext ctx;

	private EventContext eventContext;

	public EventContextStartEvent(EventContextListener eventContextListener, Object source, String summary,
			ActionState actionState, ApplicationContext ctx, EventContext eventContext)
	{
		super(source, summary, actionState);
		this.ctx = ctx;
		this.eventContext = eventContext;
	}

	public ApplicationContext getCtx()
	{
		return ctx;
	}

	public EventContext getEventContext()
	{
		return eventContext;
	}
}