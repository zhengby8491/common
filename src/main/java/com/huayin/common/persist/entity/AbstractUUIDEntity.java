package com.huayin.common.persist.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * <pre>
 * UUID生成策略，移植性较好 ，但占用长度过长，且可读性差，一般不推荐
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年5月6日
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
//@EntityListeners(OperateLogListener.class)
public class AbstractUUIDEntity extends AbstractEntity
{

	private static final long serialVersionUID = 7575289264917305323L;

	@Id
	@GeneratedValue(generator = "uuidGenerator")
	@GenericGenerator(name = "uuidGenerator", strategy = "uuid") // 这个是hibernate的注解/生成32位UUID
	private String id;

	/**
	 * 备注，说明，解释，参考等
	 */
	private String memo;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
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

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

}
