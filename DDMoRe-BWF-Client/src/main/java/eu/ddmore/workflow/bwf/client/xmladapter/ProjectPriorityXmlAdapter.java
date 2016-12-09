package eu.ddmore.workflow.bwf.client.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import eu.ddmore.workflow.bwf.client.enumeration.ProjectPriority;
import eu.ddmore.workflow.bwf.client.util.Primitives;

public class ProjectPriorityXmlAdapter extends XmlAdapter<String, ProjectPriority> {

    @Override
    public String marshal(ProjectPriority v) throws Exception {
    	if (v != null) {
    		return v.toString();
    	}
    	return null;
    }

    @Override
    public ProjectPriority unmarshal(String v) throws Exception {
    	if (Primitives.isNotEmpty(v)) {
    		return ProjectPriority.valueOf(v);
    	}
    	return null;
    }
}
