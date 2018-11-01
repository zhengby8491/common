package com.huayin.common.persist.jdbc.parse;



/**
 * 
 * 单句查询语句解析器
 * @author 何杨（heyang78@gmail.com）
 *
 * @since 2009-2-2 下午03:30:54
 * @version 1.00
 */
public class SelectSqlParser extends BaseSingleSqlParser{

	public SelectSqlParser(String originalSql) {
		super(originalSql);
	}

	@Override
	protected void initializeSegments() {		
		segments.add(new SqlSegment("(select)(.+)(from)","[,]"));
		segments.add(new SqlSegment("(from)(.+)( where | on | having | group\\s+by | order\\s+by | ENDOFSQL)","(,|\\s+left\\s+join\\s+|\\s+right\\s+join\\s+|\\s+inner\\s+join\\s+)"));
		segments.add(new SqlSegment("(where|on|having)(.+)( group\\s+by | order\\s+by | ENDOFSQL)","(and|or)"));
		segments.add(new SqlSegment("(group\\s+by)(.+)( order\\s+by| ENDOFSQL)","[,]"));
		segments.add(new SqlSegment("(order\\s+by)(.+)( ENDOFSQL)","[,]"));
	}	
}