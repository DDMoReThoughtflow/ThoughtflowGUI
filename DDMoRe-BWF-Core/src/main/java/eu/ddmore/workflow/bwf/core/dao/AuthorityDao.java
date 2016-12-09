package eu.ddmore.workflow.bwf.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import eu.ddmore.workflow.bwf.client.enumeration.ApplicationRole;
import eu.ddmore.workflow.bwf.client.model.Authority;

@Repository
public class AuthorityDao extends BaseDao {

	public List<Authority> getByIdUser(Long idUser) {
		if (idUser == null) {
			throw new RuntimeException("User id must not be null.");
		}
		
		String sql = "SELECT * FROM authority WHERE idUser = ?";
		return getJdbcTemplate().query(sql, new AuthorityRowMapper(), idUser);
	}

	public int deleteByIdUser(Long idUser) {
		if (idUser == null) {
			throw new RuntimeException("User id must not be null.");
		}
		
		String sql = "DELETE FROM authority WHERE idUser = ?";
		return getJdbcTemplate().update(sql, idUser);
	}

	// ------------------------------------------------------------- Row mapper
	
	/**
	 * Default {@link RowMapper} for {@link Authority} 
	 */
	private class AuthorityRowMapper implements RowMapper<Authority> {
		@Override
		public Authority mapRow(ResultSet rs, int rowNum) throws SQLException {
			Authority entity = new Authority();
			entity.setId(getLong(rs, "id"));
			entity.setRole(ApplicationRole.valueOf(getString(rs, "role")));
			entity.setIdUser(getLong(rs, "idUser"));
			return entity;
		};
	}
}
