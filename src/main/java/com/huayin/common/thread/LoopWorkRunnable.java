/**
 * <pre>
 * Title: 		LoopWorkRunnable.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2009-6-5 下午04:14:38
 * Copyright: 	Copyright (c) 2009
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.thread;

/**
 * <pre>
 * 循环工作线程执行实现
 * </pre>
 * @author linriqing
 * @version 1.0, 2009-6-5
 */
public final class LoopWorkRunnable implements Runnable
{
	private final LoopWorkable loopwork;

	private final String threadName;

	private final WorkScenarioInfo workscenarioinfo;

	/**
	 * 构造函数
	 * @param threadName 线程名称
	 * @param workscenarioinfo 场景测试结果对象
	 * @param loopwork 单元测试实现
	 */
	public LoopWorkRunnable(String threadName, WorkScenarioInfo workscenarioinfo, LoopWorkable loopwork)
	{
		this.threadName = threadName;
		this.workscenarioinfo = workscenarioinfo;
		this.loopwork = loopwork;
	}

	public void run()
	{
		WorkThreadInfo threadInfo = new WorkThreadInfo();
		threadInfo.threadName = this.threadName;
		workscenarioinfo.threadInfo.add(threadInfo);
		threadInfo.startTime = System.currentTimeMillis();
		threadInfo.workCounter = new Counter(threadInfo.threadName);
		try
		{
			Object initObject = loopwork.init();
			while (!Thread.currentThread().isInterrupted())
			{
				WorkCellInfo workInfo = new WorkCellInfo();
				if (!loopwork.prepare(initObject))
				{
					break;
				}

				long requestStartTime = System.currentTimeMillis();
				Object result = loopwork.doWork(initObject);
				long requestEndTime = System.currentTimeMillis();
				threadInfo.endTime = System.currentTimeMillis();

				if (!Thread.currentThread().isInterrupted())
				{
					threadInfo.workCounter.increase();
					workInfo.startTime = requestStartTime;
					workInfo.endTime = requestEndTime;
					workInfo.result = result;
					threadInfo.workInfos.add(workInfo);
				}
			}
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		finally
		{
			threadInfo.endTime = System.currentTimeMillis();
			synchronized (workscenarioinfo)
			{
				workscenarioinfo.threadCounter.increase();
				workscenarioinfo.notifyAll();
			}
		}
	}
}