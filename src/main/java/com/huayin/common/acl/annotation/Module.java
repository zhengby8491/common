/**
 * <pre>
 * Title: 		Module.java
 * Author:		linriqing
 * Create:	 	2010-6-7 下午02:59:14
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.acl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 受管理的模块项
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-6-7
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Module
{

	public String showText();

	public String parent() default "";
}
