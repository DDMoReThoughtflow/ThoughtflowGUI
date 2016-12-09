package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "users")
public class Users implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<User> users;
	
	public Users() {
		this(new ArrayList<User>());
	}
	
	public Users(List<User> users) {
		super();
		if (users != null) {
			this.users = users;
		} else {
			this.users = new ArrayList<User>();;
		}
	}
	
	@XmlElement(name = "user")
	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public void addUser(User user) {
		this.users.add(user);
	}
	
	public int addUsers(List<User> users) {
		if (Primitives.isNotEmpty(users)) {
			this.users.addAll(users);
			return users.size();
		}
		return 0;
	}
	
	public boolean hasUser(Long id) {
		User user = new User();
		user.setId(id);
		return hasUser(user);
	}

	public boolean hasUser(User user) {
		return hasUsers() && getUsers().contains(user);
	}
	
	public int size() {
		return this.users != null ? this.users.size() : 0;
	}
	
	public boolean hasUsers() {
		return size() > 0;
	}
	
	public List<User> toList() {
		return (this.users != null ? this.users : new ArrayList<User>());
	}
}
