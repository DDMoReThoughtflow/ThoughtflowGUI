package eu.ddmore.workflow.bwf.client.exception;

public class RestUserException extends Exception {

	private static final long serialVersionUID = 1L;

	public RestUserException() {
		super();
	}

	public RestUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RestUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestUserException(String message) {
		super(message);
	}

	public RestUserException(Throwable cause) {
		super(cause);
	}
}
