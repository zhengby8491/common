/**
 * <pre>
 * Title: 		ClassPathScanner.java
 * Author:		linriqing
 * Create:	 	2011-6-21 下午03:19:35
 * Copyright: 	Copyright (c) 2011
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * 类或资源扫描器,可以批量过滤CLASSPATH路径和JAR中的任意内容
 * <p>
 * This implementation is based on Spring's {@link org.springframework.core.type.classreading.MetadataReader
 * MetadataReader} facility, backed by an ASM.
 * @author bencmai
 * @see org.springframework.core.type.classreading.MetadataReaderFactory
 * @see org.springframework.core.type.AnnotationMetadata
 */
public class ClassPathAnnotationScaner
{
	protected final Log logger = LogFactory.getLog(getClass());

	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

	private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

	private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();

	private final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();

	/** Add an include type filter to the <i>end</i> of the inclusion list. */
	public void addIncludeFilter(TypeFilter includeFilter)
	{
		this.includeFilters.add(includeFilter);
	}

	/** Add an exclude type filter to the <i>front</i> of the exclusion list. */
	public void addExcludeFilter(TypeFilter excludeFilter)
	{
		this.excludeFilters.add(0, excludeFilter);
	}

	/** Reset the configured type filters. */
	public void resetFilters()
	{
		this.includeFilters.clear();
		this.excludeFilters.clear();
	}

	/**
	 * 将.包路径解释为/路径 <br>
	 * Resolve the specified base package into a pattern specification for the package search path.
	 * <p>
	 * The default implementation resolves placeholders against system properties, and converts a "."-based package path
	 * to a "/"-based resource path.
	 * @param basePackage the base package as specified by the user
	 * @return the pattern specification to be used for package searching
	 */
	public String packageToRelativedPath(String basePackage)
	{
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

	/**
	 * 和配置的排除过滤器和包含过滤器进行匹配过滤<br>
	 * Determine whether the given class does not match any exclude filter and does match at least one include filter.
	 * @param metadataReader the ASM ClassReader for the class
	 * @return whether the class qualifies as a candidate component
	 */
	protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException
	{
		for (TypeFilter tf : this.excludeFilters)
		{
			if (tf.match(metadataReader, this.metadataReaderFactory))
			{
				return false;
			}
		}
		for (TypeFilter tf : this.includeFilters)
		{
			if (tf.match(metadataReader, this.metadataReaderFactory))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 扫描类路径并根据过滤器以找出合条件的类元数据<br>
	 * <p>
	 * 注:如果包是空白字符,只能扫描到非JAR中的内容
	 * 
	 * <pre>
     * ClassPathScaner p = new ClassPathScaner();
     * // p.addIncludeFilter(new AssignableTypeFilter(TypeFilter.class));
     * // Set&lt;MetadataReader&gt; bd = p.findCandidateClasss(&quot;org.springframework&quot;);
     * p.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
     * Set&lt;MetadataReader&gt; bd = p.findCandidateClasss(&quot;com&quot;);
     * for (MetadataReader b : bd) {
     *     System.out.println(b.getClassMetadata().getClassName());
     * }
     * p.resetFilters();
     * </pre>
	 * @param basePackage the package to check for annotated classes
	 * @return a corresponding Set of autodetected bean definitions
	 */
	public Set<MetadataReader> findCandidateClasss(String... basePackage)
	{
		Set<MetadataReader> candidates = new LinkedHashSet<MetadataReader>();
		try
		{
			for (String s : basePackage)
			{
				String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packageToRelativedPath(s)
						+ "/" + this.resourcePattern;
				if (logger.isDebugEnabled())
					logger.debug("扫描包:" + packageSearchPath);
				Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
				for (Resource resource : resources)
				{
					if (resource.isReadable())
					{
						try
						{
							MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
							if (isCandidateComponent(metadataReader))
							{
								candidates.add(metadataReader);
								if (logger.isDebugEnabled())
									logger.debug("匹配:" + metadataReader.getClassMetadata().getClassName());
							}
						}
						catch (Throwable ex)
						{
							// throw new Exception("类符合条件但读取失败: " + resource, ex);
						}
					}
					else
					{
						if (logger.isDebugEnabled())
							logger.debug("因为扫描到的类无法读取元数据,忽略: " + resource);
					}
				}
			}
		}
		catch (IOException ex)
		{
			// throw new IOException("扫描时发生 I/O 异常", ex);
		}
		return candidates;
	}

	/**
	 * 扫描CLASSPATH路径以找出合条件的文件
	 * <p>
	 * 注:如果包是空白字符,只能扫描到非JAR中的内容
	 * 
	 * <pre>
     * ClassPathScaner p = new ClassPathScaner();
     * List&lt;Resource&gt; rs = p.findCandidateFiles(".properties";, &quot;META-INF&quot;, "com.yotexs.model";, "org.yotexs.model";); for (Resource r : rs) {
	 * System.out.println(r.getURI().toURL().toString()); } 
	 * </pre>
	 * @param patterns ant方式的匹配模式 如:**&frasl;*.xml
	 * @param basePackage 要扫描的包组 如:com.yotexs,META-INF
	 * @return
	 */
	public List<Resource> findCandidateFiles(String patterns, String... basePackage)
	{
		List<Resource> resources = new LinkedList<Resource>();
		try
		{
			for (String p : basePackage)
			{
				String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packageToRelativedPath(p)
						+ "/" + patterns;
				Resource[] current = this.resourcePatternResolver.getResources(packageSearchPath);
				if (current.length == 0)
					continue;
				resources.addAll(Arrays.asList(current));
			}
			return resources;
		}
		catch (IOException ex)
		{
			// throw new IOException("扫描时发生 I/O 异常", ex);
			return null;
		}
	}

	// ----------------------------------------------------------------------------------------------
	public static void main(String a[]) throws Exception
	{
		// -----------------------------------------直接扫描
		// Enumeration<URL> xmls = Thread.currentThread().getContextClassLoader()
		// .getResources("META-INF/persistence.xml");
		// while (xmls.hasMoreElements()) {
		// URL url = xmls.nextElement();
		// System.out.println(url.toURI().toURL());
		// }
		// -----------------------------------------直接扫描
		// ResourcePatternResolver rp = new PathMatchingResourcePatternResolver();
		// String packages = "META-INF";
		// Resource[] rs = rp.getResources("classpath*:/" + packages + "/**/*.*");
		// for (Resource r : rs) {
		// System.out.println(r);
		// }
		// -----------------------------------------类扫描
		ClassPathAnnotationScaner p = new ClassPathAnnotationScaner();
		// p.addIncludeFilter(new AssignableTypeFilter(TypeFilter.class));
		// Set<MetadataReader> bd = p.findCandidateClasss("org.springframework");
		p.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		Set<MetadataReader> bd = p.findCandidateClasss("com");
		for (MetadataReader b : bd)
		{
			System.out.println("<class>" + b.getClassMetadata().getClassName() + "</class>");
		}

		// -----------------------------------------资源扫描
		// ClassPathScaner p = new ClassPathScaner();
		// List<Resource> rs = p.findCandidateFiles("**/*.xml", "META-INF", "com", "org");
		// for (Resource r : rs) {
		// System.out.println(r.getURI().toURL().toString());
		// }
	}

}
