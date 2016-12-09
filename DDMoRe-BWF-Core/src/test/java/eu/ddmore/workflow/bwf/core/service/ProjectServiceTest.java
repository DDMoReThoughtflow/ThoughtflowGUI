package eu.ddmore.workflow.bwf.core.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.ddmore.workflow.bwf.client.model.File;
import eu.ddmore.workflow.bwf.client.service.ProjectService;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.core.BaseTest;

public class ProjectServiceTest extends BaseTest {

	@Autowired private ProjectService projectService;
	
	@Test
	public void testRepo() {
		List<File> files = this.projectService.getFiles(1L, null, false, false, false);
		assertListNotEmpty(files);
	}
	
	@Test
	public void testGetAuditTrail() throws Exception {
		String json = this.projectService.getAuditTrail(1L, "repo:4bf8c20c3cd38f233f23bfff3412fe63d78b4f26/models/Step1/Step1.mdl", File.class, 5);
		Assert.assertNotNull(json);
		Assert.assertTrue(Primitives.isNotEmpty(json));
//		System.out.println(json);
	}
}
