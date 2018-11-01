/**
 * <pre>
 * Title: 		ObjectHelper.java
 * Project: 	AnteAgent
 * Type:		com.huayin.util.ObjectHelper
 * Author:		linriqing
 * Create:	 	2006-10-16 下午06:27:06
 * Copyright: 	Copyright (c) 2006
 * Company:
 * <pre>
 */
package com.huayin.common.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.beans.DirectFieldAccessor;

/**
 * <pre>
 * 对象帮助类
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-10-16
 */
public class ObjectHelper implements Serializable
{
	static
	{
		ConvertUtils.register(new Converter()
		{
		    @SuppressWarnings("unchecked")
			public Object convert(@SuppressWarnings("rawtypes") Class type, Object value){  
		        if(value == null){  
		            return null;  
		        }else if(type == Timestamp.class){  
		            return convertToDate(type, value, "yyyy-MM-dd HH:mm:ss");  
		        }else if(type == Date.class){  
		            return convertToDate(type, value, "yyyy-MM-dd");  
		        }else if(type == String.class){  
		            return convertToString(type, value);  
		        }  
		  
		        throw new ConversionException("不能转换 " + value.getClass().getName() + " 为 " + type.getName());  
		    }
		   
			protected Object convertToDate(@SuppressWarnings("rawtypes") Class type, Object value, String pattern) {  
		        SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
		        if(value instanceof String){  
		            try{  
		                if(StringUtils.isEmpty(value.toString())){  
		                    return null;  
		                }  
		                Date date = sdf.parse((String) value);  
		                if(type.equals(Timestamp.class)){  
		                    return new Timestamp(date.getTime());  
		                }  
		                return date;  
		            }catch(Exception pe){  
		                return null;  
		            }  
		        }else if(value instanceof Date){  
		            return value;  
		        }  
		          
		        throw new ConversionException("不能转换 " + value.getClass().getName() + " 为 " + type.getName());  
		    }  
		  
		    protected Object convertToString(@SuppressWarnings("rawtypes") Class type, Object value) {  
		        if(value instanceof Date){  
		            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		              
		            if (value instanceof Timestamp) {  
		                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		            }   
		              
		            try{  
		                return sdf.format(value);  
		            }catch(Exception e){  
		                throw new ConversionException("日期转换为字符串时出错！");  
		            }  
		        }else{  
		            return value.toString();  
		        }  
		    }  
		}, java.util.Date.class);
	}
	private static final String ADVISED_FIELD_NAME = "advised";

	private static final String CLASS_JDK_DYNAMIC_AOP_PROXY = "org.springframework.aop.framework.JdkDynamicAopProxy";

	private static final long serialVersionUID = 5855066687351994401L;

