/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.common.persist.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;

/**
 * <pre>
 * 框架 - 通用主键生成策略，数字型，可分布式，缓存后效率高，可移植，推荐使用
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月26日
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
// @EntityListeners(OperateLogListener.class)
public class AbstractTableIdEntity extends AbstractEntity
{

	private static final long serialVersionUID = 7575289264917305323L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLESEQ")
	@TableGenerator(name = "TABLESEQ", table = "TABLE_SEQUENCES", allocationSize = 100, pkColumnName = "TABLENAME", valueColumnName = "COUNTSEQ")
	private Long id;

	/**
	 * 备注，说明，解释，参考等
	 */
	private String memo;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}
}
