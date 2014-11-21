/*
* 2014-11-20 上午9:59:31
* 吴健 HQ01U8435
*/

package com.wj.jssdb.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.wj.jssdb.core.SSDBCoderUtil;

public class TestBytesObject {

	private List<User> users = new ArrayList<User>();
	public void initUsers() {
		users.clear();
		for(int i = 0; i < 100000; i ++) {
			User u = new User("wjyuian", i + 10);
			u.setAddress("上海市浦东新区康桥镇康桥东路800号——" + i);
			users.add(u);
		}
	}
	public Coster testEncodeDecode() {
		Coster coster = null;
		initUsers();
		System.out.println("test encode decode:");
		byte[][] bas2 = new byte[users.size()][];
		long b = System.currentTimeMillis();
		int i = 0;
		for(User u : users) {
			bas2[i] = SSDBCoderUtil.encode(u);
			i ++;
		}
		long cost1 = System.currentTimeMillis() - b;
		System.out.println("encode " + bas2.length + ", cost " + cost1);
		List<User> us2 = new ArrayList<User>();
		b = System.currentTimeMillis();
		for (int j = 0; j < bas2.length; j++) {
			User temp = SSDBCoderUtil.decode(bas2[j]);
			us2.add(temp);
		}
		long cost2 = System.currentTimeMillis() - b;
		System.out.println("decode " + us2.size() + ", cost " + cost2);
		System.out.println();
		
		coster = new Coster("byte-objcet", cost1, cost2);
		return coster;
	}
	public Coster testByteObject() {
		Coster coster = null;
		initUsers();
		System.out.println("test byte object:");
		byte[][] bas = new byte[users.size()][];
		long b = System.currentTimeMillis();
		int i = 0;
		for(User u : users) {
			bas[i] = SSDBCoderUtil.objectToByte(u);
			i ++;
		}
		long cost1 = System.currentTimeMillis() - b;
		System.out.println("convert " + bas.length + ", cost " + cost1);
		List<User> us1 = new ArrayList<User>();
		b = System.currentTimeMillis();
		for (int j = 0; j < bas.length; j++) {
			User temp = SSDBCoderUtil.byteToObject(bas[j]);
			us1.add(temp);
		}
		long cost2 = System.currentTimeMillis() - b;
		System.out.println("convert " + us1.size() + ", cost " + cost2);
		System.out.println();
		coster = new Coster("encode-decode", cost1, cost2);
		return coster;
	}
	@Test
	public void test() {
		Coster c1 = testByteObject();
		Coster c2 = testEncodeDecode();
		c2.prepareTo(c1);
	}
}
