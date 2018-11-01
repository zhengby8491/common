/**
 * <pre>
 * Author:		zhaojitao
 * Create:	 	2010-6-24 下午03:47:49
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.eds;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.huayin.common.cis.ComponentContextLoader;
import com.huayin.common.eds.Event.ActionState;
import com.huayin.common.exception.SystemException;
import com.huayin.common.util.AnnotationScanHelper;

/**
 * <pre>
 * 事件处理容器上下文
 * 声明事件:
 * <code>
 * EventContextImpl.getInstance().perform(new {@link Event}(this, "testEvent", {@link ActionState}.Starting));
 * </code>
 * 监听事件：
 * <code>
 * \@Listener(events = { EventListenerTest.TestEvent.class }, create = InstanceType.FACTORY, factory = "getInstance", index = 1)
 * public class EventListenerTest implements EventListener
 * {
 * ...
 * }
 * </code>
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-6-24
 */
@Configuration
public class EventContextImpl implements EventContext
{
	/**
	 * <pre>
	 * 事件处理容器上下文私有静态内部实现类, 私有静态内部类, 只有当有引用时, 该类才会被装载
	 * </pre>
	 * @author zhaojitao
	 * @version 1.0, 2014-3-13
	 */
	private static final class EventContextInnerImpl implements EventContext
	{
		protected static final Lock contextLock = new ReentrantLock();

		public static boolean initFlag = false;

		protected static final Map<Class<? extends Event>, TreeSet<ListenerContainer>> listenerPool = Collections
				.synchronizedMap(new HashMap<Class<? extends Event>, TreeSet<ListenerContainer>>());

		public static final Lock lock = new ReentrantLock();

		protected static final ExecutorService threadPool = Executors.newCachedThreadPool();

		private ApplicationContext applicationContext;

		private EventContextInnerImpl(ApplicationContext context)
		{
			try
			{
				lock.lockInterruptibly();
				try
				{
					if (!initFlag)
					{
						applicationContext = context;
						logger.info("初始化事件容器上下文开始...");
						this.scanListener();
						logger.info("初始化事件容器上下文完毕...");
						initFlag = true;
					}
				}
				finally
				{
					lock.unlock();
				}
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException("初始化事件容器上下文异常", e);
			}
		}

		public int addListener(Class<? extends Event> eventClass, EventListener listener, int index, boolean isBlock,
				List<Class<? extends Event>> excludes)
		{
			if ((eventClass == null) || (listener == null))
			{
				throw new SystemException("参数(事件类型{" + eventClass.getSimpleName() + "}或监听器实例{" + listener + "})为空");
			}
			else
			{
				contextLock.lock();
				try
				{
					Set<String> excludesSet = new HashSet<String>();
					if (excludes != null)
					{
						for (Class<? extends Event> exclude : excludes)
						{
							excludesSet.add(exclude.getName());
						}
					}
					index = processAddListener(eventClass, listener, index, isBlock);
					// 将监听子类的事件
					List<Class<? extends Event>> findSubEventByType = AnnotationScanHelper
							.findSubEventByType(eventClass);
					for (Class<? extends Event> class1 : findSubEventByType)
					{
						if (!excludesSet.contains(class1.getName()))
						{
							addListener(class1, listener, -1, isBlock, excludes);
						}
					}
					return index;
				}
				finally
				{
					contextLock.unlock();
				}
			}
		}

		/**
		 * <pre>
		 * 为事件添加监听器
		 * </pre>
		 * @param listener 监听器实例
		 * @param annotation 监听器注解信息
		 * @param applicationContext Spring应用上下文
		 */
		private void addListener(EventListener listener, Listener annotation)
		{
			for (Class<? extends Event> type : annotation.events())
			{
				addListener(type, listener, annotation.index(), annotation.isBlock(),
						Arrays.asList(annotation.excludes()));
			}
		}

