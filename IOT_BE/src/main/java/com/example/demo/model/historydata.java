package com.example.demo.model;

public class historydata {
	private String device;
	private int data_change;
	private String time_change;
	public historydata(String device, int data_change, String time_change) {
		this.device = device;
		this.data_change = data_change;
		this.time_change = time_change;
	}
	public historydata() {
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public int getData_change() {
		return data_change;
	}
	public void setData_change(int data_change) {
		this.data_change = data_change;
	}
	public String getTime_change() {
		return time_change;
	}
	public void setTime_change(String time_change) {
		this.time_change = time_change;
	}
	
}
