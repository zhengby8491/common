package com.huayin.common.web.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@ContextConfiguration(locations = { "classpath:test-rest-definition.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class RestClientTest
{
	@Autowired
	RestTemplate restTemplate;
	String userName="admin";
	String password="123456";
	String url="http://localhost:8080/msrns/httpservice/process";
	@Test
	public void testExecGet()
	{
		RestClient client=new RestClient(restTemplate, userName, password);
		System.out.println(client);
	}

	@Test
	public void testExecPost()
	{
		//fail("Not yet implemented");
	}

}
