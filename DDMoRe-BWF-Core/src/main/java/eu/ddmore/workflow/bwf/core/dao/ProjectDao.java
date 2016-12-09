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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import eu.ddmore.workflow.bwf.client.enumeration.ProjectPriority;
import eu.ddmore.workflow.bwf.client.exception.DuplicateException;
import eu.ddmore.workflow.bwf.client.exception.EntityInUseException;
import eu.ddmore.workflow.bwf.client.model.Project;

@Repository
public class ProjectDao extends BaseDao {

	@Autowired private UserDao userDao;
	
	public List<Project> search(boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		String sql = "SELECT * FROM project";
		return getJdbcTemplate().query(sql, new ProjectRowMapper(loadOwner, loadAccess, loadReviewers));
	}
	
	public Project getById(Long id, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (id == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		try {
			String sql = "SELECT * FROM project WHERE id = ?";
			return getJdbcTemplate().queryForObject(sql, new ProjectRowMapper(loadOwner, loadAccess, loadReviewers), id);	
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<Project> getByIds(List<Long> idList, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (isEmpty(idList)) {
			return new ArrayList<Project>();
		}

		StringBuilder sb = new StringBuilder("SELECT * FROM project WHERE ");
		sb.append(getInClause(idList, "id"));

		return getJdbcTemplate().query(sb.toString(), new ProjectRowMapper(loadOwner, loadAccess, loadReviewers), idList.toArray());	
	}
	
	public Project getByProjectname(String projectname, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (isEmpty(projectname)) {
			throw new RuntimeException("Projectname must not be empty.");
		}
		
		try {
			String sql = "SELECT * FROM project WHERE name = ?";
			return getJdbcTemplate().queryForObject(sql, new ProjectRowMapper(loadOwner, loadAccess, loadReviewers), projectname);	
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}
	
	public String getGitUrlById(Long id) {
		if (id == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		try {
			String sql = "SELECT gitUrl FROM project WHERE id = ?";
			return getJdbcTemplate().queryForObject(sql, String.class, id);	
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}
	
	public String getGitUrlByProjectname(String projectname) {
		if (isEmpty(projectname)) {
			throw new RuntimeException("Projectname must not be empty.");
		}
		
		try {
			String sql = "SELECT gitUrl FROM project WHERE name = ?";
			return getJdbcTemplate().queryForObject(sql, String.class, projectname);	
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}
	
	public Project insertOrUpdate(final Project project) throws DuplicateException {
		if (project == null) {
			throw new RuntimeException("Project must not be empty.");
		}
		
		assertDuplicate(project.getId(), "project", false, new String[] {"name"}, new Object[] {project.getName()});
		
		Long id = null;
		final Date now = new Date();
		boolean isNew = (project.getId() == null);
		
		if (isNew) {
			final String sql = "INSERT INTO project (createdAt, modifiedAt, name, gitUrl, priority, idOwner) VALUES (?, ?, ?, ?, ?, ?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					int index = 0;
					setTimestamp(ps, ++index, now);
					setTimestamp(ps, ++index, now);
					setString(ps, ++index, project.getName());
					setString(ps, ++index, project.getGitUrl());
					setString(ps, ++index, project.getPriority().toString());
					setLong(ps, ++index, project.getIdOwner());
					return ps;
				}
			};
			boolean b = getJdbcTemplate().update(psc, keyHolder) == 1;
			if (b) {
				id = keyHolder.getKey().longValue();
			}
		} else {
			String sql = "UPDATE project SET modifiedAt = ?, name = ?, gitUrl = ?, priority = ? WHERE id = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(now);
			params.add(project.getName());
			params.add(project.getGitUrl());
			params.add(project.getPriority().toString());
			params.add(project.getId());
			boolean b = getJdbcTemplate().update(sql, params.toArray()) == 1;
			if (b) {
				id = project.getId();
			}
		}
		
		return (id != null ? getById(id, false, false, false) : null);
	}

	public boolean deleteById(Long id) throws EntityInUseException {
		if (id == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		assertInUse("project_user", "idProject", id);
		assertInUse("project_reviewer", "idProject", id);

		String sql = "DELETE FROM project WHERE id = ?";
		return getJdbcTemplate().update(sql, id) > 0;
	}
	
	public List<Project> getAccessProjects(Long idUser, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (idUser == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		List<Project> projects = new ArrayList<Project>();

		String sql = "SELECT * FROM project WHERE id IN (SELECT id FROM project WHERE idOwner = ?)";
		List<Project> ownerProjects = getJdbcTemplate().query(sql, new ProjectRowMapper(loadOwner, loadAccess, loadReviewers), idUser);
		
		List<Project> accessProjects = getAccessOrReviewerProjectsInternal(idUser, true, loadOwner, loadAccess, loadReviewers);
		
		if (isNotEmpty(ownerProjects)) {
			projects.addAll(ownerProjects);
		}

		if (isNotEmpty(accessProjects)) {
			projects.addAll(accessProjects);
		}
		
		return projects;
	}

	public List<Project> getReviewerProjects(Long idUser, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (idUser == null) {
			throw new RuntimeException("User id must not be empty.");
		}

		return getAccessOrReviewerProjectsInternal(idUser, false, loadOwner, loadAccess, loadReviewers);
	}
	
	private List<Project> getAccessOrReviewerProjectsInternal(Long idUser, boolean isAccess, boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
		if (idUser == null) {
			throw new RuntimeException("User id must not be empty.");
		}
		
		String tableName = (isAccess ? "project_access pa" : "project_reviewer pa");
		String sql = "SELECT p.* FROM project p INNER JOIN " + tableName + " ON p.id = pa.idProject WHERE pa.idUser = ?";
		return getJdbcTemplate().query(sql, new ProjectRowMapper(loadOwner, loadAccess, loadReviewers), idUser);
	}
	
	public int updateAccessProjects(Long idProject, List<Long> idUserList) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}

		return updateAccessOrReviewerProjectsInternal(idProject, idUserList, true);
	}

	public int updateReviewerProjects(Long idProject, List<Long> idUserList) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}

		return updateAccessOrReviewerProjectsInternal(idProject, idUserList, false);
	}
	
	private int updateAccessOrReviewerProjectsInternal(final Long idProject, final List<Long> idUserList, boolean isAccess) {
		if (idProject == null) {
			throw new RuntimeException("Project id must not be empty.");
		}
		
		String tableName = (isAccess ? "project_access" : "project_reviewer");
		
		/** 1st: Delete all assignments */
		String sqlDelete = "DELETE FROM " + tableName + " WHERE idProject = ?";
		getJdbcTemplate().update(sqlDelete, idProject);
		
		/** 2nd: Set new assignments */
		if (isNotEmpty(idUserList)) {
			String sqlInsert = "INSERT INTO " + tableName + " (idUser, idProject) VALUES (?, ?)";
			int[] updateCounts = getJdbcTemplate().batchUpdate(sqlInsert, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					setLong(ps, 1, idUserList.get(i));
					setLong(ps, 2, idProject);
				}
				@Override
				public int getBatchSize() {
					return idUserList.size();
				}
			});
			return getBatchUpdateCountFrom(updateCounts);
		}
		
		return 0;
	}
	
	// ------------------------------------------------------------- Row mapper
	
	/**
	 * Default {@link RowMapper} for {@link Project} 
	 */
	private class ProjectRowMapper implements RowMapper<Project> {
		
		private boolean loadOwner;
		private boolean loadAccess;
		private boolean loadReviewers;
		
		private ProjectRowMapper(boolean loadOwner, boolean loadAccess, boolean loadReviewers) {
			super();
			this.loadOwner = loadOwner;
			this.loadAccess = loadAccess;
			this.loadReviewers = loadReviewers;
		}
		
		@Override
		public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
			Project entity = new Project();
			entity.setId(getLong(rs, "id"));
			entity.setCreatedAt(getDate(rs, "createdAt"));
			entity.setModifiedAt(getDate(rs, "modifiedAt"));
			entity.setName(getString(rs, "name"));
			entity.setGitUrl(getString(rs, "gitUrl"));
			entity.setPriority(ProjectPriority.valueOf(getString(rs, "priority")));
			entity.setIdOwner(getLong(rs, "idOwner"));
			if (this.loadOwner) {
				entity.setOwner(userDao.getById(entity.getIdOwner(), false, false, false));
			}
			if (this.loadAccess) {
				entity.getAccess().addUsers(userDao.getProjectAccess(entity.getId(), false, false, false));
			}
			if (this.loadReviewers) {
				entity.getReviewers().addUsers(userDao.getProjectReviewers(entity.getId(), false, false, false));
			}
			return entity;
		}
	};
}
