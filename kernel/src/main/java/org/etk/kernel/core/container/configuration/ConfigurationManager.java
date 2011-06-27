package org.etk.kernel.core.container.configuration;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.etk.kernel.core.container.xml.Component;
import org.etk.kernel.core.container.xml.Configuration;

public interface ConfigurationManager {
	/**
	 * Retrieving the Configuration which contains 
	 * the information in the xml file.
	 * @return
	 */
	public Configuration getConfiguration();
	
	public Component getComponent(String service);
	
	public Component getComponent(Class clazz) throws Exception;
	
	public Collection getComponents();
	
	public void addConfiguration(String url) throws Exception;
	
	public void addConfiguration(Collection urls) throws Exception;
	
	public void addConfiguration(URL url) throws Exception;
	
	public URL getResource(String url, String defaultURL) throws Exception;
	
	public URL getResource(String url) throws Exception;
	
	public InputStream getInputStream(String url, String defaultURL) throws Exception;
	
	public InputStream getInputStream(String url) throws Exception;
	
	public boolean isDefault(String value);
	
	public URL getURL(String uri) throws Exception;

}
