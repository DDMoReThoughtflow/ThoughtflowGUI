package eu.ddmore.workflow.bwf.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.ddmore.workflow.bwf.client.model.Assumption;
import eu.ddmore.workflow.bwf.client.model.Decision;
import eu.ddmore.workflow.bwf.client.model.File;
import eu.ddmore.workflow.bwf.client.model.ResultEntry;
import eu.ddmore.workflow.bwf.client.service.FileService;
import eu.ddmore.workflow.bwf.client.util.Primitives;

@Service(value="fileService")
public class FileServiceImpl extends BaseService implements FileService {

	private static final long serialVersionUID = 1L;
	
	@Value(value="${rest.service.contextPath.file:file}")
	private String contextPathRepository;
	
	@Override
	protected String getServiceRestContextPath() {
		return this.contextPathRepository;
	}
	
	@Override
	public File getFile(Long idProject, String idFile, boolean loadContent, boolean loadAssumptions, boolean loadDecisions) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idFile)) {
			throw new RuntimeException("File id must not be empty.");
		}
		
		return doRestGet("id", File.class, "idProject", idProject, "idFile", idFile, "loadContent", loadContent, 
				"loadAssumptions", loadAssumptions, "loadDecisions", loadDecisions);
	}

	@Override
	public Assumption getAssumption(Long idProject, String idAssumption) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idAssumption)) {
			throw new RuntimeException("Assumption id must not be empty.");
		}
		
		return doRestGet("assumption", Assumption.class, "idProject", idProject, "idAssumption", idAssumption);
	}

	@Override
	public Assumption addAssumption(Long idProject, Assumption assumption) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (assumption == null || assumption.getFile() == null || Primitives.isEmpty(assumption.getFile().getId())) {
			throw new RuntimeException("Assumption file id must not be empty.");
		}
		
		return doRestPut("assumption", Assumption.class, assumption, "idProject", idProject);
	}

	@Override
	public Assumption updateAssumption(Long idProject, Assumption assumption) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (assumption == null || assumption.getFile() == null || Primitives.isEmpty(assumption.getFile().getId())) {
			throw new RuntimeException("Assumption file id must not be empty.");
		}
		
		return doRestPost("assumption", Assumption.class, assumption, "idProject", idProject);
	}

	@Override
	public Boolean deleteAssumption(Long idProject, String idAssumption) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idAssumption)) {
			throw new RuntimeException("Assumption file id must not be empty.");
		}
		
		return doRestDelete("assumption", ResultEntry.class, "idProject", idProject, "idAssumption", idAssumption).getBooleanValue();
	}
	
	@Override
	public Decision getDecision(Long idProject, String idDecision) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idDecision)) {
			throw new RuntimeException("Decision id must not be empty.");
		}
		
		return doRestGet("decision", Decision.class, "idProject", idProject, "idDecision", idDecision);
	}
	
	@Override
	public Decision addDecision(Long idProject, Decision decision) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (decision == null || decision.getFile() == null || Primitives.isEmpty(decision.getFile().getId())) {
			throw new RuntimeException("Decision file id must not be empty.");
		}
		
		return doRestPut("decision", Decision.class, decision, "idProject", idProject);
	}

	@Override
	public Decision updateDecision(Long idProject, Decision decision) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (decision == null || decision.getFile() == null || Primitives.isEmpty(decision.getFile().getId())) {
			throw new RuntimeException("Decision file id must not be empty.");
		}
		
		return doRestPost("decision", Decision.class, decision, "idProject", idProject);
	}

	@Override
	public Boolean deleteDecision(Long idProject, String idDecision) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idDecision)) {
			throw new RuntimeException("Decision id must not be empty.");
		}
		
		return doRestDelete("decision", ResultEntry.class, "idProject", idProject, "idDecision", idDecision).getBooleanValue();
	}	
	
	@Override
	public Boolean markModel(Long idProject, String idFile, Boolean finalModel, Boolean baseModel, Boolean pivotalModel) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idFile)) {
			throw new RuntimeException("File id must not be empty.");
		}
		
		List<Object> parameters = new ArrayList<Object>();
		parameters.add("idProject");
		parameters.add(idProject);
		parameters.add("idFile");
		parameters.add(idFile);
		if (finalModel != null) {
			parameters.add("finalModel");
			parameters.add(finalModel);
		}
		if (baseModel != null) {
			parameters.add("baseModel");
			parameters.add(baseModel);
		}
		if (pivotalModel != null) {
			parameters.add("pivotalModel");
			parameters.add(pivotalModel);
		}
		
		return doRestGet("mark", ResultEntry.class, parameters.toArray()).getBooleanValue();
	}

	@Override
	public Boolean passedQc(Long idProject, String idFile, boolean passed) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idFile)) {
			throw new RuntimeException("File id must not be empty.");
		}
		
		return doRestGet("qc", ResultEntry.class, "idProject", idProject, "idFile", idFile, "passed", passed).getBooleanValue();
	}
}
