package com.huayin.common.persist.jdbc.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * 单句Sql解析器，单句即非嵌套的意思
 * @author 何杨（heyang78@gmail.com）
 *
 * @since 2009-2-2 下午03:01:06
 * @version 1.00
 */
public abstract class BaseSingleSqlParser{
	/**
	 * 原始Sql语句
	 */
	protected String originalSql;
	
	/**
	 * Sql语句片段
	 */
	protected List<SqlSegment> segments;
	
	/**
	 * 构造函数，传入原始Sql语句，进行劈分。
	 * @param originalSql
	 */
	public BaseSingleSqlParser(String originalSql){
		this.originalSql=originalSql;
		segments=new ArrayList<SqlSegment>();
		initializeSegments();
		splitSql2Segment();
	}
	
	/**
	 * 初始化segments，强制子类实现
	 *
	 */
	protected abstract void initializeSegments();
	
	/**
	 * 将originalSql劈分成一个个片段
	 *
	 */
	protected void splitSql2Segment() {
		for(SqlSegment sqlSegment:segments){
			sqlSegment.parse(originalSql);
		}		
	}
	
	/**
	 * 得到解析完毕的Sql语句
	 * @return 解析完毕的Sql语句
	 */
	public String getParsedSql() {
		StringBuffer sb=new StringBuffer();
		
		for(SqlSegment sqlSegment:segments){
			sb.append(sqlSegment.getParsedSqlSegment()+"\n");
		}			
		
		String retval=sb.toString().replaceAll("\n+", "\n");		
		return retval;
	}
}