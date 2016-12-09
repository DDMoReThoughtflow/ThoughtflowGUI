package eu.ddmore.workflow.bwf.core.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.ddmore.workflow.bwf.client.model.User;
import eu.ddmore.workflow.bwf.client.service.UserService;
import eu.ddmore.workflow.bwf.core.BaseTest;

public class UserServiceTest extends BaseTest {

	@Autowired private UserService userService;
	
	@Test
	public void testGetAll() throws Exception {
		List<User> users = this.userService.search(true, true, true);
		Assert.assertNotNull(users);
		Assert.assertTrue(users.size() > 0);
	}
}
