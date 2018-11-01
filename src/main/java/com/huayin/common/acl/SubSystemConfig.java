/**
 * <pre>
 * Title: 		SystemConfig.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.acl.SystemConfig
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
 * 子系统配置信息
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2007-6-8
 */
public class SubSystemConfig extends Config
{
	private static final long serialVersionUID = -8350438852927187301L;

	/**
	 * Action集合
	 */
	private ActionObject[] actions = new ActionObject[0];

	/**
	 * 对没有配置权限的物件是否默认允许访问
	 */
	private boolean defaultAllowPrivilege;

	/**
	 * 是否默认
	 */
	private boolean defaultSystem;

	/**
	 * 不校验权限的action集合
	 */
	private String[] excludeActions = new String[0];

	/**
	 * 导入定义文件
	 */
	private String importFile;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 节点集合
	 */
	private NodeObject[] nodes = new NodeObject[0];

	/**
	 * 显示文字
	 */
	private String showText;

	/**
	 * 排序号
	 */
	private int sortNo;

	/**
	 * @return Action集合
	 * @see com.huayin.common.acl.SubSystemConfig#actions
	 */
	public ActionObject[] getActions()
	{
		return actions;
	}

	/**
	 * @return 是否默认
	 * @see com.huayin.common.acl.SubSystemConfig#defaultSystem
	 */
	public boolean getDefaultSystem()
	{
		return defaultSystem;
	}

	public String[] getExcludeActions()
	{
		return excludeActions;
	}

	/**
	 * @return 导入定义文件
	 * @see com.huayin.common.acl.SubSystemConfig#importFile
	 */
	public String getImportFile()
	{
		return importFile;
	}

	/**
	 * @return 名称
	 * @see com.huayin.common.acl.SubSystemConfig#name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return 节点集合
	 * @see com.huayin.common.acl.SubSystemConfig#nodes
	 */
	public NodeObject[] getNodes()
	{
		return nodes;
	}

	/**
	 * @return 显示文字
	 * @see com.huayin.common.acl.SubSystemConfig#showText
	 */
	public String getShowText()
	{
		return showText;
	}

	/**
	 * @return 排序号
	 * @see com.huayin.common.acl.SubSystemConfig#sortNo
	 */
	public int getSortNo()
	{
		return sortNo;
	}

	public boolean getDefaultAllowPrivilege()
	{
		return defaultAllowPrivilege;
	}

	/**
	 * @param actions Action集合
	 * @see com.huayin.common.acl.SubSystemConfig#actions
	 */
	public void setActions(ActionObject[] actions)
	{
		this.actions = actions;
	}

	public void setDefaultAllowPrivilege(boolean defaultAllowPrivilege)
	{
		this.defaultAllowPrivilege = defaultAllowPrivilege;
	}

	/**
	 * @param defaultNode 是否默认
	 * @see com.huayin.common.acl.SubSystemConfig#defaultSystem
	 */
	public void setDefaultSystem(boolean defaultNode)
	{
		this.defaultSystem = defaultNode;
	}

	public void setExcludeActions(String[] excludeActions)
	{
		this.excludeActions = excludeActions;
	}

	/**
	 * @param importFile 导入定义文件
	 * @see com.huayin.common.acl.SubSystemConfig#importFile
	 */
	public void setImportFile(String importFile)
	{
		this.importFile = importFile;
	}

	/**
	 * @param name 名称
	 * @see com.huayin.common.acl.SubSystemConfig#name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param nodes 节点集合
	 * @see com.huayin.common.acl.SubSystemConfig#nodes
	 */
	public void setNodes(NodeObject[] nodes)
	{
		this.nodes = nodes;
	}

	/**
	 * @param showText 显示文字
	 * @see com.huayin.common.acl.SubSystemConfig#showText
	 */
	public void setShowText(String showText)
	{
		this.showText = showText;
	}

	/**
	 * @param sortNo 排序号
	 * @see com.huayin.common.acl.SubSystemConfig#sortNo
	 */
	public void setSortNo(int sortNo)
	{
		this.sortNo = sortNo;
	}
}
