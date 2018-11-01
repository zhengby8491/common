package com.huayin.common.area.vo;

import java.util.List;

/**
 * <pre>
 * 地址实体对象
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-9-25
 */
public class Area
{
	private Long id = null;

	private String name = null;

	private Long level = null;

	private List<Area> childList = null;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Long getLevel()
	{
		return level;
	}

	public void setLevel(Long level)
	{
		this.level = level;
	}

	public List<Area> getChildList()
	{
		return childList;
	}

	public void setChildList(List<Area> childList)
	{
		this.childList = childList;
	}
}
