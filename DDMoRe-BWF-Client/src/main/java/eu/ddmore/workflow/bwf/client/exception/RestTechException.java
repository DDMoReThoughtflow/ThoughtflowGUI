package eu.ddmore.workflow.bwf.client.exception;

public class RestTechException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RestTechException() {
		super();
	}

	public RestTechException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RestTechException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestTechException(String message) {
		super(message);
	}

	public RestTechException(Throwable cause) {
		super(cause);
	}
}
