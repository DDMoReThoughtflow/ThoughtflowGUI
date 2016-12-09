package eu.ddmore.workflow.bwf.core.http;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.ddmore.workflow.bwf.core.BaseTest;

public class HttpPostTest extends BaseTest {

	@Autowired private HttpGetPostService restGetPostService;
	
	@Test
	public void testGetFileContent() throws Exception {
		String fileIdentifier = "https://raw.githubusercontent.com/halfmanhalfgeek/MDLProject/911493aa4eb180787e86b19d4275071565a8df46/models/warfarin_conc.csv";
		ResponseData responseData = this.restGetPostService.getRestGetResponse(fileIdentifier, null);
		
		Assert.assertNotNull(responseData);
		Assert.assertEquals(200, responseData.getCode());
		Assert.assertNotNull(responseData.getData());
		Assert.assertTrue(responseData.getData().length > 0);
	}
}
