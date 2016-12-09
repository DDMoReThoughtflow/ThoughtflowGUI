package eu.ddmore.workflow.server.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "provtypes")
public class ProvTypes implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ProvType> provTypes;
	
	public ProvTypes() {
		this(new ArrayList<ProvType>());
	}
	
	public ProvTypes(List<ProvType> provTypes) {
		super();
		if (provTypes != null) {
			this.provTypes = provTypes;
		} else {
			this.provTypes = new ArrayList<ProvType>();;
		}
	}
	
	@XmlElement(name = "provtype")
	public List<ProvType> getProvTypes() {
		return this.provTypes;
	}

	public void setProvTypes(List<ProvType> provTypes) {
		this.provTypes = provTypes;
	}
	
	public void addProvType(ProvType provTypes) {
		this.provTypes.add(provTypes);
	}
	
	public int addProvTypes(List<ProvType> provTypes) {
		if (Primitives.isNotEmpty(provTypes)) {
			this.provTypes.addAll(provTypes);
			return provTypes.size();
		}
		return 0;
	}
	
	public int size() {
		return this.provTypes != null ? this.provTypes.size() : 0;
	}
	
	public boolean hasProvTypes() {
		return size() > 0;
	}
	
	public List<ProvType> toList() {
		return (this.provTypes != null ? this.provTypes : new ArrayList<ProvType>());
	}
}
