package eu.ddmore.workflow.bwf.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Element;
import org.openprovenance.prov.model.HasLabel;
import org.openprovenance.prov.model.HasLocation;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.HasTime;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Relation;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import eu.ddmore.workflow.bwf.client.enumeration.FileType;
import eu.ddmore.workflow.bwf.client.enumeration.ProvType;
import eu.ddmore.workflow.bwf.client.model.File;
import eu.ddmore.workflow.bwf.client.util.Constants;
import eu.ddmore.workflow.bwf.client.util.Primitives;

public class BaseProvServiceImpl {
	
	private static final Logger LOG = LoggerFactory.getLogger(BaseProvServiceImpl.class);

	protected String getId(QualifiedName qualifiedName) {
		return qualifiedName.getPrefix() + ":" + qualifiedName.getLocalPart();
	}
	
	protected List<File> getFiles(Document doc) {
		List<File> files = new ArrayList<File>();
		
		if (doc == null) {
			return files;
		}
		
		List<StatementOrBundle> statementsOrBundles = doc.getStatementOrBundle();
		if (Primitives.isEmpty(statementsOrBundles)) {
			return files;
		}
		
		Map<QualifiedName, File> entities = new HashMap<QualifiedName, File>();
		Map<QualifiedName, eu.ddmore.workflow.bwf.client.model.Activity> activities = 
				new HashMap<QualifiedName, eu.ddmore.workflow.bwf.client.model.Activity>();
		
		for (StatementOrBundle statementOrBundle : statementsOrBundles) {
			if (statementOrBundle instanceof Entity) {
				Entity provFile = (Entity) statementOrBundle;
				File modelFile = toModelFile(provFile);
				entities.put(provFile.getId(), modelFile);
			} else if (statementOrBundle instanceof Activity) {
				Activity provActivity = (Activity) statementOrBundle;
				eu.ddmore.workflow.bwf.client.model.Activity modelActivity = toModelActivity(provActivity);
				activities.put(provActivity.getId(), modelActivity);
			}
		}
		
		for (StatementOrBundle statementOrBundle : statementsOrBundles) {
			if (statementOrBundle instanceof Relation) {
				Relation relation = (Relation) statementOrBundle;
				
				if (relation instanceof WasGeneratedBy) {
					WasGeneratedBy wasGeneratedBy = (WasGeneratedBy) relation;
					QualifiedName qnActivity = wasGeneratedBy.getActivity();
					if (qnActivity != null) {
						eu.ddmore.workflow.bwf.client.model.Activity modelActivity = activities.get(qnActivity);
						if (qnActivity != null) {
							QualifiedName qnEntity = wasGeneratedBy.getEntity();
							if (qnEntity != null) {	
								File modelFile = entities.get(qnEntity);
								if (modelFile != null) {	
									if (!modelFile.getActivities().hasActivity(modelActivity)) {
										modelFile.getActivities().addActivity(modelActivity);
										setFileLastModified(modelFile, modelActivity);
									}
								}
							}
						}
					}
				} else if (statementOrBundle instanceof Used) {
					Used used = (Used) relation;
					QualifiedName qnActivity = used.getActivity();
					if (qnActivity != null) {
						eu.ddmore.workflow.bwf.client.model.Activity modelActivity = activities.get(qnActivity);
						if (qnActivity != null) {
							QualifiedName qnEntity = used.getEntity();
							if (qnEntity != null) {	
								File modelFile = entities.get(qnEntity);
								if (modelFile != null) {	
									if (!modelFile.getActivities().hasActivity(modelActivity)) {
										modelFile.getActivities().addActivity(modelActivity);
										setFileLastModified(modelFile, modelActivity);
									}
								}
							}
						}
					}
				} 
			}
		}
		
		files.addAll(entities.values());
		return files;
	}
	
