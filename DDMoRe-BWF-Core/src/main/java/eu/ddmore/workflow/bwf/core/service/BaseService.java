package eu.ddmore.workflow.bwf.core.service;

import java.io.InputStream;
import java.io.Serializable;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.ddmore.workflow.bwf.client.enumeration.RestCallType;
import eu.ddmore.workflow.bwf.client.exception.RestException;
import eu.ddmore.workflow.bwf.client.model.ErrorMessage;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.client.util.RESTFile;
import eu.ddmore.workflow.bwf.client.util.RESTResponse;

public abstract class BaseService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static String DEFAULT_REQUEST_TYPE = MediaType.APPLICATION_JSON; 

	@Value(value="${rest.client.service.contextPath.base:#{null}}")
	private String contextPathBase;

	@Autowired private RestService restService;
	
	private Logger log;

	protected abstract String getServiceRestContextPath();
	
	protected String getRestContextPath() {
		return (Primitives.isNotEmpty(this.contextPathBase) ? 
				(this.contextPathBase + "/" + getServiceRestContextPath()) : getServiceRestContextPath()); 
	}
	
	// ----------------------------------------------------------------- Helper

	protected <T> T doRestGet(Class<T> resultClass, Object... queryParameters) {
		return doRestGet(null, resultClass, queryParameters);
	}
	
	protected <T> T doRestGet(String restMethod, Class<T> resultClass, Object... queryParameters) {
		return doRest(restMethod, resultClass, RestCallType.GET, null, queryParameters);
	}

	protected <T> T doRestPut(Class<T> resultClass, Object entity, Object... queryParameters) {
		return doRestPut(null, resultClass, entity, queryParameters);
	}
	
	protected <T> T doRestPut(String restMethod, Class<T> resultClass, Object entity, Object... queryParameters) {
		return doRestPut(restMethod, resultClass, RestCallType.PUT, entity, queryParameters);
	}

	protected <T> T doRestPost(Class<T> resultClass, Object entity, Object... queryParameters) {
		return doRestPost(null, resultClass, entity, queryParameters);
	}
	
	protected <T> T doRestPost(String restMethod, Class<T> resultClass, Object entity, Object... queryParameters) {
		return doRest(restMethod, resultClass, RestCallType.POST, entity, queryParameters);
	}
	
	protected <T> T doRestDelete(Class<T> resultClass, Object... queryParameters) {
		return doRestDelete(null, resultClass, queryParameters);
	}

	protected <T> T doRestDelete(String restMethod, Class<T> resultClass, Object... queryParameters) {
		return doRest(restMethod, resultClass, RestCallType.DELETE, null);
	}

	private <T> T doRest(String restMethod, Class<T> resultClass, RestCallType restType, Object entity, Object... queryParameters) {
		Response response = null;
		RESTResponse<T> restResponse = null;
		
		try {
			String adaptedRestMethod = (Primitives.isNotEmpty(restMethod) ? ("/" + restMethod) : "");  
			
			WebTarget webTarget = getRestService().getClient().target((getRestService().getUrl() + 
					":" + getRestService().getPort())).path((getRestContextPath() + adaptedRestMethod));
			
			if (Primitives.isNotEmpty(queryParameters)) {
				for (int i = 0; i < queryParameters.length; i=i+2) {
					webTarget = webTarget.queryParam((String) queryParameters[i], queryParameters[i+1]);
				}
			}
			
			if (RestCallType.GET == restType) {
				response = webTarget.request(DEFAULT_REQUEST_TYPE).buildGet().invoke();
			} else if (RestCallType.PUT == restType) {
				response = webTarget.request(DEFAULT_REQUEST_TYPE).buildPut(Entity.entity(entity, DEFAULT_REQUEST_TYPE)).invoke();
			} else if (RestCallType.POST == restType) {
				response = webTarget.request(DEFAULT_REQUEST_TYPE).buildPost(Entity.entity(entity, DEFAULT_REQUEST_TYPE)).invoke();
			} else if (RestCallType.DELETE == restType) {
				response = webTarget.request(DEFAULT_REQUEST_TYPE).buildDelete().invoke();
			}
			
			restResponse = getResponseResult(response, resultClass);
		} catch (Exception ex) {
			String exceptionMessage = buildUnexpectedException();
			getLog().error(exceptionMessage, ex);
			throw new RestException(exceptionMessage, ex);
		} finally {
			closeQuietly(response);
		}
		
		if (restResponse.isError()) {
			throw new RestException(restResponse.getErrorMessage());
		}
		return restResponse.getResult();
	}

	private String buildUnexpectedException() {
		StringBuilder sb = new StringBuilder();
		sb.append("Unexpected error calling location '").append((getRestService().getUrl() + ":" + getRestService().getPort())).append("'.");
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	protected <T> RESTResponse<T> getResponseResult(Response response, Class<T> clazz) {
		T result = null;
		ErrorMessage errorMessage = null;
		StatusType status = response.getStatusInfo();
		if (status.getStatusCode() == Response.Status.OK.getStatusCode()) {
			if (clazz != RESTFile.class) {
				result = response.readEntity(clazz);	
			} else {
				result = (T)extractFile(response);
			}
		} else {
			errorMessage = response.readEntity(ErrorMessage.class);
		} 
		
		if (errorMessage == null && status.getStatusCode() == Response.Status.NOT_FOUND.getStatusCode()) {
			errorMessage = new ErrorMessage(status.getStatusCode(), status.getReasonPhrase());
		}
		
		return new RESTResponse<T>(status.getStatusCode(), result, (errorMessage == null ? null : errorMessage.getMessage()));
	}
		
	private RESTFile extractFile(Response response) {
		InputStream content = response.readEntity(InputStream.class);
		String contentLength = response.getHeaderString("Content-Length");
		long length = -1;
		if (contentLength != null) {
			try {
				length = Long.valueOf(contentLength);
			} catch (NumberFormatException ex) {}
		}
		String fileName = null;
		String contentDisposition = response.getHeaderString("Content-Disposition");
		String pattern = "attachment; filename=";
		if (contentDisposition != null && contentDisposition.startsWith(pattern)) {
			String value = contentDisposition.substring(pattern.length());
			if (value.startsWith("")) {
				value = value.substring(1);
			}
			if (value.endsWith("")) {
				value = value.substring(0, value.length() - 1);
			}
			if (!value.isEmpty()) {
				fileName = value;
			}
		}
		return new RESTFile(fileName, content, length);
	}
	
	protected void closeQuietly(Response response) {
		if (response != null) { try { response.close(); } catch (Exception ex) {} }
	}
	
	// --------------------------------------------------------- Getter, Setter

	protected Logger getLog() {
		if (this.log == null) {
			this.log = LoggerFactory.getLogger(getClass());
		}
		return this.log;
	}

	protected RestService getRestService() {
		return this.restService;
	}
}
