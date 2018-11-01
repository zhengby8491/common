package com.huayin.common.persist.jdbc.parse;


/**
 * 
 * 单句插入语句解析器
 * @author 何杨（heyang78@gmail.com）
 *
 * @since 2009年2月3日9:16:44
 * @version 1.00
 */
public class InsertSqlParser extends BaseSingleSqlParser{

	public InsertSqlParser(String originalSql) {
		super(originalSql);
	}

	@Override
	protected void initializeSegments() {		
		segments.add(new SqlSegment("(insert into)(.+)([(])","[,]"));
		segments.add(new SqlSegment("([(])(.+)( [)] values )","[,]"));
		segments.add(new SqlSegment("([)] values [(])(.+)( [)])","[,]"));		
	}	
	
	@Override
	public String getParsedSql() {
		String retval=super.getParsedSql();
		retval=retval+")";
		return retval;
	}
}