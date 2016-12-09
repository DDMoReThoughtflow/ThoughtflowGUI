package eu.ddmore.workflow.bwf.core.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import eu.ddmore.workflow.bwf.client.exception.DuplicateException;
import eu.ddmore.workflow.bwf.client.exception.EntityInUseException;
import eu.ddmore.workflow.bwf.client.util.Primitives;

public class BaseDao extends JdbcDaoSupport {

	private static final int IN_CLAUSE_LIMIT = 1000;
	
	private Logger log;
	
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	@PostConstruct
	public void init() {
		super.setDataSource(this.dataSource);
		/** Upgrade fetch size: Fetch size should be based on VM heap memory setting */
		getJdbcTemplate().setFetchSize(1000);
	}
	
	// ----------------------------------------------------------------- Helper
	
	protected String getString(ResultSet rs, String field) throws SQLException {
		return getString(rs, field, null);
	}
	
	protected String getString(ResultSet rs, String field, String defaultValue) throws SQLException {
		String value = rs.getString(field);
		return !rs.wasNull() ? value : defaultValue; 
	}

	protected void setString(PreparedStatement ps, int parameterIndex, String value) throws SQLException {
		setString(ps, parameterIndex, value, null);
	}

	protected void setString(PreparedStatement ps, int parameterIndex, String value, String defaultValue) throws SQLException {
		String s = value != null ? value : defaultValue;
		if (s != null) {
			ps.setString(parameterIndex, s);
		} else {
			ps.setNull(parameterIndex, java.sql.Types.VARCHAR);
		}
	}

	protected Integer getInteger(ResultSet rs, String field) throws SQLException {
		return getInteger(rs, field, null);
	}
	
	protected Integer getInteger(ResultSet rs, String field, Integer defaultValue) throws SQLException {
		Integer value = rs.getInt(field);
		return !rs.wasNull() ? value : defaultValue; 
	}

	protected void setInteger(PreparedStatement ps, int parameterIndex, Integer value) throws SQLException {
		setInteger(ps, parameterIndex, value, null);
	}

	protected void setInteger(PreparedStatement ps, int parameterIndex, Integer value, Integer defaultValue) throws SQLException {
		Integer i = value != null ? value : defaultValue;
		if (i != null) {
			ps.setInt(parameterIndex, i);
		} else {
			ps.setNull(parameterIndex, java.sql.Types.INTEGER);
		}
	}

	protected Long getLong(ResultSet rs, String field) throws SQLException {
		return getLong(rs, field, null);
	}
	
	protected Long getLong(ResultSet rs, String field, Long defaultValue) throws SQLException {
		Long value = rs.getLong(field);
		return !rs.wasNull() ? value : defaultValue;
	}

	protected void setLong(PreparedStatement ps, int parameterIndex, Long value) throws SQLException {
		setLong(ps, parameterIndex, value, null);
	}

	protected void setLong(PreparedStatement ps, int parameterIndex, Long value, Long defaultValue) throws SQLException {
		Long l = value != null ? value : defaultValue;
		if (l != null) {
			ps.setLong(parameterIndex, l);
		} else {
			ps.setNull(parameterIndex, java.sql.Types.BIGINT);
		}
	}

	protected Double getDouble(ResultSet rs, String field) throws SQLException {
		return getDouble(rs, field, null);
	}
	
	protected Double getDouble(ResultSet rs, String field, Double defaultValue) throws SQLException {
		Double value = rs.getDouble(field);
		return !rs.wasNull() ? value : defaultValue;
	}

	protected void setDouble(PreparedStatement ps, int parameterIndex, Double value) throws SQLException {
		setDouble(ps, parameterIndex, value, null);
	}

	protected void setDouble(PreparedStatement ps, int parameterIndex, Double value, Double defaultValue) throws SQLException {
		Double f = value != null ? value : defaultValue;
		if (f != null) {
			ps.setDouble(parameterIndex, f);
		} else {
			ps.setNull(parameterIndex, java.sql.Types.DOUBLE);
		}
	}

	protected Float getFloat(ResultSet rs, String field) throws SQLException {
		return getFloat(rs, field, null);
	}
	
	protected Float getFloat(ResultSet rs, String field, Float defaultValue) throws SQLException {
		Float value = rs.getFloat(field);
		return !rs.wasNull() ? value : defaultValue;
	}

	protected void setFloat(PreparedStatement ps, int parameterIndex, Float value) throws SQLException {
		setFloat(ps, parameterIndex, value, null);
	}

	protected void setFloat(PreparedStatement ps, int parameterIndex, Float value, Float defaultValue) throws SQLException {
		Float f = value != null ? value : defaultValue;
		if (f != null) {
			ps.setFloat(parameterIndex, f);
		} else {
			ps.setNull(parameterIndex, java.sql.Types.FLOAT);
		}
	}

	protected Boolean getBoolean(ResultSet rs, String field) throws SQLException {
		return getBoolean(rs, field, null);
	}
	
	protected Boolean getBoolean(ResultSet rs, String field, Boolean defaultValue) throws SQLException {
		Boolean value = rs.getBoolean(field);
		return !rs.wasNull() ? value : defaultValue;
	}