		private Constructor<?> getConstructor(final Class<?> clazz) throws PrivilegedActionException,
				NoSuchMethodException
		{
			Constructor<?> constructorToUse = null;
			if (System.getSecurityManager() != null)
			{
				constructorToUse = AccessController.doPrivileged(new PrivilegedExceptionAction<Constructor<?>>()
				{
					public Constructor<?> run() throws Exception
					{
						return clazz.getDeclaredConstructor((Class[]) null);
					}
				});
			}
			else
			{
				constructorToUse = clazz.getDeclaredConstructor((Class[]) null);
			}
			if ((!Modifier.isPublic(constructorToUse.getModifiers()) || !Modifier.isPublic(constructorToUse
					.getDeclaringClass().getModifiers())) && !constructorToUse.isAccessible())
			{
				constructorToUse.setAccessible(true);
			}
			return constructorToUse;
		}

		/**
		 * <pre>
		 * 发布事件，默认异步处理，是否异步处理由监听器自己设定
		 * 如果关心监听者处理进程的话,可以给event设置{@link EventCallback}对象.
		 * </pre>
		 * @param event 事件对象
		 * @see Event
		 * @see EventCallback
		 */
		public void perform(final Event event)
		{
			// 先取事件对应的监听器容器
			TreeSet<ListenerContainer> set = listenerPool.get(event.getClass());
			// 如果不存在则直接返回
			if (set != null)
			{
				for (Iterator<ListenerContainer> iterator = set.iterator(); iterator.hasNext();)
				{
					final ListenerContainer container = (ListenerContainer) iterator.next();
					final EventListener listener = container.getListener();
					if (container.isBlock())
					{
						listener.update(event, container);
					}
					else
					{
						threadPool.execute(new Runnable()
						{
							public void run()
							{
								// 如果发布者需要事件监听者回调
								if (event.getCallback() != null)
								{
									try
									{
										listener.update(event, container);
										event.getCallback().callback(event, container, false, null);
									}
									catch (Exception e)
									{
										event.getCallback().callback(event, container, true, e);
									}
								}
								else
								{
									listener.update(event, container);
								}
							}
						});
					}
				}
			}
		}

		private int processAddListener(Class<? extends Event> eventClass, EventListener listener, int index,
				boolean isBlock)
		{
			// 先取事件对应的监听器容器
			TreeSet<ListenerContainer> set = listenerPool.get(eventClass);

			// 判断当前需要注册的监听器是否已经存在
			boolean flagExist = false;

			// 如果不存在则新建一个事件对应的监听器容器
			// 创建一个监听器容器
			ListenerContainer container = new ListenerContainer(eventClass, listener, index, isBlock,
					applicationContext);
			if (set == null)
			{
				set = new TreeSet<ListenerContainer>();
				listenerPool.put(eventClass, set);
			}
			else
			{
				if (index != -1)
				{
					for (Iterator<ListenerContainer> iterator = set.iterator(); iterator.hasNext();)
					{
						ListenerContainer containerRep = iterator.next();
						if (containerRep.getIndex() == index)
						{
							logger.warn("事件类型{" + eventClass.getSimpleName() + "}已经存在相同索引{" + index
									+ "}的监听器, 监听器添加失败...");
							throw new SystemException("事件类型{" + eventClass.getSimpleName() + "}已经存在相同索引{" + index
									+ "}的监听器, 监听器添加失败...");
						}
					}
				}
				else
				{
					for (Iterator<ListenerContainer> iterator = set.iterator(); iterator.hasNext();)
					{
						ListenerContainer containerRep = iterator.next();
						if (containerRep.getListener().getClass().equals(listener.getClass()))
						{
							// logger.warn("事件类型{" + eventClass.getName() + "}已经存在相同的监听器{" +
							// listener.getClass().getName()
							// + "}, 监听器添加失败...");
							index = containerRep.getIndex();
							flagExist = true;
						}
					}
				}
			}
			// 不存在监听器则新建监听器容器并设值加入
			if (!flagExist)
			{
				// 如果未设优先级
				if (index == -1)
				{
					if (set.isEmpty())
					{
						// 监听器容器集合为空, 则设为默认值1000
						index = 1000;
					}
					else
					{
						// 监听器容器集合不为空, 则设为最后一个监听器的优先级+1
						index = set.last().getIndex() + 1;
					}
				}
				container.setIndex(index);
				set.add(container);
				listener.start(container);
				logger.info("事件类型{" + eventClass.getSimpleName() + "}成功添加索引{" + index + "}的监听器{"
						+ listener.getClass().getSimpleName() + "}...");
			}
			return index;
		}

