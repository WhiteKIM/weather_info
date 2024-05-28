package egovframework.example.sample.service;

public class DayTemperature {
	private int min;// 최저 기온
	private int max;// 최고 기온
	
	public DayTemperature() {
		
	}
	
	public DayTemperature(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "DayTemperature [min=" + min + ", max=" + max + "]";
	}
}
