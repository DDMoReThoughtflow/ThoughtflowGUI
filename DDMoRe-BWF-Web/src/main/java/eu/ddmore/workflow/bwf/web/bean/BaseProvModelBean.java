package eu.ddmore.workflow.bwf.web.bean;

import eu.ddmore.workflow.bwf.client.model.BaseProvModel;

public class BaseProvModelBean<M extends BaseProvModel> extends BaseModelBean<M> {

	private static final long serialVersionUID = 1L;

	public BaseProvModelBean(M model) {
		super(model);
	}

	public BaseProvModelBean(boolean selected, M model) {
		super(selected, model);
	}

	public String getId() {
		return hasModel() ? getModel().getId() : null;
	}

	public void setId(String id) {
		if (hasModel()) {
			getModel().setId(id);
		}
	}	
}
