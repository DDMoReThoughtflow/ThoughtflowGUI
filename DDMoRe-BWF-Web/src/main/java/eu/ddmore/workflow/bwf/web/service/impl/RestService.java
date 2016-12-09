package eu.ddmore.workflow.bwf.web.service.impl;

import java.security.KeyManagementException;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.log4j.Logger;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.ddmore.workflow.bwf.client.util.NullHostnameVerifier;
import eu.ddmore.workflow.bwf.client.util.NullX509TrustManager;

//TODO: Create commons project and move to
@Service
public class RestService {

	private static final Logger LOG = Logger.getLogger(RestService.class);
	
	@Value(value="${rest.url}")
	private String url;
	
	@Value(value="${rest.port}")
	private int port;
	
	@Value(value="${rest.useSSL:false}")
	private Boolean useSSL;
	
	@Value(value="${rest.trustStore.path:#{null}}")
	private String trustStorePath;

	@Value(value="${rest.trustStore.password:#{null}}")
	private String trustStorePassword;
	
	private Client client;

	@PostConstruct
	public void init() {
		try {
			this.client = buildRestClient();
		} catch (KeyManagementException ex) {
			LOG.error("Error connect to REST service '" + (getUrl() + ":" + getPort()) + "'.");
		}
	}
	
	// ----------------------------------------------------------------- Helper
	
	private Client buildRestClient() throws KeyManagementException {
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		if (getUrl().toLowerCase().startsWith("https")) {
			if (getUseSSL()) {
				SSLContext sslContext = buildSSLContext(getTrustStorePath(), getTrustStorePassword());
				clientBuilder = clientBuilder.sslContext(sslContext);
				clientBuilder = clientBuilder.hostnameVerifier(NullHostnameVerifier.INSTANCE);
			} else {
				SslConfigurator sslConfig = SslConfigurator.newInstance();
			    TrustManager[] trustManagerArray = {
			    		NullX509TrustManager.INSTANCE
			    };
			    SSLContext sslContext = sslConfig.createSSLContext();
			    sslContext.init(null, trustManagerArray, null);
			    clientBuilder = clientBuilder.sslContext(sslContext);
			    clientBuilder = clientBuilder.hostnameVerifier(NullHostnameVerifier.INSTANCE);
			}
		}
		clientBuilder = clientBuilder.register(MultiPartFeature.class);
//		clientBuilder = clientBuilder.register(HttpAuthenticationFeature.basic(username, password));
		Client client = clientBuilder.build();
		return client;
	}
	
	private SSLContext buildSSLContext(String trustStorePath, String trustStorePassword) {
		SSLContext sslContext = null;
		SslConfigurator sslConfig = SslConfigurator.newInstance()
				.trustStoreFile(trustStorePath)
				.trustStorePassword(trustStorePassword)
				.trustStoreType("JKS").keyStoreFile(trustStorePath)
				.keyStorePassword(trustStorePassword)
				.securityProtocol("TLS");
		sslContext = sslConfig.createSSLContext();
		return sslContext;		
	}
	
	// --------------------------------------------------------- Getter, Setter

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Boolean getUseSSL() {
		return this.useSSL;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	public String getTrustStorePath() {
		return this.trustStorePath;
	}

	public void setTrustStorePath(String trustStorePath) {
		this.trustStorePath = trustStorePath;
	}

	public String getTrustStorePassword() {
		return this.trustStorePassword;
	}

	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
