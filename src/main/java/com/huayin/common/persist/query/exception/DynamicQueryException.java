package com.huayin.common.persist.query.exception;

/**
 * <pre>
 * 动态查询构造异常
 * </pre>
 * @author chenjian
 * @version 1.0, 2011-7-29
 */
public class DynamicQueryException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public DynamicQueryException()
	{
		super();
	}

	public DynamicQueryException(String message)
	{
		super(message);
	}

	public DynamicQueryException(Throwable cause)
	{
		super(cause);
	}
}
