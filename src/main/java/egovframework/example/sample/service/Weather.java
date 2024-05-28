package egovframework.example.sample.service;

import java.util.List;

public class Weather implements Comparable<Weather>{
	private String baseDate;
	private String baseTime;
	private Category category;
	private String fcstDate;
	private String fcstTime;
	private String fcstValue;
	private int nx;
	private int ny;

	public String getBaseDate() {
		return baseDate;
	}

	public void setBaseDate(String baseDate) {
		this.baseDate = baseDate;
	}

	public String getBaseTime() {
		return baseTime;
	}

	public void setBaseTime(String baseTime) {
		this.baseTime = baseTime;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getFcstDate() {
		return fcstDate;
	}

	public void setFcstDate(String fcstDate) {
		this.fcstDate = fcstDate;
	}

	public String getFcstTime() {
		return fcstTime;
	}

	public void setFcstTime(String fcstTime) {
		this.fcstTime = fcstTime;
	}

	public String getFcstValue() {
		return fcstValue;
	}

	public void setFcstValue(String fcstValue) {
		this.fcstValue = fcstValue;
	}

	public int getNx() {
		return nx;
	}

	public void setNx(int nx) {
		this.nx = nx;
	}

	public int getNy() {
		return ny;
	}

	public void setNy(int ny) {
		this.ny = ny;
	}

	@Override
	public int compareTo(Weather o) {
		// TODO Auto-generated method stub
		int myDate = Integer.parseInt(this.fcstDate);
		int compDate = Integer.parseInt(o.getFcstDate());
		return (myDate - compDate) * -1;
	}
}