	protected File toModelFile(Entity provEntity) {
		File modelFile = new File();
		
		modelFile.setId(getId(provEntity.getId()));
		modelFile.setProvType(ProvType.getProvType(provEntity.getType().get(0).getValue().toString()));
		if (ProvType.MODEL == modelFile.getProvType() || ProvType.PLAN == modelFile.getProvType()) {
			modelFile.setType(FileType.INPUT);
		} else {
			modelFile.setType(FileType.OUTPUT);
		}
		
		if (Primitives.isNotEmpty(provEntity.getOther())) {
			for (Other other : provEntity.getOther()) {
				if (Constants.PROVO_LOCAL_PART_QC_STATUS.equals(other.getElementName().getLocalPart())) {
					modelFile.setPassedQc(Boolean.valueOf(other.getValue().toString()));
				} else if (ProvType.MODEL == modelFile.getProvType()) {
					if (Constants.PROVO_LOCAL_PART_BASE.equals(other.getElementName().getLocalPart())) {
						modelFile.setBaseModel(Boolean.valueOf(other.getValue().toString()));
					} else if (Constants.PROVO_LOCAL_PART_FINAL.equals(other.getElementName().getLocalPart())) {
						modelFile.setFinalModel(Boolean.valueOf(other.getValue().toString()));
					} else if (Constants.PROVO_LOCAL_PART_PIVOTAL.equals(other.getElementName().getLocalPart())) {
						modelFile.setPivotalModel(Boolean.valueOf(other.getValue().toString()));
					}
				}
			}
		}
		
		return modelFile;
	}
	
	protected eu.ddmore.workflow.bwf.client.model.Activity toModelActivity(Activity provActivity) {
		eu.ddmore.workflow.bwf.client.model.Activity modelActivity = new eu.ddmore.workflow.bwf.client.model.Activity();

		modelActivity.setId(getId(provActivity.getId()));
		modelActivity.setName(provActivity.getId().getLocalPart());
		if (Primitives.isNotEmpty(provActivity.getLabel())) {
			modelActivity.setLabel(provActivity.getLabel().get(0).getValue());
		}
		if (provActivity.getStartTime() != null) {
			modelActivity.setStart(provActivity.getStartTime().toGregorianCalendar().getTime());
		}
		if (provActivity.getEndTime() != null) {
			modelActivity.setEnd(provActivity.getEndTime().toGregorianCalendar().getTime());
		}
		if (Primitives.isNotEmpty(provActivity.getOther())) {
			for (Other other : provActivity.getOther()) {
				if (Constants.PROVO_LOCAL_PART_DESCRIPTION.equals(other.getElementName().getLocalPart())) {
					modelActivity.setDescription(other.getValue().toString());
				} else if (Constants.PROVO_LOCAL_PART_NOTE.equals(other.getElementName().getLocalPart())) {
					modelActivity.setNote(other.getValue().toString());
				}
			}
		}
		
		return modelActivity;
	}
	
	protected void setFileLastModified(File modelFile, eu.ddmore.workflow.bwf.client.model.Activity modelActivity) {
		if (modelActivity.getEnd() != null) {
			modelFile.setModifiedAt(modelActivity.getEnd());
		} else if (modelActivity.getStart() != null) {
			modelFile.setModifiedAt(modelActivity.getStart());
		}
	}
	
