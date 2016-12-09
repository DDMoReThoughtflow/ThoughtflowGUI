package eu.ddmore.workflow.bwf.client.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.ddmore.workflow.bwf.client.util.Primitives;

public class EntityInUseException extends RestTechException {

	private static final long serialVersionUID = 1L;

	private List<Object> columnValues;
	private String tableName;
	private String columnName;
	
	public EntityInUseException(Object columnValue, String tableName, String columnName) {
		super();
		this.columnValues = new ArrayList<Object>();
		if (columnValue != null) {
			this.columnValues.add(columnValue);
		}
		this.tableName = tableName;
		this.columnName = columnName;
	}
	
	public EntityInUseException(Object columnValue, String tableName, String columnName, String message) {
		super();
		this.columnValues = new ArrayList<Object>();
		if (columnValue != null) {
			this.columnValues.add(columnValue);
		}
		this.tableName = tableName;
		this.columnName = columnName;
	}
	
	public EntityInUseException(List<Object> columnValues, String tableName, String columnName) {
		super();
		this.columnValues = new ArrayList<Object>();
		if (Primitives.isNotEmpty(columnValues)) {
			this.columnValues.addAll(columnValues);
		}
		this.tableName = tableName;
		this.columnName = columnName;
	}

	public EntityInUseException(List<Long> columnValues, String tableName, String columnName, String message) {
		super(message);
		this.columnValues = new ArrayList<Object>();
		if (Primitives.isNotEmpty(columnValues)) {
			this.columnValues.addAll(columnValues);
		}
		this.tableName = tableName;
		this.columnName = columnName;
	}
	
	public List<Object> getColumnValues() {
		return this.columnValues;
	}

	public String getTableName() {
		return this.tableName;
	}

	public String getColumnName() {
		return this.columnName;
	}

	@Override
	public String getMessage() {
		if (super.getMessage() != null) {
			return super.getMessage();
		}
		StringBuilder builder = new StringBuilder();
		if (getColumnValues() != null && !getColumnValues().isEmpty()) {
			for (Iterator<Object> it = getColumnValues().iterator(); it.hasNext();) {
				builder.append(it.next());
				if (it.hasNext()) {
					builder.append(", ");
				}
			}
		}
		return "Entity referenced by others for table '" + getTableName() + "' and column '" + getColumnName() + "': Values are '" + builder.toString() + "'";
	}
}
