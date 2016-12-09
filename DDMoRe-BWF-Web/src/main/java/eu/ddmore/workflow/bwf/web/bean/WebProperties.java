package eu.ddmore.workflow.bwf.web.bean;

import java.io.Serializable;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class WebProperties implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value="#{applicationProperties}")
	private Properties applicationProperties;

	public Properties getApplicationProperties() {
		return this.applicationProperties;
	}

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
	public String getProperty(String key) {
		return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        if (getApplicationProperties().containsKey(key)) {
        	return getApplicationProperties().getProperty(key).trim();
        }
        return defaultValue;
    }

    public String getStringProperty(String key) {
        return getProperty(key, null);
    }

    public String getStringProperty(String key, String defaultValue) {
    	return getProperty(key, defaultValue);
    }
    
	public Integer getIntegerProperty(String key) {
        return getIntegerProperty(key, null);
    }
    
	public Integer getIntegerProperty(String key, Integer defaultValue) {
		String property = getProperty(key);
        if (property != null) {
        	return Integer.valueOf(property);
        }
        return defaultValue;
    }

	public Long getLongProperty(String key) {
        return getLongProperty(key, null);
    }
    
	public Long getLongProperty(String key, Long defaultValue) {
		String property = getProperty(key);
        if (property != null) {
        	return Long.valueOf(property);
        }
        return defaultValue;
    }

	public Double getDoubleProperty(String key) {
        return getDoubleProperty(key, null);
    }
    
	public Double getDoubleProperty(String key, Double defaultValue) {
		String property = getProperty(key);
        if (property != null) {
        	return Double.valueOf(property);
        }
        return defaultValue;
    }

	public Boolean getBooleanProperty(String key) {
		return getBooleanProperty(key, false);
	}

    public Boolean getBooleanProperty(String key, Boolean defaultValue) {
		String property = getProperty(key);
        if (property != null) {
        	return Boolean.valueOf(property);
        }
        return defaultValue;
    }
}
