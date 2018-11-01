/**
 * <pre>
 * Title: 		LoopWorkable.java
 * Project: 	Common-Util
 * Author:		linriqing
 * Create:	 	2009-6-5 下午03:01:55
 * Copyright: 	Copyright (c) 2009
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.thread;

import java.io.Serializable;

/**
 * <pre>
 * 可循环工作接口
 * </pre>
 * @author linriqing
 * @version 1.0, 2009-6-5
 */
public interface LoopWorkable extends Serializable
{
	/**
	 * <pre>
	 * 需要重复做的具体业务逻辑
	 * </pre>
	 * @param initObject 初始化生成的对象
	 * @return 执行结果
	 */
	Object doWork(Object initObject);

	/**
	 * <pre>
	 * 每次调用doWork前先调用prepare检查是否满足执行条件
	 * </pre>
	 * @param initObject 初始化生成的对象
	 * @return 是否满足执行条件
	 */
	boolean prepare(Object initObject);

	/**
	 * <pre>
	 * 独立线程初始化时会调用的方法
	 * </pre>
	 * @return 初始化的对象
	 */
	Object init();
}