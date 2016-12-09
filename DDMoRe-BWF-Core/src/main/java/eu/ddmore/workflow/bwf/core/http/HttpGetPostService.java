package eu.ddmore.workflow.bwf.core.http;

import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.ddmore.workflow.bwf.client.util.Constants;
import eu.ddmore.workflow.bwf.client.util.Primitives;

@Service
public class HttpGetPostService {

	/** Number of concurrent connections for the manager */
	@Value(value="${httpclient.max.total.connections:1}")
	private Integer maxTotalConnections;

	/** Timeout in ms on how long we'll wait for a connection from the manager */
	@Value(value="${httpclient.connection.manager.timeout:120000}")
	private Integer connectionManagerTimeout;

	/** Connection timeout in ms (how long it takes to connect to remote host) */
	@Value(value="${httpclient.connection.timeout:120000}")
	private Integer connectionTimeout;
	
	/** Socket timeout in ms (how long it takes to retrieve data from remote host) */
	@Value(value="${httpclient.so.timeout:1800000}")
	private Integer soTimeout;
	
	/** Proxy configuration */
	@Value(value="${httpclient.proxy.enabled:#{null}}")
	private Boolean proxyEnabled;
	
	@Value(value="${httpclient.proxy.host:#{null}}")
	private String proxyHost;
	
	@Value(value="${httpclient.proxy.port:#{null}}")
	private Integer proxyPort;

	@Value(value="${httpclient.proxy.user:#{null}}")
	private String proxyUser;
	
	@Value(value="${httpclient.proxy.password:#{null}}")
	private String proxyPassword;
	
	/** Proxy access */
	private RequestConfig requestConfig;
	private CredentialsProvider credentialsProvider;
	
	/** HTTP connection pool */
	private PoolingHttpClientConnectionManager connectionManager;

	public ResponseData getRestGetResponse(String resourceURL, Map<String, String> headerMap) throws Exception {
		/** Initialize get method */
		HttpGet httpGet = new HttpGet(resourceURL);
		
		try {
			/** Set request header */
			if (headerMap != null && !headerMap.isEmpty()) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					String value = entry.getValue();
					if (value != null && value.contains(";")) {
						value = value.substring(0, value.indexOf(';'));
					}
					if (value != null) {
						httpGet.addHeader(entry.getKey(), value);
					}
				}
			}
			
			/** Initialize and do call */
			HttpClient httpClient = newHttpClient();
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
			ResponseData responseData = new ResponseData(statusCode);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseData.setData(IOUtils.toByteArray(entity.getContent()));
			}
			
			return responseData;
		} finally {
			httpGet.releaseConnection();
		}		
	}
	
	public ResponseData getRestPostResponse(String resourceURL, String inputRequestBody, Map<String, String> headerMap, String contentType) throws Exception {
		/** Initialize post method */
		HttpPost httpPost = new HttpPost(resourceURL);
		
		try {
			/** Set request header */
			if (headerMap != null && !headerMap.isEmpty()) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					String value = entry.getValue();
					if (value != null && value.contains(";")) {
						value = value.substring(0, value.indexOf(";"));
					}
					if (value != null) {
						httpPost.addHeader(entry.getKey(), value);
					}
				}
			}
			
			/** Set post string */
			if (Primitives.isNotEmpty(inputRequestBody)) {
				StringEntity requestEntity = new StringEntity(inputRequestBody, ContentType.create(contentType, Constants.UTF_8));
				httpPost.setEntity(requestEntity);
			}
			
			/** Initialize and do call */
			HttpClient httpClient = newHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
			ResponseData responseData = new ResponseData(statusCode);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseData.setData(IOUtils.toByteArray(entity.getContent()));
			}
			
			return responseData;
		} finally {
			httpPost.releaseConnection();
		}
	}

	// --------------------------------------------------------- Getter, Setter
	
	private Boolean getProxyEnabled() {
		if (this.proxyEnabled == null) {
			this.proxyEnabled = false;
		}
		return this.proxyEnabled;
	}
	
	private CredentialsProvider getCredentialsProvider() {
		if (this.credentialsProvider == null) {
			this.credentialsProvider = new BasicCredentialsProvider();
			this.credentialsProvider.setCredentials(
	                new AuthScope(this.proxyHost, org.apache.http.auth.AuthScope.ANY_PORT),
	                new UsernamePasswordCredentials(this.proxyUser, this.proxyPassword)
	        );

		}
		return this.credentialsProvider;
	}
	
	private RequestConfig getRequestConfig() {
		if (this.requestConfig == null) {
			if (getProxyEnabled()) {
				this.requestConfig = RequestConfig.custom()
						.setConnectionRequestTimeout(this.connectionManagerTimeout)
						.setConnectTimeout(this.connectionTimeout)
						.setSocketTimeout(this.soTimeout)
						.setProxy(new HttpHost(this.proxyHost, this.proxyPort))
						.build();
			} else {
				this.requestConfig = RequestConfig.custom()
						.setConnectionRequestTimeout(this.connectionManagerTimeout)
						.setConnectTimeout(this.connectionTimeout)
						.setSocketTimeout(this.soTimeout)
						.build();
			}
		}
		return this.requestConfig;
	}
	
	private PoolingHttpClientConnectionManager getConnectionManager() {
		if (this.connectionManager == null) {
			this.connectionManager = new PoolingHttpClientConnectionManager();
			this.connectionManager.setMaxTotal(this.maxTotalConnections);
			this.connectionManager.setDefaultMaxPerRoute(this.maxTotalConnections);
		}
		return this.connectionManager;
	}
	
	// ----------------------------------------------------------------- Helper
	
	private HttpClient newHttpClient() {
		if (getProxyEnabled()) {
			return HttpClients.custom()
					.setConnectionManager(getConnectionManager())
					.setDefaultRequestConfig(getRequestConfig())
					.setDefaultCredentialsProvider(getCredentialsProvider())
					.build();
		} else {
			return HttpClients.custom()
					.setConnectionManager(getConnectionManager())
					.setDefaultRequestConfig(getRequestConfig())
					.build();
		}
	}
}
