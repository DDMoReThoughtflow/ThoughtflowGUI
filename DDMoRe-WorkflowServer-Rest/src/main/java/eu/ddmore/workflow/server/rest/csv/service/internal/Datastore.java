package eu.ddmore.workflow.server.rest.csv.service.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Datastore {
	
	
	private Map<String, Repository> repositories = new HashMap<>();

	public Repository getRepository(String repoId) {
		return repositories.get(repoId);
	}
	
	
	public void load(String datastorePath, String entityFile, String agentFile, String activityFile, String relationFile) throws FileNotFoundException, IOException {
		File dsDir = new File(datastorePath);
		if (dsDir.exists() && dsDir.isDirectory()) {
			for (File repoDir : dsDir.listFiles()) {
				if(repoDir.isDirectory()) {
					Repository repository = new Repository(repoDir.getName());
					repository.load(repoDir, entityFile, agentFile, activityFile, relationFile);
					repositories.put(repository.getId(), repository);
				}
			}
		} else {
			throw new IllegalArgumentException("Datastore Path '" + datastorePath + "' doesn't exist or isn't a directory");
		}
	}
	
	

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (Repository repository : repositories.values()) {
			sb.append("*******************************************************\r\n");
			sb.append(repository.getId() + "\r\n");
			sb.append("-------------------------------------------------------\r\n");
			sb.append(repository.toString());
			sb.append("\r\n");
		}
		
		return sb.toString();
	}

	
}

