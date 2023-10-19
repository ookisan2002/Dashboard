package com.example.demo.model;

public class data {
	private double temperature;
    private double humidity;
    private double dust;
    private double light;
    private String time;
	public data() {
	}
	public data(double temperature, double humidity,double dust,double light,String time) {
		super();
		this.temperature = temperature;
		this.humidity = humidity;
		this.dust=dust;
		this.light=light;
		this.time=time;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getHumidity() {
		return humidity;
	}
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	public double getDust() {
		return dust;
	}
	public void setDust(double dust) {
		this.dust = dust;
	}
	public double getLight() {
		return light;
	}
	public void setLight(double light) {
		this.light = light;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "data [temperature=" + temperature + ", humidity=" + humidity + ", dust=" + dust + ", light=" + light
				+ ", time=" + time + "]";
	}
	
	
    
    
}
