package eu.ddmore.workflow.bwf.web.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class MessageProvider {

	private static ResourceBundle bundle;

	public static ResourceBundle getBundle() {
		if (bundle == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			bundle = context.getApplication().getResourceBundle(context, "msgs");
		}
		return bundle;
	}

	public static String getValue(String key) {
		String result = null;
		try {
			result = getBundle().getString(key);
		} catch (MissingResourceException e) {
			result = "???" + key + "???";
		}
		return result;
	}
	
	public static String getValue(String key, String... placeholder) {
		String value = getValue(key);
		int index = 0;
		while (true) {
			String placeholderString = "{$" + index + "}";
			if (value.contains(placeholderString) && placeholder != null && placeholder.length > index) {
				value = value.replace(placeholderString, placeholder[index]);
				index++;
			} else {
				break;
			}
		}
		return value;
	}
	
	public static String msgs(String key) {
		return getValue(key);
	}
	
	public static String msgs(String key, String... placeholder) {
		return getValue(key, placeholder);
	}

	public static String[] getValues(String... keys) {
		String[] values = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			values[i] = getValue(keys[i]);
		}
		return values;
	}

	public static void reset() {
		bundle = null;
	}
}
