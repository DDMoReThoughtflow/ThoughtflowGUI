package eu.ddmore.workflow.bwf.core.http;

public class ResponseData {

	private int code;
	private byte[] data;
	
	public ResponseData(int code) {
		super();
		this.code = code;
	}
	
	public ResponseData(int code, byte[] data) {
		super();
		this.code = code;
		this.data = data;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResponseData [code=" + this.code + ", data=" + (this.data != null ? this.data.length : "null")  + "]";
	}
}
