package com.huayin.common.template.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.file.FileUtil;
import com.huayin.common.file.WriteFile;
import com.huayin.common.template.ITemplate;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * <pre>
 * FreeMarker模板实现
 * </pre>
 * @author chenjian
 * @version 1.0, 2012-2-21
 */
public class FreemarkerTemplate implements ITemplate
{
	private static final Log log = LogFactory.getLog(FreemarkerTemplate.class);

	/**
	 * 模板目录
	 */
	private String templatePath;

	/**
	 * 次对象只需要实例化一次
	 */
	private Configuration config;

	/**
	 * 全局共享变量
	 */
	private Map<String, Object> sharedVariable;

	/**
	 * 文件编码类型
	 */
	private String encoding = "UTF-8";

	public FreemarkerTemplate(String templatePath, Map<String, Object> sharedVariable, String encoding)
	{
		this.templatePath = templatePath;
		this.sharedVariable = sharedVariable;
		if (encoding != null)
		{
			this.encoding = encoding;
		}
		this.init();
	}

	private void init()
	{
		config = new Configuration();
		// 设置要解析的模板所在的目录，并加载模板文件
		try
		{
			config.setDirectoryForTemplateLoading(new File(templatePath));
			if (sharedVariable != null)
			{
				for (Map.Entry<String, Object> entry : sharedVariable.entrySet())
				{
					config.setSharedVariable(entry.getKey(), entry.getValue());
				}
			}
		}
		catch (Exception e)
		{
			log.error("初始化Configuration异常.", e);
		}
		// 设置包装器，并将对象包装为数据模型
		config.setObjectWrapper(new DefaultObjectWrapper());
	}

	@Override
	public String createFile(final String templateName, final String filePath, final String fileName,
			final Map<String, Object> data, boolean rename) throws IOException
	{
		File f = FileUtil.createTempFile(filePath, fileName, new WriteFile()
		{
			public boolean write(File f)
			{
				Writer out = null;
				Template template = null;
				try
				{
					template = config.getTemplate(templateName, encoding);
					FileOutputStream fos = new FileOutputStream(f);
					out = new OutputStreamWriter(fos, encoding);
					template.process(data, out);
					log.info("FreeMarkert创建静态文件{" + fileName + "}创建成功");
					return true;
				}
				catch (Exception e)
				{
					log.error("FreeMarkert解析模板异常。", e);
					throw new RuntimeException(e);
				}
				finally
				{
					colseStream(out);
				}
			}
		});
		return f.getName();
	}

	@Override
	public String createString(String templateName, Map<String, Object> data)
	{
		Writer out = null;
		Template template = null;
		try
		{
			template = config.getTemplate(templateName, encoding);
			out = new StringWriter();
			template.process(data, out);
		}
		catch (Exception e)
		{
			log.error("FreeMarkert解析模板异常。", e);
			throw new RuntimeException(e);
		}
		finally
		{
			colseStream(out);
		}
		return out.toString();
	}

	private void colseStream(Writer out)
	{
		try
		{
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			log.error("FreeMarkert创建静态HTML关闭IO流出现异常。", e);
		}
	}
}
