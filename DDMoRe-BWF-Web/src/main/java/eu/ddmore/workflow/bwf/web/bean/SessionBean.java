package eu.ddmore.workflow.bwf.web.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import eu.ddmore.workflow.bwf.client.model.User;
import eu.ddmore.workflow.bwf.web.spring.UserInfo;
import eu.ddmore.workflow.bwf.web.util.Constants;
import eu.ddmore.workflow.bwf.web.util.MessageProvider;

@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value="#{applicationBean}")
	private ApplicationBean applicationBean;

	@ManagedProperty(value="#{webProperties}")
	private WebProperties webProperties;

	private Locale locale;
	private User user;
	private String version;
	private String env;
	private String envString;
	private Boolean dev;
	private Boolean test;
	private Boolean prod;
	
	// Store any data in session
	private Map<String, Object> dataMap;
	
	@PostConstruct
	public void init() {
		this.locale = new Locale(Constants.DEFAULT_LOCALE);
		this.dataMap = new HashMap<String, Object>();
	}

	// --------------------------------------------------------- Getter, Setter
	
	public Locale getLocale() {
		return this.locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public User getUser() {
		if (this.user == null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			this.user = ((UserInfo) authentication.getPrincipal()).getUser();
			if (this.user != null) {
				getFacesContext().getExternalContext().getSessionMap().put(Constants.SESSION_PROP_USER, this.user);
			}
		}
		return this.user;
	}
	
	public String getVersion() {
		if (this.version == null) {
			this.version = getWebProperties().getProperty("version");
		}
		return this.version;
	}

    public String getEnv() {
    	if (this.env == null) {
    		this.env = getWebProperties().getProperty("env");
			if (this.env != null) {
				this.env = MessageProvider.getValue(this.env.toLowerCase());
			}
    	}
		return this.env;
	}

	public String getEnvString() {
		if (getEnv() != null) {
			this.envString = MessageProvider.getValue(this.env.toLowerCase());
		} else {
			this.envString = "UNKNOWN";
		}
		return this.envString;
	}

	public Boolean getDev() {
		if (this.dev == null) {
			if (getEnv() != null && "DEV".equalsIgnoreCase(getEnv())) {
				this.dev = true;
			} else {
				this.dev = false;	
			}
		}
		return this.dev;
	}

	public Boolean getTest() {
		if (this.test == null) {
			if (getEnv() != null && "TEST".equalsIgnoreCase(getEnv())) {
				this.test = true;
			} else {
				this.test = false;	
			}
		}
		return this.test;
	}

	public Boolean getProd() {
		if (this.prod == null) {
			if (getEnv() != null && "PROD".equalsIgnoreCase(getEnv())) {
				this.prod = true;
			} else {
				this.prod = false;	
			}
		}
		return this.prod;
	}

	public Map<String, Object> getDataMap() {
		return this.dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public Object getData(String key) {
		return this.dataMap.get(key);
	}

	public Object setData(String key, Object value) {
		return this.dataMap.put(key, value);
	}

	public Object removeData(String key) {
		return this.dataMap.remove(key);
	}
	
	// ------------------------------------------------------- Spring innjected
	
	public ApplicationBean getApplicationBean() {
		return this.applicationBean;
	}

	public void setApplicationBean(ApplicationBean applicationBean) {
		this.applicationBean = applicationBean;
	}
	
	public WebProperties getWebProperties() {
		return this.webProperties;
	}

	public void setWebProperties(WebProperties webProperties) {
		this.webProperties = webProperties;
	}

	// ----------------------------------------------------------------- Helper
	
	public Boolean isLoggedIn() {
    	SecurityContext securityContext = SecurityContextHolder.getContext();
    	if (securityContext != null) {
    		Authentication authentication = securityContext.getAuthentication();
   			return authentication != null && authentication instanceof UsernamePasswordAuthenticationToken && authentication.isAuthenticated();
    	}
    	return false;
    }
	
	/**
	 * Retuns the current page name as String.
	 * @return The current page name.
	 */
	public String getPage() {
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

	private FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
}
