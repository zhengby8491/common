/**
 * <pre>
 * Title: 		LoopWorkController.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2009-6-6 下午05:38:05
 * Copyright: 	Copyright (c) 2009
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.thread;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 循环工作线程控制类
 * 调用示例:
 * <code>
 *	WorkScenarioInfo workscenarioinfo = new LoopWorkController(new LoopWorkable()
 *	{
 *		private String result = null;
 *		public void doWork()
 *		{
 *			// do something and set result (option) for return...
 *		}
 *		public Object getResult()
 *		{
 *			// option, leave blank if no need result
 *			if (result != null)
 *			{
 *				return result;
 *			}
 *			else
 *			{
 *				return null;
 *			}
 *		}
 *		public boolean prepare()
 *		{
 *			// check loop conditions and return...
 *		}
 *	}, threadCount, runTime).execute();
 *	for (WorkThreadInfo workthreadinfo : workscenarioinfo.threadInfo)
 *	{
 *		for (WorkCellInfo workcellinfo : workthreadinfo.workInfos)
 *		{
 *			// process workcellinfo.result...
 *		}
 *	}
 * </code>
 * </pre>
 * @author linriqing
 * @version 1.0, 2009-6-7
 */
public class LoopWorkController
{
	private final LoopWorkable loopwork;

	private final int runTime;

	private final int threadCount;

	/**
	 * 构造函数
	 * @param loopwork 可循环工作的实现
	 * @param threadCount 线程数量， 当低于1000的时候， 启动的线程池大小等于threadCount
	 * @param runTime 循环工作执行持续的时间， 单位秒
	 */
	public LoopWorkController(LoopWorkable loopwork, int threadCount, int runTime)
	{
		this.threadCount = threadCount;
		this.runTime = runTime;
		this.loopwork = loopwork;
	}

	/**
	 * <pre>
	 * 启动循环工作
	 * </pre>
	 * @throws InterruptedException
	 */
	public WorkScenarioInfo execute() throws InterruptedException
	{
		WorkScenarioInfo workscenarioinfo = new WorkScenarioInfo();
		int poolSize = 1000;
		if (threadCount < poolSize)
		{
			poolSize = threadCount;
		}
		final ExecutorService exec = Executors.newFixedThreadPool(poolSize);
		Set<Future<?>> threads = new HashSet<Future<?>>();
		for (int i = 0; i < threadCount; i++)
		{
			Future<?> future = new FutureTask<Object>(new LoopWorkRunnable(String.valueOf(i), workscenarioinfo,
					loopwork), null);
			threads.add(future);
		}

		workscenarioinfo.startTime = System.currentTimeMillis();
		long runTimeInMillionSecond = runTime * 1000;
		for (Future<?> future : threads)
		{
			exec.execute((Runnable) future);
		}

		exec.shutdown();
		while ((System.currentTimeMillis() - workscenarioinfo.startTime) < runTimeInMillionSecond)
		{
			if (!exec.isTerminated())
			{
				exec.awaitTermination(20, TimeUnit.MILLISECONDS);
			}
			else
			{
				break;
			}
		}
		for (Future<?> future : threads)
		{
			future.cancel(true);
		}
		workscenarioinfo.endTime = System.currentTimeMillis();
		return workscenarioinfo;
	}
}