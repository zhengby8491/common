/**
 * <pre>
 * Title: 		AuthorityObject.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.acl.object.AuthorityObject
 * Author:		linriqing
 * Create:	 	2007-6-21 上午07:34:00
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.acl.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * <pre>
 * 权限物件
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-01-01
 */
public class Permission implements Serializable
{
	private static final long serialVersionUID = -4969975632609308675L;

	/**
	 * 子权限集合
	 */
	@Transient
	private ActionObject[] childActions = new ActionObject[0];

	/**
	 * 子节点集合
	 */
	@Transient
	private NodeObject[] childNodes = new NodeObject[0];

	/**
	 * 权限说明
	 */
	@Column(length = 255)
	private String description;
	
	/**
	 * 权限编号
	 */
	@Id
	@Column(length = 10)
	private String id;

	@Transient
	private Permission parent;

	/**
	 * 显示文字
	 */
	@Column(length = 50)
	private String showText;

	@Override
	public boolean equals(Object obj)
	{
		return this.id.equalsIgnoreCase(((Permission) obj).getId());
	}

	/**
	 * @return 子权限集合
	 */
	@Transient
	public ActionObject[] getChildActions()
	{
		return childActions;
	}

	/**
	 * @return 子节点集合
	 */
	@Transient
	public NodeObject[] getChildNodes()
	{
		return childNodes;
	}

	/**
	 * @return 权限说明
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @return 权限编号
	 */
	public String getId()
	{
		return id;
	}

	@Transient
	public Permission getParent()
	{
		return parent;
	}

	@Override
	public int hashCode()
	{
		return (id != null ? id.hashCode() : 0);
	}

	/**
	 * @param childActions 子权限集合
	 */
	public void setChildActions(ActionObject[] childActions)
	{
		this.childActions = childActions;
	}

	/**
	 * @param childNodes 子节点集合
	 */
	public void setChildNodes(NodeObject[] childNodes)
	{
		this.childNodes = childNodes;
	}

	/**
	 * @param description 权限说明
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @param id 权限编号
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	public void setParent(Permission parent)
	{
		this.parent = parent;
	}

	/**
	 * @return 显示文字
	 */
	public String getShowText()
	{
		return showText;
	}

	/**
	 * @param showText 显示文字
	 */
	public void setShowText(String showText)
	{
		this.showText = showText;
	}
}
