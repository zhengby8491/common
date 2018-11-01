/**
 * <pre>
 * Title: 		QueryActionUtil.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2007-9-10 下午04:35:45
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.web.core;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.cis.ComponentContextLoader;
import com.huayin.common.persist.PersistProvider;
import com.huayin.common.web.tags.TagConstant;

/**
 * <pre>
 * 通用查询Action
 * 自定义的查询请覆盖方法:
 * com.huayin.common.framework.core.QueryAction.doQuery(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-9-10
 */
public class QueryActionUtil
{
	private static final PersistProvider daoproxy = ComponentContextLoader.getBean(PersistProvider.class);

	private final static Log logger = LogFactory.getLog(QueryActionUtil.class);

	/**
	 * 总记录数
	 */
	private long entityCount = 0;

	/**
	 * 实体对象类限制名
	 */
	private String entityName = "java.lang.Object";

	/**
	 * 显示列表的forward名称
	 */
	private String forwardName = "list";

	/**
	 * 多表排序说明,不要加Order By 如:id desc,name desc...
	 */
	private String orderBy;

	/**
	 * 总页数
	 */
	private int pageCount = 0;

	/**
	 * 每页显示的记录数
	 */
	private int pageSize = 15;

	/**
	 * 准备显示的页码
	 */
	private int preparePageIndex = 1;

	/**
	 * 主键名
	 */
	private String primaryKey = "";

	/**
	 * 多表查询返回的字段 如:a.Id,a.gameId,b.gameName
	 */
	private String returnColumn;

	/**
	 * 多表查询关联的条件 如:a.gameId=b.gameId and 在后面带上and
	 */
	private String tableFactor = "";

	/**
	 * 表或者视图名
	 */
	private String tableName = "";

	/**
	 * 多表分页用来判断的字段 如 id
	 */
	private String whereId = "";

	/**
	 * <pre>
	 * 计算并设置当前页
	 * </pre>
	 * @param entityCount 总记录数
	 * @throws Exception 应用异常
	 */
	protected final long calAndSetPageIndex(long entityCount)
	{
		this.entityCount = entityCount;
		if (entityCount > 0)
		{
			this.pageCount = (int) ((this.entityCount % this.pageSize == 0) ? (this.entityCount / this.pageSize)
					: (this.entityCount / this.pageSize + 1));
			if (this.preparePageIndex > this.pageCount)
			{
				this.preparePageIndex = this.pageCount;
			}
		}
		return this.preparePageIndex;
	}

	public final void doIt(HttpServletRequest arg2, HttpServletResponse arg3) throws Exception
	{

		String currentPageIndex = arg2.getParameter(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_CURRENTPAGEINDEX);
		String paramPageSize = arg2.getParameter(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_PAGESIZE);

		try
		{
			if ((currentPageIndex != null) && (!"".equals(currentPageIndex)))
			{
				this.preparePageIndex = Integer.parseInt(currentPageIndex);
				if (this.preparePageIndex < 1)
				{
					this.preparePageIndex = 1;
				}
			}
			else
			{
				this.preparePageIndex = 1;
			}

			if ((paramPageSize != null) && (!"".equals(paramPageSize)))
			{
				int tempPageSize = Integer.parseInt(paramPageSize);
				if (tempPageSize > 0)
				{
					this.pageSize = tempPageSize;
				}
			}
		}
		catch (RuntimeException e)
		{
			logger.warn("分页查询时取分页参数错误,将使用默认值.");
		}

		// 设置分页查询使用的常量属性
		// 设置当前页码
		arg2.setAttribute(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_CURRENTPAGEINDEX,
				String.valueOf(this.preparePageIndex));
		// 设置总页数
		arg2.setAttribute(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_PAGECOUNT, String.valueOf(this.pageCount));
		// 设置每页记录数
		arg2.setAttribute(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_PAGESIZE, String.valueOf(this.pageSize));
		// 设置每页记录数
		arg2.setAttribute(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_ENTITYCOUNT, String.valueOf(this.entityCount));
	}

	/**
	 * <pre>
	 * 通过实体类型分页
	 * 注意：此方法以主键排倒序。
	 * </pre>
	 * @param request http请求对象
	 * @throws Exception 应用异常
	 */
	protected final void doPaginationFast(HttpServletRequest request) throws Exception
	{
		this.doPaginationFast(request, null);
	}

