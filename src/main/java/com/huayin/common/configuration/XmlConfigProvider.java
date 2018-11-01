/**
 * <pre>
 * Title: 		XmlConfigProvider.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.configuration.XmlConfigProvider
 * Author:		linriqing
 * Create:	 	2007-1-17 12:41:02
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.configuration;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import com.huayin.common.util.ClassHelper;
import com.huayin.common.util.Dom4jHelper;

/**
 * <pre>
 * XML文件配置装载的实现类
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
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-1-17
 * @param <T> 泛型类型
 */
public final class XmlConfigProvider<T extends Config> implements ConfigProvider<T>
{
	private static final Log logger = LogFactory.getLog(XmlConfigProvider.class);

	private Map<String, T> configMap = new HashMap<String, T>();

	private final String filepath;

	private Class<T> type;

	/**
	 * 构造函数
	 * @param filepath
	 * @param type 类型
	 */
	protected XmlConfigProvider(String filepath, Class<T> type)
	{
		super();
		this.filepath = filepath;
		this.type = type;
		this.loadAllConfigs();
	}

	/**
	 * <pre>
	 * 将XML元素反序列化到对象中
	 * </pre>
	 * @param element XML元素
	 * @param object 反序列化后的对象
	 */
	private void deserialConfig(Element element, Object object)
	{
		if (object == null)
		{
			return;
		}

		Class<?> instanceClass = object.getClass();
		if (Collection.class.isAssignableFrom(instanceClass))
		{
			// (Class<T>) ((ParameterizedType) instanceClass.getGenericSuperclass()).getActualTypeArguments()[0];
			// Type genericType = field.getGenericType();
			// Class<?> componentType = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
			logger.warn("配置文件不支持集合类型数据,请使用数组形式.");
			//throw new Exception("配置文件不支持集合类型数据,请使用数组形式.");
			return;
		}
		else if (instanceClass.isArray())
		{
			String classname = instanceClass.getComponentType().getName();
			String shortName = ClassHelper.getShortTypeName(classname);
			List<?> list = element.elements(shortName);
			for (int i = 0; i < list.size(); i++)
			{
				Element subelement = (Element) list.get(i);
				try
				{
					Object config = (Object) instanceClass.getComponentType().newInstance();
					this.deserialConfig(subelement, config);
					Array.set(object, i, config);
				}
				catch (Exception e)
				{
					logger.warn("反序列化配置文件中的数组异常.");
					continue;
				}
			}
		}
		else
		{
			Class<?> type = instanceClass;
			List<Field> fieldList = new ArrayList<Field>();
			// 父类的方法
			while (!type.equals(Object.class))
			{
				Field[] fields = type.getDeclaredFields();
				fieldList.addAll(Arrays.asList(fields));
				type = type.getSuperclass();
			}

			for (Field field : fieldList)
			{
				field.setAccessible(true);
				String propertyName = field.getName();
				Class<?> paramType = field.getType();
				try
				{
					if (!Modifier.isFinal(field.getModifiers()))
					{
						if (paramType.isArray())
						{
							Class<?> elementType = paramType.getComponentType();
							if (elementType.equals(String.class))
							{
								List<?> list = element.elements(propertyName);

								if (list.size() == 0)
								{
									continue;
								}

								Object objArray = Array.newInstance(elementType, list.size());
								for (int j = 0; j < list.size(); j++)
								{
									Element strElement = (Element) list.get(j);
									Array.set(objArray, j, strElement.getText());
								}
								field.set(object, objArray);
							}
							else if (elementType.isPrimitive())
							{
								List<?> list = element.elements(propertyName);

								if (list.size() == 0)
								{
									continue;
								}

								Object objArray = Array.newInstance(elementType, list.size());
								if (Boolean.TYPE.equals(elementType))
								{
									for (int j = 0; j < list.size(); j++)
									{
										Element strElement = (Element) list.get(j);
										Array.set(objArray, j, Boolean.valueOf(strElement.getText()));
									}
									field.set(object, objArray);
								}
								else if (java.lang.Byte.TYPE.equals(elementType))
								{
									for (int j = 0; j < list.size(); j++)
									{
										Element strElement = (Element) list.get(j);
										Array.set(objArray, j, new Byte(Byte.parseByte(strElement.getText())));
									}
									field.set(object, objArray);
								}
								else if (Short.TYPE.equals(elementType))
								{
									for (int j = 0; j < list.size(); j++)
									{
										Element strElement = (Element) list.get(j);
										Array.set(objArray, j, new Short(Short.parseShort(strElement.getText())));
									}
									field.set(object, objArray);
								}
								else if (java.lang.Integer.TYPE.equals(elementType))
								{
									for (int j = 0; j < list.size(); j++)
									{
										Element strElement = (Element) list.get(j);
										Array.set(objArray, j, new Integer(Integer.parseInt(strElement.getText())));
									}
									field.set(object, objArray);
								}
								else if (java.lang.Long.TYPE.equals(elementType))
								{
									for (int j = 0; j < list.size(); j++)
									{
										Element strElement = (Element) list.get(j);
										Array.set(objArray, j, new Long(Long.parseLong(strElement.getText())));
									}
									field.set(object, objArray);
								}
								else if (Float.TYPE.equals(elementType))
								{
									for (int j = 0; j < list.size(); j++)
									{
										Element strElement = (Element) list.get(j);
										Array.set(objArray, j, new Float(Float.parseFloat(strElement.getText())));
									}
									field.set(object, objArray);
								}
								else if (java.lang.Double.TYPE.equals(elementType))
								{
									for (int j = 0; j < list.size(); j++)
									{
										Element strElement = (Element) list.get(j);
										Array.set(objArray, j, new Double(Double.parseDouble(strElement.getText())));
									}
									field.set(object, objArray);
								}
								else
								{
									logger.warn("配置文件不支持的类型数据.");
									throw new Exception("配置文件不支持的类型数据.");
								}
							}
							else if (Boolean.class.equals(paramType))
							{
								if (element.attribute(propertyName) != null)
								{
									field.set(object, Boolean.valueOf(element.attribute(propertyName).getText()));
								}
							}
							else if (java.lang.Byte.class.equals(paramType))
							{
								if (element.attribute(propertyName) != null)
								{
									field.set(object, Byte.valueOf(element.attribute(propertyName).getText()));
								}
							}
							else if (Number.class.isAssignableFrom(paramType))
							{
								if (element.attribute(propertyName) != null)
								{
									if (Short.class.equals(paramType))
									{
										field.set(object, Short.valueOf(element.attribute(propertyName).getText()));
									}
									else if (java.lang.Integer.class.equals(paramType))
									{
										field.set(object, Integer.valueOf(element.attribute(propertyName).getText()));
									}
									else if (java.lang.Long.class.equals(paramType))
									{
										field.set(object, Long.valueOf(element.attribute(propertyName).getText()));
									}
									else if (Float.class.equals(paramType))
									{
										field.set(object, Float.valueOf(element.attribute(propertyName).getText()));
									}
									else if (java.lang.Double.class.equals(paramType))
									{
										field.set(object, Double.valueOf(element.attribute(propertyName).getText()));
									}
									else
									{
										logger.warn("配置文件不支持的类型数据.");
										throw new Exception("配置文件不支持的类型数据.");
									}
								}
							}
							else if (Date.class.isAssignableFrom(elementType))
							{
								List<?> list = element.elements(propertyName);

								if (list.size() == 0)
								{
									continue;
								}

								Object objArray = Array.newInstance(elementType, list.size());
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								for (int j = 0; j < list.size(); j++)
								{
									Element strElement = (Element) list.get(j);
									Array.set(objArray, j, df.parse(strElement.getText()));
								}
								field.set(object, objArray);
							}
							else
							{
								String classname = elementType.getName();
								String shortName = ClassHelper.getShortTypeName(classname);
								if (element != null)
								{
									List<?> list = element.elements(shortName);
									if (list.size() == 0)
									{
										continue;
									}
									Object objArray = Array.newInstance(elementType, list.size());
									this.deserialConfig(element, objArray);
									field.set(object, objArray);
								}
							}
						}
						else if (Collection.class.isAssignableFrom(paramType))
						{
							logger.warn("配置文件不支持集合类型数据,请使用数组形式.");
							throw new Exception("配置文件不支持集合类型数据,请使用数组形式.");
						}
						else if (paramType.equals(String.class))
						{
							if (element != null)
							{
								if (element.attribute(propertyName) != null)
								{
									field.set(object, element.attribute(propertyName).getText());
								}
							}
						}
						else if (Number.class.isAssignableFrom(paramType))
						{
							if (element.attribute(propertyName) != null)
							{
								if (Short.class.equals(paramType))
								{
									field.set(object, Short.valueOf(element.attribute(propertyName).getText()));
								}
								else if (java.lang.Integer.class.equals(paramType))
								{
									field.set(object, Integer.valueOf(element.attribute(propertyName).getText()));
								}
								else if (java.lang.Long.class.equals(paramType))
								{
									field.set(object, Long.valueOf(element.attribute(propertyName).getText()));
								}
								else if (Float.class.equals(paramType))
								{
									field.set(object, Float.valueOf(element.attribute(propertyName).getText()));
								}
								else if (java.lang.Double.class.equals(paramType))
								{
									field.set(object, Double.valueOf(element.attribute(propertyName).getText()));
								}
							}
						}
						else if (Boolean.class.equals(paramType))
						{
							if (element.attribute(propertyName) != null)
							{
								field.set(object, Boolean.valueOf(element.attribute(propertyName).getText()));
							}
						}
						else if (java.lang.Byte.class.equals(paramType))
						{
							if (element.attribute(propertyName) != null)
							{
								field.set(object, Byte.valueOf(element.attribute(propertyName).getText()));
							}
						}
						else if (paramType.isPrimitive())
						{
							if (element.attribute(propertyName) != null)
							{
								if (Boolean.TYPE.equals(paramType))
								{
									field.set(object, Boolean.valueOf(element.attribute(propertyName).getText()));
								}
								else if (java.lang.Byte.TYPE.equals(paramType))
								{
									field.set(object, Byte.valueOf(element.attribute(propertyName).getText()));
								}
								else if (Short.TYPE.equals(paramType))
								{
									field.set(object, Short.valueOf(element.attribute(propertyName).getText()));
								}
								else if (java.lang.Integer.TYPE.equals(paramType))
								{
									field.set(object, Integer.valueOf(element.attribute(propertyName).getText()));
								}
								else if (java.lang.Long.TYPE.equals(paramType))
								{
									field.set(object, Long.valueOf(element.attribute(propertyName).getText()));
								}
								else if (Float.TYPE.equals(paramType))
								{
									field.set(object, Float.valueOf(element.attribute(propertyName).getText()));
								}
								else if (java.lang.Double.TYPE.equals(paramType))
								{
									field.set(object, Double.valueOf(element.attribute(propertyName).getText()));
								}
								else
								{
									logger.warn("配置文件不支持的类型数据.");
									throw new Exception("配置文件不支持的类型数据.");
								}
							}
						}
						else if (Date.class.isAssignableFrom(paramType))
						{
							if (element.attribute(propertyName) != null)
							{
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								field.set(object, df.parse(element.attribute(propertyName).getText()));
							}
						}
						else
						{
							if (Serializable.class.isAssignableFrom(paramType))
							{
								String classname = paramType.getName();
								String shortName = ClassHelper.getShortTypeName(classname);
								if (element != null)
								{
									Element subelement = element.element(shortName);
									try
									{
										Object config = (Object) paramType.newInstance();
										this.deserialConfig(subelement, config);
										field.set(object, config);
									}
									catch (Exception e)
									{
										logger.warn("反序列化配置文件中的对象异常.");
										continue;
									}
								}
							}
						}
					}
				}
				catch (Exception e)
				{
					logger.warn("反序列化配置文件中的属性{" + propertyName + "}异常");
					continue;
				}
			}
		}
	}

