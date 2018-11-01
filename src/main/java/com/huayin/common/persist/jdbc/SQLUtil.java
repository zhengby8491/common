/**
 * <pre>
 * Title: 		SQLUtil.java
 * Author:		zhaojitao
 * Create:	 	2007-6-7 下午07:03:48
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.persist.jdbc;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.huayin.common.configuration.ConfigProvider;
import com.huayin.common.configuration.ConfigProviderFactory;
import com.huayin.common.exception.DataOperationException;
import com.huayin.common.persist.ClientContextHolder;

/**
 * <pre>
 * JDBC的SQL语句帮助类
 * </pre>
 * @author sellone
 * @version 1.0, 2007-6-7
 */
public class SQLUtil
{
	/**
	 * <pre>
	 * TODO 输入类型说明
	 * </pre>
	 * @author zhaojitao
	 * @version 1.0, 2011-10-10
	 */
	public enum DriverName
	{
		MySQL, HSQL, Oracle, SQLite, SQLServer;
		public static DriverName parseDriverName(String name)
		{
			if (name.equalsIgnoreCase("Microsoft SQL Server"))
			{
				return DriverName.SQLServer;
			}
			else if (name.equalsIgnoreCase("HSQL Database Engine"))
			{
				return DriverName.HSQL;
			}
			else
			{
				return DriverName.valueOf(name);
			}
		}
	}

	private static final String Default_Date_Format = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 统计
	 */
	public static final int ENTITY_COUNT = 4;

	/**
	 * 删除
	 */
	public static final int ENTITY_DELETE = 3;

	/**
	 * 插入
	 */
	public static final int ENTITY_INSERT = 1;

	/**
	 * 新增或更新
	 */
	public static final int ENTITY_SAVE_OR_UPDATE = 5;

	/**
	 * 查询
	 */
	public static final int ENTITY_SELECT = 0;

	/**
	 * 准备进行更新的查询
	 */
	public static final int ENTITY_SELECT_FOR_UPDATE = 6;

	/**
	 * 更新
	 */
	public static final int ENTITY_UPDATE = 2;

	public final static String JDBC_CONFIG_PATH = "entity-config.xml";

	private static final Log logger = LogFactory.getLog(SQLUtil.class);

