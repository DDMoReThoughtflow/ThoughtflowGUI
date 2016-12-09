package eu.ddmore.workflow.bwf.web.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(eager=true)
@ApplicationScoped
public class ApplicationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger LOG = LoggerFactory.getLogger(ApplicationBean.class);
	
	@PostConstruct
	public void init() {
		LOG.info("Startup application bean.");
	}
}
