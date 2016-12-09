package eu.ddmore.workflow.bwf.web.util;

import org.junit.Assert;
import org.junit.Test;

import eu.ddmore.workflow.bwf.client.util.StringEncrypter;

public class StringEncrypterTest {

	@Test
	public void testEncrypter() throws Exception {
		StringEncrypter encrypter = new StringEncrypter();
		
		String encryptedUsername = encrypter.encrypt("username");
		String encryptedPassword = encrypter.encrypt("password");
		
		Assert.assertNotNull(encryptedUsername);
		Assert.assertNotNull(encryptedPassword);
		
//		System.out.println(">>> username: " + encryptedUsername);
//		System.out.println(">>> password: " + encryptedPassword);
	}
}
