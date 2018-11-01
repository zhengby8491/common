package com.huayin.common.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BeanCloneTest
{
	@Test
	public void testCloneTClassOfK()
	{
		A a=new A();
		a.setName("a");
		a.setAge(1);
		B b=new B();
		b.setName("b");
		b.setAge(2);
		b.setDescription("dddd");
		b=BeanClone.clone(a,B.class);
		System.out.println("testCloneTClassOfK:"+JsonUtils.toJson(b));
	}

	@Test
	public void testCloneListOfTClassOfK()
	{
		List<A> as=new ArrayList<A>();
		A a1=new A();
		a1.setName("a1");
		a1.setAge(1);
		
		A a2=new A();
		a2.setName("a2");
		a2.setAge(2);
		
		as.add(a1);
		as.add(a2);
		List<B> bs=BeanClone.clone(as, B.class);
		System.out.println("testCloneListOfTClassOfK:"+JsonUtils.toJson(bs));
	}
	@Test
	public void testCopy()
	{
		A a=new A();
		a.setName("a");
		a.setAge(null);
		B b=new B();
		b.setName("b");
		b.setAge(2);
		b.setDescription("wo shi b");
		System.out.println("old data"+JsonUtils.toJson(b));
		//BeanClone.copy(a,b);
		PropertyClone.copyProperties(b, a, true,new String[]{"age"});
		System.out.println("new data:"+JsonUtils.toJson(b));
		
		
		
	}
}
