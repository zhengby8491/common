/**
 * <pre>
 * Title: 		ConfigProvider.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.configuration.ConfigProvider
 * Author:		linriqing
 * Create:	 	2007-1-17 10:23:18
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.configuration;

import java.util.Map;

/**
 * <pre>
 * 配置装载接口
 * 一般来说，配置可以存放在数据库、XML配置文件、Properties文件，实现读取这些配置的都可以继承此类。
 * 支持以下的基本数据类型:
 * java.lang.Boolean
 * java.lang.Short
 * java.lang.Byte
 * java.lang.Integer
 * java.lang.Long
 * java.lang.Float
 * java.lang.Double
 *
 * 引用数据类型类型支持继承自com.huayin.util.configuration.Config的派生类.
 * 不支持集合数据类型,即继承自java.util.Collection的派生类.
 *
 * 注意：此类不适合装载大批量数据的配置信息，超过100个配置信息建议独立实现。
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-1-17
 */
public interface ConfigProvider<T>
{
	/**
	 * <pre>
	 * 读取所有配置信息
	 * </pre>
	 * @return 配置信息映射表
	 */
	Map<String, T> getAllConfig();

	/**
	 * <pre>
	 * 重新装载所有配置信息映射表
	 * </pre>
	 */
	void reloadAllConfig();

	/**
	 * <pre>
	 * 通过主键加载配置信息
	 * </pre>
	 * @param keyValue 主键的值
	 * @return 配置信息对象
	 */
	T getConfigByPrimaryKey(String keyValue);

	/**
	 * <pre>
	 * 重新装载指定的配置信息映射表
	 * </pre>
	 * @param keyValue 主键的值
	 */
	void reloadConfigByPrimaryKey(String keyValue);

}
