package eu.ddmore.workflow.server.client.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "entityquery")
public class EntityQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	private String repo_id;
	private String entity_id;
	private ProvTypes provTypes;
	private Relations relations;
	private Integer depth;
	private String user_id;
	
	public EntityQuery() {
		this(null, null);
	}

	public EntityQuery(String repo_id) {
		this(repo_id, null);	
	}

	public EntityQuery(String repo_id, String entity_id) {
		super();
		this.repo_id = repo_id;
		this.entity_id = entity_id;
		this.depth = 0;
		this.provTypes = new ProvTypes();
		this.relations = new Relations();
	}

	public String getRepo_id() {
		return this.repo_id;
	}
	
	public void setRepo_id(String repo_id) {
		this.repo_id = repo_id;
	}
	
	public String getEntity_id() {
		return this.entity_id;
	}

	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}

	public ProvTypes getProvTypes() {
		return this.provTypes;
	}

	public void setProvTypes(ProvTypes provTypes) {
		this.provTypes = provTypes;
	}

	public Relations getRelations() {
		return this.relations;
	}

	public void setRelations(Relations relations) {
		this.relations = relations;
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
