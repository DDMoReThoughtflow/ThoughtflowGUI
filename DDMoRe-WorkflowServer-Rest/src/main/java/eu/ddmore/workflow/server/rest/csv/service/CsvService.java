package eu.ddmore.workflow.server.rest.csv.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Identifiable;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.xml.InternationalizedString;
import org.openprovenance.prov.xml.ProvFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.ddmore.workflow.bwf.client.util.Constants;
import eu.ddmore.workflow.server.client.model.ActivityQuery;
import eu.ddmore.workflow.server.client.model.AgentQuery;
import eu.ddmore.workflow.server.client.model.EntityQuery;
import eu.ddmore.workflow.server.client.model.ProvTypes;
import eu.ddmore.workflow.server.client.model.UploadQuery;
import eu.ddmore.workflow.server.rest.csv.service.internal.Datastore;
import eu.ddmore.workflow.server.rest.csv.service.internal.Edge;
import eu.ddmore.workflow.server.rest.csv.service.internal.Repository;
import eu.ddmore.workflow.server.rest.csv.service.internal.Vertex;

@Service
public class CsvService {
	
	private static final String VCS_PREFIX = "vcs";
	
	private static ProvFactory PROV_FACTORY;

	@PostConstruct
	public void init() {
		PROV_FACTORY = ProvFactory.getFactory();
	}
	
	private Datastore datastore;

	@Value(value = "${csv.data.path}")
	private String csvDataPath;

	@Value(value = "${csv.data.entities:entities.csv}")
	private String entitiesFile;

	@Value(value = "${csv.data.agents:agents.csv}")
	private String agentsFile;

	@Value(value = "${csv.data.activities:activities.csv}")
	private String activitiesFile;
	
	@Value(value = "${csv.data.relations:relations.csv}")
	private String relationsFile;
	
	// ---------------------------------------------------------------- Service

	public String agents(AgentQuery agentQuery) {
		return queryDatastoreByType(agentQuery.getRepo_id(), agentQuery.getDepth(), Vertex.Type.Agent);
	}
	
	public String entity(EntityQuery entityQuery) {
		if (StringUtils.isNotEmpty(entityQuery.getEntity_id())) {
			return queryDatastoreById(entityQuery.getRepo_id(), entityQuery.getEntity_id(), entityQuery.getDepth());
		} else {
			return queryDatastoreByType(entityQuery.getRepo_id(), entityQuery.getDepth(), Vertex.Type.Entity);
		}
	}

	public String activity(ActivityQuery activityQuery) {
		if (StringUtils.isNotEmpty(activityQuery.getActivity_id())) {
			return queryDatastoreById(activityQuery.getRepo_id(), activityQuery.getActivity_id(), activityQuery.getDepth());
		} else {
			return queryDatastoreByType(activityQuery.getRepo_id(), activityQuery.getDepth(), Vertex.Type.Activity);
		}
	}

	public String agent(AgentQuery agentQuery) {
		if (StringUtils.isNotEmpty(agentQuery.getAgent_id())) {
			return queryDatastoreById(agentQuery.getRepo_id(), agentQuery.getAgent_id(), agentQuery.getDepth());
		} else {
			return queryDatastoreByType(agentQuery.getRepo_id(), agentQuery.getDepth(), Vertex.Type.Agent);
		}
	}

	public boolean upload(UploadQuery uploadQuery, byte[] data) {
		return true;
	}

	// --------------------------------------------------------- Getter, Setter

	public String getCsvDataPath() {
		return this.csvDataPath;
	}

	public void setCsvDataPath(String csvDataPath) {
		this.csvDataPath = csvDataPath;
	}
	
	public String getEntitiesFile() {
		return entitiesFile;
	}

	public void setEntitiesFile(String entitiesFile) {
		this.entitiesFile = entitiesFile;
	}

	public String getAgentsFile() {
		return agentsFile;
	}

	public void setAgentsFile(String agentsFile) {
		this.agentsFile = agentsFile;
	}

