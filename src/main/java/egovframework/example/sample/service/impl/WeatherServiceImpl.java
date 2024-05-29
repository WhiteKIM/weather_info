package egovframework.example.sample.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.example.sample.service.WeatherService;
import egovframework.example.sample.service.WeatherWarnVO;
import egovframework.example.utils.LatXLonY;
import egovframework.example.utils.LocationUtils;
import egovframework.example.cmmn.Response;
import egovframework.example.cmmn.response.dust.DustResponseRoot;
import egovframework.example.sample.service.DayCondition;
import egovframework.example.sample.service.DayTemperature;
import egovframework.example.sample.service.DustVO;
import egovframework.example.sample.service.MidTermWeather;
import egovframework.example.sample.service.Weather;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.property.EgovPropertyService;

@Service("weatherService")
public class WeatherServiceImpl extends EgovAbstractServiceImpl implements WeatherService {
	private final Logger log = LoggerFactory.getLogger(WeatherServiceImpl.class);

	@Resource(name = "propertiesService")
	private EgovPropertyService propertiesService;

	// 단기예보 조회
	@Override
	public List<Weather> getWeatherList(Double x, Double y) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders header = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(header);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		ObjectMapper mapper = new ObjectMapper();
		LatXLonY point = null;
		int nx = 57;// 기본값 X축 좌표
		int ny = 127;// 기본값 Y축 좌표

		if (x != null && y != null) {
			point = LocationUtils.convertGRID_GPS(0, x.doubleValue(), y.doubleValue());
		}

		if (point != null) {
			nx = point.getX();
			ny = point.getY();
		}

		URI uri = UriComponentsBuilder.fromHttpUrl(propertiesService.getString("apiUrl"))
				.queryParam("serviceKey", propertiesService.getString("apiKey")).queryParam("pageNo", 1)
				.queryParam("numOfRows", 1000).queryParam("dataType", "json")
				.queryParam("base_date", format.format(LocalDate.now())).queryParam("base_time", setBaseTime())
				.queryParam("nx", nx).queryParam("ny", ny).build().encode().toUri();

		ResponseEntity<Response> entities = restTemplate.exchange(uri, HttpMethod.GET, entity, Response.class);
		if(entities.getBody().getResponse().getBody() == null) {
			return new ArrayList<>();//빈 값을 반환한다.
		}
		
