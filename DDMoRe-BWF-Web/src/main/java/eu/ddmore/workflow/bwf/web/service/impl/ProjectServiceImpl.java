package eu.ddmore.workflow.bwf.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.ddmore.workflow.bwf.client.exception.DuplicateException;
import eu.ddmore.workflow.bwf.client.exception.EntityInUseException;
import eu.ddmore.workflow.bwf.client.model.Activities;
import eu.ddmore.workflow.bwf.client.model.Activity;
import eu.ddmore.workflow.bwf.client.model.Assumption;
import eu.ddmore.workflow.bwf.client.model.Assumptions;
import eu.ddmore.workflow.bwf.client.model.AuditTrailModel;
import eu.ddmore.workflow.bwf.client.model.Decision;
import eu.ddmore.workflow.bwf.client.model.Decisions;
import eu.ddmore.workflow.bwf.client.model.File;
import eu.ddmore.workflow.bwf.client.model.Files;
import eu.ddmore.workflow.bwf.client.model.Project;
import eu.ddmore.workflow.bwf.client.model.Projects;
import eu.ddmore.workflow.bwf.client.model.QcEntries;
import eu.ddmore.workflow.bwf.client.model.QcEntry;
import eu.ddmore.workflow.bwf.client.model.ResultEntry;
import eu.ddmore.workflow.bwf.client.model.User;
import eu.ddmore.workflow.bwf.client.model.Users;
import eu.ddmore.workflow.bwf.client.service.ProjectService;
import eu.ddmore.workflow.bwf.client.util.Primitives;

@Service(value="projectService")
public class ProjectServiceImpl extends BaseService implements ProjectService {

	private static final long serialVersionUID = 1L;
	
	@Value(value="${rest.service.contextPath.project:project}")
	private String contextPathRepository;
	
	@Override
	protected String getServiceRestContextPath() {
		return this.contextPathRepository;
	}

	@Override
	public List<Project> search(boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		Projects projects = doRestGet(Projects.class, 
				"loadOwner", loadOwner, "loadAccess", loadAccess, "loadReviewers", loadReviewers);
		return toProjectList(projects);
	}

	@Override
	public Project getById(Long id, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (id == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		return doRestGet(("id/" + id), Project.class, 
				"loadOwner", loadOwner, "loadAccess", loadAccess, "loadReviewers", loadReviewers);
	}

	@Override
	public Project getByProjectname(String projectname, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (Primitives.isEmpty(projectname)) {
			throw new RuntimeException("Projectname must not be empty.");
		}
		
		return doRestGet(("projectname/" + projectname), Project.class, 
				"loadOwner", loadOwner, "loadAccess", loadAccess, "loadReviewers", loadReviewers);
	}

	@Override
	public Project insertOrUpdate(Project project) throws DuplicateException {
		if (project == null) {
			throw new RuntimeException("Project must not be empty.");
		}
		
		Project insertedOrUpdatedProject = doRestPost(Project.class, project);
		return insertedOrUpdatedProject;
	}

	@Override
	public Boolean deleteById(Long id) throws EntityInUseException {
		if (id == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		ResultEntry resultEntry = doRestDelete(("id/" + id), ResultEntry.class);
		return resultEntry.getBooleanValue();
	}

	@Override
	public Boolean deleteByProjectname(String projectname) throws EntityInUseException {
		if (Primitives.isEmpty(projectname)) {
			throw new RuntimeException("Projectname must not be empty.");
		}
		
		ResultEntry resultEntry = doRestDelete(("projectname/" + projectname), ResultEntry.class);
		return resultEntry.getBooleanValue();
	}
	
	@Override
	public List<Project> getAccessProjects(Long idUser, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (idUser == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		return getAccessOrReviewerProjectsInternal(idUser, loadOwner, loadAccess, loadReviewers, true);
	}

	@Override
	public List<Project> getReviewerProjects(Long idUser, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (idUser == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		return getAccessOrReviewerProjectsInternal(idUser, loadOwner, loadAccess, loadReviewers, false);
	}
	
	private List<Project> getAccessOrReviewerProjectsInternal(Long idUser, boolean loadOwner, boolean loadAccess, boolean loadReviewers, boolean isAccess) {
		if (idUser == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		String restMethod = ((isAccess ? "access/" : "reviewer/") + idUser);
		Projects projects = doRestGet(restMethod, Projects.class, 
				"loadOwner", loadOwner, "loadAccess", loadAccess, "loadReviewers", loadReviewers);
		return toProjectList(projects);
	}
	
	@Override
	public List<QcEntry> getQcChecklist(Long idProject) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		QcEntries qcEntries = doRestGet(("qc/" + idProject), QcEntries.class);
		return toQcEntryList(qcEntries);
	}
	
	@Override
	public List<Assumption> getAssumptions(Long idProject) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}

		Assumptions assumptions = doRestGet(("assumption/" + idProject), Assumptions.class);
		return toAssumptionList(assumptions);
	}

	@Override
	public List<Decision> getDecisions(Long idProject) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}

		Decisions decisions = doRestGet(("decision/" + idProject), Decisions.class);
		return toDecisionList(decisions);
	}
	
