package eu.ddmore.workflow.server.client.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import eu.ddmore.workflow.bwf.client.enumeration.ProvType;
import eu.ddmore.workflow.bwf.client.util.Primitives;

public class ProvTypeXmlAdapter extends XmlAdapter<String, ProvType> {

    @Override
    public String marshal(ProvType v) throws Exception {
    	if (v != null) {
    		return v.toString();
    	}
    	return null;
    }

    @Override
    public ProvType unmarshal(String v) throws Exception {
    	if (Primitives.isNotEmpty(v)) {
    		return ProvType.getProvType(v);
    	}
    	return null;
    }
}
