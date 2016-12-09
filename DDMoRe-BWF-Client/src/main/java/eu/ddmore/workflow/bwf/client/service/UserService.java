package eu.ddmore.workflow.bwf.client.service;

import java.util.List;

import eu.ddmore.workflow.bwf.client.exception.DuplicateException;
import eu.ddmore.workflow.bwf.client.exception.EntityInUseException;
import eu.ddmore.workflow.bwf.client.model.User;

public interface UserService {

	User login(String username, String password); 
	
	List<User> search(boolean loadAuthorities, boolean loadProjects, boolean loadMembers);
	User getById(Long id, boolean loadAuthorities, boolean loadProjects, boolean loadMembers);
	User getByUsername(String username, boolean loadAuthorities, boolean loadProjects, boolean loadMembers);
	
	User insertOrUpdate(User user) throws DuplicateException;
	Boolean deleteById(Long id) throws EntityInUseException;
	Boolean deleteByUsername(String username) throws EntityInUseException;

	List<User> getMembersById(Long id, boolean loadAuthorities, boolean loadProjects, boolean loadMembers);
	List<User> getMembersByUsername(String username, boolean loadAuthorities, boolean loadProjects, boolean loadMembers);

}
