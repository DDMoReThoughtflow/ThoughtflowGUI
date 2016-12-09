package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "activities")
public class Activities implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Activity> activities;
	
	public Activities() {
		this(new ArrayList<Activity>());
	}
	
	public Activities(List<Activity> activities) {
		super();
		if (activities != null) {
			this.activities = activities;
		} else {
			this.activities = new ArrayList<Activity>();;
		}
	}
	
	@XmlElement(name = "activity")
	public List<Activity> getActivities() {
		return this.activities;
	}

	public void setActivitys(List<Activity> activities) {
		this.activities = activities;
	}
	
	public void addActivity(Activity activity) {
		this.activities.add(activity);
	}
	
	public int addActivities(List<Activity> activities) {
		if (Primitives.isNotEmpty(activities)) {
			this.activities.addAll(activities);
			return activities.size();
		}
		return 0;
	}

	public boolean hasActivity(String id) {
		Activity activity = new Activity();
		activity.setId(id);
		return hasActivity(activity);
	}

	public boolean hasActivity(Activity activity) {
		return hasActivitys() && getActivities().contains(activity);
	}
	
	public int size() {
		return this.activities != null ? this.activities.size() : 0;
	}

	public boolean hasActivitys() {
		return size() > 0;
	}
	
	public List<Activity> toList() {
		return (this.activities != null ? this.activities : new ArrayList<Activity>());
	}
}
