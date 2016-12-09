package eu.ddmore.workflow.server.rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ddmore.workflow.bwf.client.util.Primitives;

public class Configuration {

	private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
	
	private static Properties properties;
	
	static {
		try {
			properties = getPropertiesForFilenameOnClasspath(Constants.PROP_FILENAME);
		} catch (IOException ex) {
			LOG.error("Could not init properties.", ex);
		}
	}
	
	public static String getProperty(String key) {
		return getProperty(key, false);
	}

	public static String getProperty(String key, boolean required) {
		String value = getProperty(key, null);
		if (value == null && required) {
			throw new IllegalArgumentException("Property '" + key + "' must not be empty.");
		}
		return value;
	}

	public static String getProperty(String key, String defaultValue) {
		String strValue = properties.getProperty(key);
		if (Primitives.isNotEmpty(strValue)) {
			return strValue;
		}
		return defaultValue;
	}
	
	public static String getStringProperty(String key) {
		return getProperty(key, false);
	}

	public static String getStringProperty(String key, boolean required) {
		return getProperty(key, required);
	}

	public static String getStringProperty(String key, String defaultValue) {
		return getProperty(key, defaultValue);
	}
	
	public static Integer getIntegerProperty(String key) {
		return getIntegerProperty(key, false);
	}

	public static Integer getIntegerProperty(String key, boolean required) {
		Integer value = getIntegerProperty(key, null);
		if (value == null && required) {
			throw new IllegalArgumentException("Property '" + key + "' must not be empty.");
		}
		return value;
	}

	public static Integer getIntegerProperty(String key, Integer defaultValue) {
		String strValue = getStringProperty(key);
		if (Primitives.isNotEmpty(strValue)) {
			try {
				return Integer.valueOf(strValue);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Property '" + key + "' is not a valid value: " + strValue);
			}
		}
		return defaultValue;
	}
	
	public Long getLongProperty(String key) {
		return getLongProperty(key, false);
	}

	public static Long getLongProperty(String key, boolean required) {
		Long value = getLongProperty(key, null);
		if (value == null && required) {
			throw new IllegalArgumentException("Property '" + key + "' must not be empty.");
		}
		return value;
	}

	public static Long getLongProperty(String key, Long defaultValue) {
		String strValue = getStringProperty(key);
		if (Primitives.isNotEmpty(strValue)) {
			try {
				return Long.valueOf(strValue);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Property '" + key + "' is not a valid value: " + strValue);
			}
		}
		return defaultValue;
	}
	
	public static Double getDoubleProperty(String key) {
		return getDoubleProperty(key, false);
	}

	public static Double getDoubleProperty(String key, boolean required) {
		Double value = getDoubleProperty(key, null);
		if (value == null && required) {
			throw new IllegalArgumentException("Property '" + key + "' must not be empty.");
		}
		return value;
	}

	public static Double getDoubleProperty(String key, Double defaultValue) {
		String strValue = getStringProperty(key);
		if (Primitives.isNotEmpty(strValue)) {
			try {
				return Double.valueOf(strValue);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Property '" + key + "' is not a valid value: " + strValue);
			}
		}
		return defaultValue;
	}
	
	public Boolean getBooleanProperty(String key) {
		return getBooleanProperty(key, false);
	}

	public static Boolean getBooleanProperty(String key, boolean required) {
		Boolean value = getBooleanProperty(key, null);
		if (value == null && required) {
			throw new IllegalArgumentException("Property '" + key + "' must not be empty.");
		}
		return value;
	}

	public static Boolean getBooleanProperty(String key, Boolean defaultValue) {
		String strValue = getStringProperty(key);
		if (Primitives.isNotEmpty(strValue)) {
			if ("true".equalsIgnoreCase(strValue) || "1".equalsIgnoreCase(strValue)) {
				return Boolean.TRUE;
			} else if ("false".equalsIgnoreCase(strValue) || "0".equalsIgnoreCase(strValue)) {
				return Boolean.FALSE;
			} else {
				throw new IllegalArgumentException("Property '" + key + "' is not a valid value: " + strValue);
			}
		}
		return defaultValue;
	}
	
	private static Properties getPropertiesForFilenameOnClasspath(String propertiesFilename) throws IOException {
		Properties properties = null;
		InputStream inputStream = null;
		try {
			inputStream = Primitives.getInputStreamForFilenameOnClasspath(propertiesFilename);
			if (inputStream != null) {
				properties = new Properties();
				properties.load(inputStream);	
			}
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return properties;
	}
}
