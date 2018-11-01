/**
 * <pre>
 * Title: 		EventListener.java
 * Author:		zhaojitao
 * Create:	 	2010-6-24 下午04:32:52
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.eds;

import com.huayin.common.eds.EventContextImpl.ListenerContainer;
import com.huayin.common.exception.ServiceException;

/**
 * <pre>
 * 事件监听器接口
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-6-24
 */
public interface EventListener
{
	/**
	 * <pre>
	 * 监听器注销时触发的动作
	 * </pre>
	 * @param container 监听器的容器对象
	 */
	void destory(ListenerContainer container);

	/**
	 * <pre>
	 * 监听器注册时触发的动作
	 * </pre>
	 * @param container 监听器的容器对象
	 */
	void start(ListenerContainer container);

	/**
	 * <pre>
	 * 事件监听处理, 事件发布者一般都不关心结果的
	 * 事件发布者唯一可能知道事件处理失败的方法是捕捉异常
	 * 注意:
	 * 1.event中可能设置了asynEvent属性, 事件服务框架会自动根据该属性决定通知方式, 监听器不用再重复处理;
	 * 2.event中可能设置了callback属性, 事件服务框架会自动检查发布结果来调用callback, 监听器应避免重复调用.
	 * </pre>
	 * @param event 事件参数
	 * @param container 监听器的容器对象
	 */
	<T extends Event> void update(T event, ListenerContainer container) throws ServiceException;
}
