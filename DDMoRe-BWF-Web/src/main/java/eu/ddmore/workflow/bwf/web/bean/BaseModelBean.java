package eu.ddmore.workflow.bwf.web.bean;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import eu.ddmore.workflow.bwf.client.model.BaseModel;
import eu.ddmore.workflow.bwf.client.util.Primitives;

public class BaseModelBean<M extends BaseModel> implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean selected;
	private M model;
	
	public BaseModelBean(M model) {
		this(false, model);
	}

	public BaseModelBean(boolean selected, M model) {
		this.selected = selected;
		this.model = model;
		init();
	}
	
	protected void init() {
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public M getModel() {
		return this.model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	public Boolean hasModel() {
		return getModel() != null;
	}
	
	public static <B extends BaseModelBean<M>, M extends BaseModel> List<B> toBeanList(List<M> entities, Class<B> beanClass)  {
		List<B> beanList = new ArrayList<B>();
		if (entities != null && !entities.isEmpty()) {
			for (M entity: entities) {
				beanList.add(toBean(entity, beanClass));
			}
		}
		return beanList;
	}

	public static <B extends BaseModelBean<M>, M extends BaseModel> List<M> toEntityList(List<B> beans, Class<M> entityClass)  {
		List<M> entityList = new ArrayList<M>();
		if (beans != null && !beans.isEmpty()) {
			for (B bean: beans) {
				entityList.add(bean.getModel());
			}
		}
		return entityList;
	}

	public static <B extends BaseModelBean<M>, M extends BaseModel> B toBean(M entity, Class<B> beanClass) {
		Constructor<B> constructor = ClassUtils.getConstructorIfAvailable(beanClass, entity.getClass());
		return BeanUtils.instantiateClass(constructor, entity);
	}
	
	protected boolean isEmpty(String value) {
		return Primitives.isEmpty(value);
	}

	protected boolean isNotEmpty(String value) {
		return Primitives.isNotEmpty(value);
	}
	
	protected boolean isEmpty(List<?> list) {
		return Primitives.isEmpty(list);
	}

	protected boolean isNotEmpty(List<?> list) {
		return Primitives.isNotEmpty(list);
	}

	protected boolean isEmpty(Set<?> set) {
		return Primitives.isEmpty(set);
	}

	protected boolean isNotEmpty(Set<?> set) {
		return Primitives.isNotEmpty(set);
	}
	
	protected boolean isEmpty(Object[] array) {
		return Primitives.isEmpty(array);
	}

	protected boolean isNotEmpty(Object[] array) {
		return Primitives.isNotEmpty(array);
	}
}
