package eu.ddmore.workflow.bwf.rest.service;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import eu.ddmore.workflow.bwf.client.model.Assumption;
import eu.ddmore.workflow.bwf.client.model.Decision;
import eu.ddmore.workflow.bwf.client.model.File;
import eu.ddmore.workflow.bwf.client.model.ResultEntry;
import eu.ddmore.workflow.bwf.client.service.FileService;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.rest.spring.AppContext;

@Path("/rest/file")
public class FileRestService extends BaseRestService {

	private FileService fileService;
	
	@GET
	@Path("/ping")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response ping() {
		return Response.status(Status.OK).entity(new ResultEntry(true)).build();
	}
	
	@GET
	@Path("/id")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getFile(
			@QueryParam("idProject") Long idProject,
			@QueryParam("idFile") String idFile,
			@DefaultValue("false") @QueryParam("loadContent") boolean loadContent,
			@DefaultValue("false") @QueryParam("loadAssumptions") boolean loadAssumptions, 
			@DefaultValue("false") @QueryParam("loadDecisions") boolean loadDecisions) {
		
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (Primitives.isEmpty(idFile)) {
			return error(Status.BAD_REQUEST, "File id must not be empty.");
		}
		
		try {
			File file = getFileService().getFile(idProject, idFile, loadContent, loadAssumptions, loadDecisions);
			return Response.status(Status.OK).entity(file).build();
		} catch (Throwable ex) {
			throw exception("Error get file with id '" + idFile + "'.", ex);
		}
	}

	@GET
	@Path("/assumption")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAssumption(
			@QueryParam("idProject") Long idProject, 
			@QueryParam("idAssumption") String idAssumption) {
		
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (Primitives.isEmpty(idAssumption)) {
			return error(Status.BAD_REQUEST, "Assumption id must not be empty.");
		}
		
		try {
			Assumption assumption = getFileService().getAssumption(idProject, idAssumption);
			return Response.status(Status.OK).entity(assumption).build();
		} catch (Throwable ex) {
			throw exception("Error get assumption with id '" + idAssumption + "'.", ex);
		}
	}
	
	@PUT
	@Path("/assumption")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addAssumption(@QueryParam("idProject") Long idProject, Assumption assumption) {
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (assumption == null || assumption.getFile() == null || Primitives.isEmpty(assumption.getFile().getId())) {
			return error(Status.BAD_REQUEST, "Assumption file id must not be empty.");
		}
		
		try {
			Assumption addedAssumption = getFileService().addAssumption(idProject, assumption);
			return Response.status(Status.OK).entity(addedAssumption).build();
		} catch (Throwable ex) {
			throw exception("Error add assumption for file with id '" + assumption.getFile().getId() + "'.", ex);
		}
	}

	@POST
	@Path("/assumption")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateAssumption(@QueryParam("idProject") Long idProject, Assumption assumption) {
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (assumption == null || assumption.getFile() == null || Primitives.isEmpty(assumption.getFile().getId())) {
			return error(Status.BAD_REQUEST, "Assumption file id must not be empty.");
		}
		
		try {
			Assumption updatedAssumption = getFileService().updateAssumption(idProject, assumption);
			return Response.status(Status.OK).entity(updatedAssumption).build();
		} catch (Throwable ex) {
			throw exception("Error update assumption for file with id '" + assumption.getFile().getId() + "'.", ex);
		}
	}

	@DELETE
	@Path("/assumption")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteAssumption(@QueryParam("idProject") Long idProject, @QueryParam("idAssumption") String idAssumption) {
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (Primitives.isEmpty(idAssumption)) {
			return error(Status.BAD_REQUEST, "Assumption id must not be empty.");
		}
		
		try {
			Boolean b = getFileService().deleteAssumption(idProject, idAssumption);
			return Response.status(Status.OK).entity(new ResultEntry(b)).build();
		} catch (Throwable ex) {
			throw exception("Error delete assumption with id '" + idAssumption + "'.", ex);
		}
	}	
	
