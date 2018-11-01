/**
 * <pre>
 * Title: 		ModuleConfig.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.acl.ModuleConfig
 * Author:		linriqing
 * Create:	 	2007-6-8 下午08:35:29
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.acl;

import com.huayin.common.acl.vo.ActionObject;
import com.huayin.common.acl.vo.NodeObject;
import com.huayin.common.configuration.Config;

/**
 * <pre>
 * 模块配置信息
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-8
 */
public class ModuleConfig extends Config
{
	private static final long serialVersionUID = -8350438852927187301L;

	/**
	 * Action集合
	 */
	private ActionObject[] actions = new ActionObject[0];

	/**
	 * 模块编号
	 */
	private String id;

	/**
	 * 节点集合
	 */
	private NodeObject[] nodes = new NodeObject[0];

	/**
	 * @return Action集合
	 * @see com.huayin.common.acl.ModuleConfig#actions
	 */
	public ActionObject[] getActions()
	{
		return actions;
	}

	/**
	 * @return 模块编号
	 * @see com.huayin.common.acl.ModuleConfig#id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @return 节点集合
	 * @see com.huayin.common.acl.ModuleConfig#nodes
	 */
	public NodeObject[] getNodes()
	{
		return nodes;
	}

	/**
	 * @param actions Action集合
	 * @see com.huayin.common.acl.ModuleConfig#actions
	 */
	public void setActions(ActionObject[] actions)
	{
		this.actions = actions;
	}

	/**
	 * @param id 模块编号
	 * @see com.huayin.common.acl.ModuleConfig#id
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @param nodes 节点集合
	 * @see com.huayin.common.acl.ModuleConfig#nodes
	 */
	public void setNodes(NodeObject[] nodes)
	{
		this.nodes = nodes;
	}
}
