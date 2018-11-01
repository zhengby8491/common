package com.huayin.common.template;

import java.io.IOException;
import java.util.Map;

public interface ITemplate
{
	public String createString(String templateName, Map<String, Object> data);

	public String createFile(String templateName, String filePath, String fileName, Map<String, Object> data,
			boolean rename) throws IOException;
}
