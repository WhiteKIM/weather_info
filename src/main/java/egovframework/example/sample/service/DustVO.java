package egovframework.example.sample.service;

public class DustVO {
	public String cityName;// 도시명
	public String cityNameEng;// 도시명 영어
	public String coValue;// 일산화탄소 농도
	public String dataGubun;
	public String dataTime;// 측정일시
	public String districtCode;
	public String districtNumSeq;
	public String itemCode;
	public String khaiValue;// 통합 대기 환경 수치
	public String no2Value;// 이산화질소 지수
	public String numOfRows;
	public String o3Value;// 오존 질수
	public String pageNo;
	public String pm10Value;// 미세먼지 pm10 값
	public String pm25Value;// 미세먼지 pm25 값
	public String resultCode;
	public String resultMsg;
	public String returnType;
	public String searchCondition;
	public String serviceKey;
	public String sidoName;// 시도명
	public String so2Value;// 아황산가스 농도
	public String totalCount;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityNameEng() {
		return cityNameEng;
	}

	public void setCityNameEng(String cityNameEng) {
		this.cityNameEng = cityNameEng;
	}

	public String getCoValue() {
		return coValue;
	}

	public void setCoValue(String coValue) {
		this.coValue = coValue;
	}

	public String getDataGubun() {
		return dataGubun;
	}

	public void setDataGubun(String dataGubun) {
		this.dataGubun = dataGubun;
	}

	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictNumSeq() {
		return districtNumSeq;
	}

	public void setDistrictNumSeq(String districtNumSeq) {
		this.districtNumSeq = districtNumSeq;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getKhaiValue() {
		return khaiValue;
	}

	public void setKhaiValue(String khaiValue) {
		this.khaiValue = khaiValue;
	}

	public String getNo2Value() {
		return no2Value;
	}

	public void setNo2Value(String no2Value) {
		this.no2Value = no2Value;
	}

	public String getNumOfRows() {
		return numOfRows;
	}

	public void setNumOfRows(String numOfRows) {
		this.numOfRows = numOfRows;
	}

	public String getO3Value() {
		return o3Value;
	}

	public void setO3Value(String o3Value) {
		this.o3Value = o3Value;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getPm10Value() {
		return pm10Value;
	}

	public void setPm10Value(String pm10Value) {
		this.pm10Value = pm10Value;
	}

	public String getPm25Value() {
		return pm25Value;
	}

	public void setPm25Value(String pm25Value) {
		this.pm25Value = pm25Value;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getServiceKey() {
		return serviceKey;
	}

	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}

	public String getSidoName() {
		return sidoName;
	}

	public void setSidoName(String sidoName) {
		this.sidoName = sidoName;
	}

	public String getSo2Value() {
		return so2Value;
	}

	public void setSo2Value(String so2Value) {
		this.so2Value = so2Value;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return "DustVO [cityName=" + cityName + ", cityNameEng=" + cityNameEng + ", coValue=" + coValue + ", dataGubun="
				+ dataGubun + ", dataTime=" + dataTime + ", districtCode=" + districtCode + ", districtNumSeq="
				+ districtNumSeq + ", itemCode=" + itemCode + ", khaiValue=" + khaiValue + ", no2Value=" + no2Value
				+ ", numOfRows=" + numOfRows + ", o3Value=" + o3Value + ", pageNo=" + pageNo + ", pm10Value="
				+ pm10Value + ", pm25Value=" + pm25Value + ", resultCode=" + resultCode + ", resultMsg=" + resultMsg
				+ ", returnType=" + returnType + ", searchCondition=" + searchCondition + ", serviceKey=" + serviceKey
				+ ", sidoName=" + sidoName + ", so2Value=" + so2Value + ", totalCount=" + totalCount + "]";
	}

}
