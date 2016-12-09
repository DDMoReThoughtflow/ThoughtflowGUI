package eu.ddmore.workflow.bwf.web.bean;

import eu.ddmore.workflow.bwf.client.model.Decision;

public class DecisionBean extends BaseProvModelBean<Decision> {

	private static final long serialVersionUID = 1L;
	
	public DecisionBean(Decision model) {
		super(model);
	}

	public DecisionBean(boolean selected, Decision model) {
		super(selected, model);
	}
}
