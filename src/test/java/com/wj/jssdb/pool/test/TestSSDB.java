package com.wj.jssdb.pool.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wj.jssdb.core.Response;
import com.wj.jssdb.core.SSDB;
import com.wj.jssdb.core.SSDBCoderUtil;
import com.wj.jssdb.core.test.User;

/**
 * SSDB Java client SDK demo.
 */
public class TestSSDB {
	private List<User> users = new ArrayList<User>();
	public void initUsers() {
		users.clear();
		for(int i = 0; i < 5; i ++) {
			User u = new User("wjyuian", i + 10);
			u.setAddress("上海市浦东新区康桥镇康桥东路800号——" + i);
			users.add(u);
		}
	}
	private SSDB SSDB = null;
	@Before
	public void init() {
		try {
			long b = System.currentTimeMillis();
			SSDB = new SSDB("10.100.20.96", 8889, 3000);
			System.out.println("init ssdb object : " + (System.currentTimeMillis() - b));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testError() {
		while(true) {
			System.out.println(SSDB.isClosed() + " - " + SSDB.isConnected());
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testSet() throws Exception {
		User u = new User("wj", 28);
		u.setAddress("康桥东路800号");
		SSDB.set("wj", SSDBCoderUtil.encode(u));
	}

	@Test
	public void testSetnx() throws Exception {
		SSDB.setnx("master_1", "setnxValueFromJava");
	}
	@Test
	public void testIncr() throws Exception {
		SSDB.incr("testIncr", -1);
	}

	@Test
	public void testGet() throws Exception {
		long b = System.currentTimeMillis();
		User u = SSDBCoderUtil.decode(SSDB.get("wjyuian2"));
		System.out.println("testGet cost : " + (System.currentTimeMillis() - b));
		System.out.println(u);
	}
	@Test
	public void testMSet() throws Exception {
		initUsers();
		int len = users.size();
		byte[][] kvs = new byte[2 * len][];
		for(int i = 0; i < len; i ++) {
			kvs[i * 2] = ("wjyuian" + i).getBytes();
			kvs[i * 2 + 1] = SSDBCoderUtil.encode(users.get(i));
		}
		SSDB.multi_set(kvs);
	}
	@Test
	public void testMGet() throws Exception {
		long b = System.currentTimeMillis();
		String[] keys = new String[]{"wjyuian1", "wj", "wjyuian3"};
		Response response = SSDB.multi_get(keys);
		
		for(Map.Entry<byte[], byte[]> en : response.items.entrySet()) {
			String k = new String(en.getKey());
			User u = SSDBCoderUtil.decode(en.getValue());
			System.out.println(k + " : " + u);
		}
		System.out.println("mget cost : " + (System.currentTimeMillis() - b));
	}
	@Test
	public void testSetExp() throws Exception {
		SSDB.setExp("timeValue", "timeValue1", 11);
	}
	
	@After
	public void close() {
		SSDB.close();
		System.out.println("close ssdb object");
	}
	@Test
	public void testMulti_hset() throws Exception {
		byte[][] kvs = new byte[6][];
		kvs[0] = "1101".getBytes();
		kvs[1] = "175/60".getBytes();
		kvs[2] = "1102".getBytes();
		kvs[3] = "165/82".getBytes();
		kvs[4] = "1103".getBytes();
		kvs[5] = "170/81".getBytes();
		SSDB.multi_hset("testMap", kvs);
	}
	@Test
	public void testHgetAll() throws Exception {
		
		System.out.println(SSDB.hgetall("testMap"));
	}
}
