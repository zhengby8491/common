/**
 * <pre>
 * Title: 		DataBaseConfigProvider.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2007-1-17 13:38:13
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.configuration;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

/**
 * <pre>
 * 数据源配置装载的实现类
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-1-17
 */
public class DataBaseConfigProvider<T> implements ConfigProvider<T>
{
	private DataSource ds;

	private String tableName;

	private Map<String, T> configMap = new HashMap<String, T>();

	private Map<String, PropertyDescriptor> methodMap = new HashMap<String, PropertyDescriptor>();

	private String keyField;

	private Class<T> clazz;

	/**
	 * 构造函数
	 */
	public DataBaseConfigProvider(DataSource ds, String tableName, String keyField, Class<T> type) throws SQLException
	{
		super();
		this.ds = ds;
		this.tableName = tableName;
		this.keyField = keyField;
		this.clazz = type;
		this.loadAllConfig();
	}

	public T getConfigByPrimaryKey(String keyValue)
	{
		return this.configMap.get(keyValue);
	}

	public void reloadAllConfig()
	{
		loadAllConfig();
	}

	private void loadAllConfig()
	{
		try
		{
			// TypeVariable<?>[] typeParameters = this.getClass().getTypeParameters();
			// Class<T> clazz = (Class<T>) typeParameters[0].getBounds()[0];
			if (methodMap.size() == 0)
			{
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields)
				{
					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
					methodMap.put(field.getName(), pd);
				}
			}

			Connection connection = null;
			PreparedStatement prepareStatement = null;
			ResultSet executeQuery = null;
			try
			{
				connection = this.ds.getConnection();
				prepareStatement = connection.prepareStatement("SELECT * FROM " + this.tableName);
				executeQuery = prepareStatement.executeQuery();
				while (executeQuery.next())
				{
					T target = clazz.newInstance();
					for (Entry<String, PropertyDescriptor> entry : methodMap.entrySet())
					{
						PropertyDescriptor pd = entry.getValue();
						Class<?> propertyType = pd.getPropertyType();
						if (propertyType.equals(String.class))
						{
							pd.getWriteMethod().invoke(target, executeQuery.getString(pd.getName()));
						}
						else if (propertyType.isPrimitive())
						{
							if (Boolean.TYPE.equals(propertyType))
							{
								pd.getWriteMethod().invoke(target, executeQuery.getBoolean(pd.getName()));
							}
							else if (java.lang.Byte.TYPE.equals(propertyType))
							{
								pd.getWriteMethod().invoke(target, executeQuery.getByte(pd.getName()));
							}
							else if (Short.TYPE.equals(propertyType))
							{
								pd.getWriteMethod().invoke(target, executeQuery.getShort(pd.getName()));
							}
							else if (java.lang.Integer.TYPE.equals(propertyType))
							{
								pd.getWriteMethod().invoke(target, executeQuery.getInt(pd.getName()));
							}
							else if (java.lang.Long.TYPE.equals(propertyType))
							{
								pd.getWriteMethod().invoke(target, executeQuery.getLong(pd.getName()));
							}
							else if (Float.TYPE.equals(propertyType))
							{
								pd.getWriteMethod().invoke(target, executeQuery.getFloat(pd.getName()));
							}
							else if (java.lang.Double.TYPE.equals(propertyType))
							{
								pd.getWriteMethod().invoke(target, executeQuery.getDouble(pd.getName()));
							}
							else
							{
								pd.getWriteMethod().invoke(target, executeQuery.getString(pd.getName()));
							}
						}
						else if (Date.class.isAssignableFrom(propertyType))
						{
							pd.getWriteMethod().invoke(target, executeQuery.getDate(pd.getName()));
						}
						else
						{
							pd.getWriteMethod().invoke(target, executeQuery.getObject(pd.getName()));
						}
					}
					configMap.put(executeQuery.getString(this.keyField), target);
				}
			}
			finally
			{
				if (executeQuery != null)
				{
					executeQuery.close();
				}
				if (prepareStatement != null)
				{
					prepareStatement.close();
				}
				if (connection != null)
				{
					connection.close();
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public void reloadConfigByPrimaryKey(String keyValue)
	{
	}

	@Override
	public Map<String, T> getAllConfig()
	{
		return this.configMap;
	}
}
