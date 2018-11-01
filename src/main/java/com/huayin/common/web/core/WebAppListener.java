/**
 * <pre>
 * Author:		zhaojitao
 * Create:	 	2007-6-8 04:56:44PM
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.web.core;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.huayin.common.cis.ComponentContextLoader;
import com.huayin.common.eds.Event;
import com.huayin.common.eds.Event.ActionState;
import com.huayin.common.eds.EventContextImpl;
import com.huayin.common.exception.SystemException;

/**
 * <pre>
 * Web application listener
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2007-6-8
 */
public class WebAppListener extends ContextLoaderListener
{
	private static final Log logger = LogFactory.getLog(WebAppListener.class);

	/**
	 * Web server destory event
	 * @param event ServletContextEvent
	 */
	public void contextDestroyed(ServletContextEvent event)
	{
		logger.info("***Start destory webserver:" + event.getServletContext().getServletContextName() + "...");
		try
		{
			EventContextImpl.getInstance().perform(new Event(this, "***Start destory webserver:"
					+ event.getServletContext().getServletContextName() + "...", ActionState.Ended));
			super.contextDestroyed(event);
		}
		catch (Exception e)
		{
			throw new SystemException("Destory webserver{" + event.getServletContext().getServletContextName()
					+ "} cause exception", e);
		}
	}

	/**
	 * Web server initialize event
	 * @param event ServletContextEvent
	 */
	public void contextInitialized(ServletContextEvent event)
	{
		logger.info("***Start initialize webserver:" + event.getServletContext().getServletContextName() + "...");
		super.contextInitialized(event);
		ServletContext context = event.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		context.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ctx);
		ComponentContextLoader.LazyInstance.setContext(ctx);
	}
}
