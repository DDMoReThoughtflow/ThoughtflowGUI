package eu.ddmore.workflow.bwf.client.enumeration;

import eu.ddmore.workflow.bwf.client.util.Primitives;

public enum Relation {

	USED        ("used"),
	GENERATED   ("wasGeneratedBy"),
	ASSOCIATED  ("wasAssociatedWith"),
	DERIVED     ("wasDerivedFrom"),
	INFORMED    ("wasInformedBy"),
	INVALIDATED ("wasInvalidatedBy"),
	INFLUENCED  ("wasInfluencedBy"),
	ENDED       ("wasEndedBy"),
	STARTED     ("wasStartedBy");
	
	private String value;
	
	private Relation(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
	
	public static Relation getRelation(String value) {
		if (Primitives.isNotEmpty(value)) {
			String v = value.trim().toLowerCase();
			if (USED.getValue().equals(v)) {
				return USED;
			} else if (GENERATED.getValue().equals(v)) {
				return GENERATED;
			} else if (ASSOCIATED.getValue().equals(v)) {
				return ASSOCIATED;
			} else if (DERIVED.getValue().equals(v)) {
				return DERIVED;
			} else if (INFORMED.getValue().equals(v)) {
				return INFORMED;
			} else if (INVALIDATED.getValue().equals(v)) {
				return INVALIDATED;
			} else if (INFLUENCED.getValue().equals(v)) {
				return INFLUENCED;
			} else if (ENDED.getValue().equals(v)) {
				return ENDED;
			} else if (STARTED.getValue().equals(v)) {
				return STARTED;
			}
		}
		return null;
	}
}
