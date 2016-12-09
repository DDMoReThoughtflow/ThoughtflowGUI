package eu.ddmore.workflow.server.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "relations")
public class Relations implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Relation> relations;
	
	public Relations() {
		this(new ArrayList<Relation>());
	}
	
	public Relations(List<Relation> relations) {
		super();
		if (relations != null) {
			this.relations = relations;
		} else {
			this.relations = new ArrayList<Relation>();;
		}
	}
	
	@XmlElement(name = "relation")
	public List<Relation> getRelations() {
		return this.relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}
	
	public void addRelation(Relation relations) {
		this.relations.add(relations);
	}
	
	public int addRelations(List<Relation> relations) {
		if (Primitives.isNotEmpty(relations)) {
			this.relations.addAll(relations);
			return relations.size();
		}
		return 0;
	}
	
	public int size() {
		return this.relations != null ? this.relations.size() : 0;
	}
	
	public boolean hasRelations() {
		return size() > 0;
	}
	
	public List<Relation> toList() {
		return (this.relations != null ? this.relations : new ArrayList<Relation>());
	}
}
