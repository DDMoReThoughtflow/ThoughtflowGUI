package eu.ddmore.workflow.bwf.client.model;

import eu.ddmore.workflow.bwf.client.util.Constants;

public class BaseProvModel extends BaseModel {

	private static final long serialVersionUID = 1L;

	private String id; // e.g. repo:4bf8c20c3cd38f233f23bfff3412fe63d78b4f26/models/Step1/conversionReport.log
	private String location;
	
	public BaseProvModel() {
		super();
	}

	public BaseProvModel(String id) {
		super();
		setId(id);
	}

	// --------------------------------------------------------- Getter, Setter
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
		buildLocation();
	}

	public String getLocation() {
		if (this.location == null) {
			buildLocation();
		}
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	protected void buildLocation() {
		if (isNotEmpty(getId())) {
			if (getId().startsWith(Constants.PREFIX_REPO_COLON)) {
				setLocation(getId().substring(Constants.PREFIX_REPO_COLON.length()));
			} else {
				setLocation(getId());
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BaseProvModel other = (BaseProvModel) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
