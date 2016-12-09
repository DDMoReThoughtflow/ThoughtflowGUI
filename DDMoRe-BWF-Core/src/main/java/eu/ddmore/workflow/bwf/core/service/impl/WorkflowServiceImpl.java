package eu.ddmore.workflow.bwf.core.service.impl;

import java.io.File;

import javax.annotation.PostConstruct;

import org.openprovenance.prov.json.Converter;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.xml.ProvFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.ddmore.workflow.bwf.client.model.ResultEntry;
import eu.ddmore.workflow.bwf.client.service.WorkflowService;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.core.service.BaseService;
import eu.ddmore.workflow.server.client.model.ActivityQuery;
import eu.ddmore.workflow.server.client.model.AgentQuery;
import eu.ddmore.workflow.server.client.model.EntityQuery;
import eu.ddmore.workflow.server.client.model.UploadQuery;

@Service(value="workflowService")
public class WorkflowServiceImpl extends BaseService implements WorkflowService {

	private static final long serialVersionUID = 1L;

	private static ProvFactory PROV_FACTORY;
	private static Converter PROV_CONVERT;
	 
	@Value(value="${rest.client.service.contextPath.workflow:wf}")
	private String contextPathRepository;

	@PostConstruct
	public void init() {
		/** Use prov-o toolbox to parse prov-o json files. */
		PROV_FACTORY = ProvFactory.getFactory();
		PROV_CONVERT = new Converter(PROV_FACTORY);
	}

	@Override
	protected String getServiceRestContextPath() {
		return this.contextPathRepository;
	}

	@Override
	public Document entity(EntityQuery entityQuery) {
		if (entityQuery == null) {
			throw new RuntimeException("Entity query id must not be empty.");
		}
		
		ResultEntry resultEntry = doRestPost("entity", ResultEntry.class, entityQuery);
		if (resultEntry != null && Primitives.isNotEmpty(resultEntry.getValue())) {
//			System.out.println(resultEntry.getValue());
			return PROV_CONVERT.fromString(resultEntry.getValue());
		}
		return null;
	}

	@Override
	public Document activity(ActivityQuery activityQuery) {
		if (activityQuery == null) {
			throw new RuntimeException("Activity query id must not be empty.");
		}

		ResultEntry resultEntry = doRestPost("activity", ResultEntry.class, activityQuery);
		if (resultEntry != null && Primitives.isNotEmpty(resultEntry.getValue())) {
//			System.out.println(resultEntry.getValue());
			return PROV_CONVERT.fromString(resultEntry.getValue());
		}
		return null;
	}

	@Override
	public Document agent(AgentQuery agentQuery) {
		if (agentQuery == null) {
			throw new RuntimeException("Agent query id must not be empty.");
		}

		ResultEntry resultEntry = doRestPost("agent", ResultEntry.class, agentQuery);
		if (resultEntry != null && Primitives.isNotEmpty(resultEntry.getValue())) {
//			System.out.println(resultEntry.getValue());
			return PROV_CONVERT.fromString(resultEntry.getValue());
		}
		return null;
	}
	
	// TODO: Connect to DDMoRe service
	@Override
	public Boolean upload(UploadQuery uploadQuery, File file) {
		return true;
	}
}
