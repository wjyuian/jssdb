/*
* 2014-11-28 上午9:49:11
* 吴健 HQ01U8435
*/

package com.wj.jssdb.writetest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wj.jssdb.pool.JssdbClient;
import com.wj.jssdb.pool.JssdbPool;
import com.wj.jssdb.pool.JssdbPoolConfig;
import com.wj.jssdb.pool.Protocol;

public class WriteTest {
	private JssdbClient jssdbClient = null;
	
	@Before
	public void init() {
		JssdbPoolConfig config = new JssdbPoolConfig();
		config.setMaxActive(60);
		config.setMinIdle(10);
		config.setMaxIdle(30);
		jssdbClient = new JssdbClient(new JssdbPool(config, Protocol.MASTER_HOST_PORT_TIME), 
			new JssdbPool(config, Protocol.SLAVER_HOST_PORT_TIME));
	}
	public KeyValue createSkuPriceData(long from, long end) {
		KeyValue kv = new KeyValue();
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		for(long i = from; i <= end; i ++) {
			keys.add(i+"");
			values.add(i+".909");
		}
		kv.setKeys(keys);
		kv.setValues(values);
		return kv;
	}
	@Test
	public void testWriter() throws InterruptedException {
		long max = 1000000;
		int threadCount = 30;
		long step = max / threadCount;
		for(int i = 1; i <= threadCount; i ++) {
			long from = (i - 1) * step;
			long end = from + step;
			KeyValue temp = createSkuPriceData(from + 1, end);
			SsdbWriter writer = new SsdbWriter(jssdbClient, temp.getKeys(), temp.getValues());
			new Thread(writer).start();
		}
		
		long begin = System.currentTimeMillis();
		while(SsdbWriter.counter < threadCount) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
		System.out.println("SSDB多线程写性能测试>>>>");
		System.out.println("总数：" + max + "， 处理线程数：" + threadCount);
		long end = System.currentTimeMillis();
		long delt = end - begin;
		TimeLog totalLog = new TimeLog("主线程", max, delt);
		for(TimeLog line : SsdbWriter.log) {
			System.out.println(line);
		}
		System.out.println(totalLog);
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
	}
}
