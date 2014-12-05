/*
* 2014-11-21 上午10:25:22
* 吴健 HQ01U8435
*/

package com.wj.jssdb.pool.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.wj.jssdb.core.test.User;
import com.wj.jssdb.pool.JssdbClient;
import com.wj.jssdb.pool.JssdbPool;
import com.wj.jssdb.pool.JssdbPoolConfig;
import com.wj.jssdb.pool.Protocol;

public class TestJssdbClient {
	private JssdbClient client = null;
	@Before
	public void initClient() {
		JssdbPoolConfig config = new JssdbPoolConfig();
		client = new JssdbClient(new JssdbPool(config, Protocol.MASTER_HOST_PORT_TIME), 
				new JssdbPool(config, Protocol.SLAVER_HOST_PORT_TIME));
	}
	public List<User> initUsers() {
		List<User> users = new ArrayList<User>();
		users.clear();
		for(int i = 0; i < 5; i ++) {
			User u = new User("wjyuian", i + 10);
			u.setAddress("上海市浦东新区康桥镇康桥东路800号——" + i);
			users.add(u);
		}
		return users;
	}
	@Test
	public void test() throws Exception {
		String existsKey = "testKey";
		System.out.println("get from slaver : " + client.get(existsKey));	//从服务器读数据有延迟
		System.out.println("get from master: " + client.getFromMaster(existsKey));
		System.out.println("set value ");
		client.set(existsKey, "clinetValue");
		System.out.println("get from slaver： " + client.get(existsKey));	//从服务器读数据有延迟
		System.out.println("get from master： " + client.getFromMaster(existsKey));
		System.out.println();
		User u = new User("wj", 123);
		System.out.println("get pojo from slaver : " + client.getPojo("user"));	//从服务器读数据有延迟
		System.out.println("get pojo from master : " + client.getPojoFromMaster("user"));
		System.out.println("set Pojo ");
		client.setPojo("user", u);
		System.out.println("get pojo from slaver : " + client.getPojo("user"));	//从服务器读数据有延迟
		System.out.println("get pojo from master : " + client.getPojoFromMaster("user"));
		
		System.out.println();
		String increase = "increase";
		System.out.println("get[increase] : " + client.get(increase));	//从服务器读数据有延迟
		System.out.println("increase["+increase+"] : " + client.increase(increase, 1));
		System.out.println("increase["+increase+"] : " + client.increase(increase, 3));
		
		System.out.println();
		List<User> us = initUsers();
		List<String> keys = new ArrayList<String>();
		for(User user : us) {
			keys.add("wjyuian" + user.getAge());
		}
		System.out.println("get pojo multi from slaver : " + client.mGetPojo(keys));	//从服务器读数据有延迟
		System.out.println("get pojo multi from master : " + client.mGetFromMaster(keys));
		System.out.println("set pojo multi");
		client.mSetPojo(keys, us);
		System.out.println("get pojo multi from slaver : " + client.mGetPojo(keys));	//从服务器读数据有延迟
		System.out.println("get pojo multi from master : " + client.mGetFromMaster(keys));
		System.out.println();
		
		System.out.println();
		System.out.println("get from slaver : " + client.get(existsKey));	//从服务器读数据有延迟
		System.out.println("get from master: " + client.getFromMaster(existsKey));
		System.out.println("set not exists ");
		client.setNotExsits(existsKey, "noExistsValue");
		System.out.println("get from slaver : " + client.get(existsKey));	//从服务器读数据有延迟
		System.out.println("get from master: " + client.getFromMaster(existsKey));
		
		System.out.println();
		System.out.println("hget from slaver : " + client.hGet("autokey", "second"));
		System.out.println("hget from master : " + client.hGetFromMaster("autokey", "second"));
		client.hSet("autokey", "second", "secondValue");
		System.out.println("hget from slaver : " + client.hGet("autokey", "second"));
		System.out.println("hget from master : " + client.hGetFromMaster("autokey", "second"));
		
		client.hDelete("autokey", "second");
		System.out.println("hget from master : " + client.hGetFromMaster("autokey", "second"));
		client.hClear("autokey");
	}
	@Test
	public void testJssdbClientMHset() {
//		List<String> keys = new ArrayList<String>();
//		List<String> values = new ArrayList<String>();
//		for(int i = 0; i < 22; i ++) {
//			keys.add("key_" + i);
//			values.add("value_" + i);
//		}
//		client.mHSet("keyvalue", keys, values);
		long b = System.currentTimeMillis();
		System.out.println(client.hGetAll("keyvalue"));
		System.out.println(System.currentTimeMillis() - b);
		b = System.currentTimeMillis();
		System.out.println(client.hGetAll("keyvalue"));
		System.out.println(System.currentTimeMillis() - b);
	}
	@Test
	public void testHgetAll() {
		long b = System.currentTimeMillis();
		Map<String, String> map = client.hGetAll("keyvalue");
		System.out.println(System.currentTimeMillis() - b);
		System.out.println(map);
	}
	
	@Test
	public void testSlaverDelay() throws InterruptedException {
		String distKey = "testSlaverDelay";
		client.delete(distKey);
		System.out.println("delete key[" + distKey + "]");
		System.out.println("sleep 2000 ms as data in slaver is delete");
		Thread.sleep(2000);
		System.out.println("value of key[" + distKey + "] in slaver : " + client.get(distKey));
		client.set(distKey, distKey);
		long nano = System.nanoTime();
		String fromSlaver = null;
		while((fromSlaver = client.get(distKey)) == null) {
			Thread.sleep(1);
			fromSlaver = client.get(distKey);
		}
		long okNano = System.nanoTime();
		System.out.println("slaver value : " + fromSlaver);
		System.out.println("slaver delay : " + (okNano - nano) / (float)1000000 + " ms.");
	}
}
