package eu.ddmore.workflow.bwf.web.backing;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import eu.ddmore.workflow.bwf.client.model.Project;
import eu.ddmore.workflow.bwf.client.model.User;
import eu.ddmore.workflow.bwf.client.service.UserService;

@ManagedBean
@ViewScoped
public class UserProjectBacking extends AbstractProjectBacking {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value="#{userService}")
	private UserService userService;
	
	private User selectedUser;
	
	@PostConstruct
	public void init() {
		String idUserString = getParameter("id");
		if (isNotEmpty(idUserString)) {
			try {
				Long idUser = Long.valueOf(idUserString);
				this.selectedUser = getUserService().getById(idUser, false, false, false);
			} catch (Exception ex) {
				getLog().error("Could not load user for '" + idUserString + "'.", ex);
			}
		} else {
			getLog().error("No user id found. Could not load projects.");
		}
	}
	
	// ------------------------------------------------ Abstract implementation

	@Override
	protected List<Project> getProjectModels() {
		if (getSelectedUser() != null) { 
			return getProjectService().getAccessProjects(getSelectedUser().getId(), true, true, true);
		} else {
			return new ArrayList<Project>();
		}
	}

	@Override
	protected boolean getCanChange() {
		return false;
	}

	@Override
	protected String getExportFilename() {
		return "userProjects";
	}
	
	// -------------------------------------------------------- Spring injected
	
	public User getSelectedUser() {
		return this.selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	// -------------------------------------------------------- Spring injected
	
	public UserService getUserService() {
		return this.userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
