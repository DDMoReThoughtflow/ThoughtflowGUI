package eu.ddmore.workflow.bwf.client.service;

import java.util.List;

import eu.ddmore.workflow.bwf.client.exception.DuplicateException;
import eu.ddmore.workflow.bwf.client.exception.EntityInUseException;
import eu.ddmore.workflow.bwf.client.model.Activity;
import eu.ddmore.workflow.bwf.client.model.Assumption;
import eu.ddmore.workflow.bwf.client.model.AuditTrailModel;
import eu.ddmore.workflow.bwf.client.model.Decision;
import eu.ddmore.workflow.bwf.client.model.File;
import eu.ddmore.workflow.bwf.client.model.Project;
import eu.ddmore.workflow.bwf.client.model.QcEntry;

public interface ProjectService {

	List<Project> search(boolean loadOwner, boolean loadAccess, boolean loadReviewers);
	Project getById(Long id, boolean loadOwner,boolean loadAccess,  boolean loadReviewers);
	Project getByProjectname(String projectname, boolean loadOwner, boolean loadAccess, boolean loadReviewers);
	
	Project insertOrUpdate(Project project) throws DuplicateException;
	Boolean deleteById(Long id) throws EntityInUseException;
	Boolean deleteByProjectname(String projectname) throws EntityInUseException;

	List<Project> getAccessProjects(Long idUser, boolean loadOwner, boolean loadAccess, boolean loadReviewers);
	List<Project> getReviewerProjects(Long idUser, boolean loadOwner, boolean loadAccess, boolean loadReviewers);

	List<QcEntry> getQcChecklist(Long idProject);
	List<Assumption> getAssumptions(Long idProject);
	List<Decision> getDecisions(Long idProject);
	
	int updateAccessProjects(Long idProject, List<Long> idUserList);
	int updateReviewerProjects(Long idProject, List<Long> idUserList);
	
	List<Activity> getActivities(Long idProject, Integer depth);
	List<File> getFiles(Long idProject, String idEntity, boolean loadContent, boolean loadAssumptions, boolean loadDecisions);

	String getAuditTrail(Long idProject, String idEntity, Class<? extends AuditTrailModel> type,  Integer depth);

}
