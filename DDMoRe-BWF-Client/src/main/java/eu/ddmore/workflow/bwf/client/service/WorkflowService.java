package eu.ddmore.workflow.bwf.client.service;

import java.io.File;

import org.openprovenance.prov.model.Document;

import eu.ddmore.workflow.server.client.model.ActivityQuery;
import eu.ddmore.workflow.server.client.model.AgentQuery;
import eu.ddmore.workflow.server.client.model.EntityQuery;
import eu.ddmore.workflow.server.client.model.UploadQuery;

public interface WorkflowService {

	Document entity(EntityQuery entityQuery);
	Document activity(ActivityQuery activityQuery);
	Document agent(AgentQuery agentQuery);
	Boolean upload(UploadQuery uploadQuery, File file);
	
}
