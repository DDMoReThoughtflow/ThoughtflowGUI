package eu.ddmore.workflow.bwf.core.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import eu.ddmore.workflow.bwf.client.exception.DuplicateException;
import eu.ddmore.workflow.bwf.client.exception.EntityInUseException;
import eu.ddmore.workflow.bwf.client.model.Project;
import eu.ddmore.workflow.bwf.client.model.User;

@Repository
public class UserDao extends BaseDao {

	@Autowired private AuthorityDao authorityDao;
	@Autowired private ProjectDao projectDao;
	
	public User login(String username, String password) {
		if (isEmpty(username)) {
			throw new RuntimeException("Username must not be empty.");
		}
		if (isEmpty(password)) {
			throw new RuntimeException("Password must not be empty.");
		}
		
		User user = getByUsername(username, false, true, true);
		if (user != null) {
			boolean passwordValid = assertPassword(username, password);
			if (passwordValid) {
				user.getAuthorities().addAuthorities(this.authorityDao.getByIdUser(user.getId()));
				return user;
			}
		}
		return null;
	}
	
	public List<User> search(boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		String sql = "SELECT * FROM user";
		return getJdbcTemplate().query(sql, new UserRowMapper(true, true, true));
	}
	
	public User getById(Long id, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (id == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		try {
			String sql = "SELECT * FROM user WHERE id = ?";
			return getJdbcTemplate().queryForObject(sql, new UserRowMapper(loadAuthorities, loadProjects, loadMembers), id);	
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<User> getByIds(List<Long> idList, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (isEmpty(idList)) {
			return new ArrayList<User>();
		}

		StringBuilder sb = new StringBuilder("SELECT * FROM user WHERE ");
		sb.append(getInClause(idList, "id"));

		return getJdbcTemplate().query(sb.toString(), new UserRowMapper(loadAuthorities, loadProjects, loadMembers), idList.toArray());	
	}

	public User getByUsername(String username, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (isEmpty(username)) {
			throw new RuntimeException("Username must not be empty.");
		}
		
		try {
			String sql = "SELECT * FROM user WHERE username = ?";
			return getJdbcTemplate().queryForObject(sql, new UserRowMapper(loadAuthorities, loadProjects, loadMembers), username);	
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}
	
	public List<User> getMembersById(Long id, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (id == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		String sql = "SELECT u.* FROM user u INNER JOIN team t ON t.idMember = u.id WHERE t.idUser = ?";
		return getJdbcTemplate().query(sql, new UserRowMapper(false, false, false), id);
	}

	public  List<User> getProjectAccess(Long idProject, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
			
		String sql = "SELECT idUser FROM project_access WHERE idProject = ?";
		List<Long> idList = getJdbcTemplate().queryForList(sql, Long.class, idProject);
		
		if (isNotEmpty(idList)) {
			return getByIds(idList, loadAuthorities, loadProjects, loadMembers);
		}
		return new ArrayList<User>();
	}

	public  List<User> getProjectReviewers(Long idProject, boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		String sql = "SELECT idUser FROM project_reviewer WHERE idProject = ?";
		List<Long> idList = getJdbcTemplate().queryForList(sql, Long.class, idProject);
		
		if (isNotEmpty(idList)) {
			return getByIds(idList, loadAuthorities, loadProjects, loadMembers);
		}
		return new ArrayList<User>();
	}
	
	public User insertOrUpdate(final User user) throws DuplicateException {
		if (user == null) {
			throw new RuntimeException("User must not be empty.");
		}
		
		assertDuplicate(user.getId(), "user", false, new String[] {"username"}, new Object[] {user.getUsername()});
		
		Long id = null;
		final Date now = new Date();
		boolean isNew = (user.getId() == null);
		
		if (isNew) {
			final String sql = "INSERT INTO user " + 
							   "(createdAt, modifiedAt, username, password, firstname, lastname, email, phone, company, location, enabled) " + 
							   "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					int index = 0;
					setTimestamp(ps, ++index, now);
					setTimestamp(ps, ++index, now);
					setString(ps, ++index, user.getUsername());
					setString(ps, ++index, user.getPassword());
					setString(ps, ++index, user.getFirstname());
					setString(ps, ++index, user.getLastname());
					setString(ps, ++index, user.getEmail());
					setString(ps, ++index, user.getPhone());
					setString(ps, ++index, user.getCompany());
					setString(ps, ++index, user.getLocation());
					setBoolean(ps, ++index, user.getEnabled());
					return ps;
				}
			};
			boolean b = getJdbcTemplate().update(psc, keyHolder) == 1;
			if (b) {
				id = keyHolder.getKey().longValue();
			}
		} else {
			String sql = "UPDATE project SET modifiedAt = ?, username = ?, password = ?, firstname = ?, lastname = ?, " + 
						 "email = ?, phone = ?, company = ?, location = ?, enabled = ? WHERE id = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(now);
			params.add(user.getUsername());
			params.add(user.getPassword());
			params.add(user.getFirstname());
			params.add(user.getLastname());
			params.add(user.getEmail());
			params.add(user.getPhone());
			params.add(user.getCompany());
			params.add(user.getLocation());
			params.add(user.getEnabled());
			params.add(user.getId());
			boolean b = getJdbcTemplate().update(sql, params.toArray()) == 1;
			if (b) {
				id = user.getId();
			}
		}
		
		return (id != null ? getById(id, false, false, false) : null);
	}

	public boolean deleteById(Long id) throws EntityInUseException {
		if (id == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		assertInUse("project", "idOwner", id);
		assertInUse("team", "idUser", id);
		assertInUse("team", "idMember", id);
		assertInUse("project_access", "idUser", id);
		assertInUse("project_reviewer", "idUser", id);

		/** Delete user authorities */
		this.authorityDao.deleteByIdUser(id);
		
		assertInUse("authority", "idUser",id);
		
		String sql = "DELETE FROM user WHERE id = ?";
		return getJdbcTemplate().update(sql, id) > 0;
	}
	
	// ----------------------------------------------------------------- Helper
	
	private boolean assertPassword(String username, String password) {
		String sql = "SELECT password FROM user WHERE username = ?";
		try {
			String passwordInDatabase = getJdbcTemplate().queryForObject(sql, String.class, username);
			return (passwordInDatabase != null && passwordInDatabase.equals(password)); 
		} catch (EmptyResultDataAccessException ex) { }
		return false;
	}
	
	// ------------------------------------------------------------- Row mapper
	
	/**
	 * Default {@link RowMapper} for {@link User} 
	 */
	private class UserRowMapper implements RowMapper<User> {
		
		private boolean loadAuthorities;
		private boolean loadProjects;
		private boolean loadMembers;
		
		private UserRowMapper(boolean loadAuthorities, boolean loadProjects, boolean loadMembers) {
			super();
			this.loadAuthorities = loadAuthorities;
			this.loadProjects = loadProjects;
			this.loadMembers = loadMembers;
		}
		
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User entity = new User();
			entity.setId(getLong(rs, "id"));
			entity.setCreatedAt(getDate(rs, "createdAt"));
			entity.setModifiedAt(getDate(rs, "modifiedAt"));
			entity.setUsername(getString(rs, "username"));
			entity.setPassword(getString(rs, "password"));
			entity.setFirstname(getString(rs, "firstname"));
			entity.setLastname(getString(rs, "lastname"));
			entity.setEmail(getString(rs, "email"));
			entity.setPhone(getString(rs, "phone"));
			entity.setCompany(getString(rs, "company"));
			entity.setLocation(getString(rs, "location"));
			entity.setEnabled(getBoolean(rs, "enabled"));
			if (this.loadAuthorities) {
				entity.getAuthorities().addAuthorities(authorityDao.getByIdUser(entity.getId()));
			}
			if (this.loadProjects) {
				List<Project> userProjects = projectDao.getAccessProjects(entity.getId(), true, true, true);
				if (isNotEmpty(userProjects)) {
					entity.getUserProjects().addProjects(userProjects);
				}
				List<Project> reviewerProjects = projectDao.getReviewerProjects(entity.getId(), true, true, true);
				if (isNotEmpty(reviewerProjects)) {
					entity.getReviewerProjects().addProjects(reviewerProjects);
				}
			}
			if (this.loadMembers) {
				List<User> members = getMembersById(entity.getId(), false, false, false);
				if (isNotEmpty(members)) {
					entity.getMembers().addUsers(members);
				}
			}
			return entity;
		}
	};
}