	public Map<String, T> getAllConfig()
	{
		return this.configMap;
	}

	public T getConfigByPrimaryKey(String keyValue)
	{
		return this.configMap.get(keyValue);
	}

	/**
	 * <pre>
	 * 重新通过XML文件装载所有配置信息
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	private void loadAllConfigs()
	{
		this.configMap.clear();
		Document doc = Dom4jHelper.getDocument(this.filepath);
		List<?> list = doc.getRootElement().elements();
		Object object = Array.newInstance(type, list.size());
		this.deserialConfig(doc.getRootElement(), object);
		for (int i = 0; i < list.size(); i++)
		{
			T config = (T) Array.get(object, i);
			if (config == null)
			{
				continue;
			}
			try
			{
				Attribute attribute = doc.getRootElement().attribute("key");
				String keystr = String.valueOf(i);
				if (attribute != null)
				{
					String keyName = attribute.getText();
					keyName = keyName.substring(0, 1).toUpperCase() + keyName.substring(1);
					String getName = "get" + keyName;
					Method getmethod = config.getClass().getMethod(getName);
					Object keyvalue = getmethod.invoke(config, new Object[] {});
					if (keyvalue != null)
					{
						keystr = String.valueOf(keyvalue);
					}
				}

				config.setKey(keystr);
				if (!this.configMap.containsKey(keystr))
				{
					this.configMap.put(keystr, config);
				}
			}
			catch (Exception e)
			{
				logger.error("读取配置文件主键的时候异常.", e);
			}
		}
	}

	/**
	 * <pre>
	 * 重新通过XML文件装载配置信息
	 * </pre>
	 */
	private void loadConfig(String keyValue)
	{
		if (this.configMap.containsKey(keyValue))
		{
			this.configMap.remove(keyValue);
		}

		try
		{
			T config = type.newInstance();
			Document doc = Dom4jHelper.getDocument(this.filepath);
			List<?> list = doc.getRootElement().elements();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();)
			{
				Element element = (Element) iter.next();
				if (element.attribute(doc.getRootElement().attribute("key").getText()).getText()
						.equalsIgnoreCase(keyValue))
				{
					this.deserialConfig(element, config);
					config.setKey(keyValue);
					this.configMap.put(keyValue, config);
				}
			}
		}
		catch (InstantiationException e)
		{
			logger.error("重新通过XML文件装载配置信息异常", e);
		}
		catch (IllegalAccessException e)
		{
			logger.error("重新通过XML文件装载配置信息异常", e);
		}
	}

	public void reloadAllConfig()
	{
		this.loadAllConfigs();
	}

	public void reloadConfigByPrimaryKey(String keyValue)
	{
		this.loadConfig(keyValue);
	}
}
