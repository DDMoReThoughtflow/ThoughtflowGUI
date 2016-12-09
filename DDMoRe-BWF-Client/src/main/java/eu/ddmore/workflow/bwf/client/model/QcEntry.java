package eu.ddmore.workflow.bwf.client.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "qc")
public class QcEntry extends BaseProvModel {

	private static final long serialVersionUID = 1L;
	
	private String model;
	private String relation;
	private String entity;
	
	public QcEntry() {
		super();
	}

	public QcEntry(String id) {
		super(id);
	}
	
	public String getModel() {
		return this.model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getRelation() {
		return this.relation;
	}
	
	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	public String getEntity() {
		return this.entity;
	}
	
	public void setEntity(String entity) {
		this.entity = entity;
	}
}
