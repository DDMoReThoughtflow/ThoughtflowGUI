package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "assumptions")
public class Assumptions implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Assumption> assumptions;
	
	public Assumptions() {
		this(new ArrayList<Assumption>());
	}
	
	public Assumptions(List<Assumption> assumptions) {
		super();
		if (assumptions != null) {
			this.assumptions = assumptions;
		} else {
			this.assumptions = new ArrayList<Assumption>();;
		}
	}
	
	@XmlElement(name = "assumption")
	public List<Assumption> getAssumptions() {
		return this.assumptions;
	}

	public void setAssumptions(List<Assumption> assumptions) {
		this.assumptions = assumptions;
	}
	
	public void addAssumption(Assumption assumptions) {
		this.assumptions.add(assumptions);
	}
	
	public int addAssumptions(List<Assumption> assumptions) {
		if (Primitives.isNotEmpty(assumptions)) {
			this.assumptions.addAll(assumptions);
			return assumptions.size();
		}
		return 0;
	}

	public boolean hasAssumption(String id) {
		Assumption assumption = new Assumption();
		assumption.setId(id);
		return hasAssumption(assumption);
	}

	public boolean hasAssumption(Assumption assumption) {
		return hasAssumptions() && getAssumptions().contains(assumption);
	}
	
	public int size() {
		return this.assumptions != null ? this.assumptions.size() : 0;
	}
	
	public boolean hasAssumptions() {
		return size() > 0;
	}
	
	public List<Assumption> toList() {
		return (this.assumptions != null ? this.assumptions : new ArrayList<Assumption>());
	}
}
