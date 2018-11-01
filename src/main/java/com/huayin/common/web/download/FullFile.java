package com.huayin.common.web.download;

import java.io.ByteArrayOutputStream;

/**
 * @author linriqing
 * @version 1.0 下载对象类
 */
public class FullFile
{
	public static final String DOWNLOADSERVLET_FULLFILE_ATTRNAME = "DownloadServlet_FullFile_AttrName";

	/**
	 * 输出流
	 */
	private ByteArrayOutputStream baos;

	/**
	 * 内容类型
	 */
	private String content_Type;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * @return 输出流
	 */
	public ByteArrayOutputStream getBaos()
	{
		return baos;
	}

	/**
	 * @return 内容类型
	 */
	public String getContent_Type()
	{
		return content_Type;
	}

	/**
	 * @return 文件名
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param baos 输出流
	 */
	public void setBaos(ByteArrayOutputStream baos)
	{
		this.baos = baos;
	}

	/**
	 * @param content_Type 内容类型
	 */
	public void setContent_Type(String content_Type)
	{
		this.content_Type = content_Type;
	}

	/**
	 * @param fileName 文件名
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
}
