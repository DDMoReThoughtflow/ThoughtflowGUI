package eu.ddmore.workflow.bwf.core.spring;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class UsernamePaswordHashTest {

	@Test
	public void testGetHashedPassword() throws Exception {
		String username = "admin";
		String rawPasswort = "admin";
		String encodedPassword = new Md5PasswordEncoder().encodePassword(rawPasswort, username);
		Assert.assertNotNull(encodedPassword);
		System.out.println("admin=" + encodedPassword);
		
		username = "manager";
		rawPasswort = "manager";
		encodedPassword = new Md5PasswordEncoder().encodePassword(rawPasswort, username);
		Assert.assertNotNull(encodedPassword);
		System.out.println("manager=" + encodedPassword);

		username = "scientist";
		rawPasswort = "scientist";
		encodedPassword = new Md5PasswordEncoder().encodePassword(rawPasswort, username);
		Assert.assertNotNull(encodedPassword);
		System.out.println("scientist=" + encodedPassword);
	}
}
