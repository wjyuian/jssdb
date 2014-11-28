/*
* 2014-11-28 上午11:29:52
* 吴健 HQ01U8435
*/

package com.wj.jssdb.writetest;

import java.util.List;

public class KeyValue {

	private List<String> keys;
	private List<String> values;
	public KeyValue() {
	}
	public KeyValue(List<String> keys, List<String> values) {
		super();
		this.keys = keys;
		this.values = values;
	}
	public List<String> getKeys() {
		return keys;
	}
	public void setKeys(List<String> keys) {
		this.keys = keys;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
}
