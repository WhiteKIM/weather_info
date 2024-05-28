package egovframework.example.sample.service.impl;

import java.io.FileInputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

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

import egovframework.example.cmmn.Response;
import egovframework.example.cmmn.response.dust.DustResponse;
import egovframework.example.cmmn.response.dust.DustResponseRoot;
import egovframework.example.sample.service.DustService;
import egovframework.example.sample.service.DustVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.property.EgovPropertyService;

@Service("dustService")
public class DustServiceImpl extends EgovAbstractServiceImpl implements DustService {

	private final Logger log = LoggerFactory.getLogger(DustServiceImpl.class);

	@Resource(name = "propertiesService")
	private EgovPropertyService propertiesService;

	@Override
	public DustVO getDustByCity(Double x, Double y) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders header = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(header);
		ObjectMapper mapper = new ObjectMapper();
		String sido = findSidoByCity(x, y);

		URI uri = UriComponentsBuilder
				.fromHttpUrl("https://apis.data.go.kr/B552584/ArpltnStatsSvc/getCtprvnMesureSidoLIst")
				.queryParam("serviceKey", propertiesService.getString("apiKey")).queryParam("pageNo", 1)
				.queryParam("numOfRows", 100).queryParam("pageNo", 1).queryParam("returnType", "json")
				.queryParam("sidoName", sido).queryParam("searchCondition", "DAILY").build().encode().toUri();

		ResponseEntity<DustResponseRoot> entities = restTemplate.exchange(uri, HttpMethod.GET, entity,
				DustResponseRoot.class);
		List<DustVO> dustList = entities.getBody().getResponse().getBody().getItems().stream()
				.map(data -> mapper.convertValue(data, DustVO.class)).collect(Collectors.toList());

		DustVO dustVO = searchByCityName(getCityName(x, y), dustList);
		return dustVO;
	}

	@Override
	public List<DustVO> getDustBySido(String sido) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders header = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(header);
		ObjectMapper mapper = new ObjectMapper();

		URI uri = UriComponentsBuilder
				.fromHttpUrl("https://apis.data.go.kr/B552584/ArpltnStatsSvc/getCtprvnMesureSidoLIst")
				.queryParam("serviceKey", propertiesService.getString("apiKey")).queryParam("pageNo", 1)
				.queryParam("numOfRows", 100).queryParam("pageNo", 1).queryParam("returnType", "json")
				.queryParam("sidoName", sido).queryParam("searchCondition", "DAILY").build().encode().toUri();

		ResponseEntity<DustResponseRoot> entities = restTemplate.exchange(uri, HttpMethod.GET, entity,
				DustResponseRoot.class);
		return entities.getBody().getResponse().getBody().getItems().stream()
				.map(data -> mapper.convertValue(data, DustVO.class)).collect(Collectors.toList());
	}

	private String findSidoByCity(Double x, Double y) throws Exception {
		return getDistrict(x, y);
	}

	private DustVO searchByCityName(String cityName, List<DustVO> dustList) {
		log.debug("[CityName] : {}", cityName);
		for (DustVO dust : dustList) {
			if (dust.getCityName().contains(cityName) || dust.getCityName().equals(cityName)) {
				return dust;
			}
		}

		return dustList.get(0);
	}

	/**
	 * 위경도 -> 시도명 반환
	 * 
	 * @param x - 위도
	 * @param y - 경도
	 * @return
	 * @throws Exception
	 */
	private String getCityName(Double x, Double y) throws Exception {
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
						return city.getStringCellValue();
					}
				}
			}
			return "서울";// 기본값 설정
		}

		return "서울";// 가본값 서울
	}

	/**
	 * 위경도 -> 행정구역 판별
	 * 
	 * @param x - 위도
	 * @param y - 경도
	 * @return 행정구역 명
	 * @throws Exception
	 */
	private String getDistrict(Double x, Double y) throws Exception {
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
						cityName = city.getStringCellValue();
						break;
					}
				}
			}

			if (cityName != null) {
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
						
						if (city.getStringCellValue().equals(cityName) || district.getStringCellValue().equals(cityName)) {
							return getShortDistrictName(district.getStringCellValue());
						}
					}
				}
			}

			return "서울";// 기본값 설정
		}

		return "서울";// 가본값 서울/경기
	}

	/**
	 * 도시명으로 행정시도 판별
	 * 
	 * @param cityName - 판별할 시군명
	 * @return 행정시도명 줄임
	 */
	private String getShortDistrictName(String district) {
		switch (district) {
		case "경기도":
			return "경기";
		case "영서":
		case "영동":
			return "강원";
		case "충청남도":
			return "충남";
		case "충청북도":
			return "충북";
		case "전라남도":
			return "전남";
		case "전라북도":
			return "전북";
		case "경상북도":
			return "경북";
		case "경상남도":
			return "경남";
		case "제주도":
			return "제주";
		default:
			return district;
		}
	}
}
