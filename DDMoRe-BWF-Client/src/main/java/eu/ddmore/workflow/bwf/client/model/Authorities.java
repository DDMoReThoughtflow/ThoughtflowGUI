package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.ddmore.workflow.bwf.client.enumeration.ApplicationRole;
import eu.ddmore.workflow.bwf.client.util.Primitives;

@XmlRootElement(name = "authorities")
public class Authorities implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Authority> authorities;
	
	public Authorities() {
		this(new ArrayList<Authority>());
	}
	
	public Authorities(List<Authority> authorities) {
		super();
		if (authorities != null) {
			this.authorities = authorities;
		} else {
			this.authorities = new ArrayList<Authority>();;
		}
	}
	
	@XmlElement(name = "authority")
	public List<Authority> getAuthorities() {
		return this.authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public void addAuthority(Authority authority) {
		this.authorities.add(authority);
	}

	public int addAuthorities(List<Authority> authorities) {
		if (Primitives.isNotEmpty(authorities)) {
			this.authorities.addAll(authorities);
			return authorities.size();
		}
		return 0;
	}

	public boolean hasAuthority(ApplicationRole role) {
		Authority authority = new Authority();
		authority.setRole(role);
		return hasAuthority(authority);
	}

	public boolean hasAuthority(Authority authority) {
		return hasAuthorities() && getAuthorities().contains(authority);
	}
	
	public int size() {
		return this.authorities != null ? this.authorities.size() : 0;
	}
	
	public boolean hasAuthorities() {
		return size() > 0;
	}
	
	public List<Authority> toList() {
		return (this.authorities != null ? this.authorities : new ArrayList<Authority>());
	}
}
