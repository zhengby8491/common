/**
 * <pre>
 * Title: 		ActionConstant.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2007-8-13 上午02:38:32
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.web.core;

import java.io.Serializable;

/**
 * <pre>
 * 框架基础中Action用到的一些常量定义
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-8-13
 */
public class ActionConstant implements Serializable
{
	/**
	 * <pre>
	 * 登录的ActionPath {@value}
	 * 值: /logon.do
	 * </pre>
	 */
	public final static String ACTION_LOGON_NAME = "/logon.do";

	/**
	 * <pre>
	 * 用于存储页面信息的Session标识名称 {@value}
	 * 值: _Action_Page_Info_
	 * </pre>
	 */
	public final static String ACTION_PAGEINFO_NAME = "_Action_Page_Info_";

	/**
	 * <pre>
	 * 用户会话Session标识名称 {@value}
	 * 值: CUR_USER_INFO
	 * </pre>
	 */
	public final static String PROFILE_SESSION_NAME = "CUR_USER_INFO";

	/**
	 * <pre>
	 * 用于存储查询页面信息的Session标识名称 {@value}
	 * 值: _QueryAction_Page_Info_
	 * </pre>
	 */
	public final static String QUERYACTION_PAGEINFO_NAME = "_QueryAction_Page_Info_";

	/**
	 * <pre>
	 * 存放返回页面需要用的Parameter参数对象 {@value}
	 * 值: RETURN_PAGE_PARAMETER
	 * </pre>
	 */
	public final static String RETURN_PAGE_PARAMETER = "RETURN_PAGE_PARAMETER";

	/**
	 * <pre>
	 * JSP页面的WEB相对路径 {@value}
	 * 值: /pages
	 * </pre>
	 */
	public final static String STRUTS_JSP_FILEPATH_PREFIX = "/pages";

	private static final long serialVersionUID = -7795731086288495692L;
}
