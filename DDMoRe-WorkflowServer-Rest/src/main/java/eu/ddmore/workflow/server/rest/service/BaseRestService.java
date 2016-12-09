package eu.ddmore.workflow.server.rest.service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ddmore.workflow.bwf.client.model.ErrorMessage;

public class BaseRestService {

	private Logger log;
	
	protected WebApplicationException exception(String message, Throwable ex) {
		getLog().error(message, ex);
		if (ex instanceof WebApplicationException) {
			return (WebApplicationException) ex;
		}
		return new WebApplicationException(ex, Status.BAD_REQUEST);
	}
	
	protected Response error(Status status, String message) {
		ErrorMessage errorMessage = new ErrorMessage(status.getStatusCode(), message);
		return Response.status(Status.UNAUTHORIZED).entity(errorMessage).build();
	}
	
	protected Logger getLog() {
		if (this.log == null) {
			this.log = LoggerFactory.getLogger(this.getClass());
		}
		return this.log;
	}
}
