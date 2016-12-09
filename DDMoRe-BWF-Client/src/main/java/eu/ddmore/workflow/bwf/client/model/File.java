package eu.ddmore.workflow.bwf.client.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import eu.ddmore.workflow.bwf.client.enumeration.FileType;
import eu.ddmore.workflow.bwf.client.enumeration.OutputType;
import eu.ddmore.workflow.bwf.client.enumeration.ProvType;
import eu.ddmore.workflow.bwf.client.xmladapter.FileTypeXmlAdapter;
import eu.ddmore.workflow.bwf.client.xmladapter.OutputTypeXmlAdapter;
import eu.ddmore.workflow.bwf.client.xmladapter.ProvTypeXmlAdapter;

@XmlRootElement(name = "file")
public class File extends BaseProvModel implements AuditTrailModel {

	private static final long serialVersionUID = 1L;

	private String name;
	private String path;
	private FileType type;
	private ProvType provType;
	private OutputType outputType;
	private Boolean finalModel;
	private Boolean baseModel;
	private Boolean pivotalModel;
	private Boolean passedQc;
	private byte[] data;
	private String lastModifiedBy;
	
	private Assumptions assumptions;
	private Decisions decisions;
	private Activities activities;
	
	public File() {
		this(null);
	}
	
	public File(String id) {
		super(id);
		this.assumptions = new Assumptions();
		this.decisions = new Decisions();
		this.activities = new Activities();
	}
	
	@Override
	public void setId(String id) {
		super.setId(id);
		buildName();
		builPath();
	}
	
	public String getName() {
		if (this.name == null) {
			buildName();
		}
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private void buildName() {
		if (isNotEmpty(getId())) {
			int index = getId().lastIndexOf("/");
			if (index != -1) {
				setName(getId().substring(index + 1));
			}
		}
	}

	public String getPath() {
		if (this.path == null) {
			builPath();
		}
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private void builPath() {
		if (isNotEmpty(getId())) {
			int index1 = getId().indexOf("/");
			if (index1 != -1) {
				int index2 = getId().lastIndexOf("/");
				if (index2 != -1) {
					setPath(getId().substring((index1 + 1), index2));
				}
			}
		}
	}
	
	@XmlJavaTypeAdapter(FileTypeXmlAdapter.class)
	public FileType getType() {
		return this.type;
	}

	public void setType(FileType type) {
		this.type = type;
	}

	@XmlJavaTypeAdapter(ProvTypeXmlAdapter.class)
	public ProvType getProvType() {
		return this.provType;
	}

	public void setProvType(ProvType provType) {
		this.provType = provType;
	}

	@XmlJavaTypeAdapter(OutputTypeXmlAdapter.class)
	public OutputType getOutputType() {
		return this.outputType;
	}

	public void setOutputType(OutputType outputType) {
		this.outputType = outputType;
	}
	
	public Boolean getFinalModel() {
		return this.finalModel != null ? this.finalModel : false;
	}

	public void setFinalModel(Boolean finalModel) {
		this.finalModel = finalModel;
	}

	public Boolean getBaseModel() {
		return this.baseModel != null ? this.baseModel : false;
	}

	public void setBaseModel(Boolean baseModel) {
		this.baseModel = baseModel;
	}

	public Boolean getPivotalModel() {
		return this.pivotalModel != null ? this.pivotalModel : false;
	}

	public void setPivotalModel(Boolean pivotalModel) {
		this.pivotalModel = pivotalModel;
	}
	
	public Boolean getPassedQc() {
		return this.passedQc;
	}

	public void setPassedQc(Boolean passedQc) {
		this.passedQc = passedQc;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Assumptions getAssumptions() {
		return this.assumptions;
	}

	public void setAssumptions(Assumptions assumptions) {
		this.assumptions = assumptions;
	}

	public Decisions getDecisions() {
		return this.decisions;
	}

	public void setDecisions(Decisions decisions) {
		this.decisions = decisions;
	}

	public Activities getActivities() {
		return this.activities;
	}

	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	@Override
	public String toString() {
		return "[name=" + getName() + "]";
	}
}
