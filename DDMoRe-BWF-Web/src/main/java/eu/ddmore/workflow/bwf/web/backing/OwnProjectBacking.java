package eu.ddmore.workflow.bwf.web.backing;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import eu.ddmore.workflow.bwf.client.model.Project;

@ManagedBean
@ViewScoped
public class OwnProjectBacking extends AbstractProjectBacking {

	private static final long serialVersionUID = 1L;

	// ------------------------------------------------ Abstract implementation

	@Override
	protected List<Project> getProjectModels() {
		return getProjectService().getAccessProjects(getIdUser(), true, true, true);
	}

	@Override
	protected boolean getCanChange() {
		return true;
	}

	@Override
	protected String getExportFilename() {
		return "ownProjects";
	}
}
