package com.huayin.common.web.rest;

import java.util.HashMap;

import com.huayin.common.util.HttpRequestProxy;
import com.huayin.common.util.MD5;


public class Query {
	public static void main(String[] args) throws Exception {
		
		
		//String param ="{\"com\":\"shunfeng\",\"num\":\"881482881372886757\"}";
		String param ="{\"com\":\"yuantong\",\"num\":\"881310054062579936\"}";
		
		String customer ="544C7D4947B67F4AB8C55692A17660D0";
		String key = "UdbgknXz7196";
		String sign = MD5.encodeString(param+key+customer,"utf-8").toUpperCase();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("param",param);
		params.put("sign",sign);
		params.put("customer",customer);
		System.out.print(sign);//60520859B5240252EA0D1CB7FF5AA7F3
		String resp;
		try {
			resp = HttpRequestProxy.doPost("http://poll.kuaidi100.com/poll/query.do", params, "utf-8").toString();
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
