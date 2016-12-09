package eu.ddmore.workflow.bwf.client.enumeration;

import eu.ddmore.workflow.bwf.client.util.Primitives;

public enum ProvType {

	MODEL            	   ("ddmore:model"),
	DATASET          	   ("ddmore:dataset"),
	PLAN 			       ("ddmore:plan"),
	PHARM_ML_ARCHIVE 	   ("ddmore:phex"),
	STANDARD_OUTPUT_OBJECT ("ddmore:so"),
	OUTPUT 				   ("ddmore:output"),
	IMAGE 				   ("ddmore:image"),
	ASSUMPTION 			   ("ddmore:assumption"),
	DECISION 			   ("ddmore:decision"),
	DOCUMENT 			   ("ddmore:document");
	
	private String value;
	
	private ProvType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
	
	public static ProvType getProvType(String value) {
		if (Primitives.isNotEmpty(value)) {
			String v = value.trim().toLowerCase();
			if (MODEL.getValue().equals(v)) {
				return MODEL;
			} else if (DATASET.getValue().equals(v)) {
				return DATASET;
			} else if (PLAN.getValue().equals(v)) {
				return PLAN;
			} else if (PHARM_ML_ARCHIVE.getValue().equals(v)) {
				return PHARM_ML_ARCHIVE;
			} else if (STANDARD_OUTPUT_OBJECT.getValue().equals(v)) {
				return STANDARD_OUTPUT_OBJECT;
			} else if (OUTPUT.getValue().equals(v)) {
				return OUTPUT;
			} else if (IMAGE.getValue().equals(v)) {
				return IMAGE;
			} else if (ASSUMPTION.getValue().equals(v)) {
				return ASSUMPTION;
			} else if (DECISION.getValue().equals(v)) {
				return DECISION;
			} else if (DOCUMENT.getValue().equals(v)) {
				return DOCUMENT;
			}
		}
		return null;
	}
}
