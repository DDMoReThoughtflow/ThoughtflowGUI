package eu.ddmore.workflow.server.rest.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import eu.ddmore.workflow.bwf.client.model.ResultEntry;
import eu.ddmore.workflow.server.client.model.ActivityQuery;
import eu.ddmore.workflow.server.client.model.AgentQuery;
import eu.ddmore.workflow.server.client.model.EntityQuery;
import eu.ddmore.workflow.server.rest.csv.service.CsvService;
import eu.ddmore.workflow.server.rest.spring.AppContext;

@Path("/rest/wf")
public class WorkflowRestService extends BaseRestService {

	private CsvService csvService;
	
	@GET
	@Path("/ping")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response ping() {
		return Response.status(Status.OK).entity(new ResultEntry(true)).build();
	}
	
	@POST
	@Path("/entity")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response entity(EntityQuery entityQuery) {
		String json = getCsvService().entity(entityQuery);
		return Response.status(Status.OK).entity(new ResultEntry(json)).build();
	}
	
	@POST
	@Path("/activity")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response activity(ActivityQuery activityQuery) {
		String json = getCsvService().activity(activityQuery);
		return Response.status(Status.OK).entity(new ResultEntry(json)).build();
	}
	
	@POST
	@Path("/agent")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response agent(AgentQuery agentQuery) {
		String json = getCsvService().agent(agentQuery);
		return Response.status(Status.OK).entity(new ResultEntry(json)).build();
	}
	
	private CsvService getCsvService() {
		if (this.csvService == null) {
			this.csvService = AppContext.getBean(CsvService.class);
		}
		return this.csvService;
	}	
}
