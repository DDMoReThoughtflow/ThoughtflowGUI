package eu.ddmore.workflow.bwf.core.service.impl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Assumption")
public class AssumptionContent {
	private static final long serialVersionUID = 1L;
	
	private String type;
	private String body;
	private String justification;
	private String established;
	private String testable;
	private String testApproach;
	private String testOutcome;
	
	public AssumptionContent() {
		super();
	}
	
	@XmlElement(name="Type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name="AssumptionBody")
	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@XmlElement(name="Justification")
	public String getJustification() {
		return this.justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	@XmlElement(name="Established")
	public String getEstablished() {
		return this.established;
	}

	public void setEstablished(String established) {
		this.established = established;
	}

	@XmlElement(name="Testable")
	public String getTestable() {
		return this.testable;
	}

	public void setTestable(String testable) {
		this.testable = testable;
	}

	@XmlElement(name="TestApproach")
	public String getTestApproach() {
		return this.testApproach;
	}

	public void setTestApproach(String testApproach) {
		this.testApproach = testApproach;
	}

	@XmlElement(name="TestOutcome")
	public String getTestOutcome() {
		return this.testOutcome;
	}

	public void setTestOutcome(String testOutcome) {
		this.testOutcome = testOutcome;
	}

}
