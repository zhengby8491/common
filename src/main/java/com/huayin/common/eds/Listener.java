/**
 * <pre>
 * Author:		zhaojitao
 * Create:	 	2010-6-24 下午04:13:20
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.eds;

import static com.huayin.common.eds.Listener.InstanceType.NEW;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 事件监听器注解
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-6-24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Listener
{
	/**
	 * <pre>
	 * 监听器初始化的方式
	 * FACTORY - 工厂方法
	 * NEW - 新创建
	 * CONTEXT - 通过Spring应用上下文获取
	 * </pre>
	 * @author zhaojitao
	 * @version 1.0, 2014-3-14
	 */
	public enum InstanceType
	{
		/**
		 * 初始化的时候，通过工厂方法调用
		 */
		FACTORY,
		/**
		 * 初始化的时候，新创建监听器对象
		 */
		NEW,
		/**
		 * 初始化的时候，通过Spring应用上下文获取
		 */
		CONTEXT;
	}

	/**
	 * @return 监听器创建方式{@link InstanceType}
	 */
	InstanceType create() default NEW;

	/**
	 * @return 监听的事件类型
	 */
	Class<? extends Event>[] events() default {};

	/**
	 * @return 排除监听的事件类型
	 */
	Class<? extends Event>[] excludes() default {};

	/**
	 * @return 监听器调用顺序
	 */
	int index() default -1;

	/**
	 * @return 监听器是否阻塞事件处理，如果是事件处理的需要等到监听器处理返回才能继续，且抛出的异常会影响事件发布者
	 */
	boolean isBlock() default false;

	/**
	 * @return 获取实例的工厂方法，当create方式为{@link com.huayin.common.eds.Listener.InstanceType.FACTORY}，将通过工厂方法创建
	 */
	String factory() default "";

	/**
	 * @return 获取实例的Spring应用上下文Bean名称，当create方式为{@link com.huayin.common.eds.Listener.InstanceType.CONTEXT}
	 *         ，将通过Bean名称获取
	 */
	String beanName() default "";
}
