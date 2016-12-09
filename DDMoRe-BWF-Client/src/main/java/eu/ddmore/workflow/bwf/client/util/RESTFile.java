package eu.ddmore.workflow.bwf.client.util;

import java.io.InputStream;

public class RESTFile {

	private final InputStream content;
	private final String fileName;
	private final long length;

	public RESTFile(String fileName, InputStream content, long length) {
		super();
		this.fileName = fileName;
		this.content = content;
		this.length = length;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public InputStream getContent() {
		return this.content;
	}
	
	public long getLength() {
		return this.length;
	}
	
	@Override
	public String toString() {
		return this.fileName;
	}	
}
