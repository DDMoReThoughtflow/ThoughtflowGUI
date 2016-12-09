package eu.ddmore.workflow.server.client.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "activityquery")
public class ActivityQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	private String repo_id;
	private String activity_id;
	private ProvTypes provTypes;
	private Integer depth;
	private String user_id;

	public ActivityQuery() {
		this(null, null);
	}

	public ActivityQuery(String repo_id) {
		this(repo_id, null);
	}

	public ActivityQuery(String repo_id, String activity_id) {
		super();
		this.repo_id = repo_id;
		this.activity_id = activity_id;
		this.depth = 0;
		this.provTypes = new ProvTypes();
	}
	
	public String getRepo_id() {
		return this.repo_id;
	}
	
	public void setRepo_id(String repo_id) {
		this.repo_id = repo_id;
	}
	
	public String getActivity_id() {
		return this.activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public ProvTypes getProvTypes() {
		return this.provTypes;
	}

	public void setProvTypes(ProvTypes provTypes) {
		this.provTypes = provTypes;
	}

	public Integer getDepth() {
		return this.depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public String getUser_id() {
		return this.user_id;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