		/**
		 * <pre>
		 * 移除所有监听器
		 * </pre>
		 * @param eventClass 事件类型
		 */
		public void removeAllListener(Class<? extends Event> eventClass)
		{
			contextLock.lock();
			try
			{
				// 先取事件对应的监听器容器
				TreeSet<ListenerContainer> set = listenerPool.get(eventClass);
				// 如果存在则移除事件对应的监听器容器
				if (set != null)
				{
					for (Iterator<ListenerContainer> iterator = set.iterator(); iterator.hasNext();)
					{
						ListenerContainer container = iterator.next();
						EventListener listener = container.getListener();
						listener.destory(container);
						container = null;
					}
					set.clear();
					set = null;
					listenerPool.remove(eventClass);
				}
			}
			finally
			{
				contextLock.unlock();
			}
		}

		/**
		 * <pre>
		 * 移除监听器
		 * </pre>
		 * @param eventClass 事件类型
		 * @param index 监听器索引值
		 */
		public void removeListener(Class<? extends Event> eventClass, int index)
		{
			contextLock.lock();
			try
			{
				// 先取事件对应的监听器容器
				TreeSet<ListenerContainer> set = listenerPool.get(eventClass);
				// 如果存在则移除事件对应的监听器容器
				if (set != null)
				{
					ListenerContainer container = null;
					boolean isExist = false;
					for (Iterator<ListenerContainer> iterator = set.iterator(); iterator.hasNext();)
					{
						container = iterator.next();
						EventListener listener = container.getListener();
						if (container.getIndex() == index)
						{
							listener.destory(container);
							isExist = true;
							break;
						}
					}
					if (isExist)
					{
						set.remove(container);
						container = null;
					}
				}
			}
			finally
			{
				contextLock.unlock();
			}
		}

