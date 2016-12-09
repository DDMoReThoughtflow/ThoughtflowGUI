package eu.ddmore.workflow.bwf.web.backing;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;

import eu.ddmore.workflow.bwf.client.enumeration.FileType;
import eu.ddmore.workflow.bwf.client.enumeration.ProvType;
import eu.ddmore.workflow.bwf.client.model.Activity;
import eu.ddmore.workflow.bwf.client.model.Assumption;
import eu.ddmore.workflow.bwf.client.model.File;
import eu.ddmore.workflow.bwf.client.model.Project;
import eu.ddmore.workflow.bwf.client.model.User;
import eu.ddmore.workflow.bwf.client.service.FileService;
import eu.ddmore.workflow.bwf.client.service.ProjectService;
import eu.ddmore.workflow.bwf.client.service.UserService;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.web.bean.ActivityBean;
import eu.ddmore.workflow.bwf.web.bean.AssumptionBean;
import eu.ddmore.workflow.bwf.web.bean.FileBean;
import eu.ddmore.workflow.bwf.web.bean.ProjectBean;
import eu.ddmore.workflow.bwf.web.util.Constants;

@ManagedBean
@ViewScoped
public class RepoBacking extends BaseBacking {

	private static final long serialVersionUID = 1L;
	
	private static final String SESSION_PROP_ID_FILE = "ID_FILE";

	public static Comparator<FileBean> FILES_COMPARATOR = new Comparator<FileBean>() { 
		@Override
		public int compare(FileBean f1, FileBean f2) {
			if (ProvType.MODEL == f1.getModel().getProvType() && ProvType.MODEL != f2.getModel().getProvType()) {
				return -1;
			} else if (ProvType.MODEL != f1.getModel().getProvType() && ProvType.MODEL == f2.getModel().getProvType()) {
				return 1;
			} else if (ProvType.PLAN == f1.getModel().getProvType() && ProvType.PLAN != f2.getModel().getProvType()) {
				return -1;
			} else if (ProvType.PLAN != f1.getModel().getProvType() && ProvType.PLAN == f2.getModel().getProvType()) {
				return 1;
			} else {
				return f1.getModel().getName().toLowerCase().compareTo(f2.getModel().getName().toLowerCase());
			}
		}
	};

	@ManagedProperty(value="#{userService}")
	private UserService userService;
	
	@ManagedProperty(value="#{projectService}")
	private ProjectService projectService;

	@ManagedProperty(value="#{fileService}")
	private FileService fileService;
	
	private Long idProject;		 // The currently (from URL parameter) selected project
	private ProjectBean project; // The selected project object 
	private String idActivity;   // The currently (from URL parameter or click) selected activity
	
	private DefaultMenuModel breadCrumb; // Dynamic breadcrump menu depending on current stage
	private int pageStage;				 // Page stage:
	                                     //   - 0: Files overview
										 //   - 1: Activities overview
	                                     //   - 2: Show file
										 //   - 3: Edit project access
										 //   - 4: Edit project reviewers
	
	private List<FileBean> files;			 // All files
	private FileBean file; 					  // The currently selected file to show (e.g. content)
	private List<ActivityBean> activities;	  // Activities to show in table
	private ActivityBean selectedActivity;    // The selected activity bean from UI table
	private String jsonActivities;			  // JSON string to show sctivity chart
	private List<FileBean> inputFiles;		  // All input files for a selected activity
	private List<FileBean> outputFiles;		  // All output files for a selected activity
	private List<AssumptionBean> assumptions; // All assumptions for a project
	private DualListModel<User> assigments;	  // Access and Reviewer assigments
	
	private String svgData;
	
	@PostConstruct
	public void init() {
		setIdProject(Long.valueOf(getParameter("idProject")));
		setIdActivity(getParameter("idActivity"));
		setPageStage(Integer.parseInt(getParameter("pageStage")));
		if (isNotEmpty(getIdActivity())) {
			for (ActivityBean activityBean : getActivities()) {
				if (getIdActivity().equals(activityBean.getId())) {
					setSelectedActivity(activityBean);		
					selectActivityInternal(activityBean.getModel().getName());
					break;
				}
			}
		}
	}
	
	// ---------------------------------------------------------------- Actions

