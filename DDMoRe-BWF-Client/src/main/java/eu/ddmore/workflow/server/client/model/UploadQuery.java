package eu.ddmore.workflow.server.client.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "uploadquery")
public class UploadQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	private String repo_id;
	private String entity_id;
	private String ref_id;
	private ProvTypes provTypes;
	private Relation relation;
	private String user_id;
	private Boolean isNew;
	
	public UploadQuery() {
		this(null);
	}

	public UploadQuery(String repo_id) {
		this(repo_id, null);
	}

	public UploadQuery(String repo_id, String entity_id) {
		super();
		this.repo_id = repo_id;
		this.entity_id = entity_id;
		this.provTypes = new ProvTypes();
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

	public String getRef_id() {
		return this.ref_id;
	}

	public void setRef_id(String ref_id) {
		this.ref_id = ref_id;
	}

	public ProvTypes getProvTypes() {
		return this.provTypes;
	}

	public void setProvTypes(ProvTypes provTypes) {
		this.provTypes = provTypes;
	}

	public Relation getRelation() {
		return this.relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public String getUser_id() {
		return this.user_id;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Boolean getIsNew() {
		if (this.isNew == null) {
			this.isNew = Primitives.isNotEmpty(getEntity_id());
		}
		return this.isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}
}
