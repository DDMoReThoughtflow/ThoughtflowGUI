package eu.ddmore.workflow.bwf.web.bean;

import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.StreamedContent;

import eu.ddmore.workflow.bwf.client.enumeration.ProvType;
import eu.ddmore.workflow.bwf.client.model.Activity;
import eu.ddmore.workflow.bwf.client.model.File;

public class FileBean extends BaseProvModelBean<File> {

	private static final long serialVersionUID = 1L;
	
	private static final int MAX_PATH_LENGHT = 17;
	
	private Boolean isModel;
	private Boolean isPicture;
	private Boolean hasData;
	
	private String path;
	private Boolean pathAbbreviated;
	private String activitiesString;
	private String strContent;
	private StreamedContent picData;
	
	public FileBean(File model) {
		super(model);
	}

	public FileBean(boolean selected, File model) {
		super(selected, model);
	}

	@Override
	protected void init() {
		this.isModel = (ProvType.MODEL == getModel().getProvType());
		this.isPicture = (ProvType.IMAGE == getModel().getProvType());
		this.hasData = (getModel().getData() != null && getModel().getData().length > 0);
		
		if (getModel().getActivities().hasActivitys()) {
			activitiesString = "";
			for (Activity activity : getModel().getActivities().getActivities()) {
				boolean added = false;
				if (added) {
					this.activitiesString += ",";
				}
				this.activitiesString += activity.getLabel();
				added = true;
			}
		}
		
		this.pathAbbreviated = false;
		if (isNotEmpty(getModel().getPath())) {
			this.path = getModel().getPath();
			if (this.path.length() > MAX_PATH_LENGHT) {
				this.path = (this.path.substring(0, MAX_PATH_LENGHT) + "...");
				this.pathAbbreviated = true;
			}
		}
	}
	
	public Boolean getIsModel() {
		return this.isModel;
	}

	public void setIsModel(Boolean isModel) {
		this.isModel = isModel;
	}

	public Boolean getIsPicture() {
		return this.isPicture;
	}

	public void setIsPicture(Boolean isPicture) {
		this.isPicture = isPicture;
	}

	public Boolean getHasData() {
		return this.hasData;
	}

	public void setHasData(Boolean hasData) {
		this.hasData = hasData;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Boolean getPathAbbreviated() {
		return this.pathAbbreviated;
	}

	public void setPathAbbreviated(Boolean pathAbbreviated) {
		this.pathAbbreviated = pathAbbreviated;
	}

	public String getActivitiesString() {
		return this.activitiesString;
	}

	public void setActivitiesString(String activitiesString) {
		this.activitiesString = activitiesString;
	}

	public String getStrContent() {
		if (this.strContent == null && getHasData()) {
			this.strContent = new String(getModel().getData());
		}
		return this.strContent;
	}

	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}

	public StreamedContent getPicData() {
		if (this.picData == null && getHasData()) {
			this.picData = new ByteArrayContent(getModel().getData());
		}
		return this.picData;
	}

	public void setPic(StreamedContent picData) {
		this.picData = picData;
	}
}
