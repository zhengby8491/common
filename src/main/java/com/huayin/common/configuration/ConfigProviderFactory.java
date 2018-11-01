/**
 * <pre>
 * Title: 		ConfigProviderFactory.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2007-1-17 13:05:10
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.configuration;

import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.exception.SystemException;

/**
 * <pre>
 * 配置装载工厂
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-1-17
 */
public class ConfigProviderFactory
{
	/**
	 * 保存不同的实例
	 */
	private static HashMap<String, ConfigProvider<String>> configs = new HashMap<String, ConfigProvider<String>>();

	private static final Log logger = LogFactory.getLog(ConfigProviderFactory.class);

	/**
	 * 保存不同的实例
	 */
	private static HashMap<String, ConfigProvider<? extends Config>> objectConfigs = new HashMap<String, ConfigProvider<? extends Config>>();

	/**
	 * <pre>
	 * 取得指定数据表名对应的配置装载实现类唯一实例.
	 * 注意：不适合装载大批量数据的配置信息，超过100个配置信息建议独立实现。
	 * </pre>
	 * @param conn 数据源连接
	 * @param tableName 表名
	 * @param classname 配置信息类类型
	 * @return 配置装载实现类的实例
	 * @throws SQLException 数据源异常
	 */
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param <T>
	 * @param ds
	 * @param tableName
	 * @param classname
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Config> ConfigProvider<T> getInstance(DataSource ds, String tableName, String keyField,
			Class<T> classname) throws SQLException
	{
		String keyName = "db:" + tableName;
		if (objectConfigs.containsKey(keyName))
		{
			return (ConfigProvider<T>) objectConfigs.get(keyName);
		}
		else
		{
			ConfigProvider<T> parser = new DataBaseConfigProvider<T>(ds, tableName, keyField, classname);
			objectConfigs.put(keyName, parser);
			return parser;
		}
	}

	/**
	 * <pre>
	 * 取得指定配置文件对应的配置装载实现类唯一实例.
	 * 这里的参数url的格式是和类名的完全限定格式一致，
	 * 比如classpath下的com/huayin/hehe.xml文件
	 * 注意：不适合装载大批量数据的配置信息，超过100个配置信息建议独立实现。
	 * </pre>
	 * @param resourceuri 配置文件相对于类装载根目录的相对路径,前面不带"/"或"\"符号
	 * @return 配置装载实现类的实例
	 * @throws Exception
	 */
	public static ConfigProvider<String> getInstance(String resourceuri)
	{
		return getPropertiesInstance(resourceuri,null);
	
	}
	public static ConfigProvider<String> getPropertiesInstance(String resourceuri,String charset)
	{
		try
		{
			String keyName = "file:" + resourceuri;
			if (ConfigProviderFactory.configs.containsKey(keyName))
			{
				ConfigProvider<String> configProvider = ConfigProviderFactory.configs.get(keyName);
				logger.debug("load exit config:" + keyName + ", object:" + configProvider);
				return configProvider;
			}
			else
			{
				String extName = resourceuri.substring(resourceuri.lastIndexOf(".") + 1);
				if (extName.equalsIgnoreCase("properties"))
				{
					ConfigProvider<String> parser =null;
					if(charset!=null){

						parser = new PropertiesConfigProvider(resourceuri,charset);
					}else{

						parser = new PropertiesConfigProvider(resourceuri);
					}
					ConfigProviderFactory.configs.put(keyName, parser);
					logger.debug("load properties config:" + keyName + ", object:" + parser);
					return parser;
				}
				else
				{
					logger.error("不支持的配置文件类型[" + extName + "]");
					throw new UnsupportedOperationException("不支持的配置文件类型[" + extName + "]");
				}
			}
		}
		catch (Exception e)
		{
			throw new SystemException("加载properties配置文件[" + resourceuri + "]异常", e);
		}
	}
	/**
	 * <pre>
	 * 取得指定配置文件对应的配置装载实现类唯一实例.
	 * 这里的参数url的格式是和类名的完全限定格式一致，
	 * 比如classpath下的com/huayin/hehe.xml文件
	 * 注意：不适合装载大批量数据的配置信息，超过100个配置信息建议独立实现。
	 * </pre>
	 * @param resourceuri 配置文件相对于类装载根目录的相对路径,前面不带"/"或"\"符号
	 * @param classname 配置信息类类型
	 * @return 配置装载实现类的实例
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Config> ConfigProvider<T> getInstance(String resourceuri, Class<T> classname)
	{
		try
		{
			String keyName = "xml:" + resourceuri;
			if (ConfigProviderFactory.configs.containsKey(keyName))
			{
				ConfigProvider<T> parser = (ConfigProvider<T>) ConfigProviderFactory.objectConfigs.get(keyName);
				logger.debug("load exit config:" + keyName + ", object:" + parser);
				return parser;
			}
			else
			{
				String extName = resourceuri.substring(resourceuri.lastIndexOf(".") + 1);
				if (extName.equalsIgnoreCase("xml"))
				{
					ConfigProvider<T> parser = new XmlConfigProvider<T>(resourceuri, classname);
					ConfigProviderFactory.objectConfigs.put(keyName, parser);
					logger.debug("load xml config:" + keyName + ", object:" + parser);
					return parser;
				}
				else
				{
					logger.error("不支持的配置文件类型[" + extName + "]");
					throw new UnsupportedOperationException("不支持的配置文件类型[" + extName + "]");
				}
			}
		}
		catch (Exception e)
		{
			throw new SystemException("加载Xml配置文件[" + resourceuri + "]异常", e);
		}
	}
}
