package eu.ddmore.workflow.server.rest;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import eu.ddmore.workflow.server.rest.service.WorkflowRestService;
import eu.ddmore.workflow.server.rest.util.Configuration;

/**
 * Starts REST application for development.
 */
public class Application {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	public static AbstractApplicationContext CTX;
	private static Class<?>[] REST_CLASSES = new Class[] { WorkflowRestService.class }; 
	
	private CountDownLatch countDownLatch;
	
	public static void main(String[] args) {
		CTX = new ClassPathXmlApplicationContext("classpath:eu/ddmore/workflow/server/rest/applicationContext.xml");
		try {
			CTX.registerShutdownHook();
			Application application = CTX.getBean(Application.class);
			application.execute(args);
		} finally {
			CTX.close();
		}
	}
	
	public void execute(String[] args) {
		HttpServer srv = null;
		
		try {
			String url = (Configuration.getProperty("rest.server.url", true) + ":" + Configuration.getProperty("rest.server.port", true)); 
			URI baseUri = UriBuilder.fromUri(url).build();
			ResourceConfig config = new ResourceConfig(REST_CLASSES); 
			srv = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
			
			LOG.info("Workflow REST server ist listening at URL: " + url);
			
			this.countDownLatch = new CountDownLatch(1);
			this.countDownLatch.await();
			
			LOG.info("Workflow REST server is stopped.");
		} catch (Exception ex) {
			LOG.error("Workflow REST server run failed.", ex);
		} finally {
			if (srv != null) {
				srv.shutdownNow();			
			}
		}
	}
}