	protected void setBoolean(PreparedStatement ps, int parameterIndex, Boolean value) throws SQLException {
		setBoolean(ps, parameterIndex, value, null);
	}

	protected void setBoolean(PreparedStatement ps, int parameterIndex, Boolean value, Boolean defaultValue) throws SQLException {
		Boolean b = value != null ? value : defaultValue;
		if (b != null) {
			ps.setBoolean(parameterIndex, b);
		} else {
			ps.setNull(parameterIndex, java.sql.Types.BOOLEAN);
		}
	}
	
	protected Date getDate(ResultSet rs, String field) throws SQLException {
		return getDate(rs, field, null);
	}

	protected Date getDate(ResultSet rs, String field, Date defaultValue) throws SQLException {
		Timestamp timestamp = rs.getTimestamp(field);
		return timestamp != null ? new Date(timestamp.getTime()) : defaultValue;
	}

	protected void setTimestamp(PreparedStatement ps, int parameterIndex, Date date) throws SQLException {
		setTimestamp(ps, parameterIndex, date, null);
	}

	protected void setTimestamp(PreparedStatement ps, int parameterIndex, Date date, Date defaultDate) throws SQLException {
		Date d = date != null ? date : defaultDate;
		if (d != null) {
			ps.setTimestamp(parameterIndex, new Timestamp(d.getTime()));
		} else {
			ps.setNull(parameterIndex, java.sql.Types.TIMESTAMP);
		}
	}
	
	protected Object getObject(ResultSet rs, String field) throws SQLException {
		return getObject(rs, field, null);
	}
	
	protected Object getObject(ResultSet rs, String field, Object defaultValue) throws SQLException {
		Object value = rs.getObject(field);
		return !rs.wasNull() ? value : defaultValue;
	}

	protected void setObject(PreparedStatement ps, int parameterIndex, Object value) throws SQLException {
		setObject(ps, parameterIndex, value, null);
	}

	protected void setObject(PreparedStatement ps, int parameterIndex, Object value, Object defaultValue) throws SQLException {
		Object o = value != null ? value : defaultValue;
		if (o != null) {
			ps.setObject(parameterIndex, o);
		} else {
			ps.setNull(parameterIndex, java.sql.Types.NULL);
		}
	}
	
	protected boolean checkTableExists(String tableName) {
		try {
			getJdbcTemplate().update("DESCRIBE " + tableName);
		} catch (DataAccessException e) {
			return false;
		}
		return true;
	}

	protected SimpleEntry<String, List<Object>> buildWhereClause(String[] columnNames, Object[] values, boolean addWhere) {
		return buildWhereClause(columnNames, values, addWhere, "");
	}
	
	protected SimpleEntry<String, List<Object>> buildWhereClause(String[] columnNames, Object[] values, boolean addWhere, String tableAliasPrefix) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		
		if (addWhere) {
			sb.append(" WHERE ");
		}
		
		boolean appended = false;
		for (int i = 0; i < columnNames.length; i++) {
			Object value = values[i]; 
			if (value == null) {
				continue;
			}
			if (appended) {
				sb.append(" AND ");
			}
			sb.append(tableAliasPrefix).append(columnNames[i]).append(" = ?");
			params.add(value);
			appended = true;
		}
		
