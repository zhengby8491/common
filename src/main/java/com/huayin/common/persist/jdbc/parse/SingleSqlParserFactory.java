package com.huayin.common.persist.jdbc.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.huayin.common.exception.SystemException;

/**
 * 单句Sql解析器制造工厂
 * @author 何杨（heyang78@gmail.com）
 * @since 2009-2-3 上午09:45:49
 * @version 1.00
 */
public class SingleSqlParserFactory
{
	public static BaseSingleSqlParser generateParser(String sql)
	{
		sql=sql.trim();
		sql=sql.toLowerCase();
		sql=sql.replaceAll("\\s+", " ");
		sql=""+sql+" ENDOFSQL";
		if (contains(sql, "(insert into)(.+)(select)(.+)(from)(.+)"))
		{
			return new InsertSelectSqlParser(sql);
		}
		else if (contains(sql, "(select)(.+)(from)(.+)"))
		{
			return new SelectSqlParser(sql);
		}
		else if (contains(sql, "(delete from)(.+)"))
		{
			return new DeleteSqlParser(sql);
		}
		else if (contains(sql, "(update)(.+)(set)(.+)"))
		{
			return new UpdateSqlParser(sql);
		}
		else if (contains(sql, "(insert into)(.+)(values)(.+)"))
		{
			return new InsertSqlParser(sql);
		}

		// sql=sql.replaceAll("ENDSQL", "");
		throw new SystemException("很抱歉没有针对" + sql.replaceAll("ENDOFSQL", "")
				+ "的解析器,这可能是目前版本一时无法适应你的需求，也有可能是这条Sql语句存在书写错误.");
	}

	/**
	 * 看word是否在lineText中存在，支持正则表达式
	 * @param sql:要解析的sql语句
	 * @param regExp:正则表达式
	 * @return
	 */
	private static boolean contains(String sql, String regExp)
	{
		Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);
		return matcher.find();
	}
}