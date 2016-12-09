package eu.ddmore.workflow.bwf.web.selectable;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import eu.ddmore.workflow.bwf.client.enumeration.ProjectPriority;
import eu.ddmore.workflow.bwf.web.bean.AbstractSelectItems;

@ManagedBean
@ApplicationScoped
public class ProjectPriorityItems extends AbstractSelectItems<ProjectPriority> {

	private static final long serialVersionUID = 1L;

	@Override
	protected List<ProjectPriority> initSource() {
		List<ProjectPriority> items = new ArrayList<ProjectPriority>();
		for (ProjectPriority projectPriority : ProjectPriority.values()) {
			items.add(projectPriority);
		}
		return items;
	}
	
	@Override
	public String getSelectItemString(ProjectPriority src) {
		return msgs(src.toString());
	}
}
