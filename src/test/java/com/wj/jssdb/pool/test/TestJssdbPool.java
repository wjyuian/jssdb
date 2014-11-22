/*
* 2014-11-20 下午3:24:59
* 吴健 HQ01U8435
*/

package com.wj.jssdb.pool.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wj.jssdb.pool.Jssdb;
import com.wj.jssdb.pool.JssdbPool;
import com.wj.jssdb.pool.JssdbPoolConfig;

public class TestJssdbPool {
	JssdbPool jssdbPool = null;
	JssdbPoolConfig config = null;
	@Before
	public void init() {
		config = new JssdbPoolConfig();
//		config.setMaxActive(0);
//		config.setMaxWait(3000);
		jssdbPool = new JssdbPool(config, "10.100.20.246:8889:2000;10.100.20.245:8889:2000", 1);
	}
	@After
	public void destroy() {
		if(jssdbPool != null) {
			jssdbPool.destroy();
		}
	}
	@Test
	public void showConfig() {
		//连接池最小等待数
		System.out.println("MinIdle " + config.getMinIdle());
		//最大等待数
		System.out.println("MaxIdle " + config.getMaxIdle());
		//最大活动数
		System.out.println("MaxActive " + config.getMaxActive());
		//等待时间，毫秒，最长等待时间，当池中的对象被取完没有返回到池时，等待时间，当有返回的对象时就取出，到时间还没就抛错；-1表示永久等待
		System.out.println("MaxWait() " + config.getMaxWait());
		//设定在进行后台对象清理时，视休眠时间超过了多少毫秒的对象为过期。
		//过期的对象将被回收。如果这个值不是正数，那么对休眠时间没有特别的约束
		System.out.println("MinEvictableIdleTimeMillis " + config.getMinEvictableIdleTimeMillis());
		//设定在进行后台对象清理时，每次检查几个对象。如果这个值不是正数，
		//则每次检查的对象数是检查时池内对象的总数乘以这个值的负倒数再向上取整的结果――
		//也就是说，如果这个值是-2（-3、-4、-5……）的话，那么每次大约检查当时池内对象总数的1/2（1/3、1/4、1/5……）左右
		System.out.println("NumTestsPerEvictionRun " + config.getNumTestsPerEvictionRun());
		//在清理时，可以预留一定数量的idle 对象，是minEvictableIdleTimeMillis的一个附加条件，
		//如果为负值，表示如果清理，则所有idle对象都被清理；
		System.out.println("SoftMinEvictableIdleTimeMillis " + config.getSoftMinEvictableIdleTimeMillis());
		//定义清理扫描的时间间隔。配置为负值，不需要执行清理线程，默认为-1
		System.out.println("TimeBetweenEvictionRunsMillis " + config.getTimeBetweenEvictionRunsMillis());
		//当调用borrow，pool维护的对象超过maxActive时，通过配置将会出现如下动作
		System.out.println("WhenExhaustedAction " + config.getWhenExhaustedAction());
		
		System.out.println(jssdbPool.isConnected());
	}
	@Test
	public void test() {
		int i = 0;
		while(true) {
			try {
				long b = System.currentTimeMillis();
				Jssdb client = jssdbPool.getResource();
				long dtl = System.currentTimeMillis() - b;
				System.out.println( i ++ + "["+(dtl)+"]" + " -> " + client.get("jssdbPool"));
				jssdbPool.returnResource(client);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
