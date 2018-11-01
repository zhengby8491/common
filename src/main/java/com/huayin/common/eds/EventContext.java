/**
 * <pre>
 * Author:		zhaojitao
 * Create:	 	2010-8-18 上午10:30:39
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.eds;

import java.util.List;

import com.huayin.common.eds.Event.ActionState;

/**
 * <pre>
 * 事件处理容器上下文接口
 * 声明事件:
 * <code>
 * EventContextImpl.getInstance().perform(new {@link Event}(this, "testEvent", {@link ActionState}.Starting));
 * </code>
 * 监听事件：
 * <code>
 * \@Listener(events = { EventListenerTest.TestEvent.class }, create = InstanceType.FACTORY, factory = "getInstance", index = 1)
 * public class EventListenerTest implements EventListener
 * {
 * ...
 * }
 * </code>
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-8-18
 */
public interface EventContext
{
	/**
	 * 事件处理容器上下文bean名称
	 */
	public static final String CONTEXT_BEAN_NAME_EVENTCONTEXT = "eventcontext";

	/**
	 * <pre>
	 * 为事件添加监听器
	 * </pre>
	 * @param eventClass 指定监听的事件类型
	 * @param listener 监听器实例
	 * @param index 监听器优先级, -1则表示当前最低优先级
	 * @param isBlock 是否阻塞事件发布者
	 * @param excludes 排除的监听事件类型集合
	 * @return 监听器索引值
	 */
	public int addListener(Class<? extends Event> eventClass, EventListener listener, int index, boolean isBlock,
			List<Class<? extends Event>> excludes);

	/**
	 * <pre>
	 * 发布事件，异步处理
	 * 如果关心监听者处理进程的话,可以给event设置{@link EventCallback}对象.
	 * </pre>
	 * @param event 事件对象
	 * @see Event
	 * @see EventCallback
	 */
	public void perform(final Event event);

	/**
	 * <pre>
	 * 事件处理容器上下文
	 * </pre>
	 * @throws InterruptedException 线程池中断异常
	 */
	public void shutdown() throws InterruptedException;

	/**
	 * <pre>
	 * 移除所有监听器
	 * </pre>
	 * @param eventClass 事件类型
	 */
	public void removeAllListener(Class<? extends Event> eventClass);

	/**
	 * <pre>
	 * 移除监听器
	 * </pre>
	 * @param eventClass 事件类型
	 * @param index 监听器索引值
	 */
	public void removeListener(Class<? extends Event> eventClass, int index);

}
