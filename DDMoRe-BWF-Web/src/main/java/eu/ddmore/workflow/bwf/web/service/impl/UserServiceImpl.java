package eu.ddmore.workflow.bwf.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.ddmore.workflow.bwf.client.exception.DuplicateException;
import eu.ddmore.workflow.bwf.client.exception.EntityInUseException;
import eu.ddmore.workflow.bwf.client.model.ResultEntry;
import eu.ddmore.workflow.bwf.client.model.User;
import eu.ddmore.workflow.bwf.client.model.Users;
import eu.ddmore.workflow.bwf.client.service.UserService;
import eu.ddmore.workflow.bwf.client.util.Primitives;

@Service(value="userService")
public class UserServiceImpl extends BaseService implements UserService {

	private static final long serialVersionUID = 1L;

	@Value(value="${rest.service.contextPath.user:user}")
	private String contextPathRepository;
	
	@Override
	protected String getServiceRestContextPath() {
		return this.contextPathRepository;
	}

	@Override
	public User login(String username, String password) {
		if (Primitives.isEmpty(username)) {
			throw new RuntimeException("Username must not be empty.");
		}
		if (Primitives.isEmpty(password)) {
			throw new RuntimeException("Password must not be empty.");
		}
		
		return doRestGet("login", User.class, "username", username, "password", password);
	}

	@Override
	public List<User> search(boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		Users users = doRestGet(Users.class, 
				"loadAuthorities", loadAuthorities, "loadProjects", loadProjects, "loadMembers", loadMembers);
		return toUserList(users);
	}

	@Override
	public User getById(Long id, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (id == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		return doRestGet(("id/" + id), User.class, 
				"loadAuthorities", loadAuthorities, "loadProjects", loadProjects, "loadMembers", loadMembers);
	}

	@Override
	public User getByUsername(String username, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (Primitives.isEmpty(username)) {
			throw new RuntimeException("Username must not be empty.");
		}
		
		return doRestGet(("username/" + username), User.class, 
				"loadAuthorities", loadAuthorities, "loadProjects", loadProjects, "loadMembers", loadMembers);
	}

	@Override
	public User insertOrUpdate(User user) throws DuplicateException {
		if (user == null) {
			throw new RuntimeException("User must not be empty.");
		}
		
		return doRestPost(User.class, user);
	}

	@Override
	public Boolean deleteById(Long id) throws EntityInUseException {
		if (id == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		ResultEntry resultEntry = doRestDelete(("id/" + id), ResultEntry.class);
		return resultEntry.getBooleanValue();
	}

	@Override
	public Boolean deleteByUsername(String username) throws EntityInUseException {
		if (Primitives.isEmpty(username)) {
			throw new RuntimeException("Username must not be empty.");
		}
		
		ResultEntry resultEntry = doRestDelete(("username/" + username), ResultEntry.class);
		return resultEntry.getBooleanValue();
	}

	@Override
	public List<User> getMembersById(Long id, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (id == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		Users users = doRestGet(("members/id/" + id), Users.class, 
				"loadAuthorities", loadAuthorities, "loadProjects", loadProjects, "loadMembers", loadMembers);
		return toUserList(users);
	}

	@Override
	public List<User> getMembersByUsername(String username, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (Primitives.isEmpty(username)) {
			throw new RuntimeException("Username must not be empty.");
		}
		
		Users users = doRestGet(("members/username/" + username), Users.class, 
				"loadAuthorities", loadAuthorities, "loadProjects", loadProjects, "loadMembers", loadMembers);
		return toUserList(users);
	}

	// ----------------------------------------------------------------- Helper
	
	private List<User> toUserList(Users users) {
		return (users != null ? users.toList() : new ArrayList<User>());
	}
}
