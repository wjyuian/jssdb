/*
* 2014-11-28 上午10:02:57
* 吴健 HQ01U8435
*/

package com.wj.jssdb.writetest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wj.jssdb.pool.JssdbClient;

public class SsdbReader implements Runnable {
	public static Integer counter = 0;
	public static List<TimeLog> log = new ArrayList<TimeLog>();
	private JssdbClient jssdbClient = null;
	
	private List<String> keys = null;
	
	public SsdbReader(JssdbClient jssdbClient, List<String> keys) {
		super();
		this.jssdbClient = jssdbClient;
		this.keys = keys;
	}

	public JssdbClient getJssdbClient() {
		return jssdbClient;
	}

	public void setJssdbClient(JssdbClient jssdbClient) {
		this.jssdbClient = jssdbClient;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	private void finish(long size, long cost) {
		synchronized (counter) {
			counter ++;
			log.add(new TimeLog("子线程[#" + counter + "]", size, cost));
		}
	}

	@SuppressWarnings("unused")
	public void run() {
		if(jssdbClient != null && keys != null) {
			long b = System.currentTimeMillis();
			try {
				Map<String, String> rs = jssdbClient.mGetMaster(keys);
			} catch (Exception e) {
				e.printStackTrace();
			}
			long delt = System.currentTimeMillis() - b;
			finish(keys.size(), delt);
		}
	}

}
