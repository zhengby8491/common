/**
 * <pre>
 * Title: 		PersistProviderBridgeImpl.java
 * Author:		zhaojitao
 * Create:	 	2011-7-19 下午06:43:52
 * Copyright: 	Copyright (c) 2011
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.huayin.common.persist.jdbc.JdbcPersistProvider;
import com.huayin.common.persist.jpa.JpaPersistProvider;

/**
 * <pre>
 * 持久化实现提供桥接实现
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2011-7-19
 */
@Repository
@Lazy
public class PersistProviderBridgeImpl implements PersistProviderBridge
{
	private static PersistProvider jdbcPersistProvider;

	private static PersistProvider jpaPersistProvider;

	private static PersistProvider slaveJdbcPersistProvider;

	private static PersistProvider slaveJpaPersistProvider;

	@Override
	public PersistProvider getPersistProvider()
	{
		if (jpaPersistProvider != null)
		{
			switch (ClientContextHolder.getClientAction().getDbLocation())
			{
				case MASTER:
					return jpaPersistProvider;
				case SLAVE:
					if (slaveJpaPersistProvider != null)
					{
						return slaveJpaPersistProvider;
					}
					else
					{
						return jpaPersistProvider;
					}
				default:
					return jpaPersistProvider;
			}
		}
		else
		{
			switch (ClientContextHolder.getClientAction().getDbLocation())
			{
				case MASTER:
					return jdbcPersistProvider;
				case SLAVE:
					if (slaveJdbcPersistProvider != null)
					{
						return slaveJdbcPersistProvider;
					}
					else
					{
						return jdbcPersistProvider;
					}
				default:
					return jdbcPersistProvider;
			}
		}
	}

	@Override
	public PersistProvider getPersistProvider(int providerType)
	{
		if (providerType == 0)
		{
			if (jdbcPersistProvider != null)
			{
				switch (ClientContextHolder.getClientAction().getDbLocation())
				{
					case MASTER:
						return jdbcPersistProvider;
					case SLAVE:
						if (slaveJdbcPersistProvider != null)
						{
							return slaveJdbcPersistProvider;
						}
						else
						{
							return jdbcPersistProvider;
						}
					default:
						return jdbcPersistProvider;
				}
			}
			else
			{
				switch (ClientContextHolder.getClientAction().getDbLocation())
				{
					case MASTER:
						return jpaPersistProvider;
					case SLAVE:
						if (slaveJpaPersistProvider != null)
						{
							return slaveJpaPersistProvider;
						}
						else
						{
							return jpaPersistProvider;
						}
					default:
						return jpaPersistProvider;
				}
			}
		}
		else
		{
			if (jpaPersistProvider != null)
			{
				switch (ClientContextHolder.getClientAction().getDbLocation())
				{
					case MASTER:
						return jpaPersistProvider;
					case SLAVE:
						if (slaveJpaPersistProvider != null)
						{
							return slaveJpaPersistProvider;
						}
						else
						{
							return jpaPersistProvider;
						}
					default:
						return jpaPersistProvider;
				}
			}
			else
			{
				switch (ClientContextHolder.getClientAction().getDbLocation())
				{
					case MASTER:
						return jdbcPersistProvider;
					case SLAVE:
						if (slaveJdbcPersistProvider != null)
						{
							return slaveJdbcPersistProvider;
						}
						else
						{
							return jdbcPersistProvider;
						}
					default:
						return jdbcPersistProvider;
				}
			}
		}
	}

	/**
	 * @param entityManager entityManager
	 */
	@Autowired(required = false)
	@Qualifier("sharedEntityManagerBean")
	public void setEntityManager(EntityManager entityManager)
	{
		jpaPersistProvider = new JpaPersistProvider(entityManager);
	}

	/**
	 * @param jdbcTemplate jdbcTemplate
	 */
	@Autowired(required = false)
	@Qualifier("masterJdbcTemplate")
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		jdbcPersistProvider = new JdbcPersistProvider(jdbcTemplate);
	}

	@Autowired(required = false)
	@Qualifier("slaveSharedEntityManagerBean")
	public void setSlaveEntityManager(EntityManager entityManager)
	{
		slaveJpaPersistProvider = new JpaPersistProvider(entityManager);
	}

	@Autowired(required = false)
	@Qualifier("slaveJdbcTemplate")
	public void setSlaveJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		slaveJdbcPersistProvider = new JdbcPersistProvider(jdbcTemplate);
	}
}