	public void doProject() {
		if (isEmpty(getIdActivity())) {
			redirect(0);
		} else {
			redirect(1);
		}
	}

	public void doShowFile(FileBean fileBean) {
		getSessionBean().setData(SESSION_PROP_ID_FILE, fileBean.getId());
		redirect(2);
	}
	
	public void doEditAccess() {
		redirect(3);
	}

	public void doEditReviewers() {
		redirect(4);
	}
	
	public void doShowAssumptions() {
		redirect(5);
	}

	private void redirect(int pageStage) {
		String redirectString = "/views/repo.jsf?idProject=" + getIdProject() + "&pageStage=" + pageStage;
		if (isNotEmpty(getIdActivity())) {
			redirectString += ("&idActivity=" + getIdActivity());
		}
		redirect(redirectString);
	}
	
	public void markModel(File file) {
		Boolean b = getFileService().markModel(getIdProject(), file.getId(), file.getFinalModel(), file.getBaseModel(), file.getPivotalModel());
		file.setModifiedAt(new Date());
		file.setLastModifiedBy(getUsername());
		System.out.println(">>> markModel: " + file.getFinalModel() + ", " + file.getBaseModel() + ", " + file.getPivotalModel() + " = " + b);
	}
	
	public void setQcFlag(File file, boolean passed) {
		Boolean b = getFileService().passedQc(getIdProject(), file.getId(), passed);
		file.setPassedQc(b);
		file.setModifiedAt(new Date());
		file.setLastModifiedBy(getUsername());
		System.out.println(">>> setQcFlag: " + passed + " = " + b);
	}

	public void doSaveAccess() {
		List<Long> idUserList = new ArrayList<Long>();
		if (isNotEmpty(getAssigments().getTarget())) {
			for (User user : getAssigments().getTarget()) {
				idUserList.add(user.getId());
			}
		}
		
		/** Save updates */
		if (getPageStage() == 3) {
			/** Access */
			getProjectService().updateAccessProjects(getProject().getId(), idUserList);
		} else if (getPageStage() == 4) {
			/** Reviewers */
			getProjectService().updateReviewerProjects(getProject().getId(), idUserList);
		}
		
		doProject();
	}

	public void onClickActivity() {
		Map<String, String> map = getExternalContext().getRequestParameterMap();
		String activityName = map.get("activityName");
		
		if (isEmpty(activityName)) {
			return;
		}
		
		for (ActivityBean activityBean : getActivities()) {
			if (activityName.equals(activityBean.getModel().getName())) {
				setSelectedActivity(activityBean);	
				break;
			}
		}
		selectActivityInternal(activityName);
	}
	
	public void onActivitiesRowSelect(SelectEvent event) {
		ActivityBean activityBean = (ActivityBean) event.getObject();
		selectActivityInternal(activityBean.getModel().getName());
	}
	
	private void selectActivityInternal(String activityName) {
		setInputFiles(new ArrayList<FileBean>());
		setOutputFiles(new ArrayList<FileBean>());

		String idActivity = null;
		if (activityName.startsWith(eu.ddmore.workflow.bwf.client.util.Constants.PREFIX_REPO_COLON)) {
			idActivity = activityName;
		} else {
			idActivity = (eu.ddmore.workflow.bwf.client.util.Constants.PREFIX_REPO_COLON + activityName);
		}
		
		List<File> files = getProjectService().getFiles(getIdProject(), idActivity, true, true, true);
		if (isNotEmpty(files)) {
			for (File file : files) {
				if (FileType.INPUT == file.getType()) {
					getInputFiles().add(new FileBean(file));
				} else {
					getOutputFiles().add(new FileBean(file));
				}
			}
		}
		
		Collections.sort(getInputFiles(), FILES_COMPARATOR);
		Collections.sort(getOutputFiles(), FILES_COMPARATOR);
	}
	
	// --------------------------------------------------------- Getter, Setter
	
	public Long getIdProject() {
		return this.idProject;
	}

	public void setIdProject(Long idProject) {
		this.idProject = idProject;
	}
	
	public ProjectBean getProject() {
		if (this.project == null) {
			Project model = getProjectService().getById(getIdProject(), true, true, true);
			if (model != null) {
				this.project = new ProjectBean(model);
			}
		}
		return this.project;
	}

