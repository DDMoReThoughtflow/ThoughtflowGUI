	package eu.ddmore.workflow.server.rest.csv.service;

import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openprovenance.prov.json.Converter;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Element;
import org.openprovenance.prov.model.HasLabel;
import org.openprovenance.prov.model.HasLocation;
import org.openprovenance.prov.model.HasOther;
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
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.ProvFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.server.client.model.ActivityQuery;
import eu.ddmore.workflow.server.client.model.AgentQuery;
import eu.ddmore.workflow.server.client.model.EntityQuery;
import eu.ddmore.workflow.server.rest.BaseTest;

public class CsvServiceTest extends BaseTest {

	@Autowired
	private CsvService csvServiceTest;

	@Before
	public void setup()  throws URISyntaxException {
		csvServiceTest.setCsvDataPath(this.getClass().getResource("/testdata").toURI().getPath());
	}

	@Test
	public void testEntity() {
		EntityQuery query = new EntityQuery();
		query.setRepo_id("https://github.com/halfmanhalfgeek/MDLProject");
		query.setEntity_id("repo:3103d73a24a57321080d46ef04fd7c016b514000/models/Step1.mdl");
		query.setDepth(1);
		String result = csvServiceTest.entity(query);
		
		System.out.println(result);
	}
	
	@Test
	public void testEntites() {
		EntityQuery query = new EntityQuery();
		query.setRepo_id("https://github.com/halfmanhalfgeek/MDLProject");
		query.setDepth(1);
		String result = csvServiceTest.entity(query);
		System.out.println(result);
	}
	
	@Test
	public void testActivities() {
		ActivityQuery query = new ActivityQuery();
		query.setDepth(0);
		query.setRepo_id("https://github.com/halfmanhalfgeek/MDLProject");
		String result = csvServiceTest.activity(query);
		System.out.println(result);
	}
	@Test
	public void testAgents() {
		AgentQuery query = new AgentQuery();
		query.setRepo_id("https://github.com/halfmanhalfgeek/MDLProject");
		String result = csvServiceTest.agent(query);
		System.out.println(result);
	}
	
	@Test
	public void testEntity2() {
		EntityQuery query = new EntityQuery();
		query.setRepo_id("https://github.com/halfmanhalfgeek/MDLProject");
		query.setEntity_id("repo:3103d73a24a57321080d46ef04fd7c016b514000/models/Step1.mdl");
		query.setDepth(2);
		String provResult = csvServiceTest.entity(query);

		ProvFactory PROV_FACTORY = ProvFactory.getFactory();;
		Converter PROV_CONVERT = new Converter(PROV_FACTORY);;
	
		System.out.println(provResult);
		
		try {
			Document doc = PROV_CONVERT.readDocument(new ByteArrayInputStream(provResult.getBytes()));
			List<StatementOrBundle> statementsOrBundles = doc.getStatementOrBundle();
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
						edge.addProperty("target", buildActivity(nodes, ((WasInformedBy)relation).getInformed()));
					} else {
						edge.addProperty("target", index);
					}
					edge.addProperty("label", "informed");
					added = true;
				}
				
				if (added) {
					edgesArray.add(edge);
				} else {
					System.err.println("No relation edge added for '" + relation.getClass() + "'.");
				}
			}
			JsonArray nodeArray = new JsonArray();
			
			for (Element element : nodes) {
				JsonObject object = new JsonObject();
				object.addProperty("name", element.getId().getLocalPart());
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
				nodeArray.add(object);
			}
			result.add("nodes", nodeArray);
			result.add("edges", edgesArray);
			Gson gson = new Gson();
			System.out.println(gson.toJson(result));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
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
	@Test
	public void testActivity() {
		ActivityQuery query = new ActivityQuery();
		query.setRepo_id("https://github.com/halfmanhalfgeek/MDLProject");
		query.setActivity_id("repo:9");
		query.setDepth(1);
		String result = csvServiceTest.activity(query);
		
		System.out.println(result);
		
	}
	
	@Test
	public void testAgent() {
		AgentQuery query = new AgentQuery();
		query.setRepo_id("https://github.com/halfmanhalfgeek/MDLProject");
		query.setAgent_id("jwilkins");
		query.setDepth(1);
		String result = csvServiceTest.agent(query);
		
		System.out.println(result);
	}
}


