package eu.ddmore.workflow.bwf.client.service;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.lib.Ref;

//TODO: Do not use GIT Objects
//      Use Java objects as return values
//      Remove JGIT reference from pom
public interface GITService {
	
	List<Ref> getRefs();
	Ref getRef(String refName) throws IOException;
	
}
