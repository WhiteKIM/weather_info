package egovframework.example.sample.service;

import java.util.List;

public class MidTermWeather {
	private List<DayCondition> conditionList;
	private List<DayTemperature> temperatureList;
	
	public MidTermWeather() {
		
	}
	
	public MidTermWeather(List<DayCondition> conditionList, List<DayTemperature> temperatureList) {
		this.conditionList = conditionList;
		this.temperatureList = temperatureList;
	}

	public List<DayCondition> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<DayCondition> conditionList) {
		this.conditionList = conditionList;
	}

	public List<DayTemperature> getTemperatureList() {
		return temperatureList;
	}

	public void setTemperatureList(List<DayTemperature> temperatureList) {
		this.temperatureList = temperatureList;
	}
}