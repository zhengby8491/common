/**
 * <pre>
 * Title: 		FunctionButtonsTag.java
 * Project: 	AgentPortal
 * Type:		com.huayin.agentportal.core.acl.tags.FunctionButtonsTag
 * Author:		linriqing
 * Create:	 	2007-6-28 上午02:10:29
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.web.core.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.acl.vo.NodeObject;
import com.huayin.common.web.tags.ConvertParameter;
import com.huayin.common.web.tags.ConvertProcessor;

/**
 * <pre>
 * 功能按钮标签
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-28
 */
public class FunctionButtonsTag extends BodyTagSupport
{
	private final static Log logger = LogFactory.getLog(FunctionButtonsTag.class);

	private static final long serialVersionUID = -4616188357732519639L;

	private Iterator<NodeObject> btIt;

	private NodeObject nowButton;

	private ModuleButtonsTag parentButtonTag;

	public FunctionButtonsTag()
	{
		super();
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
				throw new JspException(e);
			}
		}
		NodeObject[] childNodes = nowButton.getChildNodes();
		for (NodeObject nodeObject : childNodes)
		{
			if (parentButtonTag.isNodeShow(nodeObject))
			{
				if (parentButtonTag.isParentNodeShow(nodeObject))
				{
					nowButton = nodeObject;
					return EVAL_BODY_AGAIN;
				}
			}
		}

		if (btIt.hasNext())
		{
			nowButton = btIt.next();
			return EVAL_BODY_AGAIN;
		}
		else
		{
			return SKIP_BODY;
		}
	}

	public int doEndTag() throws JspException
	{
		return super.doEndTag();
	}

	public int doStartTag() throws JspException
	{
		super.doStartTag();
		parentButtonTag = (ModuleButtonsTag) this.getParent();
		logger.debug("Doing FunctionButtonTag Tag, module name: " + this.parentButtonTag.getModuleName());
		btIt = parentButtonTag.getButtons().iterator();

		if (btIt.hasNext())
		{
			nowButton = btIt.next();
			return EVAL_BODY_BUFFERED;
		}

		return SKIP_BODY;
	}

	private String process(String content)
	{
		return ConvertParameter.convert(content, new ConvertProcessor()
		{
			public String replace(String key)
			{
				if ("name".equals(key))
				{
					return nowButton.getShowText();
				}
				else if ("dest".equals(key))
				{
					String dest = nowButton.getDestination();
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
				else if ("id".equals(key))
				{
					return nowButton.getId();
				}
				else if ("parent".equals(key))
				{
					if (nowButton.getParent() != null)
					{
						return nowButton.getParent().getId();
					}
					else
					{
						return "{" + key + "}";
					}
				}
				else if ("desc".equals(key))
				{
					return nowButton.getDescription();
				}
				else if ("moduleid".equals(key))
				{
					return parentButtonTag.getNowModule().getId();
				}
				else
				{
					return "{" + key + "}";
				}
			}
		});
	}

}
