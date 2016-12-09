package eu.ddmore.workflow.bwf.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.ddmore.workflow.bwf.client.service.GITService;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.client.util.StringEncrypter;

@Service(value="gitService")
public class GITServiceImpl implements GITService {

	private static final Logger LOG = LoggerFactory.getLogger(GITServiceImpl.class);
	
	@Value(value="${git.enabled}")
	private Boolean enabled;
	
	@Value(value="${git.encrypted}")
	private Boolean encrypted;

	@Value(value="${git.username:#{null}}")
	private String username;

	@Value(value="${git.password:#{null}}")
	private String password;
	
	private Repository repository; 

	@PostConstruct
	public void init() {
		if (!getEnabled()) {
			LOG.warn("GIT access is disabled.");
			return;
		}
		
		File workingDirectory = null;
		try {
			workingDirectory = new File(Primitives.getTempDirectory() + "\\local-git-repo");
			if (!workingDirectory.exists()) {
				workingDirectory.mkdirs();
				
				String username = null;
				String password = null;
				if (getEncrypted()) {
					StringEncrypter encrypter = new StringEncrypter();
					username = encrypter.decrypt(getUsername());
					password = encrypter.decrypt(getPassword());
				} else {
					username = getUsername();
					password = getPassword();
				}
				
				CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, password);
				
				Git git = Git.cloneRepository()
						  .setURI("https://git.code.sf.net/p/ddmore/commonconverter/")
						  .setDirectory(workingDirectory)
						  .setCredentialsProvider(cp)
						  .call();
				this.repository = git.getRepository();
			} else {
				FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
				repositoryBuilder.setMustExist(true);
				repositoryBuilder.setGitDir(workingDirectory);
				this.repository = repositoryBuilder.build();
			}
		} catch (Exception ex) {
			if (workingDirectory != null) {
				try {
					FileUtils.deleteDirectory(workingDirectory);
				} catch (IOException ex2) {
					LOG.error("Could not delete local GIT directory '" + workingDirectory + "'.", ex2);
				}
			}
			LOG.error("Error init GIT connection.", ex);
			throw new RuntimeException("Error init GIT connection.", ex);
		}	
	}

	@Override
	public List<Ref> getRefs() {
		Map<String, Ref> refs = this.repository.getAllRefs();
		return new ArrayList<>(refs.values());
	}
	
	@Override
	public Ref getRef(String refName) throws IOException {
		return this.repository.findRef(refName);
	}

	// --------------------------------------------------------- Getter, Setter
	
	public Boolean getEnabled() {
		if (this.enabled == null) {
			this.enabled = false;
		}
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getEncrypted() {
		if (this.encrypted == null) {
			this.encrypted = false;
		}
		return this.encrypted;
	}

	public void setEncrypted(Boolean encrypted) {
		this.encrypted = encrypted;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
