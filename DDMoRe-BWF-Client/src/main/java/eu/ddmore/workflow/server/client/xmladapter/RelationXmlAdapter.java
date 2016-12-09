package eu.ddmore.workflow.server.client.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import eu.ddmore.workflow.bwf.client.enumeration.Relation;
import eu.ddmore.workflow.bwf.client.util.Primitives;

public class RelationXmlAdapter extends XmlAdapter<String, Relation> {

    @Override
    public String marshal(Relation v) throws Exception {
    	if (v != null) {
    		return v.toString();
    	}
    	return null;
    }

    @Override
    public Relation unmarshal(String v) throws Exception {
    	if (Primitives.isNotEmpty(v)) {
    		return Relation.getRelation(v);
    	}
    	return null;
    }
}
