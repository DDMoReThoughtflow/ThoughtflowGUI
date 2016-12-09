package eu.ddmore.workflow.bwf.web.backing;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import eu.ddmore.workflow.bwf.client.model.Project;
import eu.ddmore.workflow.bwf.client.service.ProjectService;
import eu.ddmore.workflow.bwf.web.bean.ProjectBean;
import eu.ddmore.workflow.bwf.web.util.Constants;

public abstract class AbstractProjectBacking extends BaseBacking {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value="#{projectService}")
	private ProjectService projectService;
	
	private List<ProjectBean> projects;
	private boolean canChange;

	protected abstract List<Project> getProjectModels();
	protected abstract boolean getCanChange();
	protected abstract String getExportFilename();
	
	// ---------------------------------------------------------------- Actions
	
	public void reset() {
		setProjects(null);
	}

	public void onCellEdit(ProjectBean bean) {
		Project project = getProjectService().insertOrUpdate(bean.getModel());
		if (project != null) {
			setProjects(null);
		} else {
			showErrorMessageInDialogWithOk("Update", "Could not update project.");
		}
    }
	
	// --------------------------------------------------------- Getter, Setter
	
	public List<ProjectBean> getProjects() {
		if (this.projects == null) {
			this.projects = new ArrayList<ProjectBean>();
			List<Project> modelList = getProjectModels();
			if (isNotEmpty(modelList)) {
				for (Project model : modelList) {
					this.projects.add(new ProjectBean(model));
				}
			}
		}
		return this.projects;
	}

	public void setProjects(List<ProjectBean> projects) {
		this.projects = projects;
	}

	public boolean isCanChange() {
		return this.canChange;
	}
	
	public void setCanChange(boolean canChange) {
		this.canChange = canChange;
	}
	
	// -------------------------------------------------------- Spring injected
	
	public ProjectService getProjectService() {
		return this.projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	// ----------------------------------------------------------------- Export

	public void exportWord() {
		SimpleDateFormat df = new SimpleDateFormat(msgs("pattern_date_time_m"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XWPFDocument document = new XWPFDocument();
		
		try {
			/** Create word table **/
			XWPFTable table = document.createTable();
		   
			/** Write word table header */
			writeWordTableHeader(table, true, getExportHeader());
			
			/** Write word table content */
			for (ProjectBean projectBean : getProjects()) {
				writeWordTableRow(table, false, getExportRow(projectBean, df));
			}
			
			document.write(out);
			
			download(Constants.CONTENT_TYPE_WORD, (getUsername() + "_" + getExportFilename()), out);
		} catch (Exception ex) {
			String errorMessage = "Error export to Word.";
			getLog().error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		} finally {
			closeQuietly(document);
			IOUtils.closeQuietly(out);
		}
	}
	
	public void exportCsv() {
		SimpleDateFormat df = new SimpleDateFormat(msgs("pattern_date_time_m"));
		CSVPrinter printer = null;
		
		try {
			/** Create CSV printer */
			Object[] created = createCsvPrinter();
			printer = (CSVPrinter)created[0];
			ByteArrayOutputStream out = (ByteArrayOutputStream)created[1];
			
			/** Write csv header */
			writeCsvRecord(printer, true, getExportHeader());

			/** Write csv content */
			for (ProjectBean projectBean : getProjects()) {
				writeCsvRecord(printer, false, getExportRow(projectBean, df));
			}
			
			/** Transfer to client */
			printer.flush();
			printer.close();
			
			download(Constants.CONTENT_TYPE_CSV, (getUsername() + "_" + getExportFilename()), out);
		} catch (Exception ex) {
			String errorMessage = "Error export to CSV.";
			getLog().error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		} finally {
			closeQuietly(printer);
		}
	}
	
	private String[] getExportHeader() {
		return new String[] {
				"v_project_col_name", "v_project_col_priority", "v_project_col_owner", 
				"v_project_col_access", "v_project_col_reviewers", "txt_created", "txt_modified"
		};
	}
	
	private String[] getExportRow(ProjectBean projectBean, SimpleDateFormat df) {
		Project projectModel = projectBean.getModel();
		return new String[] {
				projectModel.getName(),
				msgs(projectModel.getPriority().toString()),
				projectModel.getOwner().getFullName(),
				projectBean.getAccessString(),
				projectBean.getReviewersString(),
				(projectModel.getCreatedAt() != null ? df.format(projectModel.getCreatedAt()) : ""),
				(projectModel.getModifiedAt() != null ? df.format(projectModel.getCreatedAt()) : "")
		};
	}
}
