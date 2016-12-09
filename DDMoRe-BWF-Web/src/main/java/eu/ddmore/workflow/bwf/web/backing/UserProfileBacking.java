package eu.ddmore.workflow.bwf.web.backing;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import eu.ddmore.workflow.bwf.client.model.Project;
import eu.ddmore.workflow.bwf.client.service.ProjectService;
import eu.ddmore.workflow.bwf.web.bean.ProjectBean;

@ManagedBean
@ViewScoped
public class UserProfileBacking extends BaseBacking {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value="#{projectService}")
	private ProjectService projectService;
	
	private List<ProjectBean> projects;

	public List<ProjectBean> getProjects() {
		if (this.projects == null) {
			this.projects = new ArrayList<ProjectBean>();
			List<Project> modelList = getProjectService().getAccessProjects(getIdUser(), true, true, true);
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
	
	// -------------------------------------------------------- Spring injected
	
	public ProjectService getProjectService() {
		return this.projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
}
