/*
* 2014-11-28 上午10:20:09
* 吴健 HQ01U8435
*/

package com.wj.jssdb.writetest;

/**
 * @author 吴健 HQ01U8435
 *
 */
public class TimeLog {

	private String name;
	private long counter;
	private long time;
	public TimeLog(String name, long counter, long time) {
		super();
		this.name = name;
		this.counter = counter;
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCounter() {
		return counter;
	}
	public void setCounter(long counter) {
		this.counter = counter;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	public String toString() {
		return name + " 处理 " + counter + " 条数据, 耗时 : " + time + " 毫秒, 速度 : " + (counter * 1000 / (float) time) + "/秒";
	}
}
