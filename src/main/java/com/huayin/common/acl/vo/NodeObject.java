/**
 * <pre>
 * Title: 		NodeObject.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.acl.object.NodeObject
 * Author:		linriqing
 * Create:	 	2007-6-21 上午07:56:50
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.acl.vo;

import com.huayin.common.util.ObjectHelper;

/**
 * <pre>
 * 节点权限物件
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-21
 */
public class NodeObject extends ActionObject implements Comparable<NodeObject>, Cloneable
{
	private static final long serialVersionUID = 5521459369794002137L;

	/**
	 * 排序号
	 */
	private int sortNo;

	public Object clone() throws CloneNotSupportedException
	{
		return ObjectHelper.byteClone(this);
	}

	public int compareTo(NodeObject o)
	{
		return this.getSortNo() - o.getSortNo();
	}

	/**
	 * @return 排序号
	 */
	public int getSortNo()
	{
		return sortNo;
	}

	/**
	 * @param sortNo 排序号
	 */
	public void setSortNo(int sortNo)
	{
		this.sortNo = sortNo;
	}
}