	public void setProject(ProjectBean project) {
		this.project = project;
	}

	public String getIdActivity() {
		return this.idActivity;
	}

	public void setIdActivity(String idActivity) {
		this.idActivity = idActivity;
	}

	public DefaultMenuModel getBreadCrumb() {
		if (this.breadCrumb == null) {
			this.breadCrumb = new DefaultMenuModel();
			
			DefaultMenuItem itemProjects = new DefaultMenuItem();
			itemProjects.setValue("Projects");
			itemProjects.setOutcome("ownProjects");
			this.breadCrumb.addElement(itemProjects);
			
			DefaultMenuItem itemProjectName = new DefaultMenuItem();
			itemProjectName.setValue(getProject().getModel().getName());
			this.breadCrumb.addElement(itemProjectName);
			
			switch (getPageStage()) {
				case 0:
				case 1:
					itemProjectName.setStyleClass("breadcrump-readonly");
					itemProjectName.setDisabled(true);
					break;
				case 2:
					itemProjectName.setCommand("#{repoBacking.doProject}");
					DefaultMenuItem itemShowFile = new DefaultMenuItem();
					if (getFile() != null && getFile().getModel() != null) {
						itemShowFile.setValue(getFile().getModel().getName());
					} else {
						itemShowFile.setValue("Error loading file");
					}
					itemShowFile.setStyleClass("breadcrump-readonly");
					itemShowFile.setDisabled(true);
					this.breadCrumb.addElement(itemShowFile);
					break;
				case 3:
					itemProjectName.setCommand("#{repoBacking.doProject}");
					DefaultMenuItem itemEditAccess = new DefaultMenuItem();
					itemEditAccess.setValue("Access");
					itemEditAccess.setStyleClass("breadcrump-readonly");
					itemEditAccess.setDisabled(true);
					this.breadCrumb.addElement(itemEditAccess);
					break;
				case 4:
					itemProjectName.setCommand("#{repoBacking.doProject}");
					DefaultMenuItem itemEditReviewers = new DefaultMenuItem();
					itemEditReviewers.setValue("Reviewers");
					itemEditReviewers.setStyleClass("breadcrump-readonly");
					itemEditReviewers.setDisabled(true);
					this.breadCrumb.addElement(itemEditReviewers);
					break;
				case 5:
					itemProjectName.setCommand("#{repoBacking.doProject}");
					DefaultMenuItem itemShowAssumptions = new DefaultMenuItem();
					itemShowAssumptions.setValue(msgs("repo_title_assumptions"));
					itemShowAssumptions.setStyleClass("breadcrump-readonly");
					itemShowAssumptions.setDisabled(true);
					this.breadCrumb.addElement(itemShowAssumptions);
					break;
				default:
					break;
			}
		}
		return this.breadCrumb;
	}

	public void setBreadCrumb(DefaultMenuModel breadCrumb) {
		this.breadCrumb = breadCrumb;
	}
	
	public int getPageStage() {
		return this.pageStage;
	}

	public void setPageStage(int pageStage) {
		this.pageStage = pageStage;
	}

	public List<ActivityBean> getActivities() {
		if (this.activities == null) {
			this.activities = new ArrayList<ActivityBean>();
			List<Activity> models = getProjectService().getActivities(getIdProject(), 0);
			if (isNotEmpty(models)) {
				for (Activity model : models) {
					this.activities.add(new ActivityBean(model));
				}
			}
		}
		return this.activities;
	}

	public void setActivities(List<ActivityBean> activities) {
		this.activities = activities;
	}