	@GET
	@Path("/decision")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDecision(@QueryParam("idProject") Long idProject, @QueryParam("idDecision") String idDecision) {
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (Primitives.isEmpty(idDecision)) {
			return error(Status.BAD_REQUEST, "Decision id must not be empty.");
		}
		
		try {
			Decision decision = getFileService().getDecision(idProject, idDecision);
			return Response.status(Status.OK).entity(decision).build();
		} catch (Throwable ex) {
			throw exception("Error get decision with id '" + idDecision + "'.", ex);
		}
	}
	
	@PUT
	@Path("/decision")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addDecision(@QueryParam("idProject") Long idProject, Decision decision) {
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (decision == null || decision.getFile() == null || Primitives.isEmpty(decision.getFile().getId())) {
			return error(Status.BAD_REQUEST, "Decision file id must not be empty.");
		}
		
		try {
			Decision addedDecision = getFileService().addDecision(idProject, decision);
			return Response.status(Status.OK).entity(addedDecision).build();
		} catch (Throwable ex) {
			throw exception("Error add decision for file with id '" + decision.getFile().getId() + "'.", ex);
		}
	}

	@POST
	@Path("/decision")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateDecision(@QueryParam("idProject") Long idProject, Decision decision) {
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (decision == null || decision.getFile() == null || Primitives.isEmpty(decision.getFile().getId())) {
			return error(Status.BAD_REQUEST, "Decision file id must not be empty.");
		}
		
		try {
			Decision updatedDecision = getFileService().updateDecision(idProject, decision);
			return Response.status(Status.OK).entity(updatedDecision).build();
		} catch (Throwable ex) {
			throw exception("Error update decision for file with id '" + decision.getFile().getId() + "'.", ex);
		}
	}

	@DELETE
	@Path("/decision")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteDecision(@QueryParam("idProject") Long idProject, @QueryParam("idDecision") String idDecision) {
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (Primitives.isEmpty(idDecision)) {
			return error(Status.BAD_REQUEST, "Decision id must be not be empty.");
		}
		
		try {
			Boolean b = getFileService().deleteDecision(idProject, idDecision);
			return Response.status(Status.OK).entity(new ResultEntry(b)).build();
		} catch (Throwable ex) {
			throw exception("Error delete decision with id '" + idDecision + "'.", ex);
		}
	}	

	@GET
	@Path("/mark")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response markModel(
			@QueryParam("idProject") Long idProject,
			@QueryParam("idFile") String idFile,
			@QueryParam("finalModel") Boolean finalModel,
			@QueryParam("baseModel") Boolean baseModel, 
			@QueryParam("pivotalModel") Boolean pivotalModel) {
		
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (Primitives.isEmpty(idFile)) {
			return error(Status.BAD_REQUEST, "File id must not be empty.");
		}
		
		try {
			Boolean b = getFileService().markModel(idProject, idFile, finalModel, baseModel, pivotalModel);
			return Response.status(Status.OK).entity(new ResultEntry(b)).build();
		} catch (Throwable ex) {
			throw exception("Error mark file with id '" + idFile + "'.", ex);
		}
	}

	@GET
	@Path("/qc")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response passedQc(
			@QueryParam("idProject") Long idProject, 
			@QueryParam("idFile") String idFile, 
			@QueryParam("passed") Boolean passed) {
		
		/** Validate user input */
		if (idProject == null) {
			return error(Status.BAD_REQUEST, "Project id must not be empty.");
		}
		if (Primitives.isEmpty(idFile)) {
			return error(Status.BAD_REQUEST, "File id must not be empty.");
		}
		if (passed == null) {
			return error(Status.BAD_REQUEST, "Passed flag must not be empty.");
		}
		
		try {
			Boolean b = getFileService().passedQc(idProject, idFile, passed);
			return Response.status(Status.OK).entity(new ResultEntry(b)).build();
		} catch (Throwable ex) {
			throw exception("Error mark qc= " + passed + " for file with id '" + idFile + "'.", ex);
		}
	}
	
	// --------------------------------------------------------- Getter, Setter

	public FileService getFileService() {
		if (this.fileService == null) {
			this.fileService = AppContext.getBean(FileService.class);
		}
		return this.fileService;
	}
}
