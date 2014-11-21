/*
* 2014-11-20 下午3:01:53
* 吴健 HQ01U8435
*/

package com.wj.jssdb;

public class JssdbException extends Exception {

	private static final long serialVersionUID = -4700467497662908605L;

	private String msg;

	public JssdbException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
