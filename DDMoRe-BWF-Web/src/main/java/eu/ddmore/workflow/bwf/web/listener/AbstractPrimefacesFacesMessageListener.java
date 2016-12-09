package eu.ddmore.workflow.bwf.web.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.primefaces.context.RequestContext;

/**
 * Listener which ensures that error messages are always rendered.
 */
public abstract class AbstractPrimefacesFacesMessageListener implements PhaseListener {

	private static final long serialVersionUID = 1L;
	
	protected abstract void update(RequestContext rc);

	@Override
	public void afterPhase(PhaseEvent event) {
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		RequestContext rc = RequestContext.getCurrentInstance();
		if (rc != null) {
			update(rc);
		}	
	}

	@Override
	public PhaseId getPhaseId() {		
		return PhaseId.RENDER_RESPONSE;
	}
}
