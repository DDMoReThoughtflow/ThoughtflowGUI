package eu.ddmore.workflow.bwf.client.util;

public class RESTResponse<T> {

	private final int status;
	private final T result;
	private String errorMessage;
	
	public RESTResponse(int status) {
		this(status, null);
	}
	
	public RESTResponse(int status, T result) {
		this(status, result, null);
	}
	
	public RESTResponse(int status, T result, String errorMessage) {
		this.status = status;
		this.result = result;
		this.errorMessage = errorMessage;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public T getResult() {
		return this.result;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public boolean isError() {
		return this.errorMessage != null;
	}
	
	@Override
	public String toString() {
		return String.valueOf(getStatus());
	}
}
