package eu.ddmore.workflow.bwf.web.bean;

import eu.ddmore.workflow.bwf.client.model.BaseIdModel;

public class BaseIdModelBean<M extends BaseIdModel> extends BaseModelBean<M> {

	private static final long serialVersionUID = 1L;

	public BaseIdModelBean(M model) {
		super(model);
	}

	public BaseIdModelBean(boolean selected, M model) {
		super(selected, model);
	}

	public Long getId() {
		return hasModel() ? getModel().getId() : null;
	}

	public void setId(Long id) {
		if (hasModel()) {
			getModel().setId(id);
		}
	}	
}
