package eu.ddmore.workflow.server.rest.csv.service.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import eu.ddmore.workflow.server.rest.csv.service.internal.Vertex.Type;	

public class Repository {

	private Map<String, Vertex> vertexMap = new HashMap<>();
	private String id;
	
	public Repository(String id) {
		super();
		this.id = id;
	}
	
	public Vertex getVertex(String vertexId) {
		return vertexMap.get(vertexId);
	}
	
	public List<Vertex> getVertices() {
		List<Vertex> vertices = new ArrayList<>(vertexMap.values());
		return vertices;
	}
	public List<Vertex> getVertices(Vertex.Type... types) {
		if (types == null || types.length == 0) {
			return getVertices();
		} 
		List<Vertex> vertices = new ArrayList<>();
		for (Vertex vertex : vertexMap.values()) {
			for (Type type : types) {
				if (type.equals(vertex.getType())) {
					vertices.add(vertex);
				}
			}
		}
		return vertices;
	}
	
	
	public Map<String, Vertex> getVertexMap() {
		return vertexMap;
	}
	public void setVertexMap(Map<String, Vertex> vertexMap) {
		this.vertexMap = vertexMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void load(File repoDir, String entityFile, String agentFile, String activityFile, String relationFile) throws FileNotFoundException, IOException {
		List<Map<String, String>> entityData = readCsv(repoDir, entityFile);
		List<Map<String, String>> activitiesData = readCsv(repoDir, activityFile);
		List<Map<String, String>> agentData = readCsv(repoDir, agentFile);
		List<Map<String, String>> relationsData = readCsv(repoDir, relationFile);

		loadVertices(Type.Entity, entityData);
		loadVertices(Type.Activity, activitiesData);
		loadVertices(Type.Agent, agentData);
		
		loadEdge(relationsData);
	}
	
	protected void loadVertices(Type type, List<Map<String, String>> data) {
		for (Map<String, String> record : data) {
			Vertex vertex = new Vertex();
			vertex.setId(record.get("id"));
			vertex.setType(type);
			vertex.getAttributes().putAll(record);
			getVertexMap().put(vertex.getId(), vertex);
		}
	}
	
	protected void loadEdge(List<Map<String, String>> data) {
		for (Map<String, String> record : data) {
			Edge edge = new Edge();
			edge.setId(record.get("id"));
			edge.setType(Edge.Type.getType(record.get("relation")));
			edge.getAttributes().putAll(record);
			
			Vertex source = getVertex(record.get("source_id"));
			if (source != null) {
				edge.setSource(source);
				source.getEdges().add(edge);
			}
			
			Vertex target = getVertex(record.get("target_id"));
			if (target != null) {
				edge.setTarget(target);
				target.getEdges().add(edge);
			}
			edge.setPlan(getVertex(record.get("plan_id")));
		}
	}
	
	protected List<Map<String, String>> readCsv(File repoDir, String filename) throws FileNotFoundException, IOException {
		List<Map<String, String>> data = new ArrayList<>();
		String[] header = null;
		if (repoDir.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(new File(repoDir, filename)))) {
				String line = null;
				int i = 0;
				while ((line = reader.readLine()) != null) {
					if (i == 0) {
						header = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ";");
					} else {
						Map<String, String> lineContent = new HashMap<>();
						String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ";");
						int j = 0;
						if (values.length == header.length) {
							for (String h : header) {
								lineContent.put(h, values.length > j ? values[j] : null);
								j++;
							}
							if (StringUtils.isNotEmpty(lineContent.get("id"))) {
								data.add(lineContent);
							}
						}
					}
					i++;
				}
				return data;
			}
		} else {
			throw new IllegalArgumentException("'" + repoDir.getPath() + "' doesn't exist");
		}
	}

	@Override
	public String toString() {
		StringBuffer sb  = new StringBuffer();
		for (Vertex vertex : getVertices()) {
			
			sb.append(vertex.getType() + "\t" + vertex.getId()).append("\r\n");
			for (Edge edge : vertex.getEdges()) {
				sb.append("\t" + edge.getType() + ": " + (edge.getSource() != null ? edge.getSource().getId() : "<missing>") + "-->" + (edge.getTarget() != null ? edge.getTarget().getId() : "<missing>") + "\r\n");
			}
		
		}
		return sb.toString();
	}
	
}
