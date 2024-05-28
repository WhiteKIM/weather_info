package egovframework.example.cmmn.response.dust;

public class DustResponse {
	private DustResponseHeader header;
	private DustResponseBody body;

	public DustResponseHeader getHeader() {
		return header;
	}

	public void setHeader(DustResponseHeader header) {
		this.header = header;
	}

	public DustResponseBody getBody() {
		return body;
	}

	public void setBody(DustResponseBody body) {
		this.body = body;
	}

}
