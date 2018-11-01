/**
 * Create at 2006-5-12 17:46:43
 */
package com.huayin.common.web.tags;

/**
 * <pre>
 * 标签用到的一些常量
 * </pre>
 * @author linriqing
 * @version 1.0, 2006-6-16
 */
public class TagConstant
{
	/**
	 * 权限名称空间前缀 {@value}
	 * @value "system."
	 */
	public final static String AUTHORITIES_NAMESPACE_PREFIX = "system.";

	/**
	 * 权限名称空间默认根 {@value}
	 * @value "root"
	 */
	public final static String AUTHORITIES_NAMESPACE_ROOT = "root";

	/**
	 * 权限名称空间分割符 {@value}
	 * @value "."
	 */
	public final static String AUTHORITIES_NAMESPACE_SPLITSYMBOL = ".";

	/**
	 * 权限名称空间通配符 {@value}
	 * @value "*"
	 */
	public final static String AUTHORITIES_NAMESPACE_WILDCARD = "*";

	/**
	 * Select控件的名称
	 * @value "select"
	 */
	public static final String ENUMTAG_INPUTTYPE_SELECT_NAME = "select";

	/**
	 * 控件值为集合时分隔符
	 * @value ","
	 */
	public static final String INPUT_COLLECTION_SPLIT_REGEX = ",";

	/**
	 * 分页标签中当前页码的参数名 {@value}
	 * @value "PaginationButton_CurrentPage"
	 */
	public final static String REQUEST_PARAMETERNAME_PAGINATION_CURRENTPAGEINDEX = "PaginationButton_CurrentPage";

	/**
	 * 分页中数据集的属性名 {@value}
	 * @value "PaginationQueryDataList"
	 */
	public final static String REQUEST_PARAMETERNAME_PAGINATION_DATALIST_NAME = "PaginationQueryDataList";

	/**
	 * 分页标签中总记录数的参数名 {@value}
	 * @value "PaginationButton_EntityCount"
	 */
	public final static String REQUEST_PARAMETERNAME_PAGINATION_ENTITYCOUNT = "PaginationButton_EntityCount";

	/**
	 * 分页标签中总页码的参数名 {@value}
	 * @value "PaginationButton_PageCount"
	 */
	public final static String REQUEST_PARAMETERNAME_PAGINATION_PAGECOUNT = "PaginationButton_PageCount";

	/**
	 * 分页标签中单页记录数的参数名 {@value}
	 * @value "PaginationButton_PageSize"
	 */
	public final static String REQUEST_PARAMETERNAME_PAGINATION_PAGESIZE = "PaginationButton_PageSize";
}
