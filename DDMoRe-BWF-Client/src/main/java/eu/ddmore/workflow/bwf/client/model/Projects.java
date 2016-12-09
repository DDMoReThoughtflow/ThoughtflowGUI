package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "projects")
public class Projects implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Project> projects;
	
	public Projects() {
		this(new ArrayList<Project>());
	}
	
	public Projects(List<Project> projects) {
		super();
		if (projects != null) {
			this.projects = projects;
		} else {
			this.projects = new ArrayList<Project>();;
		}
	}
	
	@XmlElement(name = "project")
	public List<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	public void addProject(Project projects) {
		this.projects.add(projects);
	}
	
	public int addProjects(List<Project> projects) {
		if (Primitives.isNotEmpty(projects)) {
			this.projects.addAll(projects);
			return projects.size();
		}
		return 0;
	}
	
	public boolean hasProject(Long id) {
		Project project = new Project();
		project.setId(id);
		return hasProject(project);
	}

	public boolean hasProject(Project project) {
		return hasProjects() && getProjects().contains(project);
	}
	
	public int size() {
		return this.projects != null ? this.projects.size() : 0;
	}
	
	public boolean hasProjects() {
		return size() > 0;
	}
	
	public List<Project> toList() {
		return (this.projects != null ? this.projects : new ArrayList<Project>());
	}
}
