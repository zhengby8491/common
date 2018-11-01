package com.huayin.common.web.download;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * 通用的下载Servlet
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-7-2
 */
public class DownloadServlet extends HttpServlet
{
	private static final Log logger = LogFactory.getLog(DownloadServlet.class);

	private static final long serialVersionUID = 9069413761489960738L;

	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
	{
		doPost(arg0, arg1);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException
	{
		try
		{
			FullFile fullFile = (FullFile) request.getAttribute(FullFile.DOWNLOADSERVLET_FULLFILE_ATTRNAME);
			if (fullFile != null)
			{
				ByteArrayOutputStream baos = fullFile.getBaos();
				byte[] buff = baos.toByteArray();

				logger.info("得到的数据长度:" + buff.length + "字节");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fullFile.getFileName() + "\"");
				logger.debug("ContentType:" + fullFile.getContent_Type());
				if ((fullFile.getContent_Type() == null) || ("".equals(fullFile.getContent_Type())))
				{
					response.setContentType("application/octet-stream");
				}
				else
				{
					response.setContentType(fullFile.getContent_Type());
				}
				response.getOutputStream().write(buff);
				response.getOutputStream().flush();
			}
		}
		catch (Exception e)
		{
			logger.info("下载文件异常", e);
			response.getWriter().println("下载文件错误, 没有找到指定的文件...");
			response.getWriter().close();
		}
	}
}