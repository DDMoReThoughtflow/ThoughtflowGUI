package eu.ddmore.workflow.bwf.web.selectable;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import eu.ddmore.workflow.bwf.client.enumeration.AssumptionType;
import eu.ddmore.workflow.bwf.web.bean.AbstractSelectItems;

@ManagedBean
@ApplicationScoped
public class AssumptionTypeItems extends AbstractSelectItems<AssumptionType> {

	private static final long serialVersionUID = 1L;

	@Override
	protected List<AssumptionType> initSource() {
		List<AssumptionType> items = new ArrayList<AssumptionType>();
		for (AssumptionType projectPriority : AssumptionType.values()) {
			items.add(projectPriority);
		}
		return items;
	}
	
	@Override
	public String getSelectItemString(AssumptionType src) {
		return msgs(src.toString());
	}
}
