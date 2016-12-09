package eu.ddmore.workflow.bwf.rest.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
import eu.ddmore.workflow.bwf.rest.spring.AppContext;

@Path("/rest/project")
public class ProjectRestService extends BaseRestService {

	private ProjectService projectService;
	
	@GET
	@Path("/ping")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response ping() {
		return Response.status(Status.OK).entity(new ResultEntry(true)).build();
	}

	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response search(
			@DefaultValue("false") @QueryParam("loadOwner") boolean loadOwner, 
			@DefaultValue("false") @QueryParam("loadAccess") boolean loadAccess,
			@DefaultValue("false") @QueryParam("loadReviewers") boolean loadReviewers) {
		
		try {
			List<Project> projects = getProjectService().search(loadOwner, loadAccess, loadReviewers);
			return Response.status(Status.OK).entity(new Projects(projects)).build();
		} catch (Throwable ex) {
			throw exception("Error get projects.", ex);
		}
	}
	
	@GET
	@Path("/id/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getById(
			@PathParam("id") Long id,
			@DefaultValue("false") @QueryParam("loadOwner") boolean loadOwner, 
			@DefaultValue("false") @QueryParam("loadAccess") boolean loadAccess,
			@DefaultValue("false") @QueryParam("loadReviewers") boolean loadReviewers) {

		/** Validate user input */
		if (id == null) {
			return error(Status.BAD_REQUEST, "Id must not be empty.");
		}
		
		try {
			Project project = getProjectService().getById(id, loadOwner, loadAccess, loadReviewers);
			if (project != null) {
				return Response.status(Status.OK).entity(project).build();	
			}
			return error(Status.NOT_FOUND, "No project found with id '" + id + "'");
		} catch (Throwable ex) {
			throw exception("Error get project with id '" + id + "'.", ex);
		}
	}

	@GET
	@Path("/projectname/{projectname}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getByProjectname(
			@PathParam("projectname") String projectname,
			@DefaultValue("false") @QueryParam("loadOwner") boolean loadOwner, 
			@DefaultValue("false") @QueryParam("loadAccess") boolean loadAccess,
			@DefaultValue("false") @QueryParam("loadReviewers") boolean loadReviewers) {

		/** Validate user input */
		if (Primitives.isEmpty(projectname)) {
			return error(Status.BAD_REQUEST, "Projectname must not be empty.");
		}
		
		try {
			Project project = getProjectService().getByProjectname(projectname, loadOwner, loadAccess, loadReviewers);
			if (project != null) {
				return Response.status(Status.OK).entity(project).build();	
			}
			return error(Status.NOT_FOUND, "No project found with projectname '" + projectname + "'");
		} catch (Throwable ex) {
			throw exception("Error get project with projectname '" + projectname + "'.", ex);
		}
	}
	
	@POST
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response insertOrUpdate(Project project) {
		/** Validate user input */
		if (project == null) {
			return error(Status.BAD_REQUEST, "Project must not be empty.");
		}
		
		try {
			Project insertedOrUpdatedProject = getProjectService().insertOrUpdate(project);
			return Response.status(Status.OK).entity(insertedOrUpdatedProject).build();
		} catch (Throwable ex) {
			throw exception("Error insert or update project '" + project + "'.", ex);
		}
	}
	
	@DELETE
	@Path("/id/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteById(@PathParam("id") Long id) {
		/** Validate user input */
		if (id == null) {
			return error(Status.BAD_REQUEST, "Id must not be empty.");
		}
		
		try {
			boolean b = getProjectService().deleteById(id);
			return Response.status(Status.OK).entity(new ResultEntry(b)).build();
		} catch (Throwable ex) {
			throw exception("Error delete project with id '" + id + "'.", ex);
		}
	}

	@DELETE
	@Path("/projectname/{projectname}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteById(@PathParam("projectname") String projectname) {
		/** Validate user input */
		if (Primitives.isEmpty(projectname)) {
			return error(Status.BAD_REQUEST, "Projectname must not be empty.");
		}
		
		try {
			boolean b = getProjectService().deleteByProjectname(projectname);
			return Response.status(Status.OK).entity(new ResultEntry(b)).build();
		} catch (Throwable ex) {
			throw exception("Error delete project with projectname '" + projectname + "'.", ex);
		}
	}
	
