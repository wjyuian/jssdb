/*
* 2014-11-28 下午1:36:30
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

public class ReadTest {

	private JssdbClient jssdbClient = null;

	public KeyValue createSkuPriceData(long from, long end) {
		KeyValue kv = new KeyValue();
		List<String> keys = new ArrayList<String>();
		for(long i = from; i <= end; i ++) {
			keys.add(i+"");
		}
		kv.setKeys(keys);
		return kv;
	}
	@Before
	public void init() {
		JssdbPoolConfig config = new JssdbPoolConfig();
//		config.setMaxActive(60);
//		config.setMinIdle(10);
//		config.setMaxIdle(30);
		jssdbClient = new JssdbClient(new JssdbPool(config, Protocol.MASTER_HOST_PORT_TIME), 
			new JssdbPool(config, Protocol.SLAVER_HOST_PORT_TIME));
	}
	@Test
	public void test() {
		long max = 800000;
		int threadCount = 30;
		long step = max / threadCount;
		for(int i = 1; i <= threadCount; i ++) {
			long from = (i - 1) * step;
			long end = from + step;
			KeyValue temp = createSkuPriceData(from + 1, end);
			SsdbReader reader = new SsdbReader(jssdbClient, temp.getKeys());
			new Thread(reader).start();
		}
		
		long begin = System.currentTimeMillis();
		while(SsdbReader.counter < threadCount) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
		System.out.println("SSDB读性能测试>>>>");
		System.out.println("总数：" + max + ", 处理线程数：" + threadCount);
		long end = System.currentTimeMillis();
		long delt = end - begin;
		TimeLog totalLog = new TimeLog("主线程", max, delt);
		for(TimeLog line : SsdbReader.log) {
			System.out.println(line);
		}
		System.out.println(totalLog);
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
	}
}