	protected String toAuditTrail(Document doc) {
		if (doc == null) {
			return null;
		}
		
		List<StatementOrBundle> statementsOrBundles = doc.getStatementOrBundle();
		if (Primitives.isEmpty(statementsOrBundles)) {
			return null;
		}
		
		List<Element> nodes = new ArrayList<>();
		List<Relation> edges = new ArrayList<>();
		
		if (Primitives.isNotEmpty(statementsOrBundles)) {
			for (StatementOrBundle statementOrBundle : statementsOrBundles) {
				if (statementOrBundle instanceof Element) {
					nodes.add((Element)statementOrBundle);
				} else if (statementOrBundle instanceof Relation) {
					edges.add((Relation )statementOrBundle);
				}
			}
		}
		
		JsonObject result = new JsonObject();

		JsonArray edgesArray = new JsonArray();
		for (Relation relation : edges) {
			JsonObject edge = new JsonObject();
			
			boolean added = false; 
			if (relation instanceof WasAssociatedWith) {
				edge.addProperty("source", getIndexOfEntity(nodes, (((WasAssociatedWith)relation).getAgent())));
				int index = getIndexOfEntity(nodes, ((WasAssociatedWith)relation).getActivity());
				if (index == -1) {
					edge.addProperty("target", buildActivity(nodes, ((WasAssociatedWith)relation).getActivity()));
				} else {
					edge.addProperty("target", index);
				}
				edge.addProperty("label", "associated");
				added = true; 
			} else if (relation instanceof WasGeneratedBy) {
				edge.addProperty("source", getIndexOfEntity(nodes, ((WasGeneratedBy)relation).getActivity()));
				int index = getIndexOfEntity(nodes, ((WasGeneratedBy)relation).getEntity());
				if (index == -1) {
					edge.addProperty("target", buildEntity(nodes, ((WasGeneratedBy)relation).getEntity()));
				} else {
					edge.addProperty("target", index);
				}
				edge.addProperty("label", "generated");
				added = true;
			} else if (relation instanceof Used) {
				edge.addProperty("source", getIndexOfEntity(nodes, ((Used)relation).getEntity()));
				int index = getIndexOfEntity(nodes, ((Used)relation).getActivity());
				if (index == -1) {
					edge.addProperty("target", buildActivity(nodes, ((Used)relation).getActivity()));
				} else {
					edge.addProperty("target", index);
				}
				edge.addProperty("label", "generated");
				added = true;
			} else if (relation instanceof WasDerivedFrom) {
				edge.addProperty("source", getIndexOfEntity(nodes, ((WasDerivedFrom)relation).getGeneratedEntity()));
				int index = getIndexOfEntity(nodes, ((WasDerivedFrom)relation).getUsedEntity());
				if (index == -1) {
					edge.addProperty("target", buildEntity(nodes, ((WasDerivedFrom)relation).getUsedEntity()));
				} else {
					edge.addProperty("target", index);
				}
				edge.addProperty("label", "derived");
				added = true;
			} else if (relation instanceof WasInformedBy) {
				edge.addProperty("source", getIndexOfEntity(nodes, ((WasInformedBy)relation).getInformant()));
				int index = getIndexOfEntity(nodes, ((WasInformedBy)relation).getInformed());
				if (index == -1) {
					edge.addProperty("target", buildEntity(nodes, ((WasInformedBy)relation).getInformed()));
				} else {
					edge.addProperty("target", index);
				}
				edge.addProperty("label", "informed");
				added = true;
			}
			
			/** Add addition relation properties */
			if (relation instanceof HasTime) {
				HasTime hasTime = (HasTime) relation;
				if (hasTime.getTime() != null) {
					edge.addProperty("time", hasTime.getTime().toString());					
				}
			}
			
			if (added) {
				edgesArray.add(edge);
			} else {
				LOG.warn("No relation edge added for '" + relation.getClass() + "'.");
			}
		}
		
		JsonArray nodeArray = new JsonArray();
		for (Element element : nodes) {
			JsonObject object = new JsonObject();
			
			String name = element.getId().getLocalPart();
			int index = name.indexOf("/");
			if (index != -1) {
				name = name.substring(index + 1);
			}
			
			object.addProperty("id", element.getId().toString());
			object.addProperty("name", name);
			object.addProperty("type", element.getClass().getSimpleName());
			
			if (element instanceof HasLabel && ((HasLabel)element).getLabel().size() > 0) {
				object.addProperty("label", ((HasLabel)element).getLabel().get(0).getValue());
			}
			if (element instanceof HasLocation && ((HasLocation)element).getLocation().size() > 0) {
				object.addProperty("location", ((HasLocation)element).getLocation().get(0).getValue().toString());
			}
			if (element instanceof HasOther && ((HasOther)element).getOther().size() > 0) {
				for (Other other : ((HasOther)element).getOther()) {
					object.addProperty(other.getElementName().getLocalPart(), other.getValue().toString());
				}
			}
			
			/** Add addition activity properties */
			if (element instanceof Activity) {
				Activity activity = (Activity) element;
				if (activity.getStartTime() != null) {
					object.addProperty("startTime", activity.getStartTime().toString());
				}
				if (activity.getEndTime() != null) {
					object.addProperty("endTime", activity.getEndTime().toString());
				}
			}
			
			nodeArray.add(object);
		}
		
		result.add("nodes", nodeArray);
		result.add("edges", edgesArray);
		
		Gson gson = new Gson();
		return gson.toJson(result);
	}
	
	private int getIndexOfEntity(List<Element> elements, QualifiedName id) {
		int i = 0;
		for (Element e : elements) {
			if (e.getId().equals(id)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	private int buildActivity(List<Element> nodes, QualifiedName id) {
		Activity activity = new Activity();
		activity.setId(id);
		nodes.add(activity);
		return nodes.size() - 1;
	}
	
	private int buildEntity(List<Element> nodes, QualifiedName id) {
		Entity entity = new Entity();
		entity.setId(id);
		nodes.add(entity);
		return nodes.size() - 1;
	}
	
	@SuppressWarnings("unused")
	private int buildAgent(List<Element> nodes, QualifiedName id) {
		Agent agent = new Agent();
		agent.setId(id);
		nodes.add(agent);
		return nodes.size() - 1;
	}
}
