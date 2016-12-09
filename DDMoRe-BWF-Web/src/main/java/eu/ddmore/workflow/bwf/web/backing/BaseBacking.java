package eu.ddmore.workflow.bwf.web.backing;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ddmore.workflow.bwf.client.model.User;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.web.bean.SessionBean;
import eu.ddmore.workflow.bwf.web.bean.WebProperties;
import eu.ddmore.workflow.bwf.web.enumeration.RelationalFilterOperator;
import eu.ddmore.workflow.bwf.web.util.Constants;
import eu.ddmore.workflow.bwf.web.util.FacesUtil;
import eu.ddmore.workflow.bwf.web.util.MessageProvider;

public class BaseBacking implements Serializable {

	private static final long serialVersionUID = 1L;

	private Logger log;
	
	@ManagedProperty(value="#{sessionBean}")
	private SessionBean sessionBean;

	@ManagedProperty(value="#{webProperties}")
	private WebProperties webProperties;

	// ------------------------------------------------------------ Application
	
	public User getUser() {
		return getSessionBean().getUser();
	}

	public Long getIdUser() {
		return getUser().getId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}

	public String getFullName() {
		return getUser().getFullName();
	}

	// -------------------------------------------------------------------- JSF

	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	protected ExternalContext getExternalContext() {
		return getFacesContext().getExternalContext();
	}
	
	protected void redirect(String redirectPage) {
		FacesUtil.doRedirect(getFacesContext(), redirectPage);	
	}
	
	protected UIComponent findUIComponent(String compId) {
		FacesContext context = getFacesContext();
		List<UIComponent> components = context.getViewRoot().getChildren();
		for (UIComponent currentComp : components) {
			UIComponent comp = findUIComponent(currentComp, compId, context);
			if (comp != null) {
				return comp;
			}
		}
		return null;
	}

