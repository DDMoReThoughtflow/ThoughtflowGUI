package eu.ddmore.workflow.bwf.client.exception;

public class DuplicateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String tableName;
	private String[] columnNames;
	private boolean combine;
	
	public DuplicateException(Long id, String tableName, String[] columnNames, boolean combine) {
		super();
		this.id = id;
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.combine = combine;
	}

	public DuplicateException(Long id, String tableName, String[] columnNames, boolean combine, String message) {
		super(message);
		this.id = id;
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.combine = combine;
	}

	public Long getId() {
		return this.id;
	}

	public String getTableName() {
		return this.tableName;
	}

	public String[] getColumnNames() {
		return this.columnNames;
	}

	public boolean isCombine() {
		return this.combine;
	}
	
	@Override
	public String getMessage() {
		if (super.getMessage() != null) {
			return super.getMessage();
		}
		StringBuilder builder = new StringBuilder();
		if (getColumnNames() != null && getColumnNames().length > 0) {
			for (int i = 0; i < getColumnNames().length; i++) {
				builder.append(getColumnNames()[i]);
				if ((i+1) < getColumnNames().length) {
					builder.append(", ");
				}
			}
		}
		return "Unique constraints violated for table '" + getTableName() + "'" + 
			   (getId() != null ? (" and an existing entity with id '" + getId() + "'") : " and a new entity") + 
			   ": Columns are '" + builder.toString() + "' with combine=" + isCombine();
	}
}
