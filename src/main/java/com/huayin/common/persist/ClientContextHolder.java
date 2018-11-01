/**
 * <pre>
 * Title: 		ClientContextHolder.java
 * Author:		linriqing
 * Create:	 	2010-6-11 上午09:53:19
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.persist;

import java.util.Iterator;
import java.util.Stack;

import com.huayin.common.acl.vo.Passport;

/**
 * <pre>
 * 数据源上下文持有者
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-6-11
 */
public class ClientContextHolder
{
	public static enum ActionType
	{
		DEFAULT, READONLY, WRITE;
	}

	public static class ClientAction
	{
		private boolean lastIsAction;

		private Stack<Object> actionStack = new Stack<Object>();

		public ClientAction(ActionType type)
		{
			this.setLastIsAction(true);
			this.actionStack.push(type);
		}

		public DbLocation getDbLocation()
		{
			DbLocation dbLocation = DbLocation.MASTER;
			if (!actionStack.empty())
			{
				Object peek = actionStack.peek();
				if (peek instanceof ActionType)
				{
					switch ((ActionType) peek)
					{
						case READONLY:
							dbLocation = DbLocation.SLAVE;
							break;
						default:
							break;
					}
				}

				if (peek instanceof Passport)
				{
					switch (((Passport) peek).getToken())
					{
						case SLAVE:
							dbLocation = DbLocation.SLAVE;
							break;
						default:
							break;
					}
				}
			}
			return dbLocation;
		}

		public boolean getLastIsAction()
		{
			return lastIsAction;
		}

		public void setLastIsAction(boolean lastIsAction)
		{
			this.lastIsAction = lastIsAction;
		}

		public Passport getPassport()
		{
			Passport passport = null;
			for (Iterator<Object> iterator = actionStack.iterator(); iterator.hasNext();)
			{
				Object next = iterator.next();
				if (next instanceof Passport)
				{
					passport = (Passport) next;
				}
			}
			return passport;
		}
	}

	public enum DbLocation
	{
		MASTER, SLAVE;
	}

	private static final ThreadLocal<ClientAction> contextHolder = new ThreadLocal<ClientAction>()
	{
		@Override
		protected ClientAction initialValue()
		{
			return new ClientAction(ActionType.DEFAULT);
		}
	};

	public static void clearClientAction()
	{
		ClientAction clientAction = contextHolder.get();
		clientAction.actionStack.pop();
	}

	public static ClientAction getClientAction()
	{
		return contextHolder.get();
	}

	public static void setClientAction(ActionType actionType)
	{
		ClientAction clientAction = contextHolder.get();
		clientAction.actionStack.push(actionType);
	}

	public static void setClientAction(Passport passport)
	{
		ClientAction clientAction = contextHolder.get();
		clientAction.actionStack.push(passport);
	}
}