	@GET
	@Path("/access/{idUser}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAccessProjects(
			@PathParam("idUser") Long idUser,
			@DefaultValue("false") @QueryParam("loadOwner") boolean loadOwner, 
			@DefaultValue("false") @QueryParam("loadAccess") boolean loadAccess,
			@DefaultValue("false") @QueryParam("loadReviewers") boolean loadReviewers) {

		/** Validate user input */
		if (idUser == null) {
			return error(Status.BAD_REQUEST, "User id must not be empty.");
		}
		
		try {
			return getAccessOrReviewerProjectsInternal(idUser, loadOwner, loadAccess, loadReviewers, true);
		} catch (Throwable ex) {
			throw exception("Error get access projects for user with id '" + idUser + "'.", ex);
		}
	}
	
	@GET
	@Path("/reviewer/{idUser}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getReviewerProjects(
			@PathParam("idUser") Long idUser,
			@DefaultValue("false") @QueryParam("loadOwner") boolean loadOwner, 
			@DefaultValue("false") @QueryParam("loadAccess") boolean loadAccess,
			@DefaultValue("false") @QueryParam("loadReviewers") boolean loadReviewers) {

		/** Validate user input */
		if (idUser == null) {
			return error(Status.BAD_REQUEST, "User id must not be empty.");
		}
		
		try {
			return getAccessOrReviewerProjectsInternal(idUser, loadOwner, loadAccess, loadReviewers, false);
		} catch (Throwable ex) {
			throw exception("Error get reviewer projects for user with id '" + idUser + "'.", ex);
		}
	}
	
	private Response getAccessOrReviewerProjectsInternal(Long idUser, boolean loadOwner, boolean loadAccess, boolean loadReviewers, boolean isAccess) {
		/** Validate user input */
		if (idUser == null) {
			return error(Status.BAD_REQUEST, "User id must not be empty.");
		}
		
		List<Project> projects = (isAccess ? 
				getProjectService().getAccessProjects(idUser, loadOwner, loadAccess, loadReviewers) : 
				getProjectService().getReviewerProjects(idUser, loadOwner, loadAccess, loadReviewers) );
		
		return Response.status(Status.OK).entity(new Projects(projects)).build();
	}
	
	@GET
	@Path("/qc/{idProject}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getQcChecklist(@PathParam("idProject") Long idProject) {

		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		
		try {
			List<QcEntry> qcEntries = getProjectService().getQcChecklist(idProject);
			return Response.status(Status.OK).entity(new QcEntries(qcEntries)).build();
		} catch (Throwable ex) {
			throw exception("Error get qc entires for project with id '" + idProject + "'.", ex);
		}
	}
	
	@GET
	@Path("/assumption/{idProject}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAssumptions(@PathParam("idProject") Long idProject) {
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		
		try {
			List<Assumption> assumptions = getProjectService().getAssumptions(idProject);
			return Response.status(Status.OK).entity(new Assumptions(assumptions)).build();
		} catch (Throwable ex) {
			throw exception("Error get assumptions for project with id " + idProject + ".", ex);
		}
	}

	@GET
	@Path("/decision/{idProject}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDecisions(@PathParam("idProject") Long idProject) {
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		
		try {
			List<Decision> decisions = getProjectService().getDecisions(idProject);
			return Response.status(Status.OK).entity(new Decisions(decisions)).build();
		} catch (Throwable ex) {
			throw exception("Error get decisions for project with id " + idProject + ".", ex);
		}
	}
	
	@POST
	@Path("/assign/access/{idProject}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateAccessProjects(@PathParam("idProject") Long idProject, Users users) {
		try {
			return updateAccessOrReviewerProjectsInternal(idProject, users, true);
		} catch (Throwable ex) {
			throw exception("Error update access projects.", ex);
		}
	}
	