	public ActivityBean getSelectedActivity() {
		return this.selectedActivity;
	}
	public void setSelectedActivity(ActivityBean selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	public String getJsonActivities() {
		if (this.jsonActivities == null) {
			this.jsonActivities = getProjectService().getAuditTrail(getIdProject(), null, Activity.class, 0);
		}
		return this.jsonActivities;
	}

	public void setJsonActivities(String jsonActivities) {
		this.jsonActivities = jsonActivities;
	}

	public List<FileBean> getInputFiles() {
		return this.inputFiles;
	}

	public void setInputFiles(List<FileBean> inputFiles) {
		this.inputFiles = inputFiles;
	}

	public List<FileBean> getOutputFiles() {
		return this.outputFiles;
	}

	public void setOutputFiles(List<FileBean> outputFiles) {
		this.outputFiles = outputFiles;
	}

	public List<AssumptionBean> getAssumptions() {
		if (this.assumptions == null) {
			this.assumptions = new ArrayList<AssumptionBean>();
			List<Assumption> assumptions = getProjectService().getAssumptions(getIdProject());
			if (isNotEmpty(assumptions)) {
				for (Assumption model : assumptions) {
					this.assumptions.add(new AssumptionBean(model));
				}
			}
		}
		return this.assumptions;
	}

	public void setAssumptions(List<AssumptionBean> assumptions) {
		this.assumptions = assumptions;
	}

	public List<FileBean> getFiles() {
		if (this.files == null) {
			this.files = new ArrayList<FileBean>();
			List<File> fileModels = getProjectService().getFiles(getIdProject(), null, false, false, false);
			if (isNotEmpty(fileModels)) {
				for (File fileModel : fileModels) {
					this.files.add(new FileBean(fileModel));
				}
				Collections.sort(this.files, FILES_COMPARATOR);
			}
		}
		return this.files;
	}

	public void setFiles(List<FileBean> files) {
		this.files = files;
	}
	
	public FileBean getFile() {
		if (this.file == null) {
			String idFile = (String) getSessionBean().getData(SESSION_PROP_ID_FILE);
			File model = getFileService().getFile(getIdProject(), idFile, true, true, true);
			if (model != null) {
				this.file = new FileBean(model);
			}
		}
		return this.file;
	}

	public void setFile(FileBean file) {
		this.file = file;
	}
	
	public DualListModel<User> getAssigments() {
		if (this.assigments == null) {
			List<User> usersSource = new ArrayList<User>();
	        List<User> usersTarget = new ArrayList<User>();
	        List<User> allUsers = getUserService().search(false, false, false);
	        
	        if (getPageStage() == 3) {
	        	/** Access */
		        for (User user : getProject().getModel().getAccess().getUsers()) {
		        	if (!user.equals(getProject().getModel().getOwner())) {
		        		usersTarget.add(user);
		        	}
				}
		        for (User user : allUsers) {
		        	if (!user.equals(getProject().getModel().getOwner()) && !usersTarget.contains(user)) {
		        		usersSource.add(user);
		        	}
		        }
	        } else if (getPageStage() == 4) {
	        	/** Reviewers */
		        for (User user : getProject().getModel().getReviewers().getUsers()) {
		        	if (!user.equals(getProject().getModel().getOwner())) {
		        		usersTarget.add(user);
		        	}
				}
		        for (User user : allUsers) {
		        	if (!user.equals(getProject().getModel().getOwner()) && !usersTarget.contains(user)) {
		        		usersSource.add(user);
		        	}
		        }
	        }			
	        
	        this.assigments = new DualListModel<User>(usersSource, usersTarget);
		}
		
		return this.assigments;
	}

	public void setAssigments(DualListModel<User> assigments) {
		this.assigments = assigments;
	}
	
	public String getSvgData() {
		return this.svgData;
	}

	public void setSvgData(String svgData) {
		this.svgData = svgData;
	}
	
	// -------------------------------------------------------- Spring injected
	
	public UserService getUserService() {
		return this.userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
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
	
	// ----------------------------------------------------------------- Export
	
	public void exportWord() {
		exportInternal(true);
	}
	
	public void exportCsv() {
		exportInternal(false);
	}

	public void exportInternal(boolean isWord) {
		SimpleDateFormat df = new SimpleDateFormat(msgs("pattern_date_time_m"));
		String filenamePrefix = (getExportProjectname() + (getSelectedActivity() != null ? 
				("_" + Primitives.makeValidFilename(getSelectedActivity().getModel().getLabel())) : ""));
		String contentType = isWord ? Constants.CONTENT_TYPE_WORD : Constants.CONTENT_TYPE_CSV;
		
		try {
			if (getPageStage() == 1) {
				/** Activities */
				
				byte[] dataActivities = null;
				byte[] dataInputFiles = null;
				byte[] dataOutputFiles = null;
				
				List<String[]> bodyList = new ArrayList<String[]>();
				for (ActivityBean activityBean : getActivities()) {
					bodyList.add(getExportRow(activityBean, df));
				}
				dataActivities = isWord ? 
						exportWordInternal(getExportActivitiesHeader(), bodyList) : 
						exportCsvInternal(getExportActivitiesHeader(), bodyList);
				
				/** Input files */
				if (isNotEmpty(getInputFiles())) {
					bodyList = new ArrayList<String[]>();
					for (FileBean fileBean : getInputFiles()) {
						bodyList.add(getExportRow(fileBean, df));
					}
					dataInputFiles = isWord ? 
							exportWordInternal(getExportFilesHeader(), bodyList) : 
							exportCsvInternal(getExportFilesHeader(), bodyList);
				}

				/** Output files */
				if (isNotEmpty(getOutputFiles())) {
					bodyList = new ArrayList<String[]>();
					for (FileBean fileBean : getOutputFiles()) {
						bodyList.add(getExportRow(fileBean, df));
					}
					dataOutputFiles = isWord ? 
							exportWordInternal(getExportFilesHeader(), bodyList) : 
							exportCsvInternal(getExportFilesHeader(), bodyList);
				}

				/** Download */
				if (dataInputFiles == null || dataOutputFiles == null) {
					/** Only 1 file: Do direct download */
					download(contentType, (filenamePrefix + "_activities"), dataActivities);
				} else {
					/** Multiple files: Do zip and download */
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					java.io.File zipfile = new java.io.File(Primitives.getTempDirectory(), (UUID.randomUUID().toString() + ".zip"));
					ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipfile));
					
					try {
						String extension = isWord ? "docx" : "csv";
						
						/** Activities */
						zos.putNextEntry(new ZipEntry(filenamePrefix + "_activities." + extension));
						zos.write(dataActivities);
						zos.closeEntry();
						zos.flush();
						
						/** Input files */
						if (dataInputFiles != null) {
							zos.putNextEntry(new ZipEntry(filenamePrefix + "_inputFiles." + extension));
							zos.write(dataInputFiles);
							zos.closeEntry();
							zos.flush();
						}
						
						/** Output files */
						if (dataOutputFiles != null) {
							zos.putNextEntry(new ZipEntry(filenamePrefix + "_outputFiles." + extension));
							zos.write(dataOutputFiles);
							zos.closeEntry();
							zos.flush();
						}
						
						zos.close();
						
						download(Constants.CONTENT_TYPE_ZIP, (filenamePrefix + "_activities"), Primitives.getBytesFromFile(zipfile));
					} finally {
						IOUtils.closeQuietly(out);
						IOUtils.closeQuietly(zos);
						if (zipfile.exists()) {
							zipfile.delete();
						}
					}
				}
			} else if (getPageStage() == 0) {
				/** Entities */
				
				List<String[]> bodyList = new ArrayList<String[]>();
				for (FileBean fileBean : getFiles()) {
					bodyList.add(getExportRow(fileBean, df));
				}
				
				byte[] data = isWord ? 
						exportWordInternal(getExportFilesHeader(), bodyList) : 
						exportCsvInternal(getExportFilesHeader(), bodyList);
				
				download(contentType, (filenamePrefix + "_entities"), data);
			} else if (getPageStage() == 5) {
				/** Assumtions */
				
				List<String[]> bodyList = new ArrayList<String[]>();
				for (AssumptionBean assumptionBean : getAssumptions()) {
					bodyList.add(getExportRow(assumptionBean));
				}

				byte[] data = isWord ? 
						exportWordInternal(getExportAssumptionsHeader(), bodyList) : 
						exportCsvInternal(getExportAssumptionsHeader(), bodyList);

				download(contentType, (filenamePrefix + "_assumptions"), data);
			}
		} catch (Exception ex) {
			String errorMessage = "Error export to " + (isWord ? "Word" : "CSV") + ".";
			getLog().error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		}
	}
	
