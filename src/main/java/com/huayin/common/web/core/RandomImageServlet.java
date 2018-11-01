/**
 * <pre>
 * Title: 		RandomImageServlet.java
 * Project: 	HP-Common
 * Author:		zhaojitao
 * Create:	 	2007-6-25 上午04:56:44
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.web.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.huayin.common.configuration.ConfigProvider;
import com.huayin.common.configuration.ConfigProviderFactory;
import com.huayin.common.constant.Constant;
import com.huayin.common.util.RandomImageGenerator;

/**
 * <pre>
 * 登陆页面验证码随机图片生成工具
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2007-6-25
 */
public class RandomImageServlet extends HttpServlet
{
	/**
	 * 万能验证码保存的Properties名称
	 */
	public final static String KEYNAME_UNIVERSAL_CODE = "RandomImageServlet.universalCode";

	/**
	 * 验证码保存的HTTPSessison名称
	 */
	public final static String RANDOM_LOGIN_KEY = "RANDOM_LOGIN_KEY";

	private static final String NO_USE_UNIVERSAL_CODE = "-1";

	private static final long serialVersionUID = -5814215719111548111L;

	private static final String UNIVERSAL_CODE = getUniversalCode();

	/**
	 * <pre>
	 * 校验万能验证码
	 * </pre>
	 * @param arg2 校验码
	 * @return 是否开放万能校验码并且万能验证码是否相符
	 */
	public static boolean checkUniversalCode(String arg2)
	{
		if (UNIVERSAL_CODE.equalsIgnoreCase(NO_USE_UNIVERSAL_CODE))
		{
			return false;
		}
		else
		{
			return UNIVERSAL_CODE.equalsIgnoreCase(arg2);
		}
	}

	public static String getRandomLoginKey(HttpServletRequest req)
	{
		return (String) req.getSession().getAttribute(RANDOM_LOGIN_KEY);
	}

	/**
	 * <pre>
	 * 获取万能验证码
	 * </pre>
	 * @return 万能验证码
	 */
	private static String getUniversalCode()
	{
		ConfigProvider<?> cp = ConfigProviderFactory.getInstance(Constant.HY_COMMON_PROPERTIES_FILEPATH);
		if (cp != null)
		{
			Object value = cp.getConfigByPrimaryKey(KEYNAME_UNIVERSAL_CODE);
			if (value != null)
			{
				return value.toString();
			}
			else
			{
				return NO_USE_UNIVERSAL_CODE;
			}
		}
		else
		{
			return NO_USE_UNIVERSAL_CODE;
		}

	}

	public void init() throws ServletException
	{
		System.setProperty("java.awt.headless", "true");
	}

	/**
	 * <pre>
	 * 检查验证码
	 * </pre>
	 * @param arg0 request请求对象
	 * @param requestCode 验证码
	 * @return 是否相符
	 */
	public static boolean checkSessionConfirmCode(HttpServletRequest arg0, String requestCode)
	{
		HttpSession ssn = arg0.getSession();
		if (ssn != null)
		{
			Object attribute2 = ssn.getAttribute(RANDOM_LOGIN_KEY);
			if (attribute2 != null)
			{
				String attribute = (String) attribute2;
				if (requestCode.equalsIgnoreCase(attribute))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
	{
		HttpSession ssn = arg0.getSession();
		if (ssn != null)
		{
			String randomString = RandomImageGenerator.random(4);// 生成种子
			ssn.setAttribute(RANDOM_LOGIN_KEY, randomString);// 将种子放到session里面
			arg1.setContentType("image/jpeg");// 设置图像生成格式

			RandomImageGenerator.render(randomString, 4, 70, 20, arg1.getOutputStream());// 输出到页面
		}
	}

}
