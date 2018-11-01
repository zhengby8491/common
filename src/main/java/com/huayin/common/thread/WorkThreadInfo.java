/**
 * <pre>
 * Title: 		WorkThreadInfo.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2009-6-5 下午03:04:30
 * Copyright: 	Copyright (c) 2009
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 工作线程信息
 * </pre>
 * @author linriqing
 * @version 1.0, 2009-6-5
 */
public final class WorkThreadInfo
{
	public long endTime;

	public long startTime;

	public String threadName;

	public Counter workCounter;

	public List<WorkCellInfo> workInfos = new ArrayList<WorkCellInfo>();

}