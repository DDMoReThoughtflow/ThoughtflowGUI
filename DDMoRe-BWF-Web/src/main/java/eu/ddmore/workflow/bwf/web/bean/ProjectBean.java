package eu.ddmore.workflow.bwf.web.bean;

import java.util.ArrayList;
import java.util.List;

import eu.ddmore.workflow.bwf.client.model.Project;
import eu.ddmore.workflow.bwf.client.model.User;

public class ProjectBean extends BaseIdModelBean<Project> {

	private static final long serialVersionUID = 1L;
	
	private List<String> access;
	private String accessString;
	private List<String> reviewers;
	private String reviewersString;

	public ProjectBean(boolean selected, Project model) {
		super(selected, model);
	}

	public ProjectBean(Project model) {
		super(model);
	}

	@Override
	protected void init() {
		this.access = new ArrayList<String>();
		this.accessString = "";
		this.reviewers = new ArrayList<String>();
		this.reviewersString = "";

		if (getModel().getAccess() != null && getModel().getAccess().hasUsers()) {
			boolean added = false;
			for (User user : getModel().getAccess().getUsers()) {
				this.access.add(user.getFullName());
				if (added) {
					this.accessString += ", ";
				}
				this.accessString += user.getFullName();
				added = true;
			}
		}
		if (getModel().getReviewers() != null && getModel().getReviewers().hasUsers()) {
			boolean added = false;
			for (User user : getModel().getReviewers().getUsers()) {
				this.reviewers.add(user.getFullName());
				if (added) {
					this.reviewersString += ", ";
				}
				this.reviewersString += user.getFullName();
				added = true;
			}
		}
	}
	
	public List<String> getAccess() {
		return this.access;
	}

	public void setAccess(List<String> access) {
		this.access = access;
	}

	public String getAccessString() {
		return this.accessString;
	}

	public void setAccessString(String accessString) {
		this.accessString = accessString;
	}

	public List<String> getReviewers() {
		return this.reviewers;
	}

	public void setReviewers(List<String> reviewers) {
		this.reviewers = reviewers;
	}
	
	public String getReviewersString() {
		return this.reviewersString;
	}

	public void setReviewersString(String reviewersString) {
		this.reviewersString = reviewersString;
	}
}
