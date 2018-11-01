package com.huayin.common.persist.jdbc.parse;


/**
 * 
 * 单句更新语句解析器
 * @author 何杨（heyang78@gmail.com）
 *
 * @since 2009年2月3日9:08:46
 * @version 1.00
 */
public class UpdateSqlParser extends BaseSingleSqlParser{

	public UpdateSqlParser(String originalSql) {
		super(originalSql);
	}

	@Override
	protected void initializeSegments() {		
		segments.add(new SqlSegment("(update)(.+)(set)","[,]"));
		segments.add(new SqlSegment("(set)(.+)( where | ENDOFSQL)","[,]"));
		segments.add(new SqlSegment("(where)(.+)( ENDOFSQL)","(and|or)"));
	}	
}