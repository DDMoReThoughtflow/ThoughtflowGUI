package eu.ddmore.workflow.bwf.web.bean;

import eu.ddmore.workflow.bwf.client.model.Assumption;

public class AssumptionBean extends BaseProvModelBean<Assumption> {

	private static final long serialVersionUID = 1L;
	
	public AssumptionBean(Assumption model) {
		super(model);
	}

	public AssumptionBean(boolean selected, Assumption model) {
		super(selected, model);
	}
}
