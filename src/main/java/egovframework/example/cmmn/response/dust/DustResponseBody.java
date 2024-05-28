package egovframework.example.cmmn.response.dust;

import java.util.List;
import java.util.Map;

public class DustResponseBody {
	private List<Map<String, String>> items;
	private int pageNo;
	private int numOfRows;
	private int totalCount;

	public List<Map<String, String>> getItems() {
		return items;
	}

	public void setItems(List<Map<String, String>> items) {
		this.items = items;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getNumOfRows() {
		return numOfRows;
	}

	public void setNumOfRows(int numOfRows) {
		this.numOfRows = numOfRows;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
