package egovframework.example.cmmn;

public class ResponseBody {
	private String dataType;
	private ResponseItem items;
	public int pageNo;
    public int numOfRows;
    public int totalCount;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public ResponseItem getItems() {
		return items;
	}

	public void setItems(ResponseItem items) {
		this.items = items;
	}

}