	/**
	 * <pre>
	 * 通过实体类型和条件语句分页
	 * 注意：此方法以主键排倒序。
	 * </pre>
	 * @param request http请求对象
	 * @param nameQuery 查询语句
	 * @throws Exception 应用异常
	 */
	//@SuppressWarnings({ "rawtypes" })
	protected final void doPaginationFast(HttpServletRequest request, String nameQuery) throws Exception
	{
		Class<?> entityClass = Class.forName(this.entityName);
		List<?> list =new ArrayList<Object>(0);
		if ((this.tableName != null) && (this.tableName.trim().length() > 0))
		{
			this.entityCount = QueryActionUtil.daoproxy.countEntityByNamedQuery(nameQuery);
		}
		else
		{
			this.entityCount = QueryActionUtil.daoproxy.countEntityByNamedQuery(nameQuery);
		}
		if (entityCount > 0)
		{
			this.calAndSetPageIndex(entityCount);
			list= QueryActionUtil.daoproxy.findAllEntityByNamedSql(entityClass, nameQuery, this.pageSize,
					this.preparePageIndex,new Object[]{});
		}
		this.preparePageDataList(request, list);
	}

	/**
	 * @return 总记录数
	 */
	public long getEntityCount()
	{
		return entityCount;
	}

	/**
	 * @return 实体名称
	 */
	public String getEntityName()
	{
		return entityName;
	}

	/**
	 * @return ActionForward名称
	 */
	public String getForwardName()
	{
		return forwardName;
	}

	public String getOrderBy()
	{
		return orderBy;
	}

	/**
	 * @return 总页数
	 */
	public long getPageCount()
	{
		return pageCount;
	}

	/**
	 * @return pageSize 每页显示的记录数
	 */
	public long getPageSize()
	{
		return pageSize;
	}

	/**
	 * @return 准备显示的页码
	 */
	public long getPreparePageIndex()
	{
		return preparePageIndex;
	}

	/**
	 * @return primaryKey
	 */
	public String getPrimaryKey()
	{
		return primaryKey;
	}

	public String getReturnColumn()
	{
		return returnColumn;
	}

	public String getTableFactor()
	{
		return tableFactor;
	}

	/**
	 * @return tableName
	 */
	public String getTableName()
	{
		return tableName;
	}

	public String getWhereId()
	{
		return whereId;
	}

	/**
	 * <pre>
	 * 給HTTP请求对象设置结果数据集
	 * </pre>
	 * @param request HTTP请求对象
	 * @param list 结果数据集
	 */
	@SuppressWarnings("rawtypes")
	protected final void preparePageDataList(HttpServletRequest request, List list)
	{
		request.setAttribute(TagConstant.REQUEST_PARAMETERNAME_PAGINATION_DATALIST_NAME, list);
	}

	/**
	 * @param entityCount 总记录数
	 */
	public void setEntityCount(int entityCount)
	{
		this.entityCount = entityCount;
	}

	/**
	 * @param entityName 实体名称
	 */
	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

	/**
	 * @param forwardName ActionForward名称
	 */
	public void setForwardName(String forwardName)
	{
		this.forwardName = forwardName;
	}

	public void setOrderBy(String orderBy)
	{
		this.orderBy = orderBy;
	}

	/**
	 * @param pageCount 总页数
	 */
	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
	}

	/**
	 * @param pageSize 每页显示的记录数
	 */
	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	/**
	 * @param preparePageIndex 准备显示的页码
	 */
	public void setPreparePageIndex(int preparePageIndex)
	{
		this.preparePageIndex = preparePageIndex;
	}

	/**
	 * @param primaryKey primaryKey
	 */
	public void setPrimaryKey(String primaryKey)
	{
		this.primaryKey = primaryKey;
	}

	public void setReturnColumn(String returnColumn)
	{
		this.returnColumn = returnColumn;
	}

	public void setTableFactor(String tableFactor)
	{
		this.tableFactor = tableFactor;
	}

	/**
	 * @param tableName tableName
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public void setWhereId(String whereId)
	{
		this.whereId = whereId;
	}

}
