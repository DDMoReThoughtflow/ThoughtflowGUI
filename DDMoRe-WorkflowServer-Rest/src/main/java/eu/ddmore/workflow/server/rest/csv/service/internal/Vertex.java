package eu.ddmore.workflow.server.rest.csv.service.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vertex {
	
	public enum Type {
		Entity,
		Agent, 
		Activity;
	}
	
	private String id;
	private Type type;
	
	private Map<String, String> attributes = new HashMap<>();
	
	private List<Edge> edges = new ArrayList<>();
	
	public Vertex() {
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vertex) {
			return getId().equals(((Vertex)obj).getId());
		}
		return false;
	}

}
