/**
 * <pre>
 * Title: 		RandomImageGenerator.java
 * Project: 	HP-Common
 * Type:		com.huayin.common.web.RandomImageGenerator
 * Author:		linriqing
 * Create:	 	2007-6-25 上午04:49:23
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang.RandomStringUtils;

/**
 * <pre>
 * 随机图片生成器
 * 该类用于用户登录时候需要用户根据图片内容进行填写正确后方可登录
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-25
 */
public class RandomImageGenerator
{
	// 随即生成包含验证码的字符串

	public static void main(String[] args) throws IOException
	{
		String num = random(4);
		System.out.println(num);
		renderClearNum(num, 4, new FileOutputStream("D:\\tes2.jpg"));
		System.out.println("Image generated.");
	}

	/**
	 * 根据要求的数字生成图片,背景为白色,字体大小16,字体颜色黑色粗体
	 * @param num 要生成的数字
	 * @param out 输出流
	 * @throws IOException
	 */
	public static void renderClearNum(String num, int length, OutputStream out) throws IOException
	{
		if (num.getBytes().length > length)
		{
			throw new IllegalArgumentException("The length of param num cannot exceed " + length + ".");
		}

		// 设定宽度和高度
		int width = 90;

		int height = 25;

		// 在内存中创建图象
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics2D g = (Graphics2D) bi.getGraphics();
		g.setColor(getRandColor(200, 250, 200));
		// 画边框
		java.util.Random random = new java.util.Random();
		g.setColor(getRandColor(200, 250, 200));
		g.fillRect(0, 0, width, height);

		// 设置字体
		Font mFont = new Font("Tahoma", Font.BOLD, 16);
		g.setFont(mFont);
		g.setColor(Color.BLACK);// 设置字体颜色

		// 画认证码,每个认证码在不同的水平位置
		String[] str1 = new String[length];
		for (int i = 0; i < str1.length; i++)
		{
			str1[i] = num.substring(i, i + 1);
			// 随即生成颜色
			Color color1 = new Color(random.nextInt(180), random.nextInt(180), random.nextInt(180));
			g.setColor(color1);
			g.drawString(str1[i], 20 * i + 10, 19);
		}

		// 图像生效
		g.dispose();

		// 输出页面
		ImageIO.write(bi, "jpg", out);
	}

	public static String random(int length)
	{
		// 因为o和0,l和1很难区分,所以,去掉大小写的o和l
		String str = "0123456789";
		// str =
		// "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
		// 初始化种子
		str = "23456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
		return RandomStringUtils.random(length, str);// 返回6为的字符串
	}

	/**
	 * 根据要求的数字生成图片,背景为白色,字体大小16,字体颜色黑色粗体
	 * @param num 要生成的数字
	 * @param out 输出流
	 * @throws IOException
	 */
	public static void render(String num, int length, int width, int height, OutputStream out) throws IOException
	{
		if (num.getBytes().length > length)
		{
			throw new IllegalArgumentException("The length of param num cannot exceed 6.");
		}

		// 在内存中创建图象
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics2D g = (Graphics2D) bi.getGraphics();

		// 画边框
		java.util.Random random = new java.util.Random();
		g.setColor(getRandColor(200, 250, 200));
		g.fillRect(0, 0, width, height);

		// 设置字体
		Font mFont = new Font("Tahoma", Font.BOLD, 16);
		g.setFont(mFont);
		g.setColor(Color.BLACK);// 设置字体颜色

		// 画认证码,每个认证码在不同的水平位置
		String str1[] = new String[length];
		int floatNum = ((height - 4) - 12) / 2;
		int topLine = 12 + floatNum;
		int hGap = width / (length + 1);
		int hLine = (width / (length + 1)) / 2 + (hGap - 12) / 2;
		for (int i = 0; i < str1.length; i++)
		{
			str1[i] = num.substring(i, i + 1);
			int w = 0;
			int x = (i + 1) % 3;

			// 随即生成验证码字符的水平偏移量
			if (x == random.nextInt(3))
			{
				w = topLine - random.nextInt(floatNum);
			}
			else
			{
				w = topLine + random.nextInt(floatNum);
			}

			// 随即生成颜色
			Color color1 = new Color(random.nextInt(180), random.nextInt(180), random.nextInt(180));
			g.setColor(color1);
			g.drawString(str1[i], hGap * i + hLine, w);
		}

		// // 随机产生干扰点,并用不同的颜色表示，使图象中的认证码不易被其它程序探测到
		// for (int i = 0; i < 100; i++)
		// {
		// int x = random.nextInt(width);
		// int y = random.nextInt(height);
		// Color color1 = new Color(random.nextInt(255), random.nextInt(255),
		// random.nextInt(255));
		// g.setColor(color1); // 随即画各种颜色的点
		// g.drawOval(x, y, 0, 0);
		// }

		// 画干扰线
		for (int i = 0; i < (length - 1); i++)
		{
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(12);
			int y1 = random.nextInt(12);
			Color color1 = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));