	@Override
	public int updateAccessProjects(Long idProject, List<Long> idUserList) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		return updateAccessOrReviewerProjectsInternal(idProject, idUserList, true);
	}

	@Override
	public int updateReviewerProjects(Long idProject, List<Long> idUserList) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		return updateAccessOrReviewerProjectsInternal(idProject, idUserList, false);
	}
	
	public int updateAccessOrReviewerProjectsInternal(Long idProject, List<Long> idUserList, boolean isAccess) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		Users users = new Users();
		if (Primitives.isNotEmpty(idUserList)) {
			for (Long id : idUserList) {
				User user = new User();
				user.setId(id);
				users.addUser(user);
			}
		}
		
		String restMethod = ("assign/" + (isAccess ? "access/" : "reviewer/") + idProject);
		ResultEntry resultEntry = doRestPost(restMethod, ResultEntry.class, users);
		return resultEntry.getIntegerValue();
	}
	
	@Override
	public List<Activity> getActivities(Long idProject, Integer depth) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		Integer depthAdapted = (depth != null ? depth : 0);
		
		Activities activities = doRestGet(("activity/" + idProject), Activities.class, "depth", depthAdapted);
		return toActivityList(activities);
	}
	
	@Override
	public List<File> getFiles(Long idProject, String idEntity, boolean loadContent, boolean loadAssumptions, boolean loadDecisions) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		List<Object> params = new ArrayList<Object>();
		if (Primitives.isNotEmpty(idEntity)) {
			params.add("idEntity");
			params.add(idEntity);
		}
		params.add("loadContent");
		params.add(loadContent);
		params.add("loadAssumptions");
		params.add(loadAssumptions);
		params.add("loadDecisions");
		params.add(loadDecisions);
		
		Files files = doRestGet(("file/" + idProject), Files.class, params.toArray());
		return toFileList(files);
	}

	@Override
	public String getAuditTrail(Long idProject, String idEntity, Class<? extends AuditTrailModel> type, Integer depth) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (type == null) {
			throw new RuntimeException("Requested audit trail type must not be empty.");
		}

		List<Object> params = new ArrayList<Object>();
		params.add("idProject");
		params.add(idProject);
		if (Primitives.isNotEmpty(idEntity)) {
			params.add("idEntity");
			params.add(idEntity);
		}
		params.add("type");
		params.add(type.getCanonicalName());
		params.add("depth");
		params.add(depth);
		
		ResultEntry resultEntry = doRestGet(("auditTrail/" + idProject), ResultEntry.class, params.toArray());
		return resultEntry.getValue();
	}
	
	// ----------------------------------------------------------------- Helper
	
	private List<Project> toProjectList(Projects projects) {
		return (projects != null ? projects.toList() : new ArrayList<Project>());
	}
	
	private List<File> toFileList(Files files) {
		return (files != null ? files.toList() : new ArrayList<File>());
	}
	
	private List<QcEntry> toQcEntryList(QcEntries qcEntries) {
		return (qcEntries != null ? qcEntries.toList() : new ArrayList<QcEntry>());
	}
	
	private List<Assumption> toAssumptionList(Assumptions assumptions) {
		return (assumptions != null ? assumptions.toList() : new ArrayList<Assumption>());
	}
	
	private List<Decision> toDecisionList(Decisions decisions) {
		return (decisions != null ? decisions.toList() : new ArrayList<Decision>());
	}
	
	private List<Activity> toActivityList(Activities activities) {
		return (activities != null ? activities.toList() : new ArrayList<Activity>());
	}
}
