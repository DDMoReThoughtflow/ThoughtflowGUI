package eu.ddmore.workflow.bwf.web.bean;

import eu.ddmore.workflow.bwf.client.model.Activity;

public class ActivityBean extends BaseProvModelBean<Activity> {

	private static final long serialVersionUID = 1L;
	
	public ActivityBean(Activity model) {
		super(model);
	}
	
	public ActivityBean(boolean selected, Activity model) {
		super(selected, model);
	}

}
