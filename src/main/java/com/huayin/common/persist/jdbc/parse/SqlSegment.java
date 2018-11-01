package com.huayin.common.persist.jdbc.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sql语句片段
 * 
 * @author 何杨（heyang78@gmail.com）
 *
 * @since 2009-2-2 下午03:10:29
 * @version 1.00
 */
public class SqlSegment{
	private static final String Crlf = "\n";
	private static final String FourSpace = "    ";
	
	/**
	 * Sql语句片段开头部分
	 */
	private String start;
	
	/**
	 * Sql语句片段中间部分
	 */
	private String body;
	
	/**
	 * Sql语句片段结束部分
	 */
	private String end;
	
	/**
	 * 用于分割中间部分的正则表达式
	 */
	private String bodySplitPattern;
	
	/**
	 * 表示片段的正则表达式
	 */
	private String segmentRegExp;
	
	/**
	 * 分割后的Body小片段
	 */
	private List<String> bodyPieces;
	
	/**
	 * 构造函数
	 * @param segmentRegExp 表示这个Sql片段的正则表达式
	 * @param bodySplitPattern 用于分割body的正则表达式
	 */
	public SqlSegment(String segmentRegExp,String bodySplitPattern){
		start="";
		body="";
		end="";		
		this.segmentRegExp=segmentRegExp;
		this.bodySplitPattern=bodySplitPattern;
		this.bodyPieces=new ArrayList<String>();
	}
	
	/**
	 * 从sql中查找符合segmentRegExp的部分，并赋值到start,body,end等三个属性中
	 * @param sql
	 */
	public void parse(String sql){
		Pattern pattern=Pattern.compile(segmentRegExp,Pattern.CASE_INSENSITIVE);
	    
		for(int i=0;i<=sql.length();i++){
			String shortSql=sql.substring(0, i);
			//System.out.println(shortSql);
			
		    Matcher matcher=pattern.matcher(shortSql);
	 
		    while(matcher.find()){
		    	start=matcher.group(1);
				body=matcher.group(2);
				end=matcher.group(3);
				
				parseBody();
				
				return;
		    }
		}
	    
	}
	
	/**
	 * 解析body部分
	 *
	 */
	private void parseBody(){
		List<String> ls=new ArrayList<String>();		

		Pattern p = Pattern.compile(bodySplitPattern,Pattern.CASE_INSENSITIVE);

		// 先清除掉前后空格
		body=body.trim();
		Matcher m = p.matcher(body);
		StringBuffer sb = new StringBuffer();

		boolean result = m.find();
		while (result) {
			m.appendReplacement(sb, m.group(0) + Crlf);
			result = m.find();
		}
		m.appendTail(sb);
		
		// 再按空格断行
		String[] arr=sb.toString().split("[\n]+");
		int arrLength=arr.length;
		for(int i=0;i<arrLength;i++){
			String temp=FourSpace+arr[i];
			
			if(i!=arrLength-1){
				temp=temp+Crlf;
			}
			
			ls.add(temp);
		}

		bodyPieces=ls;
	}
	
	/**
	 * 取得解析好的Sql片段
	 * @return 解析好的Sql片段
	 */
	public String getParsedSqlSegment(){
		StringBuffer sb=new StringBuffer();
		
		sb.append(start+Crlf);
		
		for(String piece:bodyPieces){
			sb.append(piece+Crlf);
		}		
		
		return sb.toString();
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}
}