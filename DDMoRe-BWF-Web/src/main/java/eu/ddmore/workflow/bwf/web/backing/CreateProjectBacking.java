package eu.ddmore.workflow.bwf.web.backing;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import eu.ddmore.workflow.bwf.client.enumeration.ProjectPriority;
import eu.ddmore.workflow.bwf.client.exception.DuplicateException;
import eu.ddmore.workflow.bwf.client.model.Project;
import eu.ddmore.workflow.bwf.client.service.ProjectService;
import eu.ddmore.workflow.bwf.web.bean.ProjectBean;

@ManagedBean
@ViewScoped
public class CreateProjectBacking extends BaseBacking {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value="#{projectService}")
	private ProjectService projectService;
	
	private ProjectBean project;
	private boolean newReppository;
	
	// ---------------------------------------------------------------- Actions
	
	public void save() {
		if (validate()) {
			getProject().getModel().setOwner(getUser());
			try {
				Project project = getProjectService().insertOrUpdate(getProject().getModel());
				if (project != null) {
					getExternalContext().redirect("ownProjects");
				} else {
					addErrorMessage("messagesCreateProject", "Error", "Could not save project!");
				}
			} catch (DuplicateException ex) {
				addErrorMessage("messagesCreateProject", "Error", "'Project Name' already exists!");
			} catch (Exception ex) {
				addErrorMessage("messagesCreateProject", "Error", "Could not save project!");
			}
		}
	}
	
	private boolean validate() {
		boolean valid = true;
		
		if (isEmpty(getProject().getModel().getName())) {
			addErrorMessage("messagesCreateProject", "Error", "'Project Name' must not be empty!");
			valid = false;
		}
		if (isEmpty(getProject().getModel().getGitUrl())) {
			addErrorMessage("messagesCreateProject", "Error", "'GIT Repository URL' must not be empty!");
			valid = false;
		} else {
			String gitUrlLower = getProject().getModel().getGitUrl().toLowerCase();
			if (!gitUrlLower.startsWith("http")) {
				addErrorMessage("messagesCreateProject", "Error", "'GIT Repository URL' is not valid!");
				valid = false;
			}
		}
		
		return valid;
	}
	
	// --------------------------------------------------------- Getter, Setter
	
	public ProjectBean getProject() {
		if (this.project == null) {
			this.project = new ProjectBean(new Project());
			this.project.getModel().setPriority(ProjectPriority.MEDIUM);
		}
		return this.project;
	}

	public void setProject(ProjectBean project) {
		this.project = project;
	}
	
	public boolean isNewReppository() {
		return this.newReppository;
	}

	public void setNewReppository(boolean newReppository) {
		this.newReppository = newReppository;
	}
	
	// -------------------------------------------------------- Spring injected
	
	public ProjectService getProjectService() {
		return this.projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
}
