package eu.ddmore.workflow.bwf.client.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import eu.ddmore.workflow.bwf.client.enumeration.FileType;
import eu.ddmore.workflow.bwf.client.util.Primitives;

public class FileTypeXmlAdapter extends XmlAdapter<String, FileType> {

    @Override
    public String marshal(FileType v) throws Exception {
    	if (v != null) {
    		return v.toString();
    	}
    	return null;
    }

    @Override
    public FileType unmarshal(String v) throws Exception {
    	if (Primitives.isNotEmpty(v)) {
    		return FileType.valueOf(v);
    	}
    	return null;
    }
}