	@POST
	@Path("/assign/reviewer/{idProject}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateReviewerProjects(@PathParam("idProject") Long idProject, Users users) {
		try {
			return updateAccessOrReviewerProjectsInternal(idProject, users, false);
		} catch (Throwable ex) {
			throw exception("Error update reviewer projects.", ex);
		}
	}
	
	private Response updateAccessOrReviewerProjectsInternal(Long idProject, Users users, boolean isAccess) {
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		
		List<Long> idUserList = new ArrayList<Long>();
		if (users != null && users.hasUsers()) {
			for (User user : users.getUsers()) {
				if (user.getId() == null) {
					return error(Status.BAD_REQUEST, "User id must not be empty.");
				}
				idUserList.add(user.getId());
			}
		}
		int count = (isAccess ? 
				getProjectService().updateAccessProjects(idProject, idUserList) : 
				getProjectService().updateReviewerProjects(idProject, idUserList) );
		return Response.status(Status.OK).entity(new ResultEntry(count)).build();
	}
	
	@GET
	@Path("/activity/{idProject}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getActivities(
			@PathParam("idProject") Long idProject, 
			@DefaultValue("0") @QueryParam("depth") Integer depth) {
		
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		
		try {
			List<Activity> activities = getProjectService().getActivities(idProject, depth);
			return Response.status(Status.OK).entity(new Activities(activities)).build();
		} catch (Throwable ex) {
			throw exception("Error get activities for project with id " + idProject + ".", ex);
		}
	}

	@GET
	@Path("/file/{idProject}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getFiles(
			@PathParam("idProject") Long idProject,
			@QueryParam("idEntity") String idEntity,
			@DefaultValue("false") @QueryParam("loadContent") boolean loadContent,
			@DefaultValue("false") @QueryParam("loadAssumptions") boolean loadAssumptions, 
			@DefaultValue("false") @QueryParam("loadDecisions") boolean loadDecisions) {

		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		
		try {
			List<File> files = getProjectService().getFiles(idProject, idEntity, loadContent, loadAssumptions, loadDecisions);
			return Response.status(Status.OK).entity(new Files(files)).build();
		} catch (Throwable ex) {
			throw exception("Error get files for project with id '" + idProject + "'" + 
					(Primitives.isNotEmpty(idEntity) ? (" and entity with id '" + idEntity + "'") : "") + ".", ex);
		}
	}
	
	@GET
	@Path("/auditTrail/{idProject}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAuditTrail(
			@PathParam("idProject") Long idProject, 
			@QueryParam("idEntity") String idEntity,
			@QueryParam("type") String type,
			@DefaultValue("0") @QueryParam("depth") Integer depth) {
		
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (Primitives.isEmpty(type)) {
			return error(Status.BAD_REQUEST, "Requested audit trail type must not be empty.");
		}
		
		try {
			Class<?> requestedAuditTrailType = Class.forName(type);
			if (requestedAuditTrailType == null) {
				return error(Status.BAD_REQUEST, "Could not get requested audit trail type for: " + type);
			}
			if (!AuditTrailModel.class.isAssignableFrom(requestedAuditTrailType)) {
				return error(Status.BAD_REQUEST, "Requested audit trail type unknown: " + requestedAuditTrailType);
			}
			
			@SuppressWarnings("unchecked")
			String json = getProjectService().getAuditTrail(idProject, idEntity, (Class<? extends AuditTrailModel>) requestedAuditTrailType, depth);
			return Response.status(Status.OK).entity(new ResultEntry(json)).build();
		} catch (Throwable ex) {
			throw exception("Error get audit trail for project with id '" + idProject + "'" + 
					(Primitives.isNotEmpty(idEntity) ? (" and entity with id '" + idEntity + "'") : "") + ".", ex);
		}
	}

	// --------------------------------------------------------- Getter, Setter
	
	public ProjectService getProjectService() {
		if (this.projectService == null) {
			this.projectService = AppContext.getBean(ProjectService.class);
		}
		return this.projectService;
	}
}
