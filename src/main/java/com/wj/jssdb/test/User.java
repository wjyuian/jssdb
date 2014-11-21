/*
* 2014-11-20 上午9:46:37
* 吴健 HQ01U8435
*/

package com.wj.jssdb.test;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 5708306306005216790L;

	private String name;
	
	private Integer age;
	
	private String address;
	
	public User() {
	}

	public User(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", address=" + address
				+ "]";
	}

}
