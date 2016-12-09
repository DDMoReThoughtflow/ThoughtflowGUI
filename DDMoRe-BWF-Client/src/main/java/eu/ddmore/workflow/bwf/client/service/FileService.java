package eu.ddmore.workflow.bwf.client.service;

import eu.ddmore.workflow.bwf.client.model.Assumption;
import eu.ddmore.workflow.bwf.client.model.Decision;
import eu.ddmore.workflow.bwf.client.model.File;

public interface FileService {
	
	/**
	 * Loads the file for the given file id.
	 */
	File getFile(Long idProject, String idFile, boolean loadContent, boolean loadAssumptions, boolean loadDecisions);
	
	/**
	 * Loads the assumption for the given assumption id.
	 */
	Assumption getAssumption(Long idProject, String idAssumption);
	/**
	 * Adds the assumption to the file given in assumption. 
	 */
	Assumption addAssumption(Long idProject, Assumption assumption);
	/**
	 * Updates the assumption for the file given in assumption. 
	 */
	Assumption updateAssumption(Long idProject, Assumption assumption);
	/**
	 * Deletes the assumption for the given assumption id.
	 */
	Boolean deleteAssumption(Long idProject, String idAssumption);
	
	/**
	 * Loads the decision for the given decision id.
	 */
	Decision getDecision(Long idProject, String idDecision);
	/**
	 * Adds the decision to the file given in decision. 
	 */
	Decision addDecision(Long idProject, Decision decision);
	/**
	 * Updates the decision for the file given in decision. 
	 */
	Decision updateDecision(Long idProject, Decision decision);
	/**
	 * Deletes the decision for the given decision id.
	 */
	Boolean deleteDecision(Long idProject, String idDecision);
	
	/**
	 * Mark file as final and/or base and/or pivotal model for the given file id.
	 */
	Boolean markModel(Long idProject, String idFile, Boolean finalModel, Boolean baseModel, Boolean pivotalModel);
	/**
	 * Set QC flag true or false for the file with the given id.
	 */
	Boolean passedQc(Long idProject, String idFile, boolean passed);
	
}
