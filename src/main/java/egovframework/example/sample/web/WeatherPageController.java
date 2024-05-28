package egovframework.example.sample.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.example.sample.service.WeatherService;
import egovframework.example.sample.service.WeatherVO;
import egovframework.example.sample.service.DustService;
import egovframework.example.sample.service.DustVO;
import egovframework.example.sample.service.MidTermWeather;
import egovframework.example.sample.service.Weather;

@Controller("weatherController")
public class WeatherPageController {
	@Resource(name = "weatherService")
	private WeatherService weatherService;
	
	@Resource(name = "dustService")
	private DustService dustService;
	
	@RequestMapping("/main.do")
	public String main(@RequestParam(required = false) Double x,@RequestParam(required = false) Double y, Model model) throws Exception {
		List<Weather> weatherList = weatherService.getWeatherList(x, y);
		List<WeatherVO> weather = groupingWeather(weatherList);
		DustVO dustVO = dustService.getDustByCity(x, y);
		Collections.sort(weather);
		System.out.print("DUST :"+dustVO.toString());
		
		model.addAttribute("weatherList", weather);
		model.addAttribute("forecast", weatherService.getWeatherForecast());
		model.addAttribute("midterm", weatherService.getMidTermWeatherForecast(x, y));
		model.addAttribute("dust", dustVO);
		model.addAttribute("warning", weatherService.getWeatherWarningList());
		
		return "weather/main";
	}
	
	// fcstTime을 기준으로 구분짓는다.
	private List<WeatherVO> groupingWeather(List<Weather> weatherList) {
		List<WeatherVO> result = new ArrayList<>();
		Map<String, List<Weather>> groupDateList = weatherList
				.stream()
				.collect(Collectors.groupingBy(Weather::getFcstDate));
		
		for(String key : groupDateList.keySet()) {
			List<Weather> data = groupDateList.get(key);
			Map<String, List<Weather>> groupDateAndTime = data.stream().collect(Collectors.groupingBy(Weather::getFcstTime));
			result.addAll(createHourWeatherInfo(groupDateAndTime));
		}
		
		return result;
	}
	
	private List<WeatherVO> createHourWeatherInfo(Map<String, List<Weather>> groupDateAndTime) {
		List<WeatherVO> result = new ArrayList<>();
		for(String key : groupDateAndTime.keySet()) {
			List<Weather> data = groupDateAndTime.get(key);
			result.add(new WeatherVO(data));
		}
		
		return result;
	}
}
