package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "agents")
public class Agents implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Agent> agents;
	
	public Agents() {
		this(new ArrayList<Agent>());
	}
	
	public Agents(List<Agent> agents) {
		super();
		if (agents != null) {
			this.agents = agents;
		} else {
			this.agents = new ArrayList<Agent>();;
		}
	}
	
	@XmlElement(name = "agent")
	public List<Agent> getAgents() {
		return this.agents;
	}

	public void setAgents(List<Agent> agents) {
		this.agents = agents;
	}
	
	public void addAgent(Agent agents) {
		this.agents.add(agents);
	}
	
	public int addAgents(List<Agent> agents) {
		if (Primitives.isNotEmpty(agents)) {
			this.agents.addAll(agents);
			return agents.size();
		}
		return 0;
	}

	public boolean hasAgent(String id) {
		Agent agent = new Agent();
		agent.setId(id);
		return hasAgent(agent);
	}

	public boolean hasAgent(Agent agent) {
		return hasAgents() && getAgents().contains(agent);
	}
	
	public int size() {
		return this.agents != null ? this.agents.size() : 0;
	}
	
	public boolean hasAgents() {
		return size() > 0;
	}
	
	public List<Agent> toList() {
		return (this.agents != null ? this.agents : new ArrayList<Agent>());
	}
}
