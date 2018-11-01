/**
 * <pre>
 * Title: 		OperateLogListener.java
 * Author:		linriqing
 * Create:	 	2010-7-6 上午10:25:57
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

import com.huayin.common.acl.vo.Passport;
import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.common.persist.entity.AuditedEntity;
import com.huayin.common.persist.entity.OperateLog;
import com.huayin.common.persist.entity.OperateLog.OperationType;

/**
 * <pre>
 * 基于Hibernate/JPA事件监听的操作日志信息记录
 * 因为persistence.xml文件中配置了以下内容:
 * {@code
 * <property name="hibernate.ejb.event.post-insert" value="com.huayin.common.persist.OperateLogListener" />
 * <property name="hibernate.ejb.event.post-update" value="com.huayin.common.persist.OperateLogListener" />
 * <property name="hibernate.ejb.event.post-delete" value="com.huayin.common.persist.OperateLogListener" />
 * }
 * 所以实际上是用了Hibernate的事件机制实现日志信息记录,如果要用jpa的需要将以上内容注释掉,并修改PostRemove,PostPersist,PostUpdate方法.
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-7-6
 */
public class OperateLogListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener
{
	private static final long serialVersionUID = 1378383641472702887L;

	@Override
	public void onPostDelete(PostDeleteEvent event)
	{
		if (event.getEntity() instanceof AuditedEntity)
		{
			Passport passport = ClientContextHolder.getClientAction().getPassport();
			if ((passport != null) && passport.getToken() != null
					&& (passport.getToken().equals(Passport.Token.MANAGE)))
			{
				AuditedEntity entity = (AuditedEntity) event.getEntity();
				OperateLog log = new OperateLog();
				log.setOperationType(OperationType.DELETE);
				log.setOperateTime(new Date());
				log.setEntityName(entity.getClass().getSimpleName());
				log.setEntityId(String.valueOf(entity.getId()));
				log.setUserId(passport.getSerialNo());
				recordHistory(event.getSession(), log);
			}
		}
	}

