package eu.ddmore.workflow.bwf.client.exception;

public class RestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RestException() {
		super();
	}

	public RestException(String message) {
		super(message);
	}

	public RestException(Throwable cause) {
		super(cause);
	}

	public RestException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
