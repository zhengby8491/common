package com.huayin.common.file;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 * 文件读写工具类
 * </pre>
 * @author chenjian
 * @version 1.0, 2012-2-25
 */
public class FileUtil
{
	/**
	 * 临时文件后缀名
	 */
	private final static String TEMP_FILE_SUFFIX = ".temp";

	/**
	 * <pre>
	 * 创建新文件
	 * </pre>
	 * @param fileName 文件名
	 * @param dir 文件路径
	 * @return 创建成功的文件
	 * @throws IOException
	 */
	public static File createTempFile(String dir, String fileName, WriteFile write)
	{
		File dirs = new File(dir);
		// 看文件夹是否存在，如果不存在新建目录
		if (!dirs.exists())
		{
			dirs.mkdirs();
		}
		File temp = new File(dir + File.separator + fileName + TEMP_FILE_SUFFIX);
		boolean isSuccess = write.write(temp);
		if (isSuccess)
		{
			File file = new File(dir + File.separator + fileName);
			boolean flag = false;
			while (!flag)
			{
				flag = temp.renameTo(file);
				if (!flag)
				{
					file.delete();
				}
			}
			return file;
		}
		else
		{
			throw new RuntimeException("创建临时文件发生异常");
		}
	}
}
