package eu.ddmore.workflow.bwf.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.ddmore.workflow.bwf.client.exception.DuplicateException;
import eu.ddmore.workflow.bwf.client.exception.EntityInUseException;
import eu.ddmore.workflow.bwf.client.model.User;
import eu.ddmore.workflow.bwf.client.service.UserService;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.core.dao.UserDao;

@Service(value="userService")
public class UserServiceImpl implements UserService {

	@Autowired private UserDao userDao;

	@Override
	public User login(String username, String password) {
		if (Primitives.isEmpty(username)) {
			throw new RuntimeException("Username must not be empty.");
		}
		if (Primitives.isEmpty(password)) {
			throw new RuntimeException("Password must not be empty.");
		}
		
		return this.userDao.login(username, password);
	}
	
	@Override
	public List<User> search(boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		return this.userDao.search(loadAuthorities, loadProjects, loadMembers);
	}

	@Override
	public User getById(Long id, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (id == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		return this.userDao.getById(id, loadAuthorities, loadProjects, loadMembers);
	}

	@Override
	public User getByUsername(String username, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (Primitives.isEmpty(username)) {
			throw new RuntimeException("Username must not be empty.");
		}
		
		return this.userDao.getByUsername(username, loadAuthorities, loadProjects, loadMembers);
	}

	@Override
	@Transactional(readOnly = false)
	public User insertOrUpdate(User user) throws DuplicateException {
		if (user == null) {
			throw new RuntimeException("User must not be empty.");
		}
		
		return this.userDao.insertOrUpdate(user);
	}

	@Override
	@Transactional(readOnly = false)
	public Boolean deleteById(Long id) throws EntityInUseException {
		if (id == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		return this.userDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = false)
	public Boolean deleteByUsername(String username) throws EntityInUseException {
		if (Primitives.isEmpty(username)) {
			throw new RuntimeException("Username must not be empty.");
		}
		
		User user = getByUsername(username, false, false, false);
		if (user != null) {
			return deleteById(user.getId());
		}
		return false;
	}
	
	@Override
	public List<User> getMembersById(Long id, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (id == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		return this.userDao.getMembersById(id, loadAuthorities, loadProjects, loadMembers);
	}

	@Override
	public List<User> getMembersByUsername(String username, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (Primitives.isEmpty(username)) {
			throw new RuntimeException("Username must not be empty.");
		}
		
		User user = getByUsername(username, false, false, false);
		if (user != null) {
			return getMembersById(user.getId(), loadAuthorities, loadProjects, loadMembers);
		}
		return new ArrayList<User>();
	}
}
