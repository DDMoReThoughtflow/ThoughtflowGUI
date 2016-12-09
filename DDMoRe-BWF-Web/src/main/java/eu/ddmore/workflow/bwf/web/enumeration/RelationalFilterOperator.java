package eu.ddmore.workflow.bwf.web.enumeration;

public enum RelationalFilterOperator {
	
	GREATER 	   (">"),
	SMALLER 	   ("<"),
	GREATER_EQUALS (">="),
	SMALLER_EQUALS ("<="),
	EQUALS		   ("="),
	EQUALS_EMPTY   ("");
	
	private String value;
	
	private RelationalFilterOperator(String value) {
		this.value = value;
	}
	
	public static RelationalFilterOperator getRelationalOperator(String value) {
		if (value != null) {
			if (value.startsWith(GREATER_EQUALS.getValue())) {
				return GREATER_EQUALS;
			} else if (value.startsWith(SMALLER_EQUALS.getValue())) {
				return SMALLER_EQUALS;
			} else if (value.startsWith(GREATER.getValue())) {
				return GREATER;
			} else if (value.startsWith(SMALLER.getValue())) {
				return SMALLER;
			} else if (value.startsWith(EQUALS.getValue())) {
				return EQUALS;
			}
		}
		return EQUALS_EMPTY;
	}

	public String getValue() {
		return this.value;
	}
}
