package eu.ddmore.workflow.bwf.client.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "activity")
public class Activity extends BaseProvModel implements AuditTrailModel {

	private static final long serialVersionUID = 1L;
	
	private String name;
    private String label;
    private String description;
    private String note;
	private Date start;
	private Date end;
	
	public Activity() {
		super();
	}

	public Activity(String id) {
		super(id);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getStart() {
		return this.start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return this.end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "[label=" + getLabel() + "]";
	}
}
