package egovframework.example.sample.service;

import java.util.List;

public interface WeatherService {
	List<Weather> getWeatherList(Double x, Double y) throws Exception;
	Weather getWeatherInfo() throws Exception;
	String getWeatherForecast() throws Exception;
	MidTermWeather getMidTermWeatherForecast(Double x, Double y) throws Exception;
	List<WeatherWarnVO> getWeatherWarningList() throws Exception;
}
