package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "qcs")
public class QcEntries implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<QcEntry> qcEntries;
	
	public QcEntries() {
		this(new ArrayList<QcEntry>());
	}
	
	public QcEntries(List<QcEntry> qcEntries) {
		super();
		if (qcEntries != null) {
			this.qcEntries = qcEntries;
		} else {
			this.qcEntries = new ArrayList<QcEntry>();;
		}
	}
	
	@XmlElement(name = "qc")
	public List<QcEntry> getQcEntries() {
		return this.qcEntries;
	}

	public void setQcEntries(List<QcEntry> qcEntries) {
		this.qcEntries = qcEntries;
	}
	
	public void addQcEntry(QcEntry qcEntry) {
		this.qcEntries.add(qcEntry);
	}
	
	public int addQcEntries(List<QcEntry> qcEntries) {
		if (Primitives.isNotEmpty(qcEntries)) {
			this.qcEntries.addAll(qcEntries);
			return qcEntries.size();
		}
		return 0;
	}
	
	public boolean hasQcEntry(String id) {
		QcEntry qcEntry = new QcEntry();
		qcEntry.setId(id);
		return hasQcEntry(qcEntry);
	}

	public boolean hasQcEntry(QcEntry qcEntry) {
		return hasQcEntries() && getQcEntries().contains(qcEntry);
	}
	
	public int size() {
		return this.qcEntries != null ? this.qcEntries.size() : 0;
	}
	
	public boolean hasQcEntries() {
		return size() > 0;
	}
	
	public List<QcEntry> toList() {
		return (this.qcEntries != null ? this.qcEntries : new ArrayList<QcEntry>());
	}
}
