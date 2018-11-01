/**
 * <pre>
 * Title: 		ManifestUtil.java
 * Project: 	HP-Common
 * Author:		linriqing
 * Create:	 	2008-5-30 上午02:32:30
 * Copyright: 	Copyright (c) 2008
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * <pre>
 * MANIFEST.MF文件读取工具类
 * </pre>
 * @author linriqing
 * @version 1.0, 2008-5-30
 */
public class ManifestUtil
{
	/**
	 * <pre>
	 * 通过文件路径读取所有属性
	 * </pre>
	 * @param filePath MANIFEST.MF文件路径
	 * @return 所有属性
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Attributes loadManifest(String filePath) throws FileNotFoundException, IOException
	{
		// 下面这段代码可以取得META-INF/MANIFEST.MF文件中的所有属性信息
		Manifest man = new Manifest(new FileInputStream(filePath));
		return man.getMainAttributes();
	}

	/**
	 * <pre>
	 * 通过文件路径读取指定属性值
	 * </pre>
	 * @param filePath MANIFEST.MF文件路径
	 * @param attributeName 属性名称
	 * @return 指定属性值
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getAttribute(String filePath, String attributeName) throws FileNotFoundException, IOException
	{
		return ManifestUtil.loadManifest(filePath).getValue(attributeName);
	}
}
