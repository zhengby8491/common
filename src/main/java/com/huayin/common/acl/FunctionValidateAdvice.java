/**
 * <pre>
 * Title: 		AuthorityValidateAdvice.java
 * Author:		linriqing
 * Create:	 	2010-6-3 下午03:21:28
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.acl;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huayin.common.acl.annotation.Function;

/**
 * <pre>
 * 权限验证方法调用拦截器
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-6-3
 */
@Component("functionValidateAdvice")
public class FunctionValidateAdvice implements MethodInterceptor
{
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable
	{
		if (invocation.getMethod().isAnnotationPresent(RequestMapping.class))
		{
			if (invocation.getMethod().isAnnotationPresent(Function.class))
			{ // 有指定注解
				String operateName = invocation.getMethod().toGenericString();

				Annotation annotation = invocation.getMethod().getAnnotation(Function.class); // 获取指定注解
				if (annotation != null)
				{
					String authorityName = ((Function) annotation).showText(); // 从注解中获取角色
					System.out.println("执行操作：" + authorityName + ", 编号" + ((Function) annotation).id() + ", 方法名："
							+ operateName);
				}

				return invocation.proceed(); // 角色匹配，继续执行方法
			}
			else
			{ // 类方法没有自定义注解，默认是不允许操作的
				return invocation.proceed();
			}
		}
		else
		{
			// 默认是不允许操作的
			return invocation.proceed();
		}
	}
}
