package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "decisions")
public class Decisions implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Decision> decisions;
	
	public Decisions() {
		this(new ArrayList<Decision>());
	}
	
	public Decisions(List<Decision> decisions) {
		super();
		if (decisions != null) {
			this.decisions = decisions;
		} else {
			this.decisions = new ArrayList<Decision>();;
		}
	}
	
	@XmlElement(name = "decision")
	public List<Decision> getDecisions() {
		return this.decisions;
	}

	public void setDecisions(List<Decision> decisions) {
		this.decisions = decisions;
	}
	
	public void addDecision(Decision decisions) {
		this.decisions.add(decisions);
	}
	
	public int addDecisions(List<Decision> decisions) {
		if (Primitives.isNotEmpty(decisions)) {
			this.decisions.addAll(decisions);
			return decisions.size();
		}
		return 0;
	}
	
	public boolean hasDecision(String id) {
		Decision decision = new Decision();
		decision.setId(id);
		return hasDecision(decision);
	}

	public boolean hasDecision(Decision decision) {
		return hasDecisions() && getDecisions().contains(decision);
	}

	public int size() {
		return this.decisions != null ? this.decisions.size() : 0;
	}
	
	public boolean hasDecisions() {
		return size() > 0;
	}
	
	public List<Decision> toList() {
		return (this.decisions != null ? this.decisions : new ArrayList<Decision>());
	}
}
