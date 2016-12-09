package eu.ddmore.workflow.bwf.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.xml.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.ddmore.workflow.bwf.client.enumeration.ProvType;
import eu.ddmore.workflow.bwf.client.exception.DuplicateException;
import eu.ddmore.workflow.bwf.client.exception.EntityInUseException;
import eu.ddmore.workflow.bwf.client.model.Assumption;
import eu.ddmore.workflow.bwf.client.model.AuditTrailModel;
import eu.ddmore.workflow.bwf.client.model.Decision;
import eu.ddmore.workflow.bwf.client.model.File;
import eu.ddmore.workflow.bwf.client.model.Project;
import eu.ddmore.workflow.bwf.client.model.QcEntry;
import eu.ddmore.workflow.bwf.client.service.FileService;
import eu.ddmore.workflow.bwf.client.service.ProjectService;
import eu.ddmore.workflow.bwf.client.service.WorkflowService;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.core.dao.ProjectDao;
import eu.ddmore.workflow.server.client.model.ActivityQuery;
import eu.ddmore.workflow.server.client.model.AgentQuery;
import eu.ddmore.workflow.server.client.model.EntityQuery;

@Service(value="projectService")
public class ProjectServiceImpl extends BaseProvServiceImpl implements ProjectService {

	@Autowired private FileService fileService;
	@Autowired private ProjectDao projectDao;
	@Autowired private WorkflowService workflowService;

	@Override
	public List<Project> search(boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		return this.projectDao.search(loadOwner, loadAccess, loadReviewers); 
	}

	@Override
	public Project getById(Long id, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (id == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		return this.projectDao.getById(id, loadOwner, loadAccess, loadReviewers);
	}

	@Override
	public Project getByProjectname(String projectname, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (Primitives.isEmpty(projectname)) {
			throw new RuntimeException("Projectname must not be empty.");
		}
		
		return this.projectDao.getByProjectname(projectname, loadOwner, loadAccess, loadReviewers);
	}

	@Override
	@Transactional(readOnly = false)
	public Project insertOrUpdate(Project project) throws DuplicateException {
		if (project == null) {
			throw new RuntimeException("Project must not be empty.");
		}
		
		return this.projectDao.insertOrUpdate(project);
	}

	@Override
	@Transactional(readOnly = false)
	public Boolean deleteById(Long id) throws EntityInUseException {
		if (id == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		return this.projectDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = false)
	public Boolean deleteByProjectname(String projectname) throws EntityInUseException {
		if (Primitives.isEmpty(projectname)) {
			throw new RuntimeException("Projectname must not be empty.");
		}
		
		Project user = getByProjectname(projectname, false, false, false);
		if (user != null) {
			return deleteById(user.getId());
		}
		return false;
	}
	
	@Override
	public List<Project> getAccessProjects(Long idUser, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (idUser == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		return this.projectDao.getAccessProjects(idUser, loadOwner, loadAccess, loadReviewers);
	}

	@Override
	public List<Project> getReviewerProjects(Long idUser, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (idUser == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		return this.projectDao.getReviewerProjects(idUser, loadOwner, loadAccess, loadReviewers);
	}

	// TODO: Connect to DDMoRe service
	@Override
	public List<QcEntry> getQcChecklist(Long idProject) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		return new ArrayList<QcEntry>();
	}

	@Override
	public List<Assumption> getAssumptions(Long idProject) {
		List<Assumption> assumptions = new ArrayList<Assumption>();
		
		List<File> files = getFiles(idProject, null, false, false, false);
		for (File file : files) {
			if (file.getProvType().equals(ProvType.ASSUMPTION)) {
				assumptions.add(this.fileService.getAssumption(idProject, file.getId()));
			}
		}
		
		return assumptions;
	}

	// TODO: Connect to DDMoRe service
	@Override
	public List<Decision> getDecisions(Long idProject) {
		return new ArrayList<Decision>();
	}
	
	@Override
	public int updateAccessProjects(Long idProject, List<Long> idUserList) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		return this.projectDao.updateAccessProjects(idProject, idUserList);
	}

	@Override
	public int updateReviewerProjects(Long idProject, List<Long> idUserList) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		return this.projectDao.updateReviewerProjects(idProject, idUserList);
	}

	@Override
	public List<eu.ddmore.workflow.bwf.client.model.Activity> getActivities(Long idProject, Integer depth) {
		List<eu.ddmore.workflow.bwf.client.model.Activity> activities = new ArrayList<eu.ddmore.workflow.bwf.client.model.Activity>();
		
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		/** Get activities */
		String gitUrl = this.projectDao.getGitUrlById(idProject);
		ActivityQuery activityQuery = new ActivityQuery(gitUrl);
		if (depth != null) {
			activityQuery.setDepth(depth);
		}
		Document doc = this.workflowService.activity(activityQuery);
		
		if (doc == null) {
			return activities;
		}
		
		List<StatementOrBundle> statementsOrBundles = doc.getStatementOrBundle();
		if (Primitives.isEmpty(statementsOrBundles)) {
			return activities;
		}
		
		for (StatementOrBundle statementOrBundle : statementsOrBundles) {
			if (statementOrBundle instanceof Activity) {
				Activity provActivity = (Activity) statementOrBundle;
				activities.add(toModelActivity(provActivity));
			}
		}
		
		return activities;
	}

	@Override
	public List<File> getFiles(Long idProject, String idEntity, boolean loadContent, boolean loadAssumptions, boolean loadDecisions) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		/** Get entities */
		String gitUrl = this.projectDao.getGitUrlById(idProject);
		EntityQuery query = new EntityQuery(gitUrl);
		if (Primitives.isNotEmpty(idEntity)) {
			query.setEntity_id(idEntity);
		}
		query.setDepth(1);
		Document doc = this.workflowService.entity(query);
		
		return getFiles(doc);
	}
	
	@Override
	public String getAuditTrail(Long idProject, String idEntity, Class<? extends AuditTrailModel> type, Integer depth) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (type == null) {
			throw new RuntimeException("Requested audit trail type must not be empty.");
		}

		/** Get audit trail data */
		String gitUrl = this.projectDao.getGitUrlById(idProject);
		Document doc = null;
		if (File.class == type) {
			EntityQuery entityQuery = new EntityQuery(gitUrl);
			if (Primitives.isNotEmpty(idEntity)) {
				entityQuery.setEntity_id(idEntity);
			}
			if (depth != null) {
				entityQuery.setDepth(depth);
			}
			doc = this.workflowService.entity(entityQuery);
		} else if (eu.ddmore.workflow.bwf.client.model.Activity.class == type) {
			ActivityQuery activityQuery = new ActivityQuery(gitUrl);
			if (Primitives.isNotEmpty(idEntity)) {
				activityQuery.setActivity_id(idEntity);
			}
			if (depth != null) {
				activityQuery.setDepth(depth);
			}
			doc = this.workflowService.activity(activityQuery);
		} else if (eu.ddmore.workflow.bwf.client.model.Agent.class == type) {
			AgentQuery agentQuery = new AgentQuery(gitUrl);
			if (Primitives.isNotEmpty(idEntity)) {
				agentQuery.setAgent_id(idEntity);
			}
			if (depth != null) {
				agentQuery.setDepth(depth);
			}
			doc = this.workflowService.agent(agentQuery);
		}
		
		return toAuditTrail(doc);
	}
}
