/**
 * <pre>
 * Title: 		EventContextListener.java
 * Project: 	Common-Util
 * Author:		zhaojitao
 * Create:	 	2013-10-31 下午01:54:59
 * Copyright: 	Copyright (c) 2013
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.eds;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.huayin.common.eds.Event.ActionState;

/**
 * <pre>
 * 事件服务启动监听器
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2013-10-31
 */
@Component
@Lazy
public class EventContextListener implements ApplicationContextAware
{
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		EventContext contextListener = EventContextImpl.getInstance();
		EventContextStartEvent event2 = new EventContextStartEvent(this, this, "***Start initialize webserver:"
				+ applicationContext.getDisplayName() + "...", ActionState.Starting, applicationContext,
				contextListener);
		contextListener.perform(event2);
	}
}