			g.setColor(color1); // 随即画各种颜色的线
			g.drawLine(x, y, x + x1, y + y1);
		}

		// 图像生效
		g.dispose();

		// 输出页面
		ImageIO.write(bi, "jpg", out);
	}

	/**
	 * 根据要求的数字生成图片,背景为白色,字体大小16,字体颜色黑色粗体
	 * @param num 要生成的数字
	 * @param out 输出流
	 * @throws IOException
	 */
	public static void render(String num, int length, OutputStream out) throws IOException
	{
		if (num.getBytes().length > length)
		{
			throw new IllegalArgumentException("The length of param num cannot exceed " + length + ".");
		}

		// 设定宽度和高度
		int width = 90;

		int height = 25;

		// 在内存中创建图象
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics2D g = (Graphics2D) bi.getGraphics();

		// 画边框
		java.util.Random random = new java.util.Random();
		g.setColor(getRandColor(200, 250, 200));
		g.fillRect(0, 0, width, height);

		// 设置字体
		Font mFont = new Font("Tahoma", Font.BOLD, 16);
		g.setFont(mFont);
		g.setColor(Color.BLACK);// 设置字体颜色

		// 画认证码,每个认证码在不同的水平位置
		String[] str1 = new String[length];
		for (int i = 0; i < str1.length; i++)
		{
			str1[i] = num.substring(i, i + 1);
			int w = 0;
			int x = (i + 1) % 3;

			// 随即生成验证码字符的水平偏移量
			if (x == random.nextInt(3))
			{
				w = 19 - random.nextInt(7);
			}
			else
			{
				w = 19 + random.nextInt(7);
			}

			// 随即生成颜色
			Color color1 = new Color(random.nextInt(180), random.nextInt(180), random.nextInt(180));
			g.setColor(color1);
			g.drawString(str1[i], 20 * i + 10, w);
		}

		// 随机产生干扰点,并用不同的颜色表示，使图象中的认证码不易被其它程序探测到
		for (int i = 0; i < 100; i++)
		{
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			Color color1 = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
			g.setColor(color1); // 随即画各种颜色的点
			g.drawOval(x, y, 0, 0);
		}

		// 画干扰线
		for (int i = 0; i < 5; i++)
		{
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(12);
			int y1 = random.nextInt(12);
			// 随即画各种颜色的线
			g.setColor(getRandColor(160, 200, 160));
			g.drawLine(x, y, x + x1, y + y1);
		}

		// 图像生效
		g.dispose();

		// 输出页面
		ImageIO.write(bi, "jpg", out);
	}

	public static Color getRandColor(int fc, int bc, int interval)
	{
		java.util.Random random = new java.util.Random();
		if (fc > 255)
		{
			fc = 255;
		}
		if (bc > 255)
		{
			bc = 255;
		}
		int r = fc + random.nextInt(bc - interval);
		int g = fc + random.nextInt(bc - interval);
		int b = fc + random.nextInt(bc - interval);
		return new Color(r, g, b);
	}
}
