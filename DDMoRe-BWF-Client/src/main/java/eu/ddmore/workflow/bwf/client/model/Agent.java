package eu.ddmore.workflow.bwf.client.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "agent")
public class Agent extends BaseProvModel implements AuditTrailModel {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	public Agent() {
		super();
	}

	public Agent(String id) {
		super(id);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected void buildLocation() {
	}
	
	@Override
	public String toString() {
		return "[name=" + getName() + "]";
	}
}
