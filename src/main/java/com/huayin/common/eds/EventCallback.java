/**
 * <pre>
 * Title: 		EventCallback.java
 * Author:		zhaojitao
 * Create:	 	2010-6-24 下午04:44:47
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.eds;

import com.huayin.common.eds.EventContextImpl.ListenerContainer;

/**
 * <pre>
 * 事件发布者和事件监听者之间可能是异步的关系， 
 * 如果发布者想要获得监听者的 处理结果， 那么需要构造一个回调对象(callback)传递到监听者， 
 * 当监听者 完成处理时候通过回调对象来将处理结果通知到事件发布者。
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-6-24
 */
public interface EventCallback
{
	/**
	 * <pre>
	 * 当事件监听器处理完成时调用此方法
	 * </pre>
	 * @param event 事件对象
	 * @param container 监听器的容器对象
	 * @param isException 是否捕捉到调用异常
	 * @param exception 监听异常信息
	 */
	public void callback(Event event, ListenerContainer container, boolean isException, Exception exception);
}
