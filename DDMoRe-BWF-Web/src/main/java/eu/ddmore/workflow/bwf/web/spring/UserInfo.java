package eu.ddmore.workflow.bwf.web.spring;

import java.security.Principal;

import eu.ddmore.workflow.bwf.client.model.User;

public class UserInfo extends User implements Principal {

	private static final long serialVersionUID = 1L;

	private User user;
	
	public UserInfo(User user) {
		super();
		this.user = user;
	}
	
	@Override
	public String getName() {
		return getUser().getUsername();
	}

	public User getUser() {
		return this.user;
	}
}
