/**
 * <pre>
 * Title: 		Dom4jHelper.java
 * Project: 	AnteAgent
 * Type:		com.huayin.anteagent.util.Dom4jHelper
 * Author:		linriqing
 * Create:	 	2006-8-8 11:08:48
 * Copyright: 	Copyright (c) 2006
 * Company:		
 * <pre>
 */
package com.huayin.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.huayin.common.exception.SystemException;

/**
 * <pre>
 * Dom4j帮助类
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-8-8
 */
public class Dom4jHelper
{
	/**
	 * <pre>
	 * 通过配置文件获取XML文档实例
	 * </pre>
	 * @param configFileName 配置文件
	 * @return XML文档实例
	 */
	public static Document getDocument(String configFileName)
	{
		InputStream is = null;
		try
		{
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);
			SAXReader saxReader = new SAXReader();
			saxReader.setValidation(false);
			Document doc = saxReader.read(is);
			return doc;
		}
		catch (DocumentException e)
		{
			throw new SystemException("Load dom4j Document file[" + configFileName + "] failed!", e);
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
					is = null;
				}
				catch (IOException e)
				{
					throw new SystemException("Load dom4j Document file[" + configFileName + "] failed!", e);
				}
			}
		}
	}

	/**
	 * <pre>
	 * 通过配置文件获取XML文档实例
	 * </pre>
	 * @param configFileName 配置文件
	 * @return XML文档实例
	 */
	public static Document getDocumentByRealPath(String configFileName)
	{
		InputStream is = null;
		try
		{
			is = new FileInputStream(configFileName);
			SAXReader saxReader = new SAXReader();
			saxReader.setValidation(false);
			Document doc = saxReader.read(is);
			return doc;
		}
		catch (Exception e)
		{
			throw new SystemException("Load dom4j Document file[" + configFileName + "] failed!", e);
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
					is = null;
				}
				catch (IOException e)
				{
					throw new SystemException("Load dom4j Document file[" + configFileName + "] failed!", e);
				}
			}
		}
	}

	/**
	 * <pre>
	 * 获取指定节点属性值字符串列表
	 * </pre>
	 * @param document 操作的xml文档对象
	 * @param XPath 节点路径表达式， 如："//rootName/elementName/@attrName"
	 * @return 节点属性值字符串列表
	 */
	public static List<String> getNodeAttributeValues(Document document, String XPath)
	{
		List<String> values = new ArrayList<String>();
		if (XPath.indexOf("@") < 0)
		{
			throw new SystemException("Document[" + document.asXML() + "] XPath[" + XPath
					+ "] getNodeAttributeValue failed, need @ symbol!");
		}
		try
		{
			List<?> list = document.selectNodes(XPath);
			Iterator<?> it = list.iterator();
			while (it.hasNext())
			{
				Attribute attri = (Attribute) it.next();
				values.add(attri.getValue());
			}
		}
		catch (Exception e)
		{
			throw new SystemException("Document[" + document.asXML() + "] XPath[" + XPath
					+ "] getNodeAttributeValue failed!", e);
		}
		return values;
	}

	/**
	 * <pre>
	 * 根据主键属性值查找节点并返回节点指定属性值
	 * </pre>
	 * @param document 操作的xml文档对象
	 * @param XPath 节点路径表达式， 如："//rootName/elementName/...", 只支持element元素表达式
	 * @param keyAttr 主键属性名
	 * @param keyValue 主键属性值
	 * @param attrName 需要修改的属性名
	 * @return 属性值
	 */
	public static String getNodeAttrValueByKeyValue(Document document, String XPath, String keyAttr, String keyValue,
			String attrName)
	{
		String attrValue = null;
		try
		{
			List<?> list = document.selectNodes(XPath);
			Iterator<?> iter = list.iterator();
			while (iter.hasNext())
			{
				Element element = (Element) iter.next();
				if (element.attributeValue(keyAttr).equals(keyValue))
				{
					if (element.attribute(attrName) != null)
					{
						attrValue = element.attributeValue(attrName);
					}
				}
			}

		}
		catch (Exception e)
		{
			throw new SystemException("Document[" + document.asXML() + "] XPath[" + XPath
					+ "] getAttrValueByKeyValue failed!", e);
		}
		return attrValue;
	}

	/**
	 * <pre>
	 * 根据主键属性值查找节点并返回节点文本值
	 * </pre>
	 * @param document 操作的xml文档对象
	 * @param XPath 节点路径表达式， 如："//rootName/elementName/...", 只支持element元素表达式
	 * @param keyAttr 主键属性名
	 * @param keyValue 主键属性值
	 * @return 节点文本值
	 */
	public static String getNodeTextByKeyValue(Document document, String XPath, String keyAttr, String keyValue)
	{
		String nodeText = null;
		try
		{
			List<?> list = document.selectNodes(XPath);
			Iterator<?> iter = list.iterator();
			while (iter.hasNext())
			{
				Element element = (Element) iter.next();
				if (element.attributeValue(keyAttr).equals(keyValue))
				{
					nodeText = element.getText();
				}
			}

		}
		catch (Exception e)
		{
			throw new SystemException("Document[" + document.asXML() + "] XPath[" + XPath
					+ "] getNodeTextByKeyValue failed!", e);
		}
		return nodeText;
	}

	/**
	 * <pre>
	 * 获取指定节点文本字符串列表
	 * </pre>
	 * @param document 操作的xml文档对象
	 * @param XPath 节点路径表达式， 如："//rootName/elementName/...", 只支持element元素表达式
	 * @return 节点文本字符串列表
	 */
	public static List<String> getNodeTextValues(Document document, String XPath)
	{
		List<String> values = new ArrayList<String>();
		try
		{
			List<?> list = document.selectNodes(XPath);
			Iterator<?> it = list.iterator();
			while (it.hasNext())
			{
				Element text = (Element) it.next();
				values.add(text.getText());
			}
		}
		catch (Exception e)
		{
			throw new SystemException("Document[" + document.asXML() + "] XPath[" + XPath
					+ "] getNodeTextValue failed!", e);
		}
		return values;
	}

	/**
	 * <pre>
	 * 根据主键属性值查找节点并返回指定子节点文本值
	 * </pre>
	 * @param document 操作的xml文档对象
	 * @param XPath 节点路径表达式， 如："//rootName/elementName/...", 只支持element元素表达式
	 * @param keyAttr 主键属性名
	 * @param keyValue 主键属性值
	 * @param elementName 子节点名称
	 * @return 子节点文本值
	 */
	public static String getSubNodeTextByKeyValue(Document document, String XPath, String keyAttr, String keyValue,
			String elementName)
	{
		String nodeText = null;
		try
		{
			List<?> list = document.selectNodes(XPath);
			Iterator<?> iter = list.iterator();
			while (iter.hasNext())
			{
				Element element = (Element) iter.next();
				if (element.attributeValue(keyAttr).equals(keyValue))
				{
					Element subele = element.element(elementName);
					if (subele != null)
					{
						nodeText = subele.getText();
					}
				}
			}

		}
		catch (Exception e)
		{
			throw new SystemException("Document[" + document.asXML() + "] XPath[" + XPath
					+ "] getSubNodeTextByKeyValue failed!", e);
		}
		return nodeText;
	}

	/**
	 * 保存xml文件;
	 * @param document xml文件流;
	 * @param resourcePath 文件存储相对于classpath的路径(包括文件名)
	 */
	public static void saveDocument(Document document, String resourcePath)
	{
		XMLWriter output = null;
		try
		{
			String classPath = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
			OutputFormat format = new OutputFormat();
			format.setEncoding(document.getXMLEncoding());
			format.setIndent(true);
			format.setIndentSize(4);
			format.setNewlines(true);
			format.setNewLineAfterDeclaration(false);
			output = new XMLWriter(new FileWriter(new File(classPath + resourcePath)), format);
			output.write(document);
			output.close();
		}
		catch (IOException e)
		{
			throw new SystemException("Document[" + document.asXML() + "] resourcePath[" + resourcePath
					+ "] saveDocument failed!", e);
		}
		finally
		{
			if (output != null)
			{
				try
				{
					output.close();
					output = null;
				}
				catch (IOException e)
				{
					throw new SystemException("Close XMLWriter failed!  Document[" + document.asXML()
							+ "] resourcePath[" + resourcePath + "].", e);
				}
			}
		}
	}

	/**
	 * 保存xml文件;
	 * @param document xml文件流;
	 * @param realPath 文件存储的全路径(包括文件名)
	 */
	public static void saveDocumentByRealPath(Document document, String realPath)
	{
		XMLWriter output = null;
		try
		{
			OutputFormat format = new OutputFormat();
			format.setEncoding(document.getXMLEncoding());
			format.setIndent(true);
			format.setIndentSize(4);
			format.setNewlines(true);
			format.setNewLineAfterDeclaration(false);
			output = new XMLWriter(new FileWriter(new File(realPath)), format);
			output.write(document);
			output.close();
		}
		catch (IOException e)
		{
			throw new SystemException("Document[" + document.asXML() + "] realPath[" + realPath
					+ "] saveDocumentByRealPath failed!", e);
		}
		finally
		{
			if (output != null)
			{
				try
				{
					output.close();
					output = null;
				}
				catch (IOException e)
				{
					throw new SystemException("Close XMLWriter failed!  Document[" + document.asXML() + "] realPath["
							+ realPath + "].", e);
				}
			}
		}
	}

	/**
	 * <pre>
	 * 根据主键属性值查找节点并修改节点指定属性值
	 * </pre>
	 * @param document 操作的xml文档对象
	 * @param XPath 节点路径表达式， 如："//rootName/elementName/...", 只支持element元素表达式
	 * @param keyAttr 主键属性名
	 * @param keyValue 主键属性值
	 * @param modifyAttr 需要修改的属性名
	 * @param modifyAttrValue 修改属性新的值
	 */
	public static void setAttrValueByKeyValue(Document document, String XPath, String keyAttr, String keyValue,
			String modifyAttr, String modifyAttrValue)
	{
		try
		{
			List<?> list = document.selectNodes(XPath);
			Iterator<?> iter = list.iterator();
			while (iter.hasNext())
			{
				Element element = (Element) iter.next();
				if (element.attributeValue(keyAttr).equals(keyValue))
				{
					if (element.attribute(modifyAttr) != null)
					{
						element.addAttribute(modifyAttr, modifyAttrValue);
					}
					else
					{
						throw new SystemException("Document[" + document.asXML() + "] XPath[" + XPath
								+ "] hasn't attribute[" + modifyAttr + "], modify failed!");
					}
				}
			}

		}
		catch (SystemException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException("Document[" + document.asXML() + "] XPath[" + XPath
					+ "] modifyAttributeByKeyValue failed!", e);
		}
	}

	/**
	 * <pre>
	 * 根据主键属性值查找节点并修改节点文本
	 * </pre>
	 * @param document 操作的xml文档对象
	 * @param XPath 节点路径表达式， 如："//rootName/elementName/...", 只支持element元素表达式
	 * @param keyAttr 主键属性名
	 * @param keyValue 主键属性值
	 * @param textValue 节点文本新的值
	 */
	public static void setNodeTextByKeyValue(Document document, String XPath, String keyAttr, String keyValue,
			String textValue)
	{
		try
		{
			List<?> list = document.selectNodes(XPath);
			Iterator<?> iter = list.iterator();
			while (iter.hasNext())
			{
				Element element = (Element) iter.next();
				if (element.attributeValue(keyAttr).equals(keyValue))
				{
					element.setText(textValue);
				}
			}

		}
		catch (Exception e)
		{
			throw new SystemException("Document[" + document.asXML() + "] XPath[" + XPath
					+ "] modifyTextByKeyValue failed!", e);
		}
	}

	/**
	 * <pre>
	 * 根据主键属性值查找节点并修改子节点文本
	 * </pre>
	 * @param document 操作的xml文档对象
	 * @param XPath 节点路径表达式， 如："//rootName/elementName/...", 只支持element元素表达式
	 * @param keyAttr 主键属性名
	 * @param keyValue 主键属性值
	 * @param elementName 子节点的元素名
	 * @param textValue 子节点的文本新的值
	 */
	public static void setSubNodeTextByKeyValue(Document document, String XPath, String keyAttr, String keyValue,
			String elementName, String textValue)
	{
		try
		{
			List<?> list = document.selectNodes(XPath);
			Iterator<?> iter = list.iterator();
			while (iter.hasNext())
			{
				Element element = (Element) iter.next();
				if (element.attributeValue(keyAttr).equals(keyValue))
				{
					Element subElement = element.element(elementName);
					if (subElement != null)
					{
						subElement.setText(textValue);
					}
					else
					{
						throw new SystemException("Document[" + document.asXML() + "] XPath[" + XPath
								+ "] hasn't Element[" + elementName + "], modify failed!");
					}
				}
			}
		}
		catch (SystemException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException("Document[" + document.asXML() + "] XPath[" + XPath
					+ "] modifySubElementTextByKeyValue failed!", e);
		}
	}

	/**
	 * 更新xml文件;
	 * @param document xml文件流;
	 * @param resourcePath 文件存储相对于classpath的路径(包括文件名)
	 */
	public static void updateDocument(Document document, String resourcePath)
	{
		XMLWriter output = null;
		try
		{
			String classPath = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
			OutputFormat format = new OutputFormat();
			format.setEncoding(document.getXMLEncoding());
			output = new XMLWriter(new FileWriter(new File(classPath + resourcePath)), format);
			output.write(document);
			output.close();
		}
		catch (IOException e)
		{
			throw new SystemException("Document[" + document.asXML() + "] resourcePath[" + resourcePath
					+ "] saveDocument failed!", e);
		}
		finally
		{
			if (output != null)
			{
				try
				{
					output.close();
					output = null;
				}
				catch (IOException e)
				{
					throw new SystemException("Close XMLWriter failed!  Document[" + document.asXML()
							+ "] resourcePath[" + resourcePath + "].", e);
				}
			}
		}
	}

	/**
	 * 更新xml文件;
	 * @param document xml文件流;
	 * @param realPath 文件存储的全路径(包括文件名)
	 */
	public static void updateDocumentByRealPath(Document document, String realPath)
	{
		XMLWriter output = null;
		try
		{
			OutputFormat format = new OutputFormat();
			format.setEncoding(document.getXMLEncoding());
			output = new XMLWriter(new FileWriter(new File(realPath)), format);
			output.write(document);
			output.close();
		}
		catch (IOException e)
		{
			throw new SystemException("Document[" + document.asXML() + "] realPath[" + realPath
					+ "] saveDocumentByRealPath failed!", e);
		}
		finally
		{
			if (output != null)
			{
				try
				{
					output.close();
					output = null;
				}
				catch (IOException e)
				{
					throw new SystemException("Close XMLWriter failed!  Document[" + document.asXML() + "] realPath["
							+ realPath + "].", e);
				}
			}
		}
	}
}
