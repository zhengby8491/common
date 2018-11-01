/**
 * <pre>
 * Title: 		Function.java
 * Author:		linriqing
 * Create:	 	2010-6-7 下午02:16:38
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.acl.annotation;
import static com.huayin.common.acl.annotation.FunctionType.BUTTON;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 受管理的功能项
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-6-7
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Function
{
	int id();

	String showText();

	FunctionType type() default BUTTON;
}
