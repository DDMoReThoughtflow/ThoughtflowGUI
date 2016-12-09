package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "files")
public class Files implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<File> files;
	
	public Files() {
		this(new ArrayList<File>());
	}
	
	public Files(List<File> files) {
		super();
		if (files != null) {
			this.files = files;
		} else {
			this.files = new ArrayList<File>();;
		}
	}
	
	@XmlElement(name = "user")
	public List<File> getFiles() {
		return this.files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
	
	public void addFile(File files) {
		this.files.add(files);
	}
	
	public int addFiles(List<File> files) {
		if (Primitives.isNotEmpty(files)) {
			this.files.addAll(files);
			return files.size();
		}
		return 0;
	}
	
	public boolean hasFile(String id) {
		File file = new File();
		file.setId(id);
		return hasFile(file);
	}

	public boolean hasFile(File file) {
		return hasFiles() && getFiles().contains(file);
	}

	public int size() {
		return this.files != null ? this.files.size() : 0;
	}
	
	public boolean hasFiles() {
		return size() > 0;
	}
	
	public List<File> toList() {
		return (this.files != null ? this.files : new ArrayList<File>());
	}
}
