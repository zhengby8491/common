/**
 * <pre>
 * Title: 		ModuleButtonsTag.java
 * Project: 	AgentPortal
 * Author:		linriqing
 * Create:	 	2007-6-28 上午02:10:29
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.web.core.tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.acl.impl.PermissionProviderImpl;
import com.huayin.common.acl.vo.NodeObject;
import com.huayin.common.acl.vo.Passport;
import com.huayin.common.constant.Constant;
import com.huayin.common.exception.SystemException;
import com.huayin.common.exception.TransException;
import com.huayin.common.web.tags.ConvertParameter;
import com.huayin.common.web.tags.ConvertProcessor;

/**
 * <pre>
 * 模块按钮标签
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-28
 */
public class ModuleButtonsTag extends BodyTagSupport
{
	private static Log logger = LogFactory.getLog(ModuleButtonsTag.class);

	private static final long serialVersionUID = 5239801873624475001L;

	private List<NodeObject> buttons;

	private Iterator<NodeObject> itrt;

	private String moduleName;

	private NodeObject nowModule;

	public ModuleButtonsTag()
	{
		super();
		buttons = new ArrayList<NodeObject>();
	}

	public int doAfterBody() throws JspException
	{
		super.doAfterBody();

		BodyContent body = this.getBodyContent();
		String content = body.getString();
		body.clearBody();

		String result = process(content);

		if (!"".equals(result))
		{
			try
			{
				getPreviousOut().print(result);
			}
			catch (IOException e)
			{
				logger.error(e.toString());
				throw new JspException(e);
			}
		}
		while (itrt.hasNext())
		{
			NodeObject moduleNode = itrt.next();
			buttons.clear();
			if (this.isNodeShow(moduleNode))
			{
				if (isParentNodeShow(moduleNode))
				{
					nowModule = moduleNode;
					moduleName = moduleNode.getShowText();
					List<NodeObject> listFunction = Arrays.asList(moduleNode.getChildNodes());
					Iterator<NodeObject> btIt = listFunction.iterator();
					while (btIt.hasNext())
					{
						NodeObject functionNode = btIt.next();
						if (this.isNodeShow(functionNode))
						{
							if (isParentNodeShow(functionNode))
							{
								buttons.add(functionNode);
							}
						}
					}

					return EVAL_BODY_AGAIN;
				}
			}
		}
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException
	{
		return super.doEndTag();
	}

	public int doStartTag() throws JspException
	{
		PermissionProviderImpl instance = PermissionProviderImpl.getInstance();
		itrt = instance.getMenuNodes(instance.getDefaultSubSystem().getName()).iterator();

		while (itrt.hasNext())
		{
			NodeObject moduleNode = (NodeObject) itrt.next();
			buttons.clear();

			if (this.isNodeShow(moduleNode))
			{
				if (isParentNodeShow(moduleNode))
				{
					nowModule = moduleNode;
					this.moduleName = moduleNode.getShowText();

					Iterator<NodeObject> btIt = Arrays.asList(moduleNode.getChildNodes()).iterator();

					while (btIt.hasNext())
					{
						NodeObject functionNode = (NodeObject) btIt.next();
						if (this.isNodeShow(functionNode))
						{
							if (isParentNodeShow(functionNode))
							{
								buttons.add(functionNode);
							}
						}
					}

					return EVAL_BODY_BUFFERED;
				}
			}
		}

		return SKIP_BODY;
	}

	public List<NodeObject> getButtons()
	{
		return this.buttons;
	}

	public String getModuleName()
	{
		return moduleName;
	}

	public NodeObject getNowModule()
	{
		return nowModule;
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	public void setNowModule(NodeObject nowModule)
	{
		this.nowModule = nowModule;
	}

	public boolean isNodeShow(NodeObject bc) throws JspException
	{
		Passport passport = (Passport) this.pageContext.getSession().getAttribute(Constant.PROFILE_SESSION_NAME);

		if (passport == null)
		{
			return false;
		}
		else
		{
			try
			{
				return PermissionProviderImpl.getInstance().validPermission(passport, bc);
			}
			catch (TransException e)
			{
				throw new SystemException(e);
			}
		}
	}

	public boolean isParentNodeShow(NodeObject bc) throws JspException
	{
		Passport passport = (Passport) this.pageContext.getSession().getAttribute(Constant.PROFILE_SESSION_NAME);

		if (passport == null)
		{
			return false;
		}
		else
		{
			try
			{
				boolean validPermission = PermissionProviderImpl.getInstance().validPermission(passport, bc);
				if (validPermission)
				{
					List<NodeObject> childNodes = new ArrayList<NodeObject>(Arrays.asList(bc.getChildNodes()));
					List<NodeObject> buttomChildNodes = new ArrayList<NodeObject>();
					if (childNodes.size() == 0)
					{
						return validPermission;
					}
					else
					{
						boolean onemoreValid = false;
						// 验证有需要显示的字节点
						while (childNodes.size() > 0)
						{
							// 遍历字节点
							for (NodeObject nodeObject : childNodes)
							{
								// 字节点是否有权限的
								boolean childValid = PermissionProviderImpl.getInstance().validPermission(passport,
										nodeObject);
								if (childValid)
								{
									NodeObject[] subChildNodes = nodeObject.getChildNodes();
									// 查找是否还有字节点
									if (subChildNodes.length > 0)
									{
										buttomChildNodes.addAll(Arrays.asList(subChildNodes));
									}
									else
									{
										// 如果没有则表示为功能项, 父节点需要显示, 直接跳出循环
										onemoreValid = true;
										break;
									}
								}
							}
							// 已经明确父节点需要显示, 直接跳出循环
							if (onemoreValid)
							{
								break;
							}

							// 有权限的子节点都有下一层的子节点
							if (buttomChildNodes.size() > 0)
							{
								// 继续深入下一层判断
								childNodes.clear();
								childNodes.addAll(buttomChildNodes);
								buttomChildNodes.clear();
							}
							else
							{
								// 字节点都没有权限或者有权限的子节点都没有下一层的子节点, 父节点可以不用显示了
								break;
							}
						}
						return onemoreValid;
					}
				}
				else
				{
					return validPermission;
				}
			}
			catch (TransException e)
			{
				throw new SystemException(e);
			}
		}
	}

	private String process(String template)
	{
		return ConvertParameter.convert(template, new ConvertProcessor()
		{
			public String replace(String key)
			{
				if ("modulename".equals(key))
				{
					return nowModule.getShowText();
				}
				else if ("moduledest".equals(key))
				{
					String dest = nowModule.getDestination();
					if (dest != null)
					{
						if (dest.startsWith("/"))
						{
							HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
							dest = req.getContextPath() + dest;
						}
						return dest;
					}
					else
					{
						return "";
					}
				}
				else if ("moduleid".equals(key))
				{
					return nowModule.getId();
				}
				else if ("moduledesc".equals(key))
				{
					return nowModule.getDescription();
				}
				else
				{
					return "{" + key + "}";
				}
			}
		});
	}

}
