/**
 * <pre>
 * Title: 		PermissionProviderImpl.java
 * Author:		linriqing
 * Create:	 	2007-6-21 上午09:25:40
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.acl.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.acl.ModuleConfig;
import com.huayin.common.acl.SubSystemConfig;
import com.huayin.common.acl.vo.ActionObject;
import com.huayin.common.acl.vo.NodeObject;
import com.huayin.common.acl.vo.Passport;
import com.huayin.common.acl.vo.Permission;
import com.huayin.common.configuration.ConfigProvider;
import com.huayin.common.configuration.ConfigProviderFactory;
import com.huayin.common.constant.Constant;
import com.huayin.common.exception.ServiceResultFactory;
import com.huayin.common.exception.SystemException;
import com.huayin.common.exception.TransException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

/**
 * <pre>
 * 权限代理接口实现
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-21
 */
public class PermissionProviderImpl
{
	/**
	 * <pre>
	 * 私有静态内部类, 只有当有引用时, 该类才会被装载
	 * </pre>
	 * @author linriqing
	 * @version 1.0, 2009-4-28
	 */
	private final static class LazyInstance
	{
		protected final static Map<String, List<String>> blurActionMap = new HashMap<String, List<String>>();

		protected static SubSystemConfig defaultSubSystem;

		/**
		 * 不检查过滤的Action权限集合
		 */
		protected static Set<String> excludeActions = new HashSet<String>();

		protected final static Map<String, String> permissionActionMap = new HashMap<String, String>();

		protected final static Map<String, Permission> permissionMap = new HashMap<String, Permission>();

		protected final static Map<String, SubSystemConfig> subSystemMap = new HashMap<String, SubSystemConfig>();

		public final static PermissionProviderImpl zzz_foo = new PermissionProviderImpl(false);
	}

	protected final static Log logger = LogFactory.getLog(PermissionProviderImpl.class);

	/**
	 * <pre>
	 * 线程安全/单例模式的获取实例方法
	 * </pre>
	 * @return 权限代理接口实现类
	 */
	public final static PermissionProviderImpl getInstance()
	{
		return LazyInstance.zzz_foo;
	}

	/**
	 * 对没有配置权限的物件是否默认允许访问
	 */
	private boolean defaultAllowPrivilege = false;

	/**
	 * 构造函数
	 */
	private PermissionProviderImpl(boolean inject)
	{
		logger.info("开始初始化权限提供程序实现...");
		this.init();
		logger.info("成功初始化权限提供程序接口实现.");
	}

	private void addSubPermission(List<Permission> list, Permission permission)
	{
		list.addAll(Arrays.asList(permission.getChildActions()));
		list.addAll(Arrays.asList(permission.getChildNodes()));
		for (NodeObject nodeObject : permission.getChildNodes())
		{
			addSubPermission(list, nodeObject);
		}
		for (ActionObject nodeObject : permission.getChildActions())
		{
			addSubPermission(list, nodeObject);
		}
	}

	private void extractPermission(ActionObject action, ActionObject subaction)
	{
		LazyInstance.permissionMap.put(subaction.getId(), subaction);
		String destination = subaction.getDestination();
		if (destination != null)
		{
			String[] subDestination = destination.split(",");
			for (String string : subDestination)
			{
				LazyInstance.permissionActionMap.put(string.trim(), subaction.getId());
				if (string != null && string.endsWith("*"))
				{
					if(!LazyInstance.blurActionMap.containsKey(subaction.getId()))
					{
						LazyInstance.blurActionMap.put(subaction.getId(), new ArrayList<String>());
					}
					LazyInstance.blurActionMap.get(subaction.getId()).add(string.trim().replaceAll("\\*", ""));
				}
			}

			if (subaction instanceof NodeObject)
			{
				String defaultDest = "";
				if (subDestination.length > 0)
				{
					defaultDest = subDestination[0];
					if (defaultDest != null && defaultDest.endsWith("*"))
					{
						defaultDest = defaultDest.trim().replaceAll("\\*", "");
					}
				}
				subaction.setDestination(defaultDest);
			}
		}

		if ((subaction.getImportFile() != null) && (subaction.getImportFile().length() > 0))
		{
			logger.info("开始初始化子节点[" + subaction.getShowText() + "]权限物件, 配置文件[" + subaction.getImportFile() + "]");
			ConfigProvider<ModuleConfig> mcp = ConfigProviderFactory.getInstance(subaction.getImportFile(),
					ModuleConfig.class);
			Map<String, ModuleConfig> moduleMap = mcp.getAllConfig();
			ModuleConfig modconfig = (ModuleConfig) moduleMap.get(subaction.getId());

			NodeObject[] totalNodes = new NodeObject[subaction.getChildNodes().length + modconfig.getNodes().length];
			System.arraycopy(subaction.getChildNodes(), 0, totalNodes, 0, subaction.getChildNodes().length);
			System.arraycopy(modconfig.getNodes(), 0, totalNodes, subaction.getChildNodes().length,
					modconfig.getNodes().length);
			subaction.setChildNodes(totalNodes);

			ActionObject[] totalActions = new ActionObject[subaction.getChildActions().length
					+ modconfig.getActions().length];
			System.arraycopy(subaction.getChildActions(), 0, totalActions, 0, subaction.getChildActions().length);
			System.arraycopy(modconfig.getActions(), 0, totalActions, subaction.getChildActions().length,
					modconfig.getActions().length);
			subaction.setChildActions(totalActions);
		}

		NodeObject[] subNodes = subaction.getChildNodes();
		for (int k = 0; k < subNodes.length; k++)
		{
			NodeObject subpermission = subNodes[k];
			subpermission.setParent(subaction);
			extractPermission(subaction, subpermission);
		}

		ActionObject[] subActions = subaction.getChildActions();
		for (int k = 0; k < subActions.length; k++)
		{
			ActionObject subpermission = subActions[k];
			subpermission.setParent(subaction);
			extractPermission(subaction, subpermission);
		}
	}

	public List<Permission> getAllPermissionBySubSystem(String subsytemName)
	{
		ArrayList<Permission> list = new ArrayList<Permission>();
		SubSystemConfig ic = (SubSystemConfig) LazyInstance.subSystemMap.get(subsytemName);
		list.addAll(Arrays.asList(ic.getNodes()));
		list.addAll(Arrays.asList(ic.getActions()));
		for (NodeObject authorityObject : ic.getNodes())
		{
			addSubPermission(list, authorityObject);
		}
		for (ActionObject nodeObject : ic.getActions())
		{
			addSubPermission(list, nodeObject);
		}
		return list;
	}

	/**
	 * @return 对没有配置权限的物件是否默认允许访问
	 */
	public boolean getDefaultAllowPrivilege()
	{
		return defaultAllowPrivilege;
	}

	public SubSystemConfig getDefaultSubSystem()
	{
		return LazyInstance.defaultSubSystem;
	}

	/**
	 * @return 不检查过滤的权限集合
	 */
	public Set<String> getExcludeActions()
	{
		return LazyInstance.excludeActions;
	}

	public List<NodeObject> getMenuNodes(String subsytemName)
	{
		SubSystemConfig ic = (SubSystemConfig) LazyInstance.subSystemMap.get(subsytemName);
		return Arrays.asList(ic.getNodes());
	}

	public Permission getPermission(String permissionId)
	{
		return LazyInstance.permissionMap.get(permissionId);
	}

	public List<Permission> getPermission(String[] permissionIds)
	{
		List<Permission> list = new ArrayList<Permission>();
		for (String id : permissionIds)
		{
			Permission permission = getPermission(id);
			if (permission != null)
			{
				list.add(permission);
			}
		}
		return list;
	}

