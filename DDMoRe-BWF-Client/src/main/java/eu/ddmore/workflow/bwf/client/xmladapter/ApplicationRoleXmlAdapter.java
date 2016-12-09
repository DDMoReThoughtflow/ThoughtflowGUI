package eu.ddmore.workflow.bwf.client.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import eu.ddmore.workflow.bwf.client.enumeration.ApplicationRole;
import eu.ddmore.workflow.bwf.client.util.Primitives;

public class ApplicationRoleXmlAdapter extends XmlAdapter<String, ApplicationRole> {

    @Override
    public String marshal(ApplicationRole v) throws Exception {
    	if (v != null) {
    		return v.toString();
    	}
    	return null;
    }

    @Override
    public ApplicationRole unmarshal(String v) throws Exception {
    	if (Primitives.isNotEmpty(v)) {
    		return ApplicationRole.valueOf(v);
    	}
    	return null;
    }
}
