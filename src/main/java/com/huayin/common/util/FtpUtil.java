package com.huayin.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.SocketException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.huayin.common.file.FileUtil;
import com.huayin.common.file.WriteFile;

/**
 * <pre>
 * Ftp上传下载工具类
 * 支持gizp压缩
 * 注意ftp上路径或文件名不要带中文，否则因为操作系统差异可能会出现乱码
 * </pre>
 * @author chenjian
 * @version 1.0, 2012-2-25
 */
public class FtpUtil
{
	private static final Log log = LogFactory.getLog(FtpUtil.class);

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * ftp地址
	 */
	private String server;

	/**
	 * ftp端口号
	 */
	private int port = 21;

	private String encode = "UTF-8";

	private final static int BUFFER_SIZE = 1024 * 4;

	private FTPClient ftpClient;

	public FtpUtil(String server, String username, String password, int port)
	{
		this(server, username, password);
		this.port = port;
	}

	public FtpUtil(String server, String username, String password)
	{
		this.username = username;
		this.password = password;
		this.server = server;
		this.init();
	}

	private void init()
	{

	}

	/**
	 * <pre>
	 * 连接FTP服务器
	 * </pre>
	 * @throws SocketException
	 * @throws IOException
	 */
	public void connect() throws SocketException, IOException
	{
		ftpClient = new FTPClient();
		ftpClient.connect(server, port);
		// log.info("系统连接到FTP服务器{" + server + "}成功!!!");
		ftpClient.login(username, password);
		ftpClient.setControlEncoding("UTF-8");
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	}

	/**
	 * <pre>
	 * ftp上传文本文件
	 * </pre>
	 * @param name 文件名
	 * @param text 文件内容
	 * @param isGzip 是否使用gizp压缩
	 * @return 成功标识
	 */
	public boolean upload(String name, String text, boolean isGzip)
	{
		byte[] by = null;
		try
		{
			by = text.getBytes(encode);
		}
		catch (Exception ex)
		{
			log.error("获取字符串编码错误", ex);
		}
		ByteArrayInputStream bi = new ByteArrayInputStream(by);
		if (!isGzip)
		{
			return upload(name, bi);
		}
		else
		{
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			try
			{
				GZIPOutputStream go = new GZIPOutputStream(bo);
				byte b[] = new byte[BUFFER_SIZE];
				int i;
				while ((i = bi.read(b)) != -1)
				{
					go.write(b, 0, i);
				}
				go.finish();
				go.close();
				InputStream is = new ByteArrayInputStream(bo.toByteArray());
				return upload(name, is);
			}
			catch (IOException e)
			{
				log.error("FTP上传文件发生异常.", e);
			}
		}
		return false;
	}

	public boolean upload(String name, InputStream stream)
	{
		boolean flag = false;
		if (ftpClient.isConnected())
		{
			try
			{
				flag = ftpClient.storeFile(name, stream);
			}
			catch (IOException e)
			{
				flag = false;
				log.error("FTP上传文件发生异常.", e);
			}
			finally
			{
				try
				{
					stream.close();
				}
				catch (IOException e)
				{
					log.error("关闭IO流异常", e);
				}
			}
		}
		else
		{
			log.info("Ftp连接不是活动状态");
		}
		return flag;
	}

	/**
	 * <pre>
	 * ftp下载文本文件
	 * </pre>
	 * @param remoteFileName 远程文件名
	 * @param isGzip 是否使用gzip解压
	 * @return 下载的字符串
	 */
	public String download(String remoteFileName, boolean isGzip)
	{
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try
		{
			boolean tr = ftpClient.retrieveFile(remoteFileName, bo);
			if (tr)
			{
				InputStreamReader ir = null;
				if (!isGzip)
				{
					ir = new InputStreamReader(new ByteArrayInputStream(bo.toByteArray()), encode);
				}
				else
				{
					ir = new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(bo.toByteArray())), encode);
				}
				BufferedReader br = new BufferedReader(ir);
				StringWriter sw = new StringWriter();
				char[] ch = new char[BUFFER_SIZE];
				int i;
				while ((i = br.read(ch)) != -1)
				{
					sw.write(ch, 0, i);
				}
				sw.flush();
				sw.close();
				br.close();
				bo.close();
				return sw.toString();
			}
			else
			{
				return null;
			}
		}
		catch (IOException e)
		{
			log.error("ftp下载文件失败", e);
			throw new RuntimeException("ftp下载文件失败");
		}
	}

	public boolean download(final String remoteFileName, String localPath, String localName)
	{
		if (ftpClient.isConnected())
		{
			try
			{
				FileUtil.createTempFile(localPath, localName, new WriteFile()
				{
					public boolean write(File f)
					{
						OutputStream stream = null;
						try
						{
							stream = new FileOutputStream(f);
							boolean tr = ftpClient.retrieveFile(remoteFileName, stream);
							return tr;
						}
						catch (IOException e)
						{
							log.error("FTP下载文件发生异常.", e);
							return false;
						}
						finally
						{
							try
							{
								stream.close();
							}
							catch (IOException e)
							{
								log.error("关闭IO流异常", e);
							}
						}
					}
				});
				return true;
			}
			catch (Exception ex)
			{
				log.error("ftp下载文件失败", ex);
			}
		}
		else
		{
			log.info("Ftp连接不是活动状态");
		}
		return false;
	}

	/**
	 * <pre>
	 * 关闭ftp连接
	 * </pre>
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		// log.info("关闭ftp链接{" + server + "}");
		if (ftpClient.isConnected())
		{
			ftpClient.disconnect();
		}
	}

	/**
	 * <pre>
	 * 更改工作目录
	 * </pre>
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean changeDirectory(String path) throws IOException
	{
		return ftpClient.changeWorkingDirectory(path);
	}

	/**
	 * <pre>
	 * 创建ftp目录
	 * </pre>
	 * @param pathName
	 * @return
	 * @throws IOException
	 */
	public boolean createDirectory(String pathName) throws IOException
	{
		return ftpClient.makeDirectory(pathName);
	}

	/**
	 * <pre>
	 * 获取当前目录下的文件列表
	 * </pre>
	 * @return
	 * @throws IOException
	 */
	public String[] getListFiels() throws IOException
	{
		return ftpClient.listNames();
	}

	public FTPFile[] listFiles() throws IOException
	{
		return ftpClient.listFiles();
	}

	public boolean deleteFile(String filename) throws IOException
	{
		return ftpClient.deleteFile(filename);
	}

	public boolean renameFile(String filename, String newPath) throws IOException
	{
		return ftpClient.rename(filename, newPath);
	}
}
