/**
 * <pre>
 * Title: 		ActionObject.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2007-6-26 上午01:47:13
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.acl.vo;

import org.springframework.web.bind.annotation.RequestMethod;

import com.huayin.common.util.ObjectHelper;

/**
 * <pre>
 * Action权限物件
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-26
 */
public class ActionObject extends Permission implements Cloneable
{
	private static final long serialVersionUID = -5879728873462263296L;

	/**
	 * Action导航目标
	 */
	private String destination;

	/**
	 * 导入定义文件
	 */
	private String importFile;

	/**
	 * 请求方法
	 */
	private String method = RequestMethod.GET.name();

	public Object clone() throws CloneNotSupportedException
	{
		return ObjectHelper.byteClone(this);
	}

	/**
	 * @return Action导航目标
	 */
	public String getDestination()
	{
		return destination;
	}

	/**
	 * @return 导入定义文件
	 */
	public String getImportFile()
	{
		return importFile;
	}

	public String getMethod()
	{
		return method;
	}

	/**
	 * @param destination Action导航目标
	 */
	public void setDestination(String destination)
	{
		this.destination = destination;
	}

	/**
	 * @param importFile 导入定义文件
	 */
	public void setImportFile(String importFile)
	{
		this.importFile = importFile;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}
}