	protected UIComponent findUIComponent(UIComponent component, String compId, FacesContext context) {
		List<UIComponent> components = component.getChildren();
		if (components != null) {
			for (UIComponent comp : components) {
				if (comp.getClientId(context).endsWith(compId)) {
					return comp;
				} else if (comp.getChildCount() > 0) {
					UIComponent tmpComp = findUIComponent(comp, compId, context);
					if (tmpComp != null) {
						return tmpComp;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Adds a message to the UI component with a given component id.
	 * 
	 * @param compId The UI id to add the message.
	 * @param message The summery and detail for the message.
	 */	
	protected void addMessageToComp(String compId, FacesMessage.Severity severity, String summary, String detail) {
		// Find Component
		FacesContext context = getFacesContext();
		List<UIComponent> components = context.getViewRoot().getChildren();
		for (UIComponent currentComp : components) {
			UIComponent comp = findUIComponent(currentComp, compId, context);
			if (comp != null) {
			 	FacesMessage message = new FacesMessage();
			 	message.setSeverity(severity);
			 	message.setSummary(summary);
			 	message.setDetail(detail);
			 	context.addMessage(comp.getClientId(context), message);
			 	// Component fount - leave
			 	break;
			}
		}
	}

	/**
	 * Retuns the request paramter for the current page request with the given key.
	 * 
	 * @param parameter The name for the request parameter to search.
	 * @return The request parameter
	 */
	protected String getParameter(String key) {
		return getExternalContext().getRequestParameterMap().get(key);
	}

	protected void addMessage(String id, FacesMessage.Severity severity, String summary, String detail) {
		getFacesContext().addMessage(id, new FacesMessage(severity, summary, detail));
	}
	
	protected void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		getFacesContext().addMessage(null, new FacesMessage(severity, summary, detail));
	}
	
	protected void addInfoMessage(String id, String summary, String detail) {
		addInfoMessage(id, summary, detail, false, (String[])null);
	}
	
	protected void addInfoMessage(String id, String summary, String detail, boolean i18n, String... placeholder) {
		String summaryString = i18n ? MessageProvider.getValue(summary, placeholder) : summary;
		String detailString = i18n ? MessageProvider.getValue(detail, placeholder) : detail;
		addMessage(id, FacesMessage.SEVERITY_INFO, summaryString, detailString);
	}

	protected void addWarnMessage(String id, String summary, String detail) {
		addWarnMessage(id, summary, detail, false, (String[])null);
	}
	
	protected void addWarnMessage(String id, String summary, String detail, boolean i18n, String... placeholder) {
		String summaryString = i18n ? MessageProvider.getValue(summary, placeholder) : summary;
		String detailString = i18n ? MessageProvider.getValue(detail, placeholder) : detail;
		addMessage(id, FacesMessage.SEVERITY_WARN, summaryString, detailString);
	}

	protected void addErrorMessage(String id, String summary, String detail) {
		addErrorMessage(id, summary, detail, false, (String[])null);
	}
	
	protected void addErrorMessage(String id, String summary, String detail, boolean i18n, String... placeholder) {
		String summaryString = i18n ? MessageProvider.getValue(summary, placeholder) : summary;
		String detailString = i18n ? MessageProvider.getValue(detail, placeholder) : detail;
		addMessage(id, FacesMessage.SEVERITY_ERROR, summaryString, detailString);
	}
	
	protected void addFatalMessage(String id, String summary, String detail) {
		addFatalMessage(id, summary, detail, false, (String[])null);
	}
	
	protected void addFatalMessage(String id, String summary, String detail, boolean i18n, String... placeholder) {
		String summaryString = i18n ? MessageProvider.getValue(summary, placeholder) : summary;
		String detailString = i18n ? MessageProvider.getValue(detail, placeholder) : detail;
		addMessage(id, FacesMessage.SEVERITY_FATAL, summaryString, detailString);
	}
	
	protected void addRequiredErrorMessage(String id, String fieldName, boolean i18n) {
		String fieldMessageString = i18n ? msgs(fieldName) : fieldName;
		addErrorMessage(id, msgs("error_summary_error"), "'" + fieldMessageString + "' " + msgs("error_isRequird"));
	}
	
	// ------------------------------------------------------------- Primefaces
	
	protected RequestContext getRequestContext() {
		return RequestContext.getCurrentInstance();	
	}
	
	protected void alert(String message) {
        RequestContext rc = getRequestContext();
    	if (rc != null) {
    		rc.execute("alert('" + message + "');");
    	}
    }

	protected void callJavaScript(String script) {
    	executeJavaScript(script);
    }

	protected void executeJavaScript(String script) {
        RequestContext rc = getRequestContext();
    	if (rc != null) {
    		rc.execute(script);
    	}
    }
    
	protected void updateComp(String compId) {
        RequestContext rc = getRequestContext();
    	if (rc != null) {
    		rc.update(compId);
    	}	
    }
	
	protected void showInfoMessageInDialog(String summary, String detail) {
		showMessageInDialog(FacesMessage.SEVERITY_INFO, summary, detail);
	}
	
	protected void showWarnMessageInDialog(String summary, String detail) {
		showMessageInDialog(FacesMessage.SEVERITY_WARN, summary, detail);
	}

	protected void showErrorMessageInDialog(String summary, String detail) {
		showMessageInDialog(FacesMessage.SEVERITY_ERROR, summary, detail);
	}

	protected void showMessageInDialog(FacesMessage.Severity severity, String summary, String detail) {
        RequestContext rc = getRequestContext();
    	if (rc != null) {
	    	FacesMessage message = new FacesMessage(severity, summary, detail);
	        rc.showMessageInDialog(message);
    	}
    }
	
	protected void showInfoMessageInDialogWithOk(String summary, String detail) {
		showMessageInDialogWithOk(FacesMessage.SEVERITY_INFO, summary, detail);
	}
	
	protected void showWarnMessageInDialogWithOk(String summary, String detail) {
		showMessageInDialogWithOk(FacesMessage.SEVERITY_WARN, summary, detail);
	}

	protected void showErrorMessageInDialogWithOk(String summary, String detail) {
		showMessageInDialogWithOk(FacesMessage.SEVERITY_ERROR, summary, detail);
	}

	protected void showMessageInDialogWithOk(FacesMessage.Severity severity, String summary, String detail) {
        RequestContext rc = getRequestContext();
    	if (rc != null) {
            rc.execute("showMessageInDialogWithOk({severity:'" + severity + "',summary:'" + summary + "',detail:'" + detail + "'});"); 
    	}
    }
	
	protected void showOverlay(String varDialog) {
		executeJavaScript("PF('" + varDialog + "').show();");
	}

	protected void hideOverlay(String varDialog) {
		executeJavaScript("PF('" + varDialog + "').hide();");
	}

    // ---------------------------------------- Filter primefaces table columns
    
	public boolean filterByInteger(Object value, Object filter, Locale locale) {
		return filterInteger((Integer)value, (String)filter);
	}

	private boolean filterInteger(Integer value, String filter) {
		if (value == null) {
			return true;
		}
		return filterDouble((double)value, filter);
	}
	
	public boolean filterByLong(Object value, Object filter, Locale locale) {
		return filterLong((Long)value, (String)filter);
	}

	private boolean filterLong(Long value, String filter) {
		if (value == null) {
			return true;
		}
		return filterDouble((double)value, filter);
	}
	
	public boolean filterByDouble(Object value, Object filter, Locale locale) {
		return filterDouble((Double)value, (String)filter);
	}

	private boolean filterDouble(Double value, String filter) {
		if (value == null) {
			return true;
		}
		
		RelationalFilterOperator operator = RelationalFilterOperator.getRelationalOperator(filter);
		if (operator == null) {
			return true;
		}
		
		try {
			String filterValueString = filter.replaceAll(" ", "").substring(operator.getValue().length()).replaceAll(",", ".");
			if (filterValueString.length() == 0) {
				return true;
			} else {
				double filterValue = Double.parseDouble(filterValueString);
				switch (operator) {
					case GREATER:
						return value > filterValue;
					case SMALLER:
						return value < filterValue;
					case GREATER_EQUALS:
						return value >= filterValue;
					case SMALLER_EQUALS:
						return value <= filterValue;
					case EQUALS:
						return value == filterValue;
					case EQUALS_EMPTY:
						return value == filterValue;
				}
				return true;
			}
		} catch (Exception ex) {
			return true;
		}
	}
	
	public boolean filterByDate(Object value, Object filter, Locale locale) {
		return filterDate((Date)value, (String)filter, msgs("pattern_date"));
	}

	public boolean filterByDateTime(Object value, Object filter, Locale locale) {
		return filterDate((Date)value, (String)filter, msgs("pattern_dateTime"));
	}
	
	private boolean filterDate(Date value, String filter, String pattern) {
		if (value == null) {
			return true;
		}
		
		RelationalFilterOperator operator = RelationalFilterOperator.getRelationalOperator(filter);
		if (operator == null) {
			return true;
		}
		
		try {
			String filterValueString = filter.substring(operator.getValue().length());
			if (filterValueString.length() == 0) {
				return true;
			} else {
				Date filterValue = new SimpleDateFormat(pattern).parse(filterValueString);
				switch (operator) {
					case GREATER:
						return value.after(filterValue);
					case SMALLER:
						return value.before(filterValue);
					case GREATER_EQUALS:
						return value.after(filterValue) || value.equals(filterValue);
					case SMALLER_EQUALS:
						return value.before(filterValue) || value.equals(filterValue);
					case EQUALS:
						return value.equals(filterValue);
					case EQUALS_EMPTY:
						return value.equals(filterValue);
				}
				return true;
			}
		} catch (Exception ex) {
			return true;
		}
	}   
	
	public boolean filterByBoolean(Object value, Object filter, Locale locale) {
		return filterBoolean((Boolean)value, (String)filter);
	}

	private boolean filterBoolean(Boolean value, String filter) {
		if (value == null || isEmpty(filter)) {
			return true;
		}
		
		String f = filter.toLowerCase();
		
		if (value) {
			return f.startsWith("j") || f.startsWith("ja") || f.startsWith("y") | f.startsWith("ye") || f.startsWith("yes");
		} else {
			return f.startsWith("n") || f.startsWith("ne") || f.startsWith("nei") | f.startsWith("nein") || f.startsWith("n") || f.startsWith("no");
		}
	}
	
	// ----------------------------------------------------------------- Helper
	
	protected String msgs(String key) {
		return MessageProvider.getValue(key);
	}
	
	protected String msgs(String key, String... placeholder) {
		return MessageProvider.getValue(key, placeholder);
	}

	/**
	 * Retuns the current page name as String.
	 * @return The current page name.
	 */
	protected String getPage() {
		String viewId = getFacesContext().getViewRoot().getViewId();
		int pos = viewId.lastIndexOf("/");
		if (pos != -1) {
			viewId = viewId.substring(pos + 1);
		}
		pos = viewId.lastIndexOf(".");
		if (pos != -1) {
			viewId = viewId.substring(0, pos);
		}
		return viewId;
	}
	
	protected void download(String contentType, String filenname, ByteArrayOutputStream out) throws Exception {
		download(contentType, filenname, out.toByteArray());
	}
	
	protected void download(String contentType, String filenname, byte[] data) throws Exception {
		String fullFilename = filenname;
		if (Constants.CONTENT_TYPE_CSV.equals(contentType)) {
			fullFilename += ("." + Constants.EXTENSION_CSV);
		} else if (Constants.CONTENT_TYPE_PDF.equals(contentType)) {
			fullFilename += ("." + Constants.EXTENSION_PDF);
		} else if (Constants.CONTENT_TYPE_WORD.equals(contentType)) {
			fullFilename += ("." + Constants.EXTENSION_WORD);
		} else if (Constants.CONTENT_TYPE_EXCEL.equals(contentType)) {
			fullFilename += ("." + Constants.EXTENSION_EXCEL);
		} else if (Constants.CONTENT_TYPE_JPEG.equals(contentType)) {
			fullFilename += ("." + Constants.EXTENSION_JPG);
		} else if (Constants.CONTENT_TYPE_ZIP.equals(contentType)) {
			fullFilename += ("." + Constants.EXTENSION_ZIP);
		}
		
		FacesUtil.download(getFacesContext(), contentType, fullFilename, true, data);
	}
	
	// ------------------------------------------------------------- Assertions
	
	protected boolean isEmpty(String value) {
		return Primitives.isEmpty(value);
	}

	protected boolean isNotEmpty(String value) {
		return Primitives.isNotEmpty(value);
	}

	protected boolean isEmpty(boolean all, String... values) {
		return Primitives.isEmpty(all, values);
	}

	protected boolean isNotEmpty(boolean all, String... values) {
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
		return Primitives.isEmpty(set);
	}

	protected boolean isNotEmpty(Set<?> set) {
		return Primitives.isNotEmpty(set);
	}

	protected boolean isEmpty(Map<?, ?> map) {
		return Primitives.isEmpty(map);
	}

	protected boolean isNotEmpty(Map<?, ?> map) {
		return Primitives.isNotEmpty(map);
	}

	protected boolean isEmpty(Object[] array) {
		return Primitives.isEmpty(array);
	}

	protected boolean isNotEmpty(Object[] array) {
		return Primitives.isNotEmpty(array);
	}
	
    // --------------------------------------------------------- Getter, Setter
    
    public SessionBean getSessionBean() {
		return this.sessionBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public WebProperties getWebProperties() {
		return this.webProperties;
	}

	public void setWebProperties(WebProperties webProperties) {
		this.webProperties = webProperties;
	}

	protected Logger getLog() {
		if (this.log == null) {
			this.log = LoggerFactory.getLogger(getClass());
		}
		return this.log;
	}

	// ----------------------------------------------------------- Export: Word
	
	protected void writeWordTableHeader(XWPFTable table,  boolean i18n, String... values) {
		XWPFTableRow tableRowOne = table.getRow(0);
		for (int i = 0; i < values.length; i++) {
			XWPFTableCell tableCell = (i > 0 ? tableRowOne.addNewTableCell() : tableRowOne.getCell(0));
			tableCell.setVerticalAlignment(XWPFVertAlign.CENTER);
			XWPFParagraph paragraph = tableCell.getParagraphs().get(0);
			paragraph.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun header = paragraph.createRun();
			header.setBold(true);
			String value = values[i];
			if (isNotEmpty(value)) {
				header.setText((i18n ? msgs(value) : value));
			} else {
				header.setText("");
			}
		}
	}
	
	protected void writeWordTableRow(XWPFTable table,  boolean i18n, String... values) {
		XWPFTableRow tableRowOne = table.createRow();
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			if (isNotEmpty(value)) {
				tableRowOne.getCell(i).setText((i18n ? msgs(value) : value));
			} else {
				tableRowOne.getCell(i).setText("");
			}
		}
	}
	
	protected void closeQuietly(XWPFDocument document) {
		try {
			if (document != null) {
				document.close();
			}
		} catch (Exception ex) {
			getLog().error("Error closing word document.", ex);
		}
	}
	
	// ------------------------------------------------------------ Export: Csv
	// http://examples.javacodegeeks.com/core-java/apache/commons/csv-commons/writeread-csv-files-with-apache-commons-csv-example/
	
	@SuppressWarnings("resource")
	protected Object[] createCsvPrinter() throws IOException {
		Object[] created = new Object[2];
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
		
		created[0] = printer;
		created[1] = out;
		
		return created;
	}

	protected void writeCsvRecord(CSVPrinter printer, boolean i18n, String... values) throws IOException {
		Object[] valueArray = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			valueArray[i] = (i18n ? msgs(values[i]) : values[i]);
		}
		printer.printRecord(valueArray);
	}
	
	protected void closeQuietly(CSVPrinter printer) {
		try {
			if (printer != null) {
				printer.close();
			}
		} catch (Exception ex) {
			getLog().error("Error closing csv printer.", ex);
		}
	}
	
	// ------------------------------------------------------------ Export: Svg
	// http://xmlgraphics.apache.org/batik/using/transcoder.html
	
	public void exportSvg(String svc, String filename) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			/** Create a JPEG transcoder */
	        JPEGTranscoder t = new JPEGTranscoder();
	
	        /** Set the transcoding hints. */ 
	        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
	
	        /** Create the transcoder input. */
	        InputStream stream = new ByteArrayInputStream(svc.getBytes(StandardCharsets.UTF_8));
	        TranscoderInput input = new TranscoderInput(stream);
	
	        /** Create the transcoder output. */
	        TranscoderOutput output = new TranscoderOutput(out);
	
	        /** Save the image. */
	        t.transcode(input, output);
	
	        /** Flush and close the stream. */
	        out.flush();
	        
	        download(Constants.CONTENT_TYPE_JPEG, filename, out);
		} catch (Exception ex) {
			String errorMessage = "Error export to png.";
			getLog().error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
}