	/**
	 * <pre>
	 * 生成统计表记录的SQL语句
	 * </pre>
	 * @param dc 表配置
	 * @param filter 条件语句，前面不加WHERE
	 * @return sql语句
	 */
	public static String createCountStatement(Entity dc, String filter) throws DataOperationException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(1) AS count FROM ");
		sb.append(dc.getTableName());
		if ((filter != null) && (!"".equals(filter)))
		{
			sb.append(" WHERE ");
			sb.append(filter);
		}
		logger.debug(sb);
		return sb.toString();
	}

	public static String getLimitString(DriverName driverName, String sql, int pageSize, int pageNum)
	{
		StringBuffer sb = new StringBuffer();
		String lowerCase = sql.toLowerCase();
		int startIndex = (pageNum - 1) * pageSize;
		int endIndex = pageNum * pageSize;
		boolean hasOffset = false;
		if (pageNum > 1)
		{
			hasOffset = true;
		}
		switch (driverName)
		{
			case MySQL:
				sb.append(sql).append(hasOffset ? " limit " + startIndex + ", " + pageSize : " limit " + pageSize);
				break;

			case Oracle:
				sql = sql.trim();
				boolean isForUpdate = false;
				if (lowerCase.endsWith(" for update"))
				{
					sql = sql.substring(0, sql.length() - 11);
					isForUpdate = true;
				}
				StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
				if (hasOffset)
				{
					pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
				}
				else
				{
					pagingSelect.append("select * from ( ");
				}
				pagingSelect.append(sql);
				if (hasOffset)
				{
					pagingSelect.append(" ) row_ where rownum <= " + endIndex + ") where rownum_ > " + startIndex);
				}
				else
				{
					pagingSelect.append(" ) where rownum <= " + endIndex + "");
				}

				if (isForUpdate)
				{
					pagingSelect.append(" for update");
				}
				sb.append(pagingSelect);
				break;

			case SQLite:
				sb.append(sql).append(hasOffset ? " limit " + startIndex + ", " + pageSize : " limit " + pageSize);
				break;

			case HSQL:
				if (!hasOffset)
				{
					int selectIndex = lowerCase.indexOf("select");
					final int selectDistinctIndex = lowerCase.indexOf("select distinct");
					int insertPoint = selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
					sb.append(sql).insert(insertPoint, " top " + pageSize + " ");
				}
				else
				{
					int selectIndex = lowerCase.indexOf("select");
					final int selectDistinctIndex = lowerCase.indexOf("select distinct");
					int insertPoint = selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
					sb.append(sql).insert(insertPoint, " limit " + startIndex + ", " + pageSize + " ");
				}
				break;
			case SQLServer:
				if (!hasOffset)
				{
					int selectIndex = lowerCase.indexOf("select");
					final int selectDistinctIndex = lowerCase.indexOf("select distinct");
					int insertPoint = selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
					sb.append(sql).insert(insertPoint, " top " + pageSize + " ");
				}
				else
				{
					startIndex++;
					int indexOf = lowerCase.indexOf(" order by ");
					String orderString = "";
					if (indexOf > -1)
					{
						orderString = sql.substring(indexOf);
						sql = sql.substring(0, indexOf);
					}
					else
					{
						orderString = " order by getdate() desc ";
					}
					sb.append("select * from ( select row_number() over(").append(orderString)
							.append(") as rowId, * from ( ");
					sb.append(sql);
					sb.append(" ) row_tempTable_ ) outer_temptable_ where rowId between " + startIndex + " and "
							+ endIndex);
				}
				break;

			default:
				break;
		}
		return sb.toString();
	}

	public String getOracleLimitString(String sql, boolean hasOffset)
	{
		sql = sql.trim();
		boolean isForUpdate = false;
		if (sql.toLowerCase().endsWith(" for update"))
		{
			sql = sql.substring(0, sql.length() - 11);
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		if (hasOffset)
		{
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		}
		else
		{
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (hasOffset)
		{
			pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
		}
		else
		{
			pagingSelect.append(" ) where rownum <= ?");
		}

		if (isForUpdate)
		{
			pagingSelect.append(" for update");
		}

		return pagingSelect.toString();
	}

	/**
	 * <pre>
	 * 生成插入语句字段部分
	 * </pre>
	 * @param dc 表配置
	 * @param entity 实体对象
	 * @param driverName TODO
	 * @return 字段部分语句
	 */
	public static String createInsertStatement(Entity dc, Object entity, DriverName driverName)
			throws DataOperationException
	{
		try
		{
			StringBuffer fieldStr = new StringBuffer(" ( ");
			StringBuffer valueStr = new StringBuffer(" ( ");
			SimpleDateFormat timeStampFormt = new SimpleDateFormat(Default_Date_Format);
			Field[] fields = dc.getFields();
			for (Field field : fields)
			{
				Class<?> rtnType = field.getType();
				Object value = field.get(entity);

				if (value != null)
				{
					String fieldName = field.getName();

					if (rtnType.equals(java.lang.String.class))
					{
						fieldStr.append(" ").append(fieldName).append(",");
						valueStr.append(" '").append(value.toString()).append("',");
					}
					else if (rtnType.equals(java.util.Date.class))
					{
						fieldStr.append(" ").append(fieldName).append(",");
						if (driverName.equals(DriverName.Oracle))
						{
							valueStr.append(" to_timestamp('").append(timeStampFormt.format((Date) value))
									.append("', 'yyyy-mm-dd hh24:mi:ss.ff3'),");
						}
						else
						{
							valueStr.append(" '").append(timeStampFormt.format((Date) value)).append("',");
						}
					}
					else if (rtnType.isEnum())
					{
						fieldStr.append(" ").append(fieldName).append(",");
						valueStr.append(" '").append(value).append("',");
					}
					else if (Number.class.isAssignableFrom(rtnType))
					{
						fieldStr.append(" ").append(fieldName).append(",");
						valueStr.append(" ").append(value).append(",");
					}
					else
					{
						fieldStr.append(" ").append(fieldName).append(",");
						valueStr.append(" '").append(value.toString()).append("',");
					}
				}
				else
				{
					if ((driverName == DriverName.Oracle)
							&& (field.getName().equalsIgnoreCase(dc.getKeyField().getName())))
					{
						if (dc.getKeySeqName() != null)
						{
							String fieldName = field.getName();
							fieldStr.append(" ").append(fieldName).append(",");
							valueStr.append(" " + dc.getKeySeqName() + ".nextval,");
						}
					}
				}
			}
			fieldStr = fieldStr.deleteCharAt(fieldStr.length() - 1);
			valueStr = valueStr.deleteCharAt(valueStr.length() - 1);
			valueStr.append(" ) ");
			fieldStr.append(" ) ").append(" VALUES ").append(valueStr);
			return fieldStr.toString();
		}
		catch (Exception e)
		{
			throw new DataOperationException("生成插入语句字段部分时异常.", e);
		}
	}

	/**
	 * <pre>
	 * 生成主键部分语句
	 * </pre>
	 * @param dc 表配置
	 * @param type 实体对象
	 * @return 主键部分语句
	 */
	public static String createKeyStatement(Entity dc, Class<?> type, String entityId) throws DataOperationException
	{
		try
		{
			Class<?> keyType = dc.getKeyField().getType();
			if (keyType.equals(java.lang.String.class))
			{
				return "'" + entityId + "'";
			}
			else
			{
				return entityId;
			}
		}
		catch (Exception e)
		{
			throw new DataOperationException("生成主键部分语句时异常.", e);
		}
	}

	/**
	 * <pre>
	 * 生成主键部分语句
	 * </pre>
	 * @param dc 表配置
	 * @param entity 实体对象
	 * @return 主键部分语句
	 */
	public static String createKeyStatement(Entity dc, Object entity) throws DataOperationException
	{
		try
		{
			Object keyValue = dc.getKeyField().get(entity);
			if (dc.getKeyField().getType().equals(java.lang.String.class))
			{
				return "'" + keyValue.toString() + "'";
			}
			else
			{
				return keyValue.toString();
			}
		}
		catch (Exception e)
		{
			throw new DataOperationException("生成主键部分语句时异常.", e);
		}
	}

	/**
	 * <pre>
	 * 生成SQL语句
	 * </pre>
	 * @param dc 表配置
	 * @param entity 实体类型
	 * @param method 增删改查
	 * @param driverName 数据库类型名称
	 * @return sql语句
	 */
	public static String createStatement(Entity dc, Class<?> entity, String entityId, int method, DriverName driverName)
			throws DataOperationException
	{
		StringBuffer sb = new StringBuffer();
		switch (method)
		{
			case SQLUtil.ENTITY_SELECT:
				sb.append("SELECT * FROM ");
				sb.append(dc.getTableName());
				sb.append(" WHERE ");
				sb.append(dc.getPrimaryKey());
				sb.append(" = ");
				sb.append(SQLUtil.createKeyStatement(dc, entity, entityId));
				break;
			case SQLUtil.ENTITY_SELECT_FOR_UPDATE:
				sb.append("SELECT * FROM ");
				sb.append(dc.getTableName());
				if (driverName.equals(DriverName.SQLServer))
				{
					sb.append(" WITH(UPDLOCK, ROWLOCK) ");
				}
				sb.append(" WHERE ");
				sb.append(dc.getPrimaryKey());
				sb.append(" = ");
				sb.append(SQLUtil.createKeyStatement(dc, entity, entityId));
				if (driverName.equals(DriverName.Oracle) || driverName.equals(DriverName.MySQL))
				{
					sb.append(" FOR UPDATE ");
				}
				break;
			case SQLUtil.ENTITY_DELETE:
				sb.append("DELETE FROM ");
				sb.append(dc.getTableName());
				sb.append(" WHERE ");
				sb.append(dc.getPrimaryKey());
				sb.append(" = ");
				sb.append(SQLUtil.createKeyStatement(dc, entity, entityId));
				break;
			case SQLUtil.ENTITY_COUNT:
				sb.append("SELECT COUNT(*) FROM ");
				sb.append(dc.getTableName());
				sb.append(" WHERE ");
				sb.append(dc.getPrimaryKey());
				sb.append(" = ");
				sb.append(SQLUtil.createKeyStatement(dc, entity, entityId));
				break;
			default:
				throw new DataOperationException("不支持的语句类型.");
		}
		logger.debug(sb);
		return sb.toString();
	}

	/**
	 * <pre>
	 * 生成SQL语句
	 * </pre>
	 * @param dc 表配置
	 * @param entity 实体对象
	 * @param method 增删改查
	 * @param driverName 数据库类型名称
	 * @return sql语句
	 */
	public static String createStatement(Entity dc, Object entity, int method, DriverName driverName)
			throws DataOperationException
	{
		StringBuffer sb = new StringBuffer();
		switch (method)
		{
			case SQLUtil.ENTITY_INSERT:
				sb.append("INSERT INTO ");
				sb.append(dc.getTableName());
				sb.append(SQLUtil.createInsertStatement(dc, entity, driverName));
				break;
			case SQLUtil.ENTITY_SELECT:
				sb.append("SELECT * FROM ");
				sb.append(dc.getTableName());
				sb.append(" WHERE ");
				sb.append(dc.getPrimaryKey());
				sb.append(" = ");
				sb.append(SQLUtil.createKeyStatement(dc, entity));
				break;
			case SQLUtil.ENTITY_SELECT_FOR_UPDATE:
				sb.append("SELECT * FROM ");
				sb.append(dc.getTableName());
				if (driverName.equals(DriverName.SQLServer))
				{
					sb.append(" WITH(UPDLOCK, ROWLOCK) ");
				}
				sb.append(" WHERE ");
				sb.append(dc.getPrimaryKey());
				sb.append(" = ");
				sb.append(SQLUtil.createKeyStatement(dc, entity));
				if (driverName.equals(DriverName.Oracle) || driverName.equals(DriverName.MySQL))
				{
					sb.append(" FOR UPDATE ");
				}
				break;
			case SQLUtil.ENTITY_UPDATE:
				sb.append("UPDATE ");
				sb.append(dc.getTableName());
				sb.append(" SET ");
				sb.append(SQLUtil.createUpdateStatement(dc, entity, driverName));
				sb.append(" WHERE ");
				sb.append(dc.getPrimaryKey());
				sb.append(" = ");
				sb.append(SQLUtil.createKeyStatement(dc, entity));
				break;
			case SQLUtil.ENTITY_DELETE:
				sb.append("DELETE FROM ");
				sb.append(dc.getTableName());
				sb.append(" WHERE ");
				sb.append(dc.getPrimaryKey());
				sb.append(" = ");
				sb.append(SQLUtil.createKeyStatement(dc, entity));
				break;
			case SQLUtil.ENTITY_COUNT:
				sb.append("SELECT COUNT(*) AS count FROM ");
				sb.append(dc.getTableName());
				sb.append(" WHERE ");
				sb.append(dc.getPrimaryKey());
				sb.append(" = ");
				sb.append(SQLUtil.createKeyStatement(dc, entity));
				break;
			case SQLUtil.ENTITY_SAVE_OR_UPDATE:
				if (SQLUtil.getEntityPrimaryKeyValue(dc, entity) != null)
				{
					sb.append("IF NOT EXISTS(" + SQLUtil.createStatement(dc, entity, SQLUtil.ENTITY_SELECT, driverName)
							+ ") ");
					sb.append(SQLUtil.createStatement(dc, entity, SQLUtil.ENTITY_INSERT, driverName));
					sb.append(" ELSE ");
					sb.append(SQLUtil.createStatement(dc, entity, SQLUtil.ENTITY_UPDATE, driverName));
				}
				else
				{
					sb.append(SQLUtil.createStatement(dc, entity, SQLUtil.ENTITY_INSERT, driverName));
				}
				break;
			default:
				throw new DataOperationException("不支持的语句类型.");
		}
		logger.debug(sb);
		return sb.toString();
	}

	/**
	 * <pre>
	 * 生成更新语句字段部分
	 * </pre>
	 * @param dc 表配置
	 * @param entity 实体对象
	 * @return 字段部分语句
	 */
	public static String createUpdateStatement(Entity dc, Object entity, DriverName driverName)
			throws DataOperationException
	{
		try
		{
			StringBuffer fieldValue = new StringBuffer();
			SimpleDateFormat timeStampFormt = new SimpleDateFormat(Default_Date_Format);
			Field[] fields = dc.getFields();
			for (Field field : fields)
			{
				Class<?> rtnType = field.getType();
				String fieldName = field.getName();
				if (!fieldName.equalsIgnoreCase(dc.getPrimaryKey()))
				{
					Object value = field.get(entity);
					if (value != null)
					{
						if (rtnType.equals(java.lang.String.class))
						{
							fieldValue.append(" ").append(fieldName).append(" = '").append(value.toString())
									.append("',");
						}
						else if (rtnType.equals(java.util.Date.class))
						{
							if (driverName.equals(DriverName.Oracle))
							{
								fieldValue.append(" ").append(fieldName).append(" = to_date('")
										.append(timeStampFormt.format((Date) value))
										.append("', 'yyyy-mm-dd hh24:mi:ss.ff3'),");
							}
							else
							{
								fieldValue.append(" ").append(fieldName).append(" = '")
										.append(timeStampFormt.format((Date) value)).append("',");
							}
						}
						else if (rtnType.isEnum())
						{
							fieldValue.append(" ").append(fieldName).append(" = '").append(value).append("',");
						}
						else if (Number.class.isAssignableFrom(rtnType))
						{
							fieldValue.append(" ").append(fieldName).append(" = ").append(value).append(",");
						}
						else
						{
							fieldValue.append(" ").append(fieldName).append(" = '").append(value.toString())
									.append("',");
						}
					}
				}
			}
			return fieldValue.substring(0, fieldValue.length() - 1);
		}
		catch (Exception e)
		{
			throw new DataOperationException("生成更新语句字段部分时异常.", e);
		}
	}

	private static void extractNamedQuery(PersistConfig persistConfig, Map<String, String> list, NamedQuery namedQuery,
			DriverName dbType)
	{
		if ((namedQuery.getDbType() == null) || (namedQuery.getDbType().equalsIgnoreCase(dbType.name())))
		{
			String importFile = namedQuery.getImportFile();
			if ((importFile != null) && (importFile.length() > 0))
			{
				ConfigProvider<NamedQuery> nameQuerys = ConfigProviderFactory.getInstance(importFile, NamedQuery.class);
				Collection<NamedQuery> values = nameQuerys.getAllConfig().values();
				for (NamedQuery namedQuery2 : values)
				{
					extractNamedQuery(persistConfig, list, namedQuery2, dbType);
				}
			}

			String name = namedQuery.getName();
			if ((name != null) && (name.length() > 0))
			{
				list.put(name, namedQuery.getSql());
				printSQL(false, persistConfig, "Bind named query[" + name + "] -> " + namedQuery.getSql());
			}
		}
	}

	/**
	 * <pre>
	 * 读取实体/数据表映射表
	 * </pre>
	 * @param persistConfig 持久化配置
	 * @return 实体/数据表映射表
	 */
	public static Map<String, Entity> getEntityMap(PersistConfig persistConfig)
	{
		Map<String, Entity> entityMap = new HashMap<String, Entity>();
		Entity[] entitys = persistConfig.getEntitys();
		for (Entity entity : entitys)
		{
			entityMap.put(entity.getEntityName(), entity);
			StringBuffer properties = new StringBuffer();
			try
			{
				Class<?> instanceClass = Class.forName(entity.getEntityName());
				List<Field> list = new ArrayList<Field>();
				HashSet<String> fieldSet = new HashSet<String>();
				// 父类的方法
				while (!instanceClass.equals(Object.class))
				{
					Field[] fields = instanceClass.getDeclaredFields();
					for (Field field : fields)
					{
						String name = field.getName();
						if (!fieldSet.contains(name))
						{
							if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))
							{
								continue;
							}
							else
							{
								boolean isException = false;
								try
								{
									new PropertyDescriptor(name, instanceClass);
								}
								catch (Exception e)
								{
									isException = true;
								}
								// JDK不同平台，子类覆盖父类的方法并且返回类型有继承关系，使用PropertyDescriptor将导致异常，所以需要手工判断处理
								if (!isException)
								{
									field.setAccessible(true);
									if (name.equals(entity.getPrimaryKey()))
									{
										entity.setKeyField(field);
									}
									properties.append(" ").append(field.getType().getSimpleName()).append(" ")
											.append(name).append(",");
									list.add(field);
									fieldSet.add(name);
								}
								else
								{
									String columnName = "";
									if (name.length() > 1)
									{
										columnName = name.substring(0, 1).toUpperCase() + name.substring(1);
									}
									else
									{
										columnName = name.toUpperCase();
									}
									Class<?> type = field.getType();
									boolean isHasGetMethod = false;
									boolean isHasSetMethod = false;
									try
									{
										Method getMethod = instanceClass.getMethod("get" + columnName);
										if (getMethod.getReturnType().isAssignableFrom(type))
										{
											isHasGetMethod = true;
										}
									}
									catch (Exception e)
									{
									}
									// 父类的方法
									while (!type.equals(Object.class))
									{
										try
										{
											instanceClass.getMethod("set" + columnName, type);
											isHasSetMethod = true;
											break;
										}
										catch (Exception e)
										{
										}
										type = type.getSuperclass();
										if (type == null)
										{
											break;
										}
									}
									if (!isHasSetMethod)
									{
										try
										{
											instanceClass.getMethod("set" + columnName, Object.class);
											isHasSetMethod = true;
										}
										catch (Exception e)
										{
										}
									}
									if (isHasGetMethod && isHasSetMethod)
									{
										field.setAccessible(true);
										if (name.equals(entity.getPrimaryKey()))
										{
											entity.setKeyField(field);
										}
										properties.append(" ").append(field.getType().getSimpleName()).append(" ")
												.append(name).append(",");
										list.add(field);
										fieldSet.add(name);
									}
								}
							}
						}
					}
					instanceClass = instanceClass.getSuperclass();
				}
				entity.setFields(list.toArray(new Field[list.size()]));
				if (properties.length() > 0)
				{
					properties.deleteCharAt(properties.length() - 1);
				}
			}
			catch (Exception e)
			{
				logger.error("读取实体{" + entity.getEntityName() + "}配置异常", e);
			}

			printSQL(false, persistConfig, "Bind table[" + entity.getTableName() + "] to " + entity.getEntityName()
					+ "[" + properties + " ].");
		}
		return entityMap;
	}

	public static Entity parseEntity(PersistConfig persistConfig, Class<?> type)
	{
		Entity entity = new Entity();
		StringBuffer properties = new StringBuffer();
		try
		{
			Class<?> instanceClass = type;
			List<Field> list = new ArrayList<Field>();
			HashSet<String> fieldSet = new HashSet<String>();
			// 父类的方法
			while (!instanceClass.equals(Object.class))
			{
				Field[] fields = instanceClass.getDeclaredFields();
				for (Field field : fields)
				{
					String name = field.getName();
					if (!fieldSet.contains(name))
					{
						if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))
						{
							continue;
						}
						else
						{
							boolean isException = false;
							try
							{
								new PropertyDescriptor(name, instanceClass);
							}
							catch (Exception e)
							{
								isException = true;
							}
							// JDK不同平台，子类覆盖父类的方法并且返回类型有继承关系，使用PropertyDescriptor将导致异常，所以需要手工判断处理
							if (!isException)
							{
								field.setAccessible(true);
								properties.append(" ").append(field.getType().getSimpleName()).append(" ").append(name)
										.append(",");
								list.add(field);
								fieldSet.add(name);
							}
							else
							{
								String columnName = "";
								if (name.length() > 1)
								{
									columnName = name.substring(0, 1).toUpperCase() + name.substring(1);
								}
								else
								{
									columnName = name.toUpperCase();
								}
								Class<?> fieldType = field.getType();
								boolean isHasGetMethod = false;
								boolean isHasSetMethod = false;
								try
								{
									Method getMethod = instanceClass.getMethod("get" + columnName);
									if (getMethod.getReturnType().isAssignableFrom(fieldType))
									{
										isHasGetMethod = true;
									}
								}
								catch (Exception e)
								{
								}
								// 父类的方法
								while (!fieldType.equals(Object.class))
								{
									try
									{
										instanceClass.getMethod("set" + columnName, fieldType);
										isHasSetMethod = true;
										break;
									}
									catch (Exception e)
									{
									}
									fieldType = fieldType.getSuperclass();
									if (fieldType == null)
									{
										break;
									}
								}
								if (!isHasSetMethod)
								{
									try
									{
										instanceClass.getMethod("set" + columnName, Object.class);
										isHasSetMethod = true;
									}
									catch (Exception e)
									{
									}
								}
								if (isHasGetMethod && isHasSetMethod)
								{
									field.setAccessible(true);
									properties.append(" ").append(field.getType().getSimpleName()).append(" ")
											.append(name).append(",");
									list.add(field);
									fieldSet.add(name);
								}
							}
						}
					}
				}
				instanceClass = instanceClass.getSuperclass();
			}
			entity.setFields(list.toArray(new Field[list.size()]));
			if (properties.length() > 0)
			{
				properties.deleteCharAt(properties.length() - 1);
			}
		}
		catch (Exception e)
		{
			logger.error("读取实体{" + type.getName() + "}配置异常", e);
		}

		printSQL(false, persistConfig, "Bind value object " + type.getName() + "[" + properties + " ].");
		return entity;
	}

	public static void printSQL(boolean showDS, PersistConfig config, String... sql)
	{
		if (config.getShowSQL())
		{
			for (String string : sql)
			{
				if (showDS)
				{
					System.out.println("JDBC[" + ClientContextHolder.getClientAction().getDbLocation() + "]: " + string
							+ "");
				}
				else
				{

					System.out.println("JDBC: " + string + "");
				}
			}
		}
	}

	/**
	 * <pre>
	 * 获取实体主键值
	 * </pre>
	 * @param dc 表配置
	 * @param entity 实体对象
	 * @return 主键值
	 * @throws DataOperationException
	 */
	public static Object getEntityPrimaryKeyValue(Entity dc, Object entity) throws DataOperationException
	{
		try
		{
			if (dc.getKeyField() != null)
			{
				return dc.getKeyField().get(entity);
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			throw new DataOperationException("获取实体主键时异常.", e);
		}
	}

	/**
	 * <pre>
	 * 读取实体/数据表映射表
	 * </pre>
	 * @param persistConfig 持久化配置
	 * @return 实体/数据表映射表
	 */
	public static Map<String, String> getNamedQueries(PersistConfig persistConfig, DriverName dbType)
	{
		NamedQuery[] namedQueries = persistConfig.getNamedQueries();
		Map<String, String> namedQueryMap = new HashMap<String, String>();
		for (NamedQuery namedQuery : namedQueries)
		{
			extractNamedQuery(persistConfig, namedQueryMap, namedQuery, dbType);
		}
		return namedQueryMap;
	}

	/**
	 * <pre>
	 * 读取持久化配置
	 * </pre>
	 * @return 持久化配置
	 */
	public static PersistConfig getPersistConfig()
	{
		ConfigProvider<PersistConfig> cp = ConfigProviderFactory.getInstance(SQLUtil.JDBC_CONFIG_PATH,
				PersistConfig.class);
		return cp.getAllConfig().values().iterator().next();
	}

	/**
	 * <pre>
	 * 设置实体主键值
	 * </pre>
	 * @param dc 表配置
	 * @param entity 实体对象
	 * @param keyValue 主键值
	 * @throws DataOperationException
	 */
	public static void setEntityPrimaryKeyValue(Entity dc, Object entity, Object keyValue)
			throws DataOperationException
	{
		try
		{
			Field keyField = dc.getKeyField();
			if ((keyField != null) && (keyValue != null))
			{
				Class<?> class1 = keyField.getType();
				Constructor<?> constructor = class1.getConstructor(String.class);
				Object newInstance = constructor.newInstance(keyValue.toString());
				keyField.set(entity, newInstance);
			}
		}
		catch (Exception e)
		{
			throw new DataOperationException("设置实体主键时异常.", e);
		}
	}

	/**
	 * <pre>
	 * 从行记录数据集转换为实体集合
	 * </pre>
	 * @param set 行记录数据集
	 * @param entityClass 实体类型
	 * @param dc 表配置
	 * @return 实体集合
	 * @throws DataOperationException 数据库异常
	 */
	public static <T> List<T> SqlRowSet2List(SqlRowSet set, Class<T> entityClass, Entity dc)
			throws DataOperationException
	{
		if (dc == null)
		{

		}
		List<T> list = new ArrayList<T>();
		try
		{
			if (set != null)
			{
				String[] columnNames = set.getMetaData().getColumnNames();
				Set<String> columnNameSet = new HashSet<String>();
				for (int i = 0; i < columnNames.length; i++)
				{
					columnNameSet.add(columnNames[i].toLowerCase());
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Default_Date_Format);
				while (set.next())
				{
					Field[] fields = dc.getFields();
					T object = entityClass.newInstance();
					for (Field field : fields)
					{
						Object value = null;
						{
							Class<?> parameterType = field.getType();

							String propertyName = field.getName().toLowerCase();
							if (columnNameSet.contains(propertyName))
							{
								value = set.getObject(propertyName);
								if (value != null)
								{
									if (Double.class.isAssignableFrom(parameterType))
									{
										value = set.getDouble(propertyName);
									}
									else if (Long.class.isAssignableFrom(parameterType))
									{
										value = set.getLong(propertyName);
									}
									else if (Integer.class.isAssignableFrom(parameterType))
									{
										value = set.getInt(propertyName);
									}
									else if (Short.class.isAssignableFrom(parameterType))
									{
										value = set.getShort(propertyName);
									}
									else if (BigDecimal.class.isAssignableFrom(parameterType))
									{
										value = set.getBigDecimal(propertyName);
									}
									else if (Boolean.class.isAssignableFrom(parameterType))
									{
										value = set.getBoolean(propertyName);
									}
									else if (Float.class.isAssignableFrom(parameterType))
									{
										value = set.getFloat(propertyName);
									}
									else if (Byte.class.isAssignableFrom(parameterType))
									{
										value = set.getByte(propertyName);
									}
									else if (java.sql.Date.class.isAssignableFrom(parameterType))
									{
										value = set.getDate(propertyName);
									}
									else if (Time.class.isAssignableFrom(parameterType))
									{
										value = set.getTime(propertyName);
									}
									else if (Timestamp.class.isAssignableFrom(parameterType))
									{
										value = set.getTimestamp(propertyName);
									}
									else if (Date.class.isAssignableFrom(parameterType))
									{
										Object sqlobject = set.getObject(propertyName);
										if (Date.class.isAssignableFrom(sqlobject.getClass()))
										{
											value = (Date) sqlobject;
										}
										else if (sqlobject.getClass().getName()
												.equalsIgnoreCase("oracle.sql.TIMESTAMP"))
										{
											Class<?> clz = value.getClass();
											Method m = clz.getMethod("dateValue");
											value = new Date(((java.sql.Date) m.invoke(sqlobject)).getTime());
										}
										else
										{
											String dateString = set.getString(propertyName);
											if (dateString != null)
											{
												value = simpleDateFormat.parse(dateString);
											}
										}
									}
									else if (parameterType.isEnum())
									{
										value = set.getString(propertyName);
										if (value != null)
										{
											Method method2 = parameterType.getMethod("valueOf", String.class);
											value = method2.invoke(null, value);
										}
									}
								}
								if (value != null)
								{
									try
									{
										field.set(object, value);
									}
									catch (Exception e)
									{
										logger.error("实例化持久化数据时异常{" + e.getMessage() + "}, 属性名{" + field.getName()
												+ "}, jdbc参数类型{" + value.getClass().getName() + "}, 继续处理.");
									}
								}
							}
						}
					}
					list.add(object);
				}
			}
		}
		catch (Exception e)
		{
			throw new DataOperationException("实例化持久化数据时异常", e);
		}
		return list;
	}
}