	public Permission getPermissionByAction(String action)
	{
		String permissionId = (String) LazyInstance.permissionActionMap.get(action);
		if (permissionId == null)
		{
			Set<Entry<String, List<String>>> values = LazyInstance.blurActionMap.entrySet();
			for (Entry<String, List<String>> permission : values)
			{
				for (String entry : permission.getValue())
				{
					if (action.startsWith(entry))
					{
						permissionId = permission.getKey();
						break;
					}
				}
			}
		}
		if (permissionId != null)
		{
			return (ActionObject) LazyInstance.permissionMap.get(permissionId);
		}
		else
		{
			return null;
		}
	}

	public List<Permission> getStructurePermissionBySubSystem(String subsytemName)
	{
		ArrayList<Permission> list = new ArrayList<Permission>();
		SubSystemConfig ic = (SubSystemConfig) LazyInstance.subSystemMap.get(subsytemName);
		list.addAll(Arrays.asList(ic.getNodes()));
		list.addAll(Arrays.asList(ic.getActions()));
		return list;
	}

	public Collection<SubSystemConfig> getSubSystems()
	{
		return LazyInstance.subSystemMap.values();
	}

	/**
	 * <pre>
	 * 初始化系统权限物件
	 * </pre>
	 */
	private void init()
	{
		try
		{
			logger.info("开始从配置文件[" + Constant.SYSTEM_PERMISSION_CONFIG_FILEPATH + "]初始化系统权限物件");
			ConfigProvider<SubSystemConfig> cp = ConfigProviderFactory.getInstance(
					Constant.SYSTEM_PERMISSION_CONFIG_FILEPATH, SubSystemConfig.class);
			Map<String, SubSystemConfig> map = cp.getAllConfig();
			for (Iterator<SubSystemConfig> iter = map.values().iterator(); iter.hasNext();)
			{
				SubSystemConfig config = (SubSystemConfig) iter.next();

				logger.info("开始初始化子系统[" + config.getShowText() + "]权限物件");

				LazyInstance.subSystemMap.put(config.getName(), config);
				if (config.getDefaultSystem())
				{
					LazyInstance.defaultSubSystem = config;
					this.defaultAllowPrivilege = config.getDefaultAllowPrivilege();
				}

				// 加载Action权限物件
				ActionObject[] actions = config.getActions();
				for (int j = 0; j < actions.length; j++)
				{
					ActionObject subpermission = actions[j];
					subpermission.setParent(null);
					extractPermission(null, subpermission);
				}

				// 加载Node权限物件
				NodeObject[] nodes = config.getNodes();
				Arrays.sort(nodes);
				for (int j = 0; j < nodes.length; j++)
				{
					NodeObject subpermission = nodes[j];
					subpermission.setParent(null);
					extractPermission(null, subpermission);
				}
				LazyInstance.excludeActions.addAll(Arrays.asList(config.getExcludeActions()));

				XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
				logger.warn(xstream.toXML(LazyInstance.blurActionMap));
			}
		}
		catch (RuntimeException e)
		{
			logger.error("初始化权限物件时异常", e);
			throw new SystemException("初始化权限物件时异常", e);
		}
	}

	public boolean validByAction(Passport passport, String destination) throws TransException
	{
		if (LazyInstance.excludeActions.contains(destination))
		{
			return true;
		}
		else
		{
			Permission authority = this.getPermissionByAction(destination);
			return this.validPermission(passport, authority);
		}
	}

	public boolean validPermission(Passport arg0, Permission arg1) throws TransException
	{
		String permissionId = null;
		if (arg1 != null)
		{
			permissionId = arg1.getId();
		}
		return this.validPermission(arg0, permissionId);
	}

	public boolean validPermission(Passport passport, String permissionId) throws TransException
	{
		if (permissionId == null)
		{
			return this.getDefaultAllowPrivilege();
		}
		else
		{
			if (LazyInstance.permissionMap.containsKey(permissionId))
			{
				if (passport != null)
				{
					return passport.getPermissions().contains(LazyInstance.permissionMap.get(permissionId));
				}
				else
				{
					throw new TransException(ServiceResultFactory.getRspMessage("4054"));
				}
			}
			else
			{
				return this.getDefaultAllowPrivilege();
			}
		}
	}
}