		return new SimpleEntry<String, List<Object>>((appended ? sb.toString() : ""), params);
	}
	
	protected String getInClause(List<?> values, String field) {
		if (isEmpty(values)) {
			return "";
		}
		
		int index = 0;
		StringBuilder sb = new StringBuilder();
		if (field != null) {
			sb.append(field).append(" ");
		}
		sb.append("IN (");
		for (Iterator<?> it = values.iterator(); it.hasNext();) {
			it.next();
			sb.append("?");
			index++;
			if ((index % IN_CLAUSE_LIMIT) == 0) {
				sb.append(")");
				if (it.hasNext()) {
					sb.append(" OR ").append(field).append(" IN (");
				}
			} else if (it.hasNext()) {
				sb.append(",");
			}
		}
		if (sb.charAt(sb.length()-1) == '?') {
			sb.append(")");
		}
		return sb.toString();
	}
	
	protected int getBatchUpdateCountFrom(int[] updateCounts) {
		int count = 0;
		if (updateCounts != null && updateCounts.length > 0) {
			for (int c : updateCounts) {
				if (c > 0) {
					count += c;	
				} else if (c == -2) {
					count++;
				}
			}
		}
		return count;
	}

	protected void assertDuplicate(Long id, String tableName, String[] columnNames, Object[] values) throws DuplicateException {
		assertDuplicate(id, tableName, false, columnNames, values);
	}
	
	protected void assertDuplicate(Long id, String idColumnName, String tableName, String[] columnNames, Object[] values) throws DuplicateException {
		assertDuplicate(id, idColumnName, tableName, false, columnNames, values);
	}
	
	protected void assertDuplicate(Long id, String tableName, boolean combine, String[] columnNames, Object[] values) throws DuplicateException {
		assertDuplicate(id, "id", tableName, combine, columnNames, values);
	}
	
	protected void assertDuplicate(Long id, String idColumnName, String tableName, boolean combine, String[] columnNames, Object[] values) throws DuplicateException {
		boolean isNew = (id == null);
		List<String> duplicates = new ArrayList<String>();
		
		if (!combine) {
			for (int i = 0; i < columnNames.length; i++) {
				boolean duplicate = false;
				if (values[i] == null) {
					continue;
				}
				String sql = "SELECT count(0) FROM " + tableName + " WHERE " + columnNames[i] + " = ?";
				int count = getJdbcTemplate().queryForObject(sql, Integer.class, values[i]);
				if (isNew && count > 0) {
					duplicate = true;
				} else if (!isNew && count > 0) {
					/** Value in update entity */
					String sqlWithId = "SELECT count(0) FROM " + tableName + " WHERE " + columnNames[i] + " = ? AND id = ?";
					int countWithId = getJdbcTemplate().queryForObject(sqlWithId, Integer.class, values[i], id);
					if (countWithId == 0) {
						duplicate = true;
					}
				}
				if (duplicate) {
					if (!columnNames[i].equals("idUserGroup") && !columnNames[i].equals("idUser")) {
						duplicates.add(columnNames[i]);
					}
				}			
			}
		} else {
			boolean duplicate = false;
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT count(0) FROM ").append(tableName);
			SimpleEntry<String, List<Object>> filter = buildWhereClause(columnNames, values, true);
			builder.append(filter.getKey());
			List<Object> parameters = new ArrayList<Object>();
			parameters.addAll(filter.getValue());
			int count = getJdbcTemplate().queryForObject(builder.toString(), Integer.class, parameters.toArray());
			if (isNew && count > 0) {
				duplicate = true;
			} else if (!isNew && count > 0) {
				/** Value in update entity */
				parameters.add(id);
				builder.append(" AND id = ?");
				int countWithId = getJdbcTemplate().queryForObject(builder.toString(), Integer.class, parameters.toArray());
				if (countWithId == 0) {
					duplicate = true;
				}
			}
			if (duplicate) {
				for (String columnName : columnNames) {
					if (columnName.equals("idUserGroup") || columnName.equals("idUser")) {
						continue;
					}
					duplicates.add(columnName);					
				}
			}
		}
		
		if (!duplicates.isEmpty()) {
			throw new DuplicateException(id, tableName, duplicates.toArray(new String[0]), combine);
		}
	}
	
	protected void assertInUse(String tableName, String columnName, Object columnValue) throws EntityInUseException {
		if (columnValue == null) {
			return;
		}
		
		StringBuilder sb = new StringBuilder("SELECT count(0) FROM ");
		sb.append(tableName).append(" WHERE ").append(columnName).append(" = ?");
		
		if (getJdbcTemplate().queryForObject(sb.toString(), Integer.class, columnValue) > 0) {
			throw new EntityInUseException(columnValue, tableName, columnName);
		}
	}
	
	protected void assertInUse(String tableName, String columnName, List<Object> columnValues) throws EntityInUseException {
		if (isEmpty(columnValues)) {
			return;
		}
		
		StringBuilder sb = new StringBuilder("SELECT count(0) FROM ");
		sb.append(tableName).append(" WHERE ");
		sb.append(getInClause(columnValues, columnName));
		
		if (getJdbcTemplate().queryForObject(sb.toString(), Integer.class, columnValues.toArray()) > 0) {
			throw new EntityInUseException(columnValues, tableName, columnName);
		}
	}
	
	// ------------------------------------------------------------- Assertions
	
	protected boolean isEmpty(String value) {
		return Primitives.isEmpty(value);
	}

	protected boolean isNotEmpty(String value) {
		return Primitives.isNotEmpty(value);
	}
	
	public static boolean isEmpty(boolean all, String... values) {
		return Primitives.isEmpty(all, values);
	}

	public static boolean isNotEmpty(boolean all, String... values) {
		return Primitives.isNotEmpty(all, values);
	}
	
	protected boolean isEmpty(List<?> list) {
		return Primitives.isEmpty(list);
	}

	protected boolean isNotEmpty(List<?> list) {
		return Primitives.isNotEmpty(list);
	}

	protected boolean isEmpty(boolean all, List<?>... lists) {
		return Primitives.isEmpty(all, lists);
	}

	protected boolean isNotEmpty(boolean all, List<?>... lists) {
		return Primitives.isNotEmpty(all, lists);
	}
	
	protected boolean isEmpty(Set<?> set) {
		return !Primitives.assertSet(set);
	}

	protected boolean isNotEmpty(Set<?> set) {
		return !isEmpty(set);
	}
	
	protected boolean isEmpty(Map<?,?> map) {
		return !Primitives.assertMap(map);
	}

	protected boolean isNotEmpty(Map<?,?> map) {
		return !isEmpty(map);
	}
	
	// --------------------------------------------------------- Getter, Setter
	
	protected Logger getLog() {
		if (this.log == null) {
			this.log = LoggerFactory.getLogger(this.getClass());
		}
		return this.log;
	}
}