	public String getActivitiesFile() {
		return activitiesFile;
	}

	public void setActivitiesFile(String activitiesFile) {
		this.activitiesFile = activitiesFile;
	}

	public String getRelationsFile() {
		return relationsFile;
	}

	public void setRelationsFile(String relationsFile) {
		this.relationsFile = relationsFile;
	}
	
	public Datastore getDatastore() {
		if(datastore == null) {
			datastore = new Datastore();
			try {
				datastore.load(getCsvDataPath(), getEntitiesFile(), getAgentsFile(), getActivitiesFile(), getRelationsFile());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return datastore; 
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	protected Document buildDocument(Namespace ns) {
		Document document = PROV_FACTORY.newDocument();
		document.setNamespace(ns);
		return document;
	}

	private Namespace buildNamespace(String repoId) {
		Namespace ns=new Namespace();
        ns.addKnownNamespaces();
//        ns.register(DEFAULT_PREFIX, "http://www.ddmore.eu/#");
        ns.register(Constants.PREFIX_DDMORE, "http://www.ddmore.eu/#");
        ns.register(Constants.PREFIX_REPO, repoId + "#");
        ns.register(VCS_PREFIX, "https://github.com/#");
        return ns;
	}

	protected boolean matchRepository(String repoId, Map<String, String> record) {
		return StringUtils.equals(repoId, record.get("repo_id"));
	}
	
	protected boolean matchEntityId(String entityId, Map<String, String> record) {
		return StringUtils.equals(entityId, record.get("repo_id"));
	}
	
	protected boolean matchProvType(ProvTypes provTypes, Map<String, String> record) {
		if (provTypes == null || provTypes.getProvTypes().size() == 0) {
			return true;
		}
		for (eu.ddmore.workflow.server.client.model.ProvType type : provTypes.getProvTypes()) {
			if (StringUtils.equals(type.getType().getValue(), record.get("prov_type"))) {
				return true;
			}
		}
		
		return false;
	}
	
	protected String toJSON(Document document) {
		InteropFramework intF=new InteropFramework();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		BufferedOutputStream out = new BufferedOutputStream(bout);
		intF.writeDocument(out, ProvFormat.JSON, document);
		return new String(bout.toByteArray());
	}
	
	protected Entity buildEntity(Namespace ns, Vertex vertex) {
		String entityId = vertex.getId();
		if (entityId.indexOf(':') > 0) {
			entityId = entityId.substring(entityId.indexOf(':') + 1);
		}
		
		Entity entity = PROV_FACTORY.newEntity(ns.qualifiedName(Constants.PREFIX_REPO, entityId, PROV_FACTORY));
		entity.getLocation().add(PROV_FACTORY.newLocation(entityId, ns.qualifiedName("xsd", "string", PROV_FACTORY)));
		if (vertex.getAttributes().get("prov_type") != null) {
			entity.getType().add(PROV_FACTORY.newType(vertex.getAttributes().get("prov_type"), ns.qualifiedName("xsd", "string", PROV_FACTORY)));
		}
		if (StringUtils.isNotEmpty(vertex.getAttributes().get("qc"))) {
			entity.getOther().add(PROV_FACTORY.newOther(ns.qualifiedName(Constants.PREFIX_DDMORE, Constants.PROVO_LOCAL_PART_QC_STATUS, PROV_FACTORY), 
					vertex.getAttributes().get("qc"), ns.qualifiedName("xsd", "boolean", PROV_FACTORY)));
		}
		if (StringUtils.isNotEmpty(vertex.getAttributes().get("base") )) {
			entity.getOther().add(PROV_FACTORY.newOther(ns.qualifiedName(Constants.PREFIX_DDMORE, Constants.PROVO_LOCAL_PART_BASE, PROV_FACTORY), 
					vertex.getAttributes().get("base"), ns.qualifiedName("xsd", "boolean", PROV_FACTORY)));
		}
		if (StringUtils.isNotEmpty(vertex.getAttributes().get("final"))) {
			entity.getOther().add(PROV_FACTORY.newOther(ns.qualifiedName(Constants.PREFIX_DDMORE, Constants.PROVO_LOCAL_PART_FINAL, PROV_FACTORY), 
					vertex.getAttributes().get("final"), ns.qualifiedName("xsd", "boolean", PROV_FACTORY)));
		}
		if (StringUtils.isNotEmpty(vertex.getAttributes().get("pivotal"))) {
			entity.getOther().add(PROV_FACTORY.newOther(ns.qualifiedName(Constants.PREFIX_DDMORE, Constants.PROVO_LOCAL_PART_PIVOTAL, PROV_FACTORY), 
					vertex.getAttributes().get("pivotal"), ns.qualifiedName("xsd", "boolean", PROV_FACTORY)));
		}
		
		return entity;
	}
	
	protected Agent buildAgent(Namespace ns, Vertex vertex) {
		String agentId = vertex.getId();
		if (agentId.indexOf(':') > 0) {
			agentId = agentId.substring(agentId.indexOf(':') + 1);
		}
		
		Agent agent = PROV_FACTORY.newAgent(ns.qualifiedName(Constants.PREFIX_REPO, agentId, PROV_FACTORY));
		return agent;
	}
	
	protected Activity buildActivity(Namespace ns, Vertex vertex) {
		String activityId = vertex.getId();
		if (activityId.indexOf(':') > 0) {
			activityId = activityId.substring(activityId.indexOf(':') + 1);
		}
		Activity activity = PROV_FACTORY.newActivity(ns.qualifiedName(Constants.PREFIX_REPO, activityId, PROV_FACTORY));
		
//		activity.getLabel().add(PROV_FACTORY.newLabel(vertex.getAttributes().get("label"), ns.qualifiedName("prov", "label", PROV_FACTORY)));
		LangString labelString = new InternationalizedString();
		labelString.setValue(vertex.getAttributes().get("label"));
//		labelString.setLang("en");
		activity.getLabel().add(labelString);
		
		if (StringUtils.isNotEmpty(vertex.getAttributes().get("description"))) {
			activity.getOther().add(PROV_FACTORY.newOther(
					ns.qualifiedName("prov", "description", PROV_FACTORY), 
					vertex.getAttributes().get("description"), 
					ns.qualifiedName("xsd", "string", PROV_FACTORY)));
		}
		if (StringUtils.isNotEmpty(vertex.getAttributes().get("note"))) {
			activity.getOther().add(PROV_FACTORY.newOther(
					ns.qualifiedName("prov", "note", PROV_FACTORY), 
					vertex.getAttributes().get("note"), 
					ns.qualifiedName("xsd", "string", PROV_FACTORY)));
		}
		
		
		try {
			String format = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
			SimpleDateFormat df = new SimpleDateFormat(format);
			if (StringUtils.isNotEmpty(vertex.getAttributes().get("start"))) {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(df.parse(vertex.getAttributes().get("start")));
				activity.setStartTime(PROV_FACTORY.newXMLGregorianCalendar(cal)); 
			}
			if (StringUtils.isNotEmpty(vertex.getAttributes().get("end"))) {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(df.parse(vertex.getAttributes().get("end")));
				activity.setEndTime(PROV_FACTORY.newXMLGregorianCalendar(cal)); 
			}
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return activity;
	}
	
	protected String getLocalPart(String idString) {
		if (idString.indexOf(':') > 0) {
			idString = idString.substring(idString.indexOf(':') + 1);
		}
		return idString;
	}
	
	protected String queryDatastoreByType(String repoId, Integer depth, Vertex.Type... types) {
		try {
			Namespace ns = buildNamespace(repoId);
			Document document = buildDocument(ns);
			
			repoId = repoId.replace(":", "");
			repoId = repoId.replace("/", "-");
			
			Datastore datastore = getDatastore();
			Repository repository = datastore.getRepository(repoId);
			
			// Depth = 0;
			List<Vertex> vertices = repository.getVertices(types);
			Set<String> alreadyAddedIds = new HashSet<>();	
			for (Vertex vertex : vertices) {
				if (Vertex.Type.Entity.equals(vertex.getType())) {
					Entity target = buildEntity(ns, vertex);
					document.getStatementOrBundle().add(target);
				} else if (Vertex.Type.Agent.equals(vertex.getType())) {
					document.getStatementOrBundle().add(buildAgent(ns, vertex));
				} else if (Vertex.Type.Activity.equals(vertex.getType())) {
					document.getStatementOrBundle().add(buildActivity(ns, vertex));
				}
					
				alreadyAddedIds.add(vertex.getId());
				loadRelatedVertices(document, ns, alreadyAddedIds, vertex, (depth != null ? depth : 0));
			}
			if (depth == 0) {
				for (Vertex vertex : vertices) {
					for (Edge edge : vertex.getEdges()) {
						if (alreadyAddedIds.contains(edge.getSource().getId()) && alreadyAddedIds.contains(edge.getTarget().getId())) {
							buildEdge(ns, document, vertex, edge);
						}
					}
				}
			}
			return toJSON(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	
	protected boolean containsEdge(Document document, String edgeId) {
		for (StatementOrBundle obj : document.getStatementOrBundle()) {
			if (obj instanceof Identifiable && ((Identifiable) obj).getId().getLocalPart().equals(edgeId)) {
				return true;
			}
		}
		return false;
	}

	protected void buildEdge(Namespace ns, Document document, Vertex source, Edge edge) {
		if (!containsEdge(document, edge.getId())) {
		
			if (edge.getType().equals(Edge.Type.ASSOCIATED)) {
				document.getStatementOrBundle().add(PROV_FACTORY.newWasAssociatedWith(
						ns.qualifiedName(Constants.PREFIX_REPO, edge.getId(), PROV_FACTORY), 
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getSource().getId()), PROV_FACTORY),
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getTarget().getId()), PROV_FACTORY)
						)
					);
			} else if (edge.getType().equals(Edge.Type.GENERATED)) {
				document.getStatementOrBundle().add(PROV_FACTORY.newWasGeneratedBy(
						ns.qualifiedName(Constants.PREFIX_REPO, edge.getId(), PROV_FACTORY), 
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getTarget().getId()), PROV_FACTORY),
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getSource().getId()), PROV_FACTORY)
						)
					);
			} else if (edge.getType().equals(Edge.Type.DERIVED)) {
				document.getStatementOrBundle().add(PROV_FACTORY.newWasDerivedFrom(
						ns.qualifiedName(Constants.PREFIX_REPO, edge.getId(), PROV_FACTORY), 
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getSource().getId()), PROV_FACTORY),
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getTarget().getId()), PROV_FACTORY)
						)
					);
			} else if (edge.getType().equals(Edge.Type.USED)) {
				document.getStatementOrBundle().add(PROV_FACTORY.newUsed(
						ns.qualifiedName(Constants.PREFIX_REPO, edge.getId(), PROV_FACTORY), 
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getSource().getId()), PROV_FACTORY),
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getTarget().getId()), PROV_FACTORY)
						)
					);
			} else if (edge.getType().equals(Edge.Type.INFORMED)) {
				document.getStatementOrBundle().add(PROV_FACTORY.newWasInformedBy(
						ns.qualifiedName(Constants.PREFIX_REPO, edge.getId(), PROV_FACTORY), 
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getSource().getId()), PROV_FACTORY),
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getTarget().getId()), PROV_FACTORY)
						)
					);
			} else if (edge.getType().equals(Edge.Type.INFLUENCED)) {
				document.getStatementOrBundle().add(PROV_FACTORY.newWasInfluencedBy(
						ns.qualifiedName(Constants.PREFIX_REPO, edge.getId(), PROV_FACTORY), 
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getSource().getId()), PROV_FACTORY),
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getTarget().getId()), PROV_FACTORY)
						)
					);
			} else if (edge.getType().equals(Edge.Type.INVALIDATED)) {
				document.getStatementOrBundle().add(PROV_FACTORY.newWasInvalidatedBy(
						ns.qualifiedName(Constants.PREFIX_REPO, edge.getId(), PROV_FACTORY), 
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getSource().getId()), PROV_FACTORY),
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getTarget().getId()), PROV_FACTORY)
						)
					);
			} else if (edge.getType().equals(Edge.Type.STARTED)) {
				document.getStatementOrBundle().add(PROV_FACTORY.newWasStartedBy(
						ns.qualifiedName(Constants.PREFIX_REPO, edge.getId(), PROV_FACTORY), 
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getSource().getId()), PROV_FACTORY),
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getTarget().getId()), PROV_FACTORY)
						)
					);
			} else if (edge.getType().equals(Edge.Type.ENDED)) {
				document.getStatementOrBundle().add(PROV_FACTORY.newWasEndedBy(
						ns.qualifiedName(Constants.PREFIX_REPO, edge.getId(), PROV_FACTORY), 
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getSource().getId()), PROV_FACTORY),
						ns.qualifiedName(Constants.PREFIX_REPO, getLocalPart(edge.getTarget().getId()), PROV_FACTORY)
						)
					);
			}
		}
	}
	
	protected String queryDatastoreById(String repoId, String entityId, Integer depth) {
		try {
			Namespace ns = buildNamespace(repoId);
			Document document = buildDocument(ns);
			
			repoId = repoId.replace(":", "");
			repoId = repoId.replace("/", "-");
			
			Datastore datastore = getDatastore();
			Repository repository = datastore.getRepository(repoId);
			
			// Depth = 0;
			Vertex vertex = repository.getVertex(entityId);
			if (Vertex.Type.Entity.equals(vertex.getType())) {
				Entity target = buildEntity(ns, vertex);
				document.getStatementOrBundle().add(target);
			} else if (Vertex.Type.Agent.equals(vertex.getType())) {
				document.getStatementOrBundle().add(buildAgent(ns, vertex));
			} else if (Vertex.Type.Activity.equals(vertex.getType())) {
				document.getStatementOrBundle().add(buildActivity(ns, vertex));
			}
			Set<String> alreadyAddedIds = new HashSet<>();		
			alreadyAddedIds.add(vertex.getId());
			int d = depth != null ? depth : 0;
			loadRelatedVertices(document, ns, alreadyAddedIds, vertex, d);
			
			
			
			
			
			return toJSON(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	protected void loadRelatedVertices(Document document, Namespace ns, Set<String> alreadyAddedIds, Vertex parent, int depth) {
		if (depth > 0) {
			for (Edge edge : parent.getEdges()) {
				buildEdge(ns, document, parent, edge);
				Vertex relatedVertex = edge.getTarget();
				if (relatedVertex.getId().equals(parent.getId())) {
					relatedVertex = edge.getSource();
				}
				if (!alreadyAddedIds.contains(relatedVertex.getId())) {
					alreadyAddedIds.add(relatedVertex.getId());
					if (relatedVertex != null && Vertex.Type.Entity.equals(relatedVertex.getType())) {
						Entity target = buildEntity(ns, relatedVertex);
						document.getStatementOrBundle().add(target);
					} else if (relatedVertex != null && Vertex.Type.Agent.equals(relatedVertex.getType())) {
						document.getStatementOrBundle().add(buildAgent(ns, relatedVertex));
					} else if (relatedVertex != null && Vertex.Type.Activity.equals(relatedVertex.getType())) {
						document.getStatementOrBundle().add(buildActivity(ns, relatedVertex));
					}
				}
				depth--;
				loadRelatedVertices(document, ns, alreadyAddedIds, relatedVertex, depth);
			}
		}
		return;
	}
}

