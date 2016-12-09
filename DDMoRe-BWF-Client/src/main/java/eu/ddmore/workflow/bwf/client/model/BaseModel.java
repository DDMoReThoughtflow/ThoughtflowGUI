package eu.ddmore.workflow.bwf.client.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.client.xmladapter.DateXmlAdapter;

public class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date createdAt;
	private Date modifiedAt;

	// --------------------------------------------------------- Getter, Setter
	
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date modifiedAt) {
		this.createdAt = modifiedAt;
	}

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	public Date getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
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
}
