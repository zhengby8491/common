package com.huayin.common.template.loader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import freemarker.cache.TemplateLoader;

/**
 * <pre>
 * 字符串模板解析
 * </pre>
 * @author chenjian
 * @version 1.0, 2012-11-22
 */
public class StringTemplateLoader implements TemplateLoader
{
	private String source;

	public StringTemplateLoader(String defaultTemplate)
	{
		this.source = defaultTemplate;
	}

	public void closeTemplateSource(Object templateSource) throws IOException
	{

	}

	public Object findTemplateSource(String name) throws IOException
	{
		return source;
	}

	public long getLastModified(Object templateSource)
	{
		return 0;
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException
	{
		return new StringReader((String) templateSource);
	}
}
