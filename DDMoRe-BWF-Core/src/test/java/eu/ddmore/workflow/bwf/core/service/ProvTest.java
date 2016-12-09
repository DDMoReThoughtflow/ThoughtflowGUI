package eu.ddmore.workflow.bwf.core.service;

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openprovenance.prov.json.Converter;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInformedBy;

public class ProvTest {

	 private static ProvFactory factory;
	 private static Converter convert;

	 @BeforeClass
	 public static void runOnceBeforeClass() {
		 factory = ProvFactory.getFactory();
		 convert = new Converter(factory);
	}
	 
	@Test
	public void testParseJson() throws Exception {
		InputStream inDoc = getClass().getResourceAsStream("used.json");
		Assert.assertNotNull(inDoc);
		
		Document doc = convert.readDocument(inDoc);
		Assert.assertNotNull(doc);
		
		List<StatementOrBundle> statementsOrBundles = doc.getStatementOrBundle();
		Assert.assertNotNull(statementsOrBundles);
		Assert.assertEquals(25, statementsOrBundles.size());
		
		int agentCount = 0;
		int activityCount = 0;
		int entityCount = 0;
		int usedCount = 0;
		int wasAssociatedWithCount = 0;
		int wasGeneratedByCount = 0;
		int wasInformedByCount = 0;
		
		for (StatementOrBundle statementOrBundle : statementsOrBundles) {
			if (statementOrBundle.getClass() == Agent.class) {
				agentCount++;
			} else if (statementOrBundle.getClass() == Activity.class) {
				activityCount++;
			} else if (statementOrBundle.getClass() == Entity.class) {
				entityCount++;
			} else if (statementOrBundle.getClass() == Used.class) {
				usedCount++;
			} else if (statementOrBundle.getClass() == WasAssociatedWith.class) {
				wasAssociatedWithCount++;
			} else if (statementOrBundle.getClass() == WasGeneratedBy.class) {
				wasGeneratedByCount++;
			} else if (statementOrBundle.getClass() == WasInformedBy.class) {
				wasInformedByCount++;
			}
		}
		
		int sum = (agentCount +activityCount + entityCount + usedCount + wasAssociatedWithCount + wasGeneratedByCount + wasInformedByCount);
		Assert.assertEquals(25, sum);
		
//		System.out.println("agentCount: " + agentCount);
//		System.out.println("activityCount: " + activityCount);
//		System.out.println("entityCount: " + entityCount);
//		System.out.println("usedCount: " + usedCount);
//		System.out.println("wasAssociatedWithCount: " + wasAssociatedWithCount);
//		System.out.println("wasGeneratedByCount: " + wasGeneratedByCount);
//		System.out.println("wasInformedByCount: " + wasInformedByCount);
//		System.out.println("sum: " + sum);
	}
}
