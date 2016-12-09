package eu.ddmore.workflow.bwf.client.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import eu.ddmore.workflow.bwf.client.enumeration.ProjectPriority;
import eu.ddmore.workflow.bwf.client.xmladapter.ProjectPriorityXmlAdapter;

@XmlRootElement(name = "project")
public class Project extends BaseIdModel {

	private static final long serialVersionUID = 1L;

	private String name;
	private String gitUrl;
	private ProjectPriority priority;
	
	private Long idOwner;
	private User owner;
	private Users access;
	private Users reviewers;

	private Files files;
	
	public Project() {
		super();
		this.files = new Files();
		this.access = new Users();
		this.reviewers = new Users();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getGitUrl() {
		return this.gitUrl;
	}

	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
	}

	@XmlJavaTypeAdapter(ProjectPriorityXmlAdapter.class)
	public ProjectPriority getPriority() {
		return this.priority;
	}

	public void setPriority(ProjectPriority priority) {
		this.priority = priority;
	}

	public Long getIdOwner() {
		if (this.idOwner == null && this.owner != null) {
			this.idOwner = this.owner.getId();
		}
		return this.idOwner;
	}

	public void setIdOwner(Long idOwner) {
		this.idOwner = idOwner;
	}

	public User getOwner() {
		if (this.owner == null && this.idOwner != null) {
			this.owner = new User();
			this.owner.setId(this.idOwner);
		}
		return this.owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Users getAccess() {
		return this.access;
	}

	public void setAccess(Users access) {
		this.access = access;
	}

	public Users getReviewers() {
		return this.reviewers;
	}

	public void setReviewers(Users reviewers) {
		this.reviewers = reviewers;
	}

	public Files getFiles() {
		return this.files;
	}

	public void setFiles(Files files) {
		this.files = files;
	}
	
	@Override
	public String toString() {
		return "[id=" + getId() + ", name=" + getName() + ", priority=" + getPriority() + ", gitUrl=" + getGitUrl() + "]";
	}
}
