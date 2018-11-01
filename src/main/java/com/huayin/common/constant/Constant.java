/**
 * <pre>
 * Title: 		MessageConstant.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.msg.agentportal.MessageConstant
 * Author:		linriqing
 * Create:	 	2007-6-14 上午11:38:54
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.constant;

import java.io.Serializable;

/**
 * <pre>
 * 交易消息常量定义
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2007-6-14
 */
public class Constant implements Serializable
{
	private static final long serialVersionUID = -3924223846414422902L;

	/**
	 * 公用组件属性配置文件 {@value}
	 * @value hy-common.properties
	 */
	public static final String HY_COMMON_PROPERTIES_FILEPATH = "hy-common.properties";

	/**
	 * 响应状态码说明配置文件 {@value}
	 * @value response-code-config.xml
	 */
	public final static String RESPONSE_CODE_CONFIG_FILEPATH = "response-code-config.xml";

	/**
	 * 权限物件定义配置文件 {@value}
	 * @value system-permission-definition.xml
	 */
	public static final String SYSTEM_PERMISSION_CONFIG_FILEPATH = "system-permission-definition.xml";

	/**
	 * 组件定义配置文件 {@value}
	 * @value system-component-definition.xml
	 */
	public static final String SYSTEM_COMPONENT_CONFIG_FILEPATH = "system-component-definition.xml";

	/**
	 * 系统成功处理信息 {@value}
	 * @value 0000
	 */
	public final static String TRANSACTION_RESPONSE_CODE_SUCCESS = "0000";

	/**
	 * 未知系统错误代码 {@value}
	 * @value 9999
	 */
	public static final String TRANSACTION_RESPONSE_CODE_UNKOWN = "9999";

	/**
	 * 系统成功处理信息 {@value}
	 * @value 成功，系统处理正常。
	 */
	public final static String TRANSACTION_RESPONSE_MESSAGE_SUCCESS = "成功，系统处理正常。";

	/**
	 * 未知系统错误信息 {@value}
	 * @value 系统错误。
	 */
	public static final String TRANSACTION_RESPONSE_MESSAGE_UNKOWN = "系统错误。";

	/**
	 * 无效数据（如果插入数据库中的数字代表无意义的话，使用该常量）
	 */
	public final static Long G_LONG_DATA_INVALID = -1L;

	/**
	 * 无效数据（如果插入数据库中的数字代表无意义的话，使用该常量）
	 */
	public final static Integer G_INT_DATA_INVALID = -1;

	/**
	 * 升序关键字
	 */
	public final static String PAGE_ASC = "ASC";

	/**
	 * 降序关键字
	 */

	public final static String PAGE_DESC = "DESC";
	
	
	/**
	 * <pre>
	 * 用户会话Session标识名称 {@value}
	 * 值: CUR_USER_INFO
	 * </pre>
	 */
	public static final String PROFILE_SESSION_NAME = "CUR_USER_INFO";

	/**
	 * <pre>
	 * 用户历史Session标识名称 {@value}
	 * 值: USER_HISTORY_INFO
	 * </pre>
	 */
	public static final String HISTORY_SESSION_NAME = "USER_HISTORY_INFO";
	/**
	 * <pre>
	 * 当前页属性标识名称 {@value}
	 * 值: CUR_PAGE_INFO
	 * </pre>
	 */
	public static final String CURRENT_PAGE_INFO_NAME = "CUR_PAGE_INFO";


	
	// 系统成功后自动刷新key值
	public final static String URL_KEY = "urlkey";

	// 页面自动跳转时间间隔(单位：毫秒)
	public final static Long REDIRECT_TIME = 3000L;

	// 系统参数key值
	public final static String PARAMS_KEY = "paramskey";

	// 系统返回成功或错误信息的key值
	public final static String MESSAGE_KEY = "messagekey";

	// ajax错误信息key值
	public final static String AJAX_ERROR_KEY = "xmlHttpRequestError";

	// 系统返回错误代码的key值
	public final static String CODE_KEY = "codekey";

}
