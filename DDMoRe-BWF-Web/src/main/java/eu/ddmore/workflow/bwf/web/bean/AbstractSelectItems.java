package eu.ddmore.workflow.bwf.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import eu.ddmore.workflow.bwf.web.util.MessageProvider;

public abstract class AbstractSelectItems<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The source objects */
	private List<T> source;
	
	/** All faces select items buid from source  */
	private List<SelectItem> selectItems;
	
	/** 
	 * Do your initialization here 
	 * 
	 * <code>
	 * @Override
     * protected List<String> initSource() {
	 *     List<String> returnList = new ArrayList<String>();
	 *     returnList.add("element 1");
	 *     returnList.add("element 2");
	 *     returnList.add("element 3");
	 *	   return returnList;
	 * }
	 * </code>
	 * */
	protected abstract List<T> initSource();
	
	public void reset() {
		setSource(null);
		setSelectItems(null);
	}
	
	public List<T> getSource() {
		if (this.source == null) {
			this.source = initSource();
		}
		return this.source;
	}

	public void setSource(List<T> source) {
		this.source = source;
	}

	public List<SelectItem> getSelectItems() {
		if (this.selectItems == null) {
			this.selectItems = new ArrayList<SelectItem>();
			for (T src : getSource()) {
				this.selectItems.add(new SelectItem(getSelectItemValue(src), getSelectItemString(src)));
			}
		}
		return this.selectItems;
	}
	
	public void setSelectItems(List<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}
	
	protected Object getSelectItemValue(T value) {
		return value;
	}
	
	public String getSelectItemString(T value) {
		return value.toString();
	}
	
	protected static String msgs(String key) {
		return MessageProvider.getValue(key);
	}
	
	protected static String msgs(String key, String... placeholder) {
		return MessageProvider.getValue(key, placeholder);
	}
}
