package eu.ddmore.workflow.bwf.core.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.openprovenance.prov.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.ddmore.workflow.bwf.client.enumeration.AssumptionType;
import eu.ddmore.workflow.bwf.client.model.Assumption;
import eu.ddmore.workflow.bwf.client.model.Decision;
import eu.ddmore.workflow.bwf.client.model.File;
import eu.ddmore.workflow.bwf.client.service.FileService;
import eu.ddmore.workflow.bwf.client.service.WorkflowService;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.core.dao.ProjectDao;
import eu.ddmore.workflow.bwf.core.http.HttpGetPostService;
import eu.ddmore.workflow.bwf.core.http.ResponseData;
import eu.ddmore.workflow.server.client.model.EntityQuery;

@Service(value="fileService")
public class FileServiceImpl extends BaseProvServiceImpl implements FileService {

	private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);
	
	@Autowired private HttpGetPostService httpGetPostService;
	@Autowired private WorkflowService workflowService;
	@Autowired private ProjectDao projectDao;
	
	@Override
	public File getFile(Long idProject, String idFile, boolean loadContent, boolean loadAssumptions, boolean loadDecisions) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idFile)) {
			throw new RuntimeException("File id must not be empty.");
		}
		
		/** Get file */
		String gitUrl = this.projectDao.getGitUrlById(idProject);
		EntityQuery entityQuery = new EntityQuery(gitUrl, idFile);
		Document doc = this.workflowService.entity(entityQuery);
		
		List<File> files = getFiles(doc);
		if (files == null || files.size() != 1) {
			return null;
		}
		File file = files.get(0);
		
		/** Adapt download file URL for host github */
		String location = (gitUrl + "/" + file.getLocation());
		if (location.contains("//github.com")) {
			location = location.replace("//github.com", "//raw.github.com");	
		}
		
		/** Try to load file content from host */
		if (loadContent) {
			try {
				ResponseData responseData = this.httpGetPostService.getRestGetResponse(location, null);
				if (responseData.getCode() == 200) { // OK
					file.setData(responseData.getData());
				}
			} catch (Exception ex) {
				String errorMessage = "Error get content for file '" + idFile + "'";
				LOG.error(errorMessage, ex);
				throw new RuntimeException(errorMessage);
			}
		}
		
		// TODO: Connect to DDMoRe service
		if (loadAssumptions) {
		}
		
		// TODO: Connect to DDMoRe service
		if (loadDecisions) {
		}
		
		return file;
	}

	// TODO: Connect to DDMoRe service
	@Override
	public Assumption getAssumption(Long idProject, String idAssumption) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idAssumption)) {
			throw new RuntimeException("Assumption id must not be empty.");
		}
		File file = getFile(idProject, idAssumption, true, false, false);
		
		InputStream in = new ByteArrayInputStream(file.getData());
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(AssumptionContent.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			AssumptionContent a = (AssumptionContent) jaxbUnmarshaller.unmarshal(in);

			Assumption assumption = new Assumption(idAssumption);
			assumption.setBody(a.getBody());
//			assumption.setEstablished(a.getEstablished());
			assumption.setJustification(a.getJustification());
			assumption.setTestable(a.getTestable());
			assumption.setTestApproach(a.getTestApproach());
			assumption.setTestOutcome(a.getTestOutcome());
			assumption.setType(AssumptionType.valueOf(a.getType().toUpperCase()));
			
			return assumption;
		} catch (JAXBException e) {
			throw new RuntimeException("Cannot parse assumption file '" + idAssumption + "'", e);
		}
		
	}

	// TODO: Connect to DDMoRe service
	@Override
	public Assumption addAssumption(Long idProject, Assumption assumption) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (assumption == null || assumption.getFile() == null || Primitives.isEmpty(assumption.getFile().getId())) {
			throw new RuntimeException("Assumption file id must not be empty.");
		}
		
		return assumption;
	}

	// TODO: Connect to DDMoRe service
	@Override
	public Assumption updateAssumption(Long idProject, Assumption assumption) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (assumption == null || assumption.getFile() == null || Primitives.isEmpty(assumption.getFile().getId())) {
			throw new RuntimeException("Assumption file id must not be empty.");
		}

		return assumption;
	}

	// TODO: Connect to DDMoRe service
	@Override
	public Boolean deleteAssumption(Long idProject, String idAssumption) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idAssumption)) {
			throw new RuntimeException("Assumption id must not be empty.");
		}
		
		return true;
	}

	// TODO: Connect to DDMoRe service
	@Override
	public Decision getDecision(Long idProject, String idDecision) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idDecision)) {
			throw new RuntimeException("Decision id must not be empty.");
		}
		
		return null;	
	}

	// TODO: Connect to DDMoRe service
	@Override
	public Decision addDecision(Long idProject, Decision decision) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (decision == null || decision.getFile() == null || Primitives.isEmpty(decision.getFile().getId())) {
			throw new RuntimeException("Decision file id must not be empty.");
		}
		
		return decision;
	}

	// TODO: Connect to DDMoRe service
	@Override
	public Decision updateDecision(Long idProject, Decision decision) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (decision == null || decision.getFile() == null || Primitives.isEmpty(decision.getFile().getId())) {
			throw new RuntimeException("Decision file id must not be empty.");
		}
		
		return decision;
	}

	// TODO: Connect to DDMoRe service
	@Override
	public Boolean deleteDecision(Long idProject, String idDecision) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idDecision)) {
			throw new RuntimeException("Decision id must not be empty.");
		}
		
		return true;
	}
	
	// TODO: Connect to DDMoRe service
	@Override
	public Boolean markModel(Long idProject, String idFile, Boolean finalModel, Boolean baseModel, Boolean pivotalModel) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idFile)) {
			throw new RuntimeException("Fileid  must not be empty.");
		}
		
		return true;
	}
	
	// TODO: Connect to DDMoRe service
	@Override
	public Boolean passedQc(Long idProject, String idFile, boolean passed) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		if (Primitives.isEmpty(idFile)) {
			throw new RuntimeException("File id must not be empty.");
		}

		return passed;
	}
}
