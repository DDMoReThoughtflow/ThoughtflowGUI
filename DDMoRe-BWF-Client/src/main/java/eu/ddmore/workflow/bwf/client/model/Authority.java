package eu.ddmore.workflow.bwf.client.model;

import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.enumeration.ApplicationRole;

@XmlRootElement(name = "authority")
public class Authority extends BaseIdModel {

	private static final long serialVersionUID = 1L;

	private ApplicationRole role;
	private Long idUser;
	
	public ApplicationRole getRole() {
		return this.role;
	}

	public void setRole(ApplicationRole role) {
		this.role = role;
	}

	public Long getIdUser() {
		return this.idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.role == null) ? 0 : this.role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Authority other = (Authority) obj;
		if (this.role != other.role) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "[id=" + getId() + ", role=" + getRole() + ", idUser=" + getIdUser() + "]";
	}
}
