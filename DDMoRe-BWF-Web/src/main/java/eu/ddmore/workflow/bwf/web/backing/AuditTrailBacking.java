package eu.ddmore.workflow.bwf.web.backing;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import eu.ddmore.workflow.bwf.client.model.Activity;
import eu.ddmore.workflow.bwf.client.model.Agent;
import eu.ddmore.workflow.bwf.client.model.AuditTrailModel;
import eu.ddmore.workflow.bwf.client.model.File;
import eu.ddmore.workflow.bwf.client.service.FileService;
import eu.ddmore.workflow.bwf.client.service.ProjectService;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.web.util.Constants;

@ManagedBean
@ViewScoped
public class AuditTrailBacking extends BaseBacking {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value="#{projectService}")
	private ProjectService projectService;

	@ManagedProperty(value="#{fileService}")
	private FileService fileService;
	
	private Long projectId;
	private String fileId;
	
	private String subTitle;
	private String entityId;
	private Class<? extends AuditTrailModel> selectedType;	
	private Integer depth;
	
	private String json;
	private String svgData;
	
	@PostConstruct
	public void init() {
		setProjectId(Long.valueOf(getParameter("projectId")));
		setFileId(getParameter("fileId"));
		setEntityId(getFileId());
		setSubTitle("File: " + getFileId());
		setSelectedType(File.class);
		setDepth(getDefaultDepth());
	}

	// ---------------------------------------------------------------- Actions

	public void depthChanged() {
		setJson(null);
	}
	
	public void exportSvg() {
		String filename = null;
		
		File file = getFileService().getFile(getProjectId(), getFileId(), false, false, false);
		if (file != null) {
			filename = Primitives.makeValidFilename(file.getName());
		} else {
			filename = "AuditTrail";
		}
		
		exportSvg(getSvgData(), filename);
	}
	
	public void reset() {
		setEntityId(getFileId());
		setSelectedType(File.class);
		setSubTitle("File: " + getFileId());
		setDepth(getDefaultDepth());
		setJson(null);
	}
	
	public void onClickNode() {
		Map<String, String> map = getExternalContext().getRequestParameterMap();
		
		String nodeName = map.get("nodeName");
		String nodeLocation = map.get("nodeLocation");
		String nodeLabel = map.get("nodeLabel");
		String nodeType = map.get("nodeType");

		String id = (isNotEmpty(nodeLocation) ? nodeLocation  : nodeName);
		
		if ("entity".equalsIgnoreCase(nodeType)) {
			setSelectedType(File.class);
			if (!id.startsWith(eu.ddmore.workflow.bwf.client.util.Constants.PREFIX_REPO_COLON)) {
				id = (eu.ddmore.workflow.bwf.client.util.Constants.PREFIX_REPO_COLON + id);
			}
			setSubTitle("File: " + id);
		} else if ("activity".equalsIgnoreCase(nodeType)) {
			setSelectedType(Activity.class);
			if (!id.startsWith(eu.ddmore.workflow.bwf.client.util.Constants.PREFIX_REPO_COLON)) {
				id = (eu.ddmore.workflow.bwf.client.util.Constants.PREFIX_REPO_COLON + id);
			}
			setSubTitle("Activity: " + nodeLabel);
		} else if ("agent".equalsIgnoreCase(nodeType)) {
			setSelectedType(Agent.class);
			setSubTitle("Agent: " + id);
		}
		setEntityId(id);
		setJson(null);
	}

	// --------------------------------------------------------- Getter, Setter
	
	public Long getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public String getSubTitle() {
		return this.subTitle;
	}

	public void setSubTitle(String subTitle) {
		if (subTitle != null) {
			subTitle = subTitle.replaceAll(eu.ddmore.workflow.bwf.client.util.Constants.PREFIX_REPO_COLON, "");
		}
		this.subTitle = subTitle;
	}

	public String getEntityId() {
		return this.entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public Class<? extends AuditTrailModel> getSelectedType() {
		return this.selectedType;
	}

	public void setSelectedType(Class<? extends AuditTrailModel> selectedType) {
		this.selectedType = selectedType;
	}
	
	public Integer getDepth() {
		return this.depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public String getJson() {
		if (this.json == null) {
			this.json = getProjectService().getAuditTrail(getProjectId(), getEntityId(), getSelectedType(), getDepth());
		}
		return this.json;
	}

	public void setJson(String json) {
		this.json = json;
	}
	
	public String getSvgData() {
		return this.svgData;
	}

	public void setSvgData(String svgData) {
		this.svgData = svgData;
	}
	
	// ----------------------------------------------------------------- Helper
	
	private Integer getDefaultDepth() {
		return getWebProperties().getIntegerProperty("auditTrail.default.depth", Constants.AUDIT_TRAIL_DEFAULT_DEPTH);
	}
	
	// -------------------------------------------------------- Spring injected
	
	public ProjectService getProjectService() {
		return this.projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	public FileService getFileService() {
		return this.fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
}
