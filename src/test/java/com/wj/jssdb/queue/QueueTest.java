/*
* 2014-11-28 下午2:37:04
* 吴健 HQ01U8435
*/

package com.wj.jssdb.queue;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;

public class QueueTest {

	@Test
	public void test() {
		Queue<String> queue = new LinkedList<String>();
		
		queue.add("11111");
		queue.add("22222");
		queue.add("33333");
		
		String next = null;
		while((next = queue.poll()) != null) {
			System.out.println(next);
		}
	}
}
