package com.huayin.common.template;

import java.util.Map;

import com.huayin.common.template.impl.FreemarkerTemplate;

/**
 * <pre>
 * 获取模板工具类
 * </pre>
 * @author chenjian
 * @version 1.0, 2012-2-21
 */
public class TemplateManager
{
	public static ITemplate createTemplate(String templatePath, Map<String, Object> sharedVariable, String encoding)
	{
		ITemplate template = new FreemarkerTemplate(templatePath, sharedVariable, encoding);
		return template;
	}

	public static ITemplate createTemplate(String templatePath, Map<String, Object> sharedVariable)
	{
		return createTemplate(templatePath, sharedVariable, null);
	}

	public static ITemplate createTemplate(String templatePath)
	{
		return createTemplate(templatePath, null, null);
	}
}
