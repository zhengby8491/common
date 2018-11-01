/**
 * <pre>
 * Title: 		ButtonObject.java
 * Author:		linriqing
 * Create:	 	2011-9-16 下午05:33:33
 * Copyright: 	Copyright (c) 2011
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.acl.vo;

import com.huayin.common.util.ObjectHelper;

/**
 * <pre>
 * 按钮权限物件
 * </pre>
 * @author linriqing
 * @version 1.0, 2011-9-16
 */
public class ButtonObject extends ActionObject implements Cloneable
{
	private static final long serialVersionUID = 3934324615700608715L;

	/**
	 * 按钮事件
	 */
	private String event;
	
	/**
	 * 按钮图标
	 */
	private String image;

	public Object clone() throws CloneNotSupportedException
	{
		return ObjectHelper.byteClone(this);
	}

	/**
	 * @return 按钮事件 
	 */
	public String getEvent()
	{
		return event;
	}

	public String getImage()
	{
		return image;
	}

	/**
	 * @param event 按钮事件
	 */
	public void setEvent(String event)
	{
		this.event = event;
	}

	public void setImage(String image)
	{
		this.image = image;
	}
}
