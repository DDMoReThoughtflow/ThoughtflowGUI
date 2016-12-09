package eu.ddmore.workflow.bwf.client.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import eu.ddmore.workflow.bwf.client.enumeration.OutputType;
import eu.ddmore.workflow.bwf.client.util.Primitives;

public class OutputTypeXmlAdapter extends XmlAdapter<String, OutputType> {

    @Override
    public String marshal(OutputType v) throws Exception {
    	if (v != null) {
    		return v.toString();
    	}
    	return null;
    }

    @Override
    public OutputType unmarshal(String v) throws Exception {
    	if (Primitives.isNotEmpty(v)) {
    		return OutputType.valueOf(v);
    	}
    	return null;
    }
}
