package eu.ddmore.workflow.bwf.web.util;

public class Constants {

	public static final String DEFAULT_LOCALE    = "en";
	
	public static final String SESSION_PROP_USER 			   = "user";
	public static final String SESSION_PROP_ID_PROJECT 		   = "ID_PROJECT";
	public static final String SESSION_PROP_PROJECT_PAGE_STAGE = "SESSION_PROP_PROJECT_PAGE_STAGE";
	
	public static final String EXTENSION_CSV   = "csv";
	public static final String EXTENSION_PDF   = "pdf";
	public static final String EXTENSION_WORD  = "docx";
	public static final String EXTENSION_EXCEL = "xlsx";
	public static final String EXTENSION_JPG   = "jpg";
	public static final String EXTENSION_ZIP   = "zip";
	
	public static final String CONTENT_TYPE_CSV   = "application/csv";
	public static final String CONTENT_TYPE_PDF   = "application/pdf";
	public static final String CONTENT_TYPE_WORD  = "application/application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public static final String CONTENT_TYPE_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String CONTENT_TYPE_JPEG  = "image/jpeg";
	public static final String CONTENT_TYPE_ZIP   = "application/zip";
	
	public static final Integer AUDIT_TRAIL_DEFAULT_DEPTH = 2;
	
	private Constants() {
		super();
	}
}
