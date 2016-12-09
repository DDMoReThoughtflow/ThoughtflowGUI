package eu.ddmore.workflow.server.rest.csv.service.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class Edge {
	
	public enum Type {

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
		
		private Type(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		public static Type getType(String value) {
			if (StringUtils.isNotEmpty(value)) {
				String v = value.trim().toLowerCase();
				if (USED.getValue().equals(v)) {
					return USED;
				} else if (GENERATED.getValue().equalsIgnoreCase(v)) {
					return GENERATED;
				} else if (ASSOCIATED.getValue().equalsIgnoreCase(v)) {
					return ASSOCIATED;
				} else if (DERIVED.getValue().equalsIgnoreCase(v)) {
					return DERIVED;
				} else if (INFORMED.getValue().equalsIgnoreCase(v)) {
					return INFORMED;
				} else if (INVALIDATED.getValue().equalsIgnoreCase(v)) {
					return INVALIDATED;
				} else if (INFLUENCED.getValue().equalsIgnoreCase(v)) {
					return INFLUENCED;
				} else if (ENDED.getValue().equalsIgnoreCase(v)) {
					return ENDED;
				} else if (STARTED.getValue().equalsIgnoreCase(v)) {
					return STARTED;
				}
			}
			return null;
		}
	}
	
	
	private String id;
	private Type type;
	private Vertex source;
	private Vertex target;

	private Vertex plan;

	private Map<String, String> attributes = new HashMap<>();
	
	private List<Edge> edges = new ArrayList<>();

	public Edge() {
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public Vertex getSource() {
		return source;
	}

	public void setSource(Vertex source) {
		this.source = source;
	}

	public Vertex getTarget() {
		return target;
	}

	public void setTarget(Vertex target) {
		this.target = target;
	}

	public Vertex getPlan() {
		return plan;
	}

	public void setPlan(Vertex plan) {
		this.plan = plan;
	}
}