		private void scanListener()
		{
			try
			{
				List<Class<?>> classList = AnnotationScanHelper.findAnnotationByType(Listener.class);
				for (Class<?> clazz : classList)
				{
					Listener annotation = clazz.getAnnotation(Listener.class);
					EventListener listener = null;
					switch (annotation.create())
					{
						case NEW:
							if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers()))
							{
								Class<?> enclosingClass = clazz.getEnclosingClass();
								Constructor<?> c = clazz.getConstructor(enclosingClass);
								listener = (EventListener) c.newInstance(getConstructor(enclosingClass).newInstance());
							}
							else
							{
								listener = (EventListener) getConstructor(clazz).newInstance();
							}
							break;

						case FACTORY:
							listener = (EventListener) clazz.getDeclaredMethod(annotation.factory()).invoke(null);
							break;

						case CONTEXT:
							// if (applicationContext != null)
						{
							listener = (EventListener) applicationContext.getBean(annotation.beanName());
						}
							// else
							// {
							// if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers()))
							// {
							// Class<?> enclosingClass = clazz.getEnclosingClass();
							// Constructor<?> c = clazz.getConstructor(enclosingClass);
							// listener = (EventListener) c.newInstance(getConstructor(enclosingClass)
							// .newInstance());
							// }
							// else
							// {
							// listener = (EventListener) getConstructor(clazz).newInstance();
							// }
							// }
							break;

						default:
							listener = (EventListener) clazz.newInstance();
							break;
					}
					addListener(listener, annotation);
				}
			}
			catch (Exception e)
			{
				logger.error("初始化事件容器上下文异常", e);
				throw new SystemException(e);
			}
		}

		@Override
		@PreDestroy
		public void shutdown() throws InterruptedException
		{
			threadPool.shutdown();
			try
			{
				threadPool.awaitTermination(5, TimeUnit.MINUTES);
			}
			finally
			{
				contextLock.lock();
				try
				{
					Collection<TreeSet<ListenerContainer>> values = listenerPool.values();
					for (TreeSet<ListenerContainer> set : values)
					{
						// 如果存在则移除事件对应的监听器容器
						if (set != null)
						{
							for (Iterator<ListenerContainer> iterator = set.iterator(); iterator.hasNext();)
							{
								ListenerContainer container = iterator.next();
								EventListener listener = container.getListener();
								listener.destory(container);
								container = null;
							}
							set.clear();
							set = null;
						}
					}
					listenerPool.clear();
				}
				finally
				{
					contextLock.unlock();
					EventContextInnerImpl.initFlag = false;
				}
			}
		}
	}

	/**
	 * <pre>
	 * 监听器容器
	 * </pre>
	 * @author zhaojitao
	 * @version 1.0, 2010-6-24
	 */
	public static class ListenerContainer implements Serializable, Comparable<ListenerContainer>
	{
		private static final long serialVersionUID = -314154594736095772L;

		private ApplicationContext applicationContext;

		private Class<? extends Event> eventClass;

		private int index;

		private boolean isBlock;

		private EventListener listener;

		/**
		 * 构造函数
		 * @param eventClass 监听器名称
		 * @param listener 监听器实例
		 * @param index 监听器调用顺序
		 * @param isBlock 监听器是否阻塞事件处理
		 * @param applicationContext Spring应用上下文
		 */
		public ListenerContainer(Class<? extends Event> eventClass, EventListener listener, int index, boolean isBlock,
				ApplicationContext applicationContext)
		{
			this.eventClass = eventClass;
			this.listener = listener;
			this.index = index;
			this.isBlock = isBlock;
			this.applicationContext = applicationContext;
		}

		@Override
		public int compareTo(ListenerContainer o)
		{
			return index - o.getIndex();
		}

		/**
		 * @return Spring应用上下文
		 */
		public ApplicationContext getApplicationContext()
		{
			return applicationContext;
		}

		/**
		 * @return eventName 监听器名称
		 */
		public Class<? extends Event> getEventClass()
		{
			return eventClass;
		}

		/**
		 * @return index 监听器调用顺序
		 */
		public int getIndex()
		{
			return index;
		}

		/**
		 * @return 监听器实例
		 */
		public EventListener getListener()
		{
			return listener;
		}

		/**
		 * @return 监听器是否阻塞事件处理
		 */
		public boolean isBlock()
		{
			return isBlock;
		}

		/**
		 * @param index index
		 */
		private void setIndex(int index)
		{
			this.index = index;
		}
	}

	private static EventContextImpl aaa_context;

	private static final Log logger = LogFactory.getLog(EventContextImpl.class);

	/**
	 * <pre>
	 * 线程安全/单例模式的获取实例方法
	 * </pre>
	 * @return 事件处理接口实现
	 */
	@Bean(name = { EventContext.CONTEXT_BEAN_NAME_EVENTCONTEXT })
	@Scope("singleton")
	public static EventContext getInstance()
	{
		if (aaa_context == null)
		{
			aaa_context = new EventContextImpl();
			aaa_context.init();
			return aaa_context;
		}
		else
		{
			return aaa_context;
		}
	}

	private EventContextInnerImpl innerContext;

	@Override
	public int addListener(Class<? extends Event> eventClass, EventListener listener, int index, boolean isBlock,
			List<Class<? extends Event>> excludes)
	{
		int addListener = innerContext.addListener(eventClass, listener, index, isBlock, excludes);
		return addListener;
	}

	private void init()
	{
		final ApplicationContext context = ComponentContextLoader.getApplicationContext();
		innerContext = new EventContextInnerImpl(context);
	}

	@Override
	public void perform(Event event)
	{
		innerContext.perform(event);
	}

	@Override
	public void removeAllListener(Class<? extends Event> eventClass)
	{
		innerContext.removeAllListener(eventClass);
	}

	@Override
	public void removeListener(Class<? extends Event> eventClass, int index)
	{
		innerContext.removeListener(eventClass, index);
	}

	@Override
	@PreDestroy()
	public void shutdown() throws InterruptedException
	{
		innerContext.shutdown();
	}
}
