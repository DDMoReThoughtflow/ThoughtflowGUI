package eu.ddmore.workflow.bwf.client.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "decision")
public class Decision extends BaseProvModel {

	private static final long serialVersionUID = 1L;

	private String description;
	public File file;
	
	public Decision() {
		super();
	}

	public Decision(String id) {
		super(id);
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
