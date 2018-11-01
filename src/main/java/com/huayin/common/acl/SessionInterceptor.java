/**
 * <pre>
 * Title: 		UserController.java
 * Author:		linriqing
 * Create:	 	2010-6-3 下午02:20:13
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.acl;

import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.huayin.common.acl.impl.PermissionProviderImpl;
import com.huayin.common.acl.vo.Passport;
import com.huayin.common.acl.vo.Permission;
import com.huayin.common.constant.Constant;
import com.huayin.common.persist.ClientContextHolder;
import com.huayin.common.web.core.ActionPageInfo;

/**
 * <pre>
 * 会话拦截器
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-6-3
 */
@Component("com.huayin.common.acl.SessionInterceptor")
public class SessionInterceptor extends HandlerInterceptorAdapter
{
	public static final Log log = LogFactory.getLog(SessionInterceptor.class);

	public static final String NOT_ALLOW_PRIVILEGE = "非常抱歉, 您无权访问!";

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		super.preHandle(request, response, handler);

		String url = request.getServletPath();
		int index = url.indexOf("?");
		if (index > 0)
		{
			url = url.substring(0, index);
		}
		boolean passFlag = false;
		for (String exclude : PermissionProviderImpl.getInstance().getExcludeActions())
		{
			if (exclude.endsWith("*"))
			{
				if (url.startsWith(exclude.replaceAll("\\*", "")))
				{
					passFlag = true;
					break;
				}
			}
			else
			{
				if (url.endsWith(exclude))
				{
					passFlag = true;
					break;
				}
			}
		}

		if (!passFlag)
		{
			// 会话检查处理
			HttpSession session = request.getSession();
			Object profile = session.getAttribute(Constant.PROFILE_SESSION_NAME);
			Object history = session.getAttribute(Constant.HISTORY_SESSION_NAME);
			if (request.getHeader("x-requested-with") != null
					&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest"))
			{
				if ((profile == null) || !(profile instanceof Passport))
				{
					response.addHeader("sessionstatus", "timeout");
					return false;
				}
				else
				{
					// String method = request.getMethod();
					Permission permissionByAction = PermissionProviderImpl.getInstance().getPermissionByAction(url);
					if (permissionByAction != null)
					{
						boolean validPermission = PermissionProviderImpl.getInstance().validPermission(
								(Passport) profile, permissionByAction);
						if (!validPermission)
						{
							response.addHeader("sessionstatus", "timeout");
							return false;
						}
					}
					return true;
				}
			}
			else
			{
				if ((profile == null) || !(profile instanceof Passport))
				{
					response.sendRedirect(request.getContextPath().concat("/index.jsp"));
					return false;
				}
				else
				{
					ModelAndView mav = new ModelAndView("/errord");
					String method = request.getMethod();
					Permission permissionByAction = PermissionProviderImpl.getInstance().getPermissionByAction(url);
					if (permissionByAction != null)
					{
						request.setAttribute(Constant.CURRENT_PAGE_INFO_NAME, permissionByAction);
						boolean validPermission = PermissionProviderImpl.getInstance().validPermission(
								(Passport) profile, permissionByAction);
						if (validPermission)
						{
							if (history != null)
							{
								LinkedBlockingQueue<ActionPageInfo> historyPageInfo = (LinkedBlockingQueue<ActionPageInfo>) history;
								ActionPageInfo api = new ActionPageInfo(permissionByAction.getId(), method);
								if (historyPageInfo.remainingCapacity() > 0)
								{
									historyPageInfo.add(api);
								}
								else
								{
									synchronized (historyPageInfo)
									{
										historyPageInfo.poll();
										historyPageInfo.add(api);
									}
								}
							}
							else
							{
								LinkedBlockingQueue<ActionPageInfo> historyPageInfo = new LinkedBlockingQueue<ActionPageInfo>(
										50);
								ActionPageInfo api = new ActionPageInfo(permissionByAction.getId(), method);
								historyPageInfo.add(api);
								session.setAttribute(Constant.HISTORY_SESSION_NAME, historyPageInfo);
							}
						}
						else
						{
							mav.addObject(Constant.MESSAGE_KEY, NOT_ALLOW_PRIVILEGE);
							throw new ModelAndViewDefiningException(mav);
						}
					}
					else
					{
						log.warn("没有找到该url的权限节点，取默认访问权限，如有需要请手动配置此URL节点到配置文件。url='" + url + "'");
						boolean defaultAllowPrivilege = PermissionProviderImpl.getInstance().getDefaultAllowPrivilege();
						if (defaultAllowPrivilege)
						{
							return defaultAllowPrivilege;
						}
						else
						{
							mav.addObject(Constant.MESSAGE_KEY, NOT_ALLOW_PRIVILEGE);
							throw new ModelAndViewDefiningException(mav);
						}
					}
				}
			}
			ClientContextHolder.setClientAction((Passport) profile);
		}
		return true;
	}
}
