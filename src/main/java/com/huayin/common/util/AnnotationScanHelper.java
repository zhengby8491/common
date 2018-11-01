package com.huayin.common.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import com.huayin.common.configuration.ConfigProvider;
import com.huayin.common.configuration.ConfigProviderFactory;
import com.huayin.common.constant.Constant;
import com.huayin.common.eds.Event;
import com.huayin.common.exception.SystemException;

/**
 * <pre>
 * 注解扫描帮助类
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2012-5-2
 */
public class AnnotationScanHelper
{
	private static final String CLASS_PATH_ANNOTATION_SCANER_CLASS_NAME = "ClassPathAnnotationScanerClassName";

	private static final String CLASS_PATH_ANNOTATION_SCANER_PACKAGE_NAME = "ClassPathAnnotationScanerPackageName";

	private static final Log logger = LogFactory.getLog(AnnotationScanHelper.class);

	private static final Set<MetadataReader> scanAnnotationClassSet = init();

	private static final Set<MetadataReader> scanEventClassSet = initEvent();

	public static List<Class<?>> findAnnotationByType(Class<? extends Annotation> annotationType)
	{
		List<Class<?>> clazzList = new ArrayList<Class<?>>();
		try
		{
			for (MetadataReader metadataReader : scanAnnotationClassSet)
			{
				AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
				if (annotationMetadata.hasAnnotation(annotationType.getName()))
				{
					clazzList.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
				}
			}
		}
		catch (Exception e)
		{
			logger.error("查找注解类型异常", e);
			throw new SystemException(e);
		}
		return clazzList;
	}

	@SuppressWarnings("unchecked")
	public static List<Class<? extends Event>> findSubEventByType(Class<? extends Event> eventType)
	{
		List<Class<? extends Event>> clazzList = new ArrayList<Class<? extends Event>>();
		try
		{
			for (MetadataReader metadataReader : scanEventClassSet)
			{
				Class<? extends Event> forName = (Class<? extends Event>) Class.forName(metadataReader
						.getClassMetadata().getClassName());
				if (eventType.isAssignableFrom(forName))
				{
					if (!forName.equals(eventType))
					{
						clazzList.add(forName);
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error("初始化事件容器上下文异常", e);
			throw new SystemException(e);
		}
		return clazzList;
	}

	private static Set<MetadataReader> initEvent()
	{
		ConfigProvider<String> cp = ConfigProviderFactory.getInstance(Constant.HY_COMMON_PROPERTIES_FILEPATH);
		String[] scanPackage = cp.getConfigByPrimaryKey(CLASS_PATH_ANNOTATION_SCANER_PACKAGE_NAME).split(",");
		ClassPathAnnotationScaner p2 = new ClassPathAnnotationScaner();
		p2.addIncludeFilter(new AssignableTypeFilter(Event.class));
		return p2.findCandidateClasss(scanPackage);
	}

	@SuppressWarnings("unchecked")
	private static Set<MetadataReader> init()
	{
		try
		{
			ConfigProvider<String> cp = ConfigProviderFactory.getInstance(Constant.HY_COMMON_PROPERTIES_FILEPATH);
			String[] scanPackage = cp.getConfigByPrimaryKey(CLASS_PATH_ANNOTATION_SCANER_PACKAGE_NAME).split(",");
			String[] scanAnnotation = cp.getConfigByPrimaryKey(CLASS_PATH_ANNOTATION_SCANER_CLASS_NAME).split(",");
			ClassPathAnnotationScaner p = new ClassPathAnnotationScaner();
			for (String string : scanAnnotation)
			{
				if (!string.isEmpty())
				{
					p.addIncludeFilter(new AnnotationTypeFilter((Class<? extends Annotation>) Class.forName(string)));
				}
			}

			return p.findCandidateClasss(scanPackage);
		}
		catch (ClassNotFoundException e)
		{
			throw new SystemException("初始化注解搜索异常，定义了不正确的注解类型：" + Constant.HY_COMMON_PROPERTIES_FILEPATH + ":"
					+ CLASS_PATH_ANNOTATION_SCANER_CLASS_NAME, e);
		}
	}
}