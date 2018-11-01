package com.huayin.common.persist.query.model;

public class Order
{
	/**
	 * <pre>
	 * TODO 输入类型说明
	 * </pre>
	 * @author linriqing
	 * @version 1.0, 2011-10-10
	 */
	public static enum OrderType
	{
		ASC, DESC
	}

	/**
	 * 排序字段
	 */
	private String field;

	/**
	 * 排序方式
	 */
	private OrderType type = OrderType.ASC;

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public OrderType getType()
	{
		return type;
	}

	public void setType(OrderType type)
	{
		this.type = type;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (field == null)
		{
			if (other.field != null)
				return false;
		}
		else if (!field.equals(other.field))
			return false;
		if (type == null)
		{
			if (other.type != null)
				return false;
		}
		else if (!type.equals(other.type))
			return false;
		return true;
	}
}
