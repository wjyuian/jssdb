/*
* 2014-11-20 上午10:14:54
* 吴健 HQ01U8435
*/

package com.wj.jssdb.test;

public class Coster {
	private String name;
	private long cost1;
	private long cost2;
	public Coster(String name, long cost1, long cost2) {
		super();
		this.name = name;
		this.cost1 = cost1;
		this.cost2 = cost2;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCost1() {
		return cost1;
	}
	public void setCost1(long cost1) {
		this.cost1 = cost1;
	}
	public long getCost2() {
		return cost2;
	}
	public void setCost2(long cost2) {
		this.cost2 = cost2;
	}
	public void prepareTo(Coster c) {
		long dlt1 = c.getCost1() - this.cost1;
		long dlt2 = c.getCost2() - this.cost2;
		System.out.println("[" + this.name + "] prepare to [" + c.getName() + "]");
		System.out.println("to bytes faster " + (dlt1 * 100 / (float) c.getCost1()) + "%");
		System.out.println("to object faster " + (dlt2 * 100 / (float) c.getCost2()) + "%");
	}
}
