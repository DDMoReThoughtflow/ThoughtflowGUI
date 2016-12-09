package eu.ddmore.workflow.bwf.client.util;

public class Constants {

	public static final String UTF_8 = "UTF-8";
	
	public static final String CR_LF = "\r\n";
	public static final String EMPTY = "";
	public static final String COMMA = ",";
	public static final String PIPE  = "|";
	
	public static final char AMPERSAND 		   = '&';
	public static final char PIPE_CHAR		   = '|';
	public static final char COMMA_CHAR		   = ',';
	public static final char SEMICOLON_CHAR	   = ';';
	public static final char CR_CHAR		   = '\r';
	public static final char LF_CHAR 	       = '\n';
	public static final char TAB_CHAR 		   = '\t';
	public static final char DOUBLE_QUOTE_CHAR = '"';

	public static final String PREFIX_DDMORE     = "ddmore";
	public static final String PREFIX_REPO       = "repo";
	public static final String PREFIX_REPO_COLON = (PREFIX_REPO + ":");
	
	public static final String PROVO_LOCAL_PART_QC_STATUS   = "qcStatus";
	public static final String PROVO_LOCAL_PART_BASE 	    = "base";
	public static final String PROVO_LOCAL_PART_FINAL       = "final";
	public static final String PROVO_LOCAL_PART_PIVOTAL     = "pivotal";
	public static final String PROVO_LOCAL_PART_DESCRIPTION = "description";
	public static final String PROVO_LOCAL_PART_NOTE   	    = "note";
	
	private Constants() {
		super();
	}
}