	@Override
	public void onPostInsert(PostInsertEvent event)
	{
		if (event.getEntity() instanceof AuditedEntity)
		{
			Passport passport = ClientContextHolder.getClientAction().getPassport();
			if ((passport != null) && passport.getToken() != null
					&& (passport.getToken().equals(Passport.Token.MANAGE)))
			{
				AuditedEntity entity = (AuditedEntity) event.getEntity();
				OperateLog log = new OperateLog();
				log.setOperationType(OperationType.CREATE);
				log.setOperateTime(new Date());
				log.setEntityName(entity.getClass().getSimpleName());
				log.setEntityId(String.valueOf(entity.getId()));
				log.setUserId(passport.getSerialNo());
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < event.getState().length; i++)
				{
					// 更新后的新值
					Object newValue = event.getState()[i];
					// 跳过集合属性
					if (newValue instanceof PersistentCollection)
					{
						continue;
					}
					// 取得属性名称
					sb.append(event.getPersister().getPropertyNames()[i]);
					sb.append("={\"");
					if (newValue instanceof AbstractEntity)
					{
						sb.append(((AuditedEntity) newValue).getId().toString());
					}
					else
					{
						sb.append(newValue != null ? newValue.toString() : null);
					}
					sb.append("\"}, ");
				}
				if (sb.length() > 0)
				{
					log.setDescription(sb.substring(0, sb.length() - 1));
				}
				recordHistory(event.getSession(), log);
			}
		}
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event)
	{
		if (event.getEntity() instanceof AuditedEntity)
		{
			Passport passport = ClientContextHolder.getClientAction().getPassport();
			if ((passport != null) && passport.getToken() != null
					&& (passport.getToken().equals(Passport.Token.MANAGE)))
			{
				OperateLog log = new OperateLog();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < event.getState().length; i++)
				{
					// 更新前的值
					Object oldValue = event.getOldState()[i];
					// 更新后的新值
					Object newValue = event.getState()[i];
					// 跳过集合属性
					if (newValue instanceof PersistentCollection)
					{
						continue;
					}
					if (oldValue != null && !oldValue.equals(newValue))
					{
						// 取得属性名称
						sb.append(event.getPersister().getPropertyNames()[i]);
						sb.append("={\"");
						// 如果更改的属性是关联对象，则存储其id
						if (oldValue instanceof AbstractEntity)
						{
							sb.append(((AuditedEntity) oldValue).getId().toString());
						}
						else
						{
							sb.append(oldValue != null ? oldValue.toString() : null);
						}
						sb.append("\" -> \"");
						if (newValue instanceof AbstractEntity)
						{
							sb.append(((AuditedEntity) newValue).getId().toString());
						}
						else
						{
							sb.append(newValue != null ? newValue.toString() : null);
						}
						sb.append("\"}, ");
					}
				}
				if (sb.length() > 0)
				{
					log.setDescription(sb.substring(0, sb.length() - 1));
				}

				AuditedEntity entity = (AuditedEntity) event.getEntity();
				log.setOperationType(OperationType.UPDATE);
				log.setOperateTime(new Date());
				log.setEntityName(entity.getClass().getSimpleName());
				log.setEntityId(String.valueOf(entity.getId()));
				log.setUserId(passport.getSerialNo());
				recordHistory(event.getSession(), log);
			}
		}
	}

	@javax.persistence.PostPersist
	public void PostPersist(AuditedEntity entity)
	{
		if (entity instanceof AuditedEntity)
		{
			Passport passport = ClientContextHolder.getClientAction().getPassport();
			if ((passport != null) && passport.getToken() != null
					&& (passport.getToken().equals(Passport.Token.MANAGE)))
			{
				OperateLog log = new OperateLog();
				log.setOperationType(OperationType.CREATE);
				log.setOperateTime(new Date());
				log.setEntityName(entity.getClass().getSimpleName());
				log.setEntityId(String.valueOf(entity.getId()));
				log.setUserId(passport.getSerialNo());
				// recordHistory(event.getSession(), log);
				System.out.println("PostPersist " + entity.getClass().getSimpleName() + "[" + entity.getId() + "]");
			}
		}
	}

	@javax.persistence.PostUpdate
	public void PostUpdate(AuditedEntity entity)
	{
		if (entity instanceof AuditedEntity)
		{
			Passport passport = ClientContextHolder.getClientAction().getPassport();
			if ((passport != null) && passport.getToken() != null
					&& (passport.getToken().equals(Passport.Token.MANAGE)))
			{
				System.out.println("PostUpdate " + entity.getClass().getSimpleName() + "[" + entity.getId() + "]");
			}
		}

	}

	@javax.persistence.PostRemove
	public void PostRemove(AuditedEntity entity)
	{
		if (entity instanceof AuditedEntity)
		{
			Passport passport = ClientContextHolder.getClientAction().getPassport();
			if ((passport != null) && passport.getToken() != null
					&& (passport.getToken().equals(Passport.Token.MANAGE)))
			{
				OperateLog log = new OperateLog();
				log.setOperationType(OperationType.DELETE);
				log.setOperateTime(new Date());
				log.setEntityName(entity.getClass().getSimpleName());
				log.setEntityId(String.valueOf(entity.getId()));
				log.setUserId(passport.getSerialNo());
				System.out.println("PostRemove " + entity.getClass().getSimpleName() + "[" + entity.getId() + "]");
			}
		}
	}

	// 另外启动一个session处理审核日志的保存
	private void recordHistory(Session session, OperateLog entry)
	{
		Session tempSession = null;
		Transaction tx = null;
		try
		{
			tempSession = session.getSessionFactory().openSession();
			tx = tempSession.beginTransaction();
			tx.begin();
			tempSession.save(entry);
			tempSession.flush();
			tx.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			if (tx != null)
			{
				tx.rollback();
			}
		}
		finally
		{
			if (tempSession != null)
			{
				tempSession.close();
			}
		}
	}

	//@Override
	public boolean requiresPostCommitHanding(EntityPersister persister)
	{
		return false;
	}
}
