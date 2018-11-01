/**
 * <pre>
 * Title: 		PropertiesConfigProvider.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.configuration.PropertiesConfigProvider
 * Author:		linriqing
 * Create:	 	2007-1-17 13:36:02
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * Properties文件配置装载的实现类
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-1-17
 */
public final class PropertiesConfigProvider implements ConfigProvider<String>
{
	private static final Log logger = LogFactory.getLog(PropertiesConfigProvider.class);

	private Map<String, String> configMap = new HashMap<String, String>();

	private final String filepath;

	/**
	 * 构造函数
	 * @param filepath
	 */
	public PropertiesConfigProvider(String filepath)
	{
		super();
		this.filepath = filepath;
		this.loadAllConfigs();
	}
	/**
	 * 构造函数
	 * @param filepath
	 */
	public PropertiesConfigProvider(String filepath,String charset)
	{
		super();
		this.filepath = filepath;
		this.loadAllConfigs(charset);
	}

	public Map<String, String> getAllConfig()
	{
		return this.configMap;
	}

	public String getConfigByPrimaryKey(String keyValue)
	{
		return (String) this.configMap.get(keyValue);
	}

	public void reloadAllConfig()
	{
		this.loadAllConfigs();
	}

	public void reloadConfigByPrimaryKey(String keyValue)
	{
		this.reloadAllConfig();
	}

	/**
	 * <pre>
	 * 重新通过XML文件装载所有配置信息
	 * </pre>
	 */
	private void loadAllConfigs()
	{
		this.configMap.clear();
		try
		{
			Properties properties = new Properties();
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(this.filepath));
			Set<Object> keySet = properties.keySet();
			for (Object object : keySet)
			{
				this.configMap.put(object.toString(), properties.getProperty(object.toString()));
			}
		}
		catch (IOException e)
		{
			logger.error("读取配置文件主键的时候异常.", e);
		}
	}

	/**
	 * <pre>
	 * 重新通过XML文件装载所有配置信息
	 * </pre>
	 */
	private void loadAllConfigs(String charset)
	{
		this.configMap.clear();
		try
		{
			Properties properties = new Properties();
			properties.load(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(this.filepath),"UTF-8"));
			Set<Object> keySet = properties.keySet();
			for (Object object : keySet)
			{
				this.configMap.put(object.toString(), properties.getProperty(object.toString()));
			}
		}
		catch (IOException e)
		{
			logger.error("读取配置文件主键的时候异常.", e);
		}
	}

}
