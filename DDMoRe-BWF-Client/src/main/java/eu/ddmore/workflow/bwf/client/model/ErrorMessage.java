package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class ErrorMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private int status;     /** Contains the same HTTP Status code returned by the server */
	private String message; /** Message describing the error*/
	
	public ErrorMessage() {
		super();
	}
	
	public ErrorMessage(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return getMessage() + " (" + getStatus() + ")";
	}
}
