package eu.ddmore.workflow.bwf.client.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import eu.ddmore.workflow.bwf.client.enumeration.ApplicationRole;

@XmlRootElement(name = "user")
public class User extends BaseIdModel {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String fullName;
	private String email;
	private String phone;
	private String company;
	private String location;
	private Boolean enabled;
	
	private Authorities authorities;
	private Projects userProjects;
	private Projects reviewerProjects;
	private Users members;
	
	public User() {
		super();
		this.enabled = true;
		this.authorities = new Authorities();
		this.userProjects = new Projects();
		this.reviewerProjects = new Projects();
		this.members = new Users();
	}
	
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFullName() {
		if (this.fullName == null) {
			StringBuilder sb = new StringBuilder();
			boolean appended = false;
			if (isNotEmpty(getFirstname())) {
				sb.append(getFirstname());
				appended = true;
			}
			if (isNotEmpty(getLastname())) {
				if (appended) {
					sb.append(" ");
				}
				sb.append(getLastname());
			}
			this.fullName = sb.toString();
		}
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public Boolean getEnabled() {
		return this.enabled != null ? this.enabled : false;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Authorities getAuthorities() {
		return this.authorities;
	}

	public void setAuthorities(Authorities authorities) {
		this.authorities = authorities;
	}

	public Projects getUserProjects() {
		return this.userProjects;
	}

	public void setUserProjects(Projects userProjects) {
		this.userProjects = userProjects;
	}

	public Projects getReviewerProjects() {
		return this.reviewerProjects;
	}

	public void setReviewerProjects(Projects reviewerProjects) {
		this.reviewerProjects = reviewerProjects;
	}
	
	public Users getMembers() {
		return this.members;
	}

	public void setMembers(Users members) {
		this.members = members;
	}

	@XmlTransient
	public boolean isAdmin() {
		for (Authority authority : getAuthorities().getAuthorities()) {
			if (ApplicationRole.ADMIN == authority.getRole()) {
				return true;
			}
		}
		return false;
	}
	
	@XmlTransient
	public boolean isManager() {
		for (Authority authority : getAuthorities().getAuthorities()) {
			if (ApplicationRole.MANAGER == authority.getRole()) {
				return true;
			}
		}
		return false;
	}

	@XmlTransient
	public boolean isScientist() {
		for (Authority authority : getAuthorities().getAuthorities()) {
			if (ApplicationRole.SCIENTIST == authority.getRole()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "[username=" + getUsername() + "]";
	}
}