	/**
	 * <pre>
	 * 以二进制方式克隆对象
	 * </pre>
	 * @param src 被克隆的对象
	 * @return 克隆的对象
	 */
	@SuppressWarnings("unchecked")
	public static final <T extends Serializable> T byteClone(T src)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(src);
			out.close();
			ByteArrayInputStream bin = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bin);
			Object clone = in.readObject();
			in.close();
			return (T) clone;
		}
		catch (ClassNotFoundException e)
		{
			throw new InternalError(e.toString());
		}
		catch (StreamCorruptedException e)
		{
			throw new InternalError(e.toString());
		}
		catch (IOException e)
		{
			throw new InternalError(e.toString());
		}
	}

	public static final Class<?> getComponentType(Class<?> object) throws IntrospectionException
	{
		if (object.isArray())
		{
			return object.getComponentType();
		}
		else if (Collection.class.isAssignableFrom(object))
		{
			Type genericType = object.getGenericSuperclass();
			return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
		}
		else if (Map.class.isAssignableFrom(object))
		{
			// TODO
			// PropertyDescriptor pd = new PropertyDescriptor("dynamicProperty", ObjectHelper.class);
			// ObjectHelper bean = new ObjectHelper();
			// PropertyEditor createPropertyEditor = pd.createPropertyEditor(bean);
			// createPropertyEditor.
			Type genericType = object.getGenericSuperclass();
			return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[1];
		}
		else
		{
			return object;
		}
	}

	/**
	 * <pre>
	 * 根据实例和属性名获取属性值
	 * </pre>
	 * @param bean 实例
	 * @param property 属性名
	 * @return 属性值
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static final Object getPropertyValue(Object bean, String property)
			throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		PropertyDescriptor pd = new PropertyDescriptor(property, bean.getClass());
		Method getMethod = pd.getReadMethod();
		return getMethod.invoke(bean);
	}

	public static Class<?> getTargetClass(Object candidate)
	{
		if (!org.springframework.aop.support.AopUtils.isJdkDynamicProxy(candidate))
		{
			return org.springframework.aop.support.AopUtils.getTargetClass(candidate);
		}

		return getTargetClassFromJdkDynamicAopProxy(candidate);
	}

	/**
	 * <pre>
	 * int数组转换为List
	 * </pre>
	 * @param a int数组
	 * @return List
	 */
	public static List<Integer> intArrayAsList(final int[] a)
	{
		if (a == null)
		{
			return new ArrayList<Integer>();
		}
		else
		{
			return new AbstractList<Integer>()
			{
				@Override
				public Integer get(int index)
				{
					return new Integer(a[index]);
				}

				// 排序所用到的方法
				public Integer set(int index, Integer o)
				{
					int oldVal = a[index];
					a[index] = ((Integer) o).intValue();
					return new Integer(oldVal);
				}

				@Override
				public int size()
				{
					return a.length;
				}
			};
		}
	}

	/**
	 * <pre>
	 * 读取对象属性值为properties文件格式字符串
	 * </pre>
	 * @param src 源对象
	 * @return properties文件格式字符串
	 */
	public static final String toPropertiesString(Object src)
	{
		return toPropertiesStringInner(src, new Stack<Object>());
	}

	/**
	 * <pre>
	 * 读取对象属性值为properties文件格式字符串
	 * </pre>
	 * @param src 源对象
	 * @return properties文件格式字符串
	 */
	public static final String toJson(Object src)
	{
		return toJsonInner(src, new Stack<Object>());
	}

	private static Class<?> getTargetClassFromJdkDynamicAopProxy(Object candidate)
	{
		try
		{
			InvocationHandler invocationHandler = Proxy.getInvocationHandler(candidate);
			if (!invocationHandler.getClass().getName().equals(CLASS_JDK_DYNAMIC_AOP_PROXY))
			{
				// 在目前的spring版本，这处永远不会执行，除非以后spring的dynamic proxy实现变掉
				return candidate.getClass();
			}
			AdvisedSupport advised = (AdvisedSupport) new DirectFieldAccessor(invocationHandler)
					.getPropertyValue(ADVISED_FIELD_NAME);
			Class<?> targetClass = advised.getTargetClass();
			if (Proxy.isProxyClass(targetClass))
			{
				// 目标类还是代理，递归
				Object target = advised.getTargetSource().getTarget();
				return getTargetClassFromJdkDynamicAopProxy(target);
			}
			return targetClass;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return candidate.getClass();
		}
	}

	/**
	 * <pre>
	 * 读取对象属性值为properties文件格式字符串
	 * </pre>
	 * @param src 源对象
	 * @return properties文件格式字符串
	 */
	private static final String toPropertiesStringInner(Object src, Stack<Object> set)
	{
		if (src != null)
		{
			for (Object object : set)
			{
				if ((object.getClass().equals(src.getClass())) && (object.equals(src)))
				{
					return "{nested reference object:" + object + "}";
				}
			}
		}
		set.push(src);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer buffer = new StringBuffer();
		try
		{
			Class<? extends Object> srcType = src.getClass();
			if (srcType.isArray())
			{
				buffer.append("[");
				int length = Array.getLength(src);
				for (int i = 0; i < length; i++)
				{
					Object object = Array.get(src, i);
					if (object != null)
					{
						Class<?> valueType = object.getClass();
						if (ClassHelper.isPrimitive4String(valueType))
						{
							buffer.append("\"");
							buffer.append(String.valueOf(object));
							buffer.append("\",");
						}
						else if (ClassHelper.isPrimitive4Number(valueType))
						{
							if (Character.TYPE.equals(valueType))
							{
								buffer.append("\"");
								buffer.append(String.valueOf(object));
								buffer.append("\",");
							}
							else
							{
								buffer.append(String.valueOf(object));
								buffer.append(",");
							}
						}
						else if (ClassHelper.isPrimitive4Date(valueType))
						{
							buffer.append("\"");
							buffer.append(sdf.format((Date) object));
							buffer.append("\",");
						}
						else if (valueType.isEnum())
						{
							buffer.append("\"");
							buffer.append(src.toString());
							buffer.append("\",");
						}
						else
						{
							buffer.append(toPropertiesStringInner(object, set));
							buffer.append(",");
						}
					}
					else
					{
						buffer.append(object);
						buffer.append(",");
					}
				}
				if (buffer.toString().endsWith(","))
				{
					buffer.deleteCharAt(buffer.length() - 1);
				}
				buffer.append("]");
			}
			else if (Collection.class.isAssignableFrom(srcType))
			{
				buffer.append("[");
				final Collection<?> optionCollection = (Collection<?>) src;
				for (Object object : optionCollection)
				{
					if (object != null)
					{
						Class<?> valueType = object.getClass();

						if (ClassHelper.isPrimitive4String(valueType))
						{
							buffer.append("\"");
							buffer.append(String.valueOf(object));
							buffer.append("\",");
						}
						else if (ClassHelper.isPrimitive4Number(valueType))
						{
							if (Character.TYPE.equals(valueType))
							{
								buffer.append("\"");
								buffer.append(String.valueOf(object));
								buffer.append("\",");
							}
							else
							{
								buffer.append(String.valueOf(object));
								buffer.append(",");
							}
						}
						else if (ClassHelper.isPrimitive4Date(valueType))
						{
							buffer.append("\"");
							buffer.append(sdf.format((Date) object));
							buffer.append("\",");
						}
						else if (valueType.isEnum())
						{
							buffer.append("\"");
							buffer.append(src.toString());
							buffer.append("\",");
						}
						else
						{
							buffer.append(toPropertiesStringInner(object, set));
							buffer.append(",");
						}
					}
					else
					{
						buffer.append(object);
						buffer.append(",");
					}
				}
				if (buffer.toString().endsWith(","))
				{
					buffer.deleteCharAt(buffer.length() - 1);
				}
				buffer.append("]");
			}
			else if (Map.class.isAssignableFrom(srcType))
			{
				buffer.append("{");
				final Map<?, ?> optionMap = (Map<?, ?>) src;
				
				Iterator<?> it = optionMap.keySet().iterator();
				while(it.hasNext())
				{
					Object next = it.next();
					Object elementObject = optionMap.get(next);
					if (elementObject != null)
					{
						Class<?> valueType = elementObject.getClass();

						if (ClassHelper.isPrimitive4Number(valueType))
						{
							if (Character.TYPE.equals(valueType))
							{
								buffer.append(String.valueOf(next)).append("=\"").append(String.valueOf(elementObject));
								buffer.append("\",");
							}
							else
							{
								buffer.append(String.valueOf(next)).append("=").append(String.valueOf(elementObject));
								buffer.append(",");
							}
						}
						else if (ClassHelper.isPrimitive4String(valueType))
						{
							buffer.append(String.valueOf(next)).append("=\"").append(String.valueOf(elementObject));
							buffer.append("\",");
						}
						else if (ClassHelper.isPrimitive4Date(valueType))
						{
							buffer.append(String.valueOf(next));
							buffer.append("=\"");
							buffer.append(sdf.format((Date) elementObject));
							buffer.append("\",");
						}
						else if (valueType.isEnum())
						{
							buffer.append(String.valueOf(next)).append("=\"");
							buffer.append(src.toString());
							buffer.append("\",");
						}
						else
						{
							buffer.append(String.valueOf(next)).append("=").append("");
							buffer.append(toPropertiesStringInner(elementObject, set));
							buffer.append(",");
						}
					}
					else
					{
						buffer.append(next).append("=").append(elementObject);
						buffer.append(",");
					}
				}
				if (buffer.toString().endsWith(","))
				{
					buffer.deleteCharAt(buffer.length() - 1);
				}
				buffer.append("}");
			}
			else if (srcType.isEnum())
			{
				buffer.append("{");
				buffer.append(src.toString());
				buffer.append("}");
			}
			else
			{
				ArrayList<Field> list = new ArrayList<Field>();
				Class<?> instanceClass = src.getClass();
				buffer.append("{");
				// 父类的方法
				while (!instanceClass.equals(Object.class))
				{
					Field[] fields = instanceClass.getDeclaredFields();
					for (Field field : fields)
					{
						if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))
						{
							continue;
						}
						else
						{
							list.add(field);
						}
					}
					instanceClass = instanceClass.getSuperclass();
				}

				// 遍历调用
				for (Iterator<Field> iter = list.iterator(); iter.hasNext();)
				{
					try
					{
						Field field = (Field) iter.next();
						Class<?> fieldType = field.getType();
						String property = field.getName();

						// 获取get方法
						Method getMethod = null;
						String getMethodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
						try
						{
							getMethod = field.getDeclaringClass().getMethod(getMethodName, new Class[] {});
						}
						catch (Exception e)
						{
							continue;
						}
						if (getMethod == null)
						{
							if (fieldType == boolean.class)
							{
								getMethodName = "is" + property.substring(0, 1).toUpperCase() + property.substring(1);
								try
								{
									getMethod = field.getDeclaringClass().getMethod(getMethodName, new Class[] {});
								}
								catch (Exception e)
								{
									continue;
								}
							}
						}
						if (getMethod != null)
						{
							Object fieldValue = getMethod.invoke(src);
							if (fieldValue != null)
							{
								Class<?> valueType = fieldValue.getClass();

								if (ClassHelper.isPrimitive4Number(fieldType)
										|| ClassHelper.isPrimitive4Number(valueType))
								{
									if (Character.TYPE.equals(fieldType))
									{
										buffer.append(property);
										buffer.append("=\"");
										buffer.append(String.valueOf(fieldValue));
										buffer.append("\",");
									}
									else
									{
										buffer.append(property);
										buffer.append("=");
										buffer.append(String.valueOf(fieldValue));
										buffer.append(",");
									}
								}
								else if (ClassHelper.isPrimitive4String(fieldType)
										|| ClassHelper.isPrimitive4String(valueType))
								{
									buffer.append(property);
									buffer.append("=\"");
									buffer.append(String.valueOf(fieldValue));
									buffer.append("\",");
								}
								else if (ClassHelper.isPrimitive4Date(fieldType)
										|| ClassHelper.isPrimitive4Date(valueType))
								{
									buffer.append(property);
									buffer.append("=\"");
									buffer.append(sdf.format((Date) fieldValue));
									buffer.append("\",");
								}
								else if (fieldType.isEnum() || valueType.isEnum())
								{
									buffer.append(property).append("=\"");
									buffer.append(fieldValue.toString());
									buffer.append("\",");
								}
								else if (fieldType.isArray() || valueType.isArray())
								{
									buffer.append(property);
									buffer.append("=");
									buffer.append(toPropertiesStringInner(fieldValue, set));
									buffer.append(",");
								}
								else if (Collection.class.isAssignableFrom(fieldType)
										|| Collection.class.isAssignableFrom(valueType))
								{
									buffer.append(property);
									buffer.append("=");
									buffer.append(toPropertiesStringInner(fieldValue, set));
									buffer.append(",");
								}
								else if (Map.class.isAssignableFrom(fieldType) || Map.class.isAssignableFrom(valueType))
								{
									buffer.append(property);
									buffer.append("=");
									buffer.append(toPropertiesStringInner(fieldValue, set));
									buffer.append(",");
								}
								else
								{
									buffer.append(property);
									buffer.append("=");
									buffer.append(toPropertiesStringInner(fieldValue, set));
									buffer.append(",");
								}
							}
						}
					}
					catch (Exception e)
					{
						continue;
					}
				}
				if (buffer.toString().endsWith(","))
				{
					buffer.deleteCharAt(buffer.length() - 1);
				}
				buffer.append("}");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			buffer.append("fetch properties value cause exception:" + e.getMessage());
		}
		set.pop();
		return buffer.toString();
	}

	/**
	 * <pre>
	 * 读取对象属性值为Json文件格式字符串
	 * </pre>
	 * @param src 源对象
	 * @return Json文件格式字符串
	 */
	private static final String toJsonInner(Object src, Stack<Object> set)
	{
		if (src != null)
		{
			for (Object object : set)
			{
				if ((object.getClass().equals(src.getClass())) && (object.equals(src)))
				{
					return "{nested reference object:" + object + "}";
				}
			}
		}
		set.push(src);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer buffer = new StringBuffer();
		try
		{
			Class<? extends Object> srcType = src.getClass();
			if (srcType.isArray())
			{
				buffer.append("[");
				int length = Array.getLength(src);
				for (int i = 0; i < length; i++)
				{
					Object object = Array.get(src, i);
					if (object != null)
					{
						Class<?> valueType = object.getClass();
						if (ClassHelper.isPrimitive4String(valueType))
						{
							buffer.append("\"");
							buffer.append(String.valueOf(object));
							buffer.append("\",");
						}
						else if (ClassHelper.isPrimitive4Number(valueType))
						{
							if (Character.TYPE.equals(valueType))
							{
								buffer.append("\"");
								buffer.append(String.valueOf(object));
								buffer.append("\",");
							}
							else
							{
								buffer.append(String.valueOf(object));
								buffer.append(",");
							}
						}
						else if (ClassHelper.isPrimitive4Date(valueType))
						{
							buffer.append("\"");
							buffer.append(sdf.format((Date) object));
							buffer.append("\",");
						}
						else if (valueType.isEnum())
						{
							buffer.append("\"");
							buffer.append(src.toString());
							buffer.append("\",");
						}
						else
						{
							buffer.append(toJsonInner(object, set));
							buffer.append(",");
						}
					}
					else
					{
						buffer.append(object);
						buffer.append(",");
					}
				}
				if (buffer.toString().endsWith(","))
				{
					buffer.deleteCharAt(buffer.length() - 1);
				}
				buffer.append("]");
			}
			else if (Collection.class.isAssignableFrom(srcType))
			{
				buffer.append("[");
				final Collection<?> optionCollection = (Collection<?>) src;
				for (Object object : optionCollection)
				{
					if (object != null)
					{
						Class<?> valueType = object.getClass();

						if (ClassHelper.isPrimitive4String(valueType))
						{
							buffer.append("\"");
							buffer.append(String.valueOf(object));
							buffer.append("\",");
						}
						else if (ClassHelper.isPrimitive4Number(valueType))
						{
							if (Character.TYPE.equals(valueType))
							{
								buffer.append("\"");
								buffer.append(String.valueOf(object));
								buffer.append("\",");
							}
							else
							{
								buffer.append(String.valueOf(object));
								buffer.append(",");
							}
						}
						else if (ClassHelper.isPrimitive4Date(valueType))
						{
							buffer.append("\"");
							buffer.append(sdf.format((Date) object));
							buffer.append("\",");
						}
						else if (valueType.isEnum())
						{
							buffer.append("\"");
							buffer.append(src.toString());
							buffer.append("\",");
						}
						else
						{
							buffer.append(toJsonInner(object, set));
							buffer.append(",");
						}
					}
					else
					{
						buffer.append(object);
						buffer.append(",");
					}
				}
				if (buffer.toString().endsWith(","))
				{
					buffer.deleteCharAt(buffer.length() - 1);
				}
				buffer.append("]");
			}
			else if (Map.class.isAssignableFrom(srcType))
			{
				buffer.append("{");
				final Map<?, ?> optionMap = (Map<?, ?>) src;
				Iterator<?> it = optionMap.keySet().iterator();
				while(it.hasNext())
				{
					Object next = it.next();
					Object elementObject = optionMap.get(next);
					if (elementObject != null)
					{
						Class<?> valueType = elementObject.getClass();

						if (ClassHelper.isPrimitive4Number(valueType))
						{
							if (Character.TYPE.equals(valueType))
							{
								buffer.append("\"").append(String.valueOf(next)).append("\"").append(":\"")
										.append(String.valueOf(elementObject));
								buffer.append("\",");
							}
							else
							{
								buffer.append("\"").append(String.valueOf(next)).append("\"").append(":")
										.append(String.valueOf(elementObject));
								buffer.append(",");
							}
						}
						else if (ClassHelper.isPrimitive4String(valueType))
						{
							buffer.append("\"").append(String.valueOf(next)).append("\"").append(":\"")
									.append(String.valueOf(elementObject));
							buffer.append("\",");
						}
						else if (ClassHelper.isPrimitive4Date(valueType))
						{
							buffer.append("\"").append(String.valueOf(next)).append("\"");
							buffer.append(":\"");
							buffer.append(sdf.format((Date) elementObject));
							buffer.append("\",");
						}
						else if (valueType.isEnum())
						{
							buffer.append("\"").append(String.valueOf(next)).append("\"").append(":\"");
							buffer.append(src.toString());
							buffer.append("\",");
						}
						else
						{
							buffer.append("\"").append(String.valueOf(next)).append("\"").append(":").append("");
							buffer.append(toJsonInner(elementObject, set));
							buffer.append(",");
						}
					}
					else
					{
						buffer.append("\"").append(next).append("\"").append(":").append(elementObject);
						buffer.append(",");
					}
				}
				if (buffer.toString().endsWith(","))
				{
					buffer.deleteCharAt(buffer.length() - 1);
				}
				buffer.append("}");
			}
			else if (srcType.isEnum())
			{
				buffer.append("{");
				buffer.append(src.toString());
				buffer.append("}");
			}
			else if (srcType.getName().equalsIgnoreCase(java.lang.Class.class.getName()))
			{
				buffer.append("{");
				buffer.append(src.toString());
				buffer.append("}");
			}
			else
			{
				ArrayList<Field> list = new ArrayList<Field>();
				Class<?> instanceClass = src.getClass();
				buffer.append("{");
				// 父类的方法
				while (!instanceClass.equals(Object.class))
				{
					Field[] fields = instanceClass.getDeclaredFields();
					for (Field field : fields)
					{
						if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))
						{
							continue;
						}
						else
						{
							list.add(field);
						}
					}
					instanceClass = instanceClass.getSuperclass();
				}

				// 遍历调用
				for (Iterator<Field> iter = list.iterator(); iter.hasNext();)
				{
					try
					{
						Field field = (Field) iter.next();
						Class<?> fieldType = field.getType();
						String property = field.getName();
						// 获取get方法
						Method getMethod = null;
						String getMethodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
						try
						{
							getMethod = field.getDeclaringClass().getMethod(getMethodName, new Class[] {});
						}
						catch (Exception e)
						{
							continue;
						}
						if (getMethod == null)
						{
							if (fieldType == boolean.class)
							{
								getMethodName = "is" + property.substring(0, 1).toUpperCase() + property.substring(1);
								try
								{
									getMethod = field.getDeclaringClass().getMethod(getMethodName, new Class[] {});
								}
								catch (Exception e)
								{
									continue;
								}
							}
						}
						if (getMethod != null)
						{
							Object fieldValue = getMethod.invoke(src);
							if (fieldValue != null)
							{
								Class<?> valueType = fieldValue.getClass();

								if (ClassHelper.isPrimitive4Number(fieldType)
										|| ClassHelper.isPrimitive4Number(valueType))
								{
									if (Character.TYPE.equals(fieldType))
									{
										buffer.append("\"").append(property).append("\"");
										buffer.append(":\"");
										buffer.append(String.valueOf(fieldValue));
										buffer.append("\",");
									}
									else
									{
										buffer.append("\"").append(property).append("\"");
										buffer.append(":");
										buffer.append(String.valueOf(fieldValue));
										buffer.append(",");
									}
								}
								else if (ClassHelper.isPrimitive4String(fieldType)
										|| ClassHelper.isPrimitive4String(valueType))
								{
									buffer.append("\"").append(property).append("\"");
									buffer.append(":\"");
									buffer.append(String.valueOf(fieldValue));
									buffer.append("\",");
								}
								else if (ClassHelper.isPrimitive4Date(fieldType)
										|| ClassHelper.isPrimitive4Date(valueType))
								{
									buffer.append("\"").append(property).append("\"");
									buffer.append(":\"");
									buffer.append(sdf.format((Date) fieldValue));
									buffer.append("\",");
								}
								else if (fieldType.isEnum() || valueType.isEnum())
								{
									buffer.append("\"").append(property).append("\"").append(":\"");
									buffer.append(fieldValue.toString());
									buffer.append("\",");
								}
								else if (fieldType.isArray() || valueType.isArray())
								{
									buffer.append("\"").append(property).append("\"");
									buffer.append(":");
									buffer.append(toJsonInner(fieldValue, set));
									buffer.append(",");
								}
								else if (Collection.class.isAssignableFrom(fieldType)
										|| Collection.class.isAssignableFrom(valueType))
								{
									buffer.append("\"").append(property).append("\"");
									buffer.append(":");
									buffer.append(toJsonInner(fieldValue, set));
									buffer.append(",");
								}
								else if (Map.class.isAssignableFrom(fieldType) || Map.class.isAssignableFrom(valueType))
								{
									buffer.append("\"").append(property).append("\"");
									buffer.append(":");
									buffer.append(toJsonInner(fieldValue, set));
									buffer.append(",");
								}
								else
								{
									buffer.append("\"").append(property).append("\"");
									buffer.append(":");
									buffer.append(toJsonInner(fieldValue, set));
									buffer.append(",");
								}
							}
						}
					}
					catch (Exception e)
					{
						continue;
					}
				}
				if (buffer.toString().endsWith(","))
				{
					buffer.deleteCharAt(buffer.length() - 1);
				}
				buffer.append("}");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			buffer.append("fetch properties value cause exception:" + e.getMessage());
		}
		set.pop();
		return buffer.toString();
	}

	public static <K> K mapToObject(Map<String, Object> map, Class<K> beanClass) throws Exception
	{
		if (map == null)
			return null;

		K k = beanClass.newInstance();
	
		org.apache.commons.beanutils.BeanUtils.populate(k, map);
		return k;
	}

	public static Map<?, ?> objectToMap(Object obj)
	{
		if (obj == null)
			return null;

		return new org.apache.commons.beanutils.BeanMap(obj);
	}

}
