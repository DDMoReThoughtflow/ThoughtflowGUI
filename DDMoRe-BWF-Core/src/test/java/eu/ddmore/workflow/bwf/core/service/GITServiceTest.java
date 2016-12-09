package eu.ddmore.workflow.bwf.core.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.ddmore.workflow.bwf.client.service.GITService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:eu/ddmore/workflow/bwf/testContext.xml"})
public class GITServiceTest {

	@Autowired private GITService gitService;
	
	@Test
	public void testGITRepository() throws Exception {
		this.gitService.getRefs();
	}
}
