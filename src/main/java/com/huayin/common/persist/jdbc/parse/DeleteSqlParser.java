package com.huayin.common.persist.jdbc.parse;


/**
 * 
 * 单句删除语句解析器
 * @author 何杨（heyang78@gmail.com）
 *
 * @since 2009年2月3日8:58:48
 * @version 1.00
 */
public class DeleteSqlParser extends BaseSingleSqlParser{

	public DeleteSqlParser(String originalSql) {
		super(originalSql);
	}

	@Override
	protected void initializeSegments() {		
		segments.add(new SqlSegment("(delete from)(.+)( where | ENDOFSQL)","[,]"));
		segments.add(new SqlSegment("(where)(.+)( ENDOFSQL)","(and|or)"));
	}	
}