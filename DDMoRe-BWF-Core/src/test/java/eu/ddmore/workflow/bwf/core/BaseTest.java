package eu.ddmore.workflow.bwf.core;

import java.util.List;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:eu/ddmore/workflow/bwf/core/testContext.xml"})
public class BaseTest {

	 public static void assertList(List<?> list, int expectedSize) {
		 Assert.assertNotNull(list);
		 Assert.assertTrue(list.size() == expectedSize);
	 }

	 public static void assertListNotEmpty(List<?> list) {
		 Assert.assertNotNull(list);
		 Assert.assertTrue(list.size() > 0);
	 }
}
