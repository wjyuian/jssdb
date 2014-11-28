/*
* 2014-11-28 下午5:07:49
* 吴健 HQ01U8435
*/

package com.wj.jssdb.pool;

public class KeyValueBean {

	private String key;
	private String value;
	public KeyValueBean() {
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public KeyValueBean(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
}