		return (entities.getBody().getResponse().getBody().getItems().getItem()).stream()
				.map(data -> mapper.convertValue(data, Weather.class)).collect(Collectors.toList());
	}

	// 일기 예보 전망 조회
	@Override
	public String getWeatherForecast() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders header = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(header);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		String dateTime = midTmFc(format);

		URI uri = UriComponentsBuilder.fromHttpUrl("https://apis.data.go.kr/1360000/MidFcstInfoService/getMidFcst")
				.queryParam("serviceKey", propertiesService.getString("apiKey")).queryParam("pageNo", 1)
				.queryParam("numOfRows", 1000).queryParam("dataType", "json").queryParam("stnId", 108)
				.queryParam("tmFc", dateTime).build().encode().toUri();

		ResponseEntity<Response> entities = restTemplate.exchange(uri, HttpMethod.GET, entity, Response.class);
		if(entities.getBody().getResponse().getBody() == null) {
			return "";//빈 값을 반환한다.
		}
		
		List<Map<String, Object>> item = entities.getBody().getResponse().getBody().getItems().getItem();
		return (String) item.get(0).get("wfSv");
	}

	// 중기 예보 조회
	// 해당 서비스는 두 개의 API를 호출해서 데이터를 만들어야 한다.
	@Override
	public MidTermWeather getMidTermWeatherForecast(Double x, Double y) throws Exception {
		String midTaURL = "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa";// 3~10일치 최저최고기온 표시
		String midLandURL = "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";// 3~10 날씨 표시
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders header = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(header);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		String dateTime = midTmFc(format);
		String regId = getRegId(x, y);
		String regId2 = getRegId2(x, y);

		URI taUri = UriComponentsBuilder.fromHttpUrl(midTaURL)
				.queryParam("serviceKey", propertiesService.getString("apiKey")).queryParam("pageNo", 1)
				.queryParam("numOfRows", 10).queryParam("dataType", "json").queryParam("regId", regId)
				.queryParam("tmFc", dateTime).build().encode().toUri();

		URI landUri = UriComponentsBuilder.fromHttpUrl(midLandURL)
				.queryParam("serviceKey", propertiesService.getString("apiKey")).queryParam("pageNo", 1)
				.queryParam("numOfRows", 10).queryParam("dataType", "json").queryParam("regId", regId2)
				.queryParam("tmFc", dateTime).build().encode().toUri();

		ResponseEntity<Response> taEntity = restTemplate.exchange(taUri, HttpMethod.GET, entity, Response.class);
		List<Map<String, Object>> item1 = new ArrayList<>();
		if(taEntity.getBody().getResponse().getBody() != null)
			item1 = taEntity.getBody().getResponse().getBody().getItems().getItem();
		
		// 1~7일은 오전 오후 기상 정보가 조회
		ResponseEntity<Response> landEntity = restTemplate.exchange(landUri, HttpMethod.GET, entity, Response.class);
		List<Map<String, Object>> item2 = new ArrayList<>();
		// 8, 9, 10일의 기상 정보는 하나만
		if(taEntity.getBody().getResponse().getBody() != null)
			item2 = landEntity.getBody().getResponse().getBody().getItems().getItem();
		
		// 일주일 정보는 오전 오후, 8일부터 10일은 하나로 오전 오후 정보로 만드는것을 고려
		Map<String, Object> landData = item2.get(0);
		List<DayCondition> conditionResult = getMidtermWeatherCondition(landData);

		// 기온은 3 ~ 10일에 대한 최고,최저 기온 전부 제공
		Map<String, Object> tmpData = item1.get(0);
		List<DayTemperature> temperatureResult = getMidtermWeatherTemperature(tmpData);

		log.debug("[tmpData] : {}", temperatureResult);
		log.debug("[LandData] : {}", conditionResult);

		return new MidTermWeather(conditionResult, temperatureResult);
	}

	@Override
	public Weather getWeatherInfo() throws Exception {
		return null;
	}

	
	/**
	 * 기상특보 조회
	 * 기상청에서 내린 기상 특보 정보를 조회합니다.
	 * @return 현재 기상 특보 목록
	 */
	@Override
	public List<WeatherWarnVO> getWeatherWarningList() throws Exception {
		String weatherWarnUrl = "https://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnMsg";// 기상특보 조회
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders header = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(header);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate now = LocalDate.now();
		String today = now.format(format);
		ObjectMapper mapper = new ObjectMapper();
		
		URI Uri = UriComponentsBuilder.fromHttpUrl(weatherWarnUrl)
				.queryParam("serviceKey", propertiesService.getString("apiKey"))
				.queryParam("pageNo", 1)
				.queryParam("numOfRows", 10)
				.queryParam("dataType", "json")
				.queryParam("stnId", 108)
				.queryParam("fromTmFc", today)
				.queryParam("toTmFc", today)
				.build().encode().toUri();
		
		ResponseEntity<Response> entities = restTemplate.exchange(Uri, HttpMethod.GET, entity, Response.class);
		List<Map<String, Object>> result = new ArrayList<>();
		
		if(entities.getBody().getResponse().getBody() == null) {
			return new ArrayList<>();//빈 배열을 반환한다.
		}
		
		result = entities.getBody().getResponse().getBody().getItems().getItem();
		return result.stream().map(data -> mapper.convertValue(data, WeatherWarnVO.class)).collect(Collectors.toList());
	}

	/**
	 * 3 ~ 10일의 최저, 최고 기온 값 가져오기
	 * taMin(index) : 최저 기온
	 * taMax(index) : 최고 기온
	 * @param tmpData
	 * @return
	 */
	private List<DayTemperature> getMidtermWeatherTemperature(Map<String, Object> tmpData) {
		int index = 3;
		int minValue = 0;
		int maxValue = 0;
		int count = 0;
		List<DayTemperature> result = new ArrayList<>();

		for (String key : tmpData.keySet()) {
			String min = "taMin" + index;
			String max = "taMax" + index;

			if (key.equals(min)) {
				count += 1;
				minValue = (Integer) tmpData.get(key);
			} else if (key.equals(max)) {
				count += 1;
				maxValue = (Integer) tmpData.get(key);
			}

			if (count == 2) {
				count = 0;
				index += 1;
				result.add(new DayTemperature(minValue, maxValue));
			}
		}

		return result;
	}

	/**
	 * 중기 기상 정보 rn{index}(am or pm) : 강우 확률 wf{index}(am or pm) : 오전 오후 기상 상태
	 * 
	 * @param landData - api로 가져온 데이터
	 * @return
	 */
	private List<DayCondition> getMidtermWeatherCondition(Map<String, Object> landData) {
		int index = 3;
		String amValue = null;
		String pmValue = null;
		String value = null;
		List<DayCondition> conditionResult = new ArrayList<>();
		int count = 0;
		Set<String> landKeySet = landData.keySet();

		for (String key : landKeySet) {
			StringBuffer sb = new StringBuffer();
			String wf = sb.append("wf").append(index).toString();

			if (key.contains(wf)) {
				if (key.contains("Am")) {
					amValue = (String) landData.get(key);
					count += 1;
				} else if (key.contains("Pm")) {
					pmValue = (String) landData.get(key);
					count += 1;
				} else {// 8 ~ 10
					value = (String) landData.get(key);
				}
			}

			if (index < 8) {
				if (count == 2) {
					index += 1;
					log.debug("[Condition] : {}, {}", amValue, pmValue);
					conditionResult.add(new DayCondition(amValue, pmValue));
					count = 0;
				}
			} else {
				index += 1;
				conditionResult.add(new DayCondition(value, value));
			}
		}

		return conditionResult;
	}

	/**
	 * 중기예보 발표기준 시간 설정
	 * 중기 예보 발표 시간 : 06, 18시
	 * @param format
	 * @return
	 */
	private String midTmFc(DateTimeFormatter format) {
		int hour = LocalDateTime.now().getHour();
		String dateTime = format.format(LocalDate.now());

		if (18 <= hour || hour < 6) {
			dateTime += "1800";
		} else {
			dateTime += "0600";
		}
		return dateTime;
	}

	/**
	 * 최근 예보 데이터를 위한 기준 시간 설정
	 * 단기 예보는 3시간마다 발표
	 * @return
	 */
	private String setBaseTime() {
		LocalDateTime now = LocalDateTime.now();

		int hour = now.getHour();
		// 예보는 3시간 기점으로 발표
		if (hour < 2) {
			hour = 23;
		} else if (hour < 5) {
			hour = 2;
		} else if (hour < 8) {
			hour = 5;
		} else if (hour < 11) {
			hour = 8;
		} else if (hour < 14) {
			hour = 11;
		} else if (hour < 17) {
			hour = 14;
		} else if (hour < 20) {
			hour = 17;
		} else if (hour < 23) {
			hour = 20;
		} else {
			hour = 23;
		}

		StringBuilder sb = new StringBuilder();
		if (hour < 10) {
			sb.append("0").append(hour);
		} else {
			sb.append(hour);
		}

		sb.append("00");

		return sb.toString();
	}

	/**
	 * 위경도 -> 시도코드 반환
	 * 
	 * @param x - 위도
	 * @param y - 경도
	 * @return
	 * @throws Exception
	 */
	private String getRegId(Double x, Double y) throws Exception {
		if (x != null && y != null) {
			// 엑셀 파일을 읽고, 해당하는 범위 내의 코드값을 가져온다.
			// 행 : 도시 | 위도 | 경도 | 중기예보 코드값
			// 열 : 각 도시별 값
			ClassPathResource resource = new ClassPathResource("city_latlon.xlsx");
			FileInputStream input = new FileInputStream(resource.getFile());
			XSSFWorkbook excel = new XSSFWorkbook(input);

			// 위경도 범위는 ±0.05 가량으로 설정한다. 필요시 조절
			double adjValue = 0.050d;

			x = (double) x;
			y = (double) y;

			// 엑셀파일 읽기 시작
			XSSFSheet sheet = excel.getSheetAt(0);

			for (int i = 0; i < 193; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					Cell city = row.getCell(0);// 도시명
					Cell latitude = row.getCell(1);// 위도
					Cell longtitue = row.getCell(2);// 경도
					Cell code = row.getCell(3);// 코드값

					double lat = latitude.getNumericCellValue();
					double lon = longtitue.getNumericCellValue();

					if ((Double.compare(x, lat + adjValue) <= 0 && Double.compare(x, lat - adjValue) >= 0)
							&& (Double.compare(y, lon + adjValue) <= 0 && Double.compare(y, lon - adjValue) >= 0)) {
						return code.getStringCellValue();
					}
				}
			}
			return "11B10101";// 기본값 설정
		}

		return "11B10101";// 가본값 서울
	}

	/**
	 * 위경도 -> 시도 코드
	 * 기상청 중기예보 지역 코드는 개각각으로 사용시 문서를 참조해야함
	 * @param x - 위도
	 * @param y - 경도
	 * @return 시도 코드
	 * @throws Exception
	 */
	private String getRegId2(Double x, Double y) throws Exception {
		if (x != null && y != null) {
			String cityName = null;
			// 엑셀 파일을 읽고, 해당하는 범위 내의 코드값을 가져온다.
			// 행 : 도시 | 위도 | 경도 | 중기예보 코드값
			// 열 : 각 도시별 값
			ClassPathResource resource1 = new ClassPathResource("city_latlon.xlsx");
			FileInputStream input1 = new FileInputStream(resource1.getFile());
			XSSFWorkbook excel1 = new XSSFWorkbook(input1);

			// 위경도 범위는 ±0.02 가량으로 설정한다. 필요시 조절
			double adjValue = 0.050d;

			x = (double) x;
			y = (double) y;

			// 엑셀파일 읽기 시작
			XSSFSheet sheet1 = excel1.getSheetAt(0);

			for (int i = 0; i < 193; i++) {
				Row row = sheet1.getRow(i);
				if (row != null) {
					Cell city = row.getCell(0);// 도시명
					Cell latitude = row.getCell(1);// 위도
					Cell longtitue = row.getCell(2);// 경도
					Cell code = row.getCell(3);// 코드값

					double lat = latitude.getNumericCellValue();
					double lon = longtitue.getNumericCellValue();

					if ((Double.compare(x, lat + adjValue) <= 0 && Double.compare(x, lat - adjValue) >= 0)
							&& (Double.compare(y, lon + adjValue) <= 0 && Double.compare(y, lon - adjValue) >= 0)) {
						log.debug("[City] : {}", city.getStringCellValue());
						cityName = code.getStringCellValue();
						break;
					}
				}
			}

			if (cityName != null) {
				log.debug("[City] : {}", cityName);
				// 도시이름으로 행정 구역을 검색하고, 해당 구역에 대한 코드를 반환하자
				ClassPathResource resource2 = new ClassPathResource("cityDistrict.xlsx");
				FileInputStream input2 = new FileInputStream(resource2.getFile());
				XSSFWorkbook excel2 = new XSSFWorkbook(input2);
				XSSFSheet sheet2 = excel2.getSheetAt(0);

				for (int i = 0; i < 291; i++) {
					Row row = sheet2.getRow(i);
					if (row != null) {
						Cell district = row.getCell(0);// 행정구역
						Cell city = row.getCell(1);// 도시명

						if (cityName.equals(district.getStringCellValue())) {
							return getDistrictCode(cityName);
						} else if (city.getStringCellValue().contains(cityName)) {
							return getDistrictCode(district.getStringCellValue());
						}
					}
				}
			}

			return "11B00000";// 기본값 설정
		}

		return "11B00000";// 가본값 서울/경기
	}
	
	/**
	 * 도시명으로 행정시도 판별
	 * @param cityName - 판별할 시군명
	 * @return 시도코드 반환
	 */
	private String getDistrictCode(String cityName) {
		switch (cityName) {
		case "서울":
		case "인천":
		case "경기":
			return "11B00000";
		case "영서":
			return "11D10000";
		case "영동":
			return "11D20000";
		case "대전":
		case "세종":
		case "충청남도":
			return "11C20000";
		case "충청북도":
			return "11C10000";
		case "광주":
		case "전라남도":
			return "11F20000";
		case "전라북도":
			return "11F10000";
		case "대구":
		case "경상북도":
			return "11H10000";
		case "부산":
		case "울산":
		case "경상남도":
			return "11H20000";
		case "제주도":
			return "11G00000";
		default:
			return "11B00000";
		}
	}
}
