package com.huayin.common.persist.query.constants;

public class QueryConstants
{

	/**
	 * <pre>
	 * 查询语言类型
	 * </pre>
	 * @author chenjian
	 * @version 1.0, 2011-9-13
	 */
	public static enum QueryType
	{
		JDBC, JPQL
	}

	/**
	 * <pre>
	 * 数据库锁类型
	 * </pre>
	 * @author chenjian
	 * @version 1.0, 2011-8-1
	 */
	public static enum LockType
	{
		LOCK_WAIT, LOCK_PASS, LOCK_NO, UNLOCK_READPAST
	}

	/**
	 * <pre>
	 * 联合抓取模式
	 * </pre>
	 * @author chenjian
	 * @version 1.0, 2011-7-29
	 */
	public static enum JoinType
	{
		DEFAULT(","), INNERJOIN("inner join"), LEFTJOIN("left join"), RIGHTJOIN("right join");

		private String value;

		JoinType(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}
	}

	/**
	 * <pre>
	 * </pre>
	 * @author chenjian
	 * @version 1.0, 2012-7-3
	 */
	public static enum UnionType
	{
		UNION("union"), UNION_ALL("union all");
		private String value;

		UnionType(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}
	}
}
