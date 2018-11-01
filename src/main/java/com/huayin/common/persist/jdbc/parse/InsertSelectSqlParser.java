package com.huayin.common.persist.jdbc.parse;


/**
 * 
 * 单句查询插入语句解析器
 * @author 何杨（heyang78@gmail.com）
 *
 * @since 2009年2月3日9:41:23
 * @version 1.00
 */
public class InsertSelectSqlParser extends BaseSingleSqlParser{

	public InsertSelectSqlParser(String originalSql) {
		super(originalSql);
	}

	@Override
	protected void initializeSegments() {		
		segments.add(new SqlSegment("(insert into)(.+)( select )","[,]"));
		segments.add(new SqlSegment("(select)(.+)(from)","[,]"));
		segments.add(new SqlSegment("(from)(.+)( where | on | having | group\\s+by | order\\s+by | ENDOFSQL)","(,|\\s+left\\s+join\\s+|\\s+right\\s+join\\s+|\\s+inner\\s+join\\s+)"));
		segments.add(new SqlSegment("(where|on|having)(.+)( group\\s+by | order\\s+by | ENDOFSQL)","(and|or)"));
		segments.add(new SqlSegment("(group\\s+by)(.+)( order\\s+by| ENDOFSQL)","[,]"));
		segments.add(new SqlSegment("(order\\s+by)(.+)( ENDOFSQL)","[,]"));
	}	
}