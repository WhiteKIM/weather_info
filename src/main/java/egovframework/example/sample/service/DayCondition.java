package egovframework.example.sample.service;

public class DayCondition {
	private String amCondition;// 오전 기상 조건
	private String pmCondition;// 오후 기상 조건

	public DayCondition() {

	}

	public DayCondition(String amCondition, String pmCondition) {
		this.amCondition = amCondition;
		this.pmCondition = pmCondition;
	}

	public String getAmCondition() {
		return amCondition;
	}

	public void setAmCondition(String amCondition) {
		this.amCondition = amCondition;
	}

	public String getPmCondition() {
		return pmCondition;
	}

	public void setPmCondition(String pmCondition) {
		this.pmCondition = pmCondition;
	}

	@Override
	public String toString() {
		return "DayCondition [amCondition=" + amCondition + ", pmCondition=" + pmCondition + "]";
	}

}