	public void exportSvg() {
		exportSvg(getSvgData(), getExportProjectname());
	}
	
	private byte[] exportWordInternal(String[] header, List<String[]> bodyList) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XWPFDocument document = new XWPFDocument();
		
		try {
			/** Create word table **/
			XWPFTable table = document.createTable();
		   
			/** Write word table header */
			writeWordTableHeader(table, true, header);			
			
			/** Write word table content */
			for (String[] row : bodyList) {
				writeWordTableRow(table, false, row);
			}
			
			document.write(out);
			
			return out.toByteArray();
		} finally {
			closeQuietly(document);
			IOUtils.closeQuietly(out);
		}
	}
	
	private byte[] exportCsvInternal(String[] header, List<String[]> bodyList) throws Exception {
		CSVPrinter printer = null;
		
		try {
			/** Create CSV printer */
			Object[] created = createCsvPrinter();
			printer = (CSVPrinter)created[0];
			ByteArrayOutputStream out = (ByteArrayOutputStream)created[1];
			
			/** Write csv header */
			writeCsvRecord(printer, true, header);

			/** Write csv content */
			for (String[] row : bodyList) {
				writeCsvRecord(printer, false, row);
			}
			
			/** Transfer to client */
			printer.flush();
			printer.close();
			
			return out.toByteArray();
		} finally {
			closeQuietly(printer);
		}
	}	
	
	private String getExportProjectname() {
		return Primitives.makeValidFilename(getProject().getModel().getName());
	}
	
	private String[] getExportFilesHeader() {
		return new String[] {
				"tbl_files_col_name", "tbl_files_col_path", "tbl_files_col_type", 
				"tbl_files_col_lastModified", "tbl_files_col_qcStatus", "tbl_files_col_final", 
				"tbl_files_col_base", "tbl_files_col_pivotal", "tbl_files_col_activities"
		};
	}

	private String[] getExportActivitiesHeader() {
		return new String[] {
				"tbl_activities_col_name", "tbl_activities_col_label", "tbl_activities_col_description", 
				"tbl_activities_col_note", "tbl_activities_col_start", "tbl_activities_col_end"
		};
	}

	private String[] getExportAssumptionsHeader() {
		return new String[] {
				"tbl_assumptions_col_type", "tbl_assumptions_col_body", 
				"tbl_assumptions_col_justification", "tbl_assumptions_col_established", 
				"tbl_assumptions_col_testable", "tbl_assumptions_col_testApproach", 
				"tbl_assumptions_col_testOutcome"
		};
	}

	private String[] getExportRow(FileBean fileBean, SimpleDateFormat df) {
		File fileModel = fileBean.getModel();
		return new String[] {
				fileModel.getName(),
				fileModel.getPath(),
				msgs(fileModel.getProvType().toString()),
				(fileModel.getLastModifiedBy() != null ? df.format(fileModel.getLastModifiedBy()) : ""),
				(fileModel.getPassedQc() != null ? (fileModel.getPassedQc() ? msgs("txt_yes") : msgs("txt_no")) : ""),
				(ProvType.MODEL == fileModel.getProvType() && fileModel.getFinalModel() != null ? (fileModel.getFinalModel() ? msgs("txt_yes") : msgs("txt_no")) : ""),
				(ProvType.MODEL == fileModel.getProvType() && fileModel.getBaseModel() != null ? (fileModel.getBaseModel() ? msgs("txt_yes") : msgs("txt_no")) : ""),
				(ProvType.MODEL == fileModel.getProvType() && fileModel.getPivotalModel() != null ? (fileModel.getPivotalModel() ? msgs("txt_yes") : msgs("txt_no")) : ""),
				fileBean.getActivitiesString()
		};
	}

	private String[] getExportRow(ActivityBean activityBean, SimpleDateFormat df) {
		Activity activityModel = activityBean.getModel();
		return new String[] {
				activityModel.getName(),
				activityModel.getLabel(),
				activityModel.getDescription(),
				activityModel.getNote(),
				(activityModel.getStart() != null ? df.format(activityModel.getStart()) : ""),
				(activityModel.getEnd() != null ? df.format(activityModel.getEnd()) : "")
		};
	}
	
	private String[] getExportRow(AssumptionBean assumptionBean) {
		Assumption assumptionModel = assumptionBean.getModel();
		return new String[] {
				msgs(assumptionModel.getType().toString()),
				assumptionModel.getBody(),
				assumptionModel.getJustification(),
				(assumptionModel.getEstablished() != null ? (assumptionModel.getEstablished() ? msgs("txt_yes") : msgs("txt_no")) : ""),
				assumptionModel.getTestable(),
				assumptionModel.getTestApproach(),
				assumptionModel.getTestOutcome()
		};
	}
}
