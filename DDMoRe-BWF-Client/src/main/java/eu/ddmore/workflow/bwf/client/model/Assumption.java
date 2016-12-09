package eu.ddmore.workflow.bwf.client.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import eu.ddmore.workflow.bwf.client.enumeration.AssumptionType;
import eu.ddmore.workflow.bwf.client.xmladapter.AssumptionTypeXmlAdapter;

@XmlRootElement(name = "assumption")
public class Assumption extends BaseProvModel {

	private static final long serialVersionUID = 1L;
	
	private AssumptionType type;
	private String body;
	private String justification;
	private Boolean established;
	private String testable;
	private String testApproach;
	private String testOutcome;
	public File file;
	
	public Assumption() {
		super();
	}

	public Assumption(String id) {
		super(id);
	}
	
	@XmlJavaTypeAdapter(AssumptionTypeXmlAdapter.class)
	public AssumptionType getType() {
		return this.type;
	}

	public void setType(AssumptionType type) {
		this.type = type;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getJustification() {
		return this.justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public Boolean getEstablished() {
		return this.established;
	}

	public void setEstablished(Boolean established) {
		this.established = established;
	}

	public String getTestable() {
		return this.testable;
	}

	public void setTestable(String testable) {
		this.testable = testable;
	}

	public String getTestApproach() {
		return this.testApproach;
	}

	public void setTestApproach(String testApproach) {
		this.testApproach = testApproach;
	}

	public String getTestOutcome() {
		return this.testOutcome;
	}

	public void setTestOutcome(String testOutcome) {
		this.testOutcome = testOutcome;
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(File file) {
		this.file = file;
	}  
}
