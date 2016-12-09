package eu.ddmore.workflow.server.client.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import eu.ddmore.workflow.server.client.xmladapter.ProvTypeXmlAdapter;

@XmlRootElement(name = "provtype")
public class ProvType implements Serializable {

	private static final long serialVersionUID = 1L;

	private eu.ddmore.workflow.bwf.client.enumeration.ProvType type;
	
	public ProvType() {
		super();
	}

	public ProvType(eu.ddmore.workflow.bwf.client.enumeration.ProvType type) {
		super();
		this.type = type;
	}

	@XmlJavaTypeAdapter(ProvTypeXmlAdapter.class)
	public eu.ddmore.workflow.bwf.client.enumeration.ProvType getType() {
		return this.type;
	}

	public void setType(eu.ddmore.workflow.bwf.client.enumeration.ProvType type) {
		this.type = type;
	}
}
