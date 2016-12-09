package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "result")
public class ResultEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	private String value;

	public ResultEntry() {
		super();
	}

	public ResultEntry(String value) {
		super();
		this.value = value;
	}

	public ResultEntry(Integer intValue) {
		super();
		if (intValue != null) {
			this.value = intValue.toString();
		}
	}

	public ResultEntry(Long longValue) {
		super();
		if (longValue != null) {
			this.value = longValue.toString();
		}
	}

	public ResultEntry(Double doubleValue) {
		super();
		if (doubleValue != null) {
			this.value = doubleValue.toString();
		}
	}

	public ResultEntry(Boolean booleanValue) {
		super();
		if (booleanValue != null) {
			this.value = booleanValue.toString();
		}
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlTransient
	public Integer getIntegerValue() {
		try {
			return Integer.valueOf(getValue());
		} catch (Exception ex) {}
		return null;
	}
	
	@XmlTransient
	public Long getLongValue() {
		try {
			return Long.valueOf(getValue());
		} catch (Exception ex) {}
		return null;
	}
	
	@XmlTransient
	public Double getDoubleValue() {
		try {
			return Double.valueOf(getValue());
		} catch (Exception ex) {}
		return null;
	}
	
	@XmlTransient
	public Boolean getBooleanValue() {
		try {
			return Boolean.valueOf(getValue());
		} catch (Exception ex) {}
		return null;
	}
}
