package eu.ddmore.workflow.server.client.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import eu.ddmore.workflow.server.client.xmladapter.RelationXmlAdapter;

@XmlRootElement(name = "relation")
public class Relation implements Serializable {

	private static final long serialVersionUID = 1L;

	private eu.ddmore.workflow.bwf.client.enumeration.Relation relation;
	
	public Relation() {
		super();
	}

	public Relation(eu.ddmore.workflow.bwf.client.enumeration.Relation relation) {
		super();
		this.relation = relation;
	}

	@XmlJavaTypeAdapter(RelationXmlAdapter.class)
	public eu.ddmore.workflow.bwf.client.enumeration.Relation getRelation() {
		return this.relation;
	}

	public void setRelation(eu.ddmore.workflow.bwf.client.enumeration.Relation relation) {
		this.relation = relation;
	}
}
