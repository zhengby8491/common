package com.huayin.common.thread;

import java.io.Serializable;
import java.util.List;

import org.junit.Test;

public class LoopWorkControllerTest implements Serializable
{
	private static final long serialVersionUID = 4449029474346672346L;

	/**
	 * Test method for {@link com.hengpeng.common.thread.LoopWorkController#execute()}.
	 * @throws InterruptedException
	 */
	@Test
	public void testExecute() throws InterruptedException
	{
		LoopWorkable loopworkable = new LoopWorkable()
		{
			private static final long serialVersionUID = 9060088476462992565L;

			public boolean prepare(Object initObject)
			{
				System.out.println("do prepare");
				// try
				{
					// Thread.sleep(50);
				}
				// catch (InterruptedException e)
				{
					// e.printStackTrace();
				}
				return true;
			}

			public Object init()
			{
				System.out.println("do init");
				return null;
			}

			public Object doWork(Object initObject)
			{
				System.out.println("do doWork");
				return null;
			}
		};
		LoopWorkController loopWorkController = new LoopWorkController(loopworkable, 1, 1);
		WorkScenarioInfo execute = loopWorkController.execute();
		List<WorkThreadInfo> list = execute.threadInfo;
		for (WorkThreadInfo workThreadInfo : list)
		{
			System.out.println(workThreadInfo.threadName + ":" + workThreadInfo.workCounter.value());
		}
	}
}
