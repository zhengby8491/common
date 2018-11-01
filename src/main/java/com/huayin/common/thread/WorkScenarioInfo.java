/**
 * <pre>
 * Title: 		WorkScenarioInfo.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2009-6-5 下午03:07:44
 * Copyright: 	Copyright (c) 2009
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 工作场景信息
 * </pre>
 * @author linriqing
 * @version 1.0, 2009-6-5
 */
public final class WorkScenarioInfo
{
	public long endTime;

	public long startTime;

	public Counter threadCounter = new Counter();

	public List<WorkThreadInfo> threadInfo = new ArrayList<WorkThreadInfo>();
}