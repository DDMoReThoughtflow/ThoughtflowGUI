package eu.ddmore.workflow.bwf.client.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import eu.ddmore.workflow.bwf.client.enumeration.AssumptionType;
import eu.ddmore.workflow.bwf.client.util.Primitives;

public class AssumptionTypeXmlAdapter extends XmlAdapter<String, AssumptionType> {

    @Override
    public String marshal(AssumptionType v) throws Exception {
    	if (v != null) {
    		return v.toString();
    	}
    	return null;
    }

    @Override
    public AssumptionType unmarshal(String v) throws Exception {
    	if (Primitives.isNotEmpty(v)) {
    		return AssumptionType.valueOf(v);
    	}
    	return null;
    }
}
