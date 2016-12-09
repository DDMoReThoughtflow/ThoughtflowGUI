package eu.ddmore.workflow.bwf.web.handler;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.FacesContext;

import eu.ddmore.workflow.bwf.web.util.Constants;

public class FullAjaxExceptionHandler extends org.omnifaces.exceptionhandler.FullAjaxExceptionHandler {

	public FullAjaxExceptionHandler(ExceptionHandler wrapped) {
		super(wrapped);
	}
	
	@Override
	protected void logException(FacesContext context, Throwable exception, String location, String message, Object... parameters) {
		Object user = context.getExternalContext().getSessionMap().get(Constants.SESSION_PROP_USER);
		if (user != null) {
			message += (" (user = " + user.toString() + ")");
		} else {
			message += (" (no user in session)");
		}
		super.logException(context, exception, location, message, parameters);
	}
}
