package org.etk.kernel.container.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.etk.common.logging.Logger;
import org.etk.kernel.container.xml.Component;
import org.etk.kernel.container.xml.Configuration;
import org.etk.kernel.container.xml.Deserializer;

public class ConfigurationManagerImpl implements ConfigurationManager {

	final static public String WAR_CONF_LOCATION = "/WEB-INF";

	protected Configuration configurations;

	// FIXME ThanhVC:: To research do not depend on the Servlet Context.
	// Find the solution from Jboss MicroContainer
	private ServletContext scontext;

	private ClassLoader scontextClassLoader;

	private String contextPath = null;

	private boolean validateSchema = true;

	private final Set<String> profiles;
	
	private final static Logger log = Logger.getLogger(ConfigurationManagerImpl.class);

	/**
	 * The URL the current document being unmarshaller.
	 */
	private static final ThreadLocal<URL> currentURL = new ThreadLocal<URL>();

	public static URL getCurrentURL() {
		return currentURL.get();
	}

	public ConfigurationManagerImpl() {
		this.profiles = Collections.emptySet();
	}

	public ConfigurationManagerImpl(Set<String> profiles) {
		this.profiles = profiles;
	}

	public ConfigurationManagerImpl(ClassLoader loader, Set<String> profiles) {
		this.scontextClassLoader = loader;
		this.profiles = profiles;
	}

	public Configuration getConfiuration() {
		return this.configurations;
	}

	public void addConfiguration(ServletContext context, String url)
			throws Exception {
		if (url == null)
			return;
		addConfiguration(context, getURL(context, url));
	}

	public void addConfiguration(String url) throws Exception {
		if (url == null)
			return;
		addConfiguration(getURL(url));
	}

	public void addConfiguration(Collection urls) throws Exception {
		Iterator i = urls.iterator();
		while (i.hasNext()) {
			URL url = (URL) i.next();
			addConfiguration(url);

		}

	}

	public void addConfiguration(URL url) throws Exception {
		addConfiguration(scontext, url);
	}

	public ConfigurationManagerImpl(ServletContext context, Set<String> profiles) {
		this.scontext = context;
		this.profiles = profiles;
	}
	/**
	 * @param context
	 * @param url
	 * @throws Exception
	 */
	private void addConfiguration(ServletContext context, URL url)
			throws Exception {
		if (url == null)
			return;

		try {
			contextPath = (new File(url.toString())).getParent() + "/";
			contextPath = contextPath.replace("\\\\", "/");

		} catch (Exception e) {
			contextPath = null;
		}

		if (currentURL.get() != null) {
			throw new IllegalStateException("Would not expect that.");
		} else {
			currentURL.set(url);
		}

	      //
	      try
	      {
	         ConfigurationUnmarshaller unmarshaller = new ConfigurationUnmarshaller(profiles);
	         Configuration conf = unmarshaller.unmarshall(url);

	         if (configurations == null)
	            configurations = conf;
	         else
	            configurations.mergeConfiguration(conf);
	         List urls = conf.getImports();
	         if (urls != null)
	         {
	            for (int i = 0; i < urls.size(); i++)
	            {
	               String uri = (String)urls.get(i);
	               URL urlObject = getURL(uri);
	               if (urlObject != null)
	               {
	                  if (LOG_DEBUG)
	                     log.info("\timport " + urlObject);
	                  // Set the URL of imported file
	                  currentURL.set(urlObject);
	                  conf = unmarshaller.unmarshall(urlObject);
	                  configurations.mergeConfiguration(conf);
	               }
	               else
	               {
	                  log.warn("Couldn't process the URL for " + uri + " configuration file ignored ");
	               }
	            }
	         }
	      }
	      catch (Exception ex)
	      {
	    	  ex.printStackTrace();	         
	    	  log.error("Cannot process the configuration " + url, ex);
	      }
	      finally
	      {
	         currentURL.set(null);
	      }

	}

	/**
	 * Recursively import the configuration files
	 * 
	 * @param unmarshaller
	 *            the unmarshaller used to unmarshall the configuration file to
	 *            import
	 * @param conf
	 *            the configuration in which we get the list of files to import
	 * @throws Exception
	 *             if an exception occurs while loading the files to import
	 */
	private void importConf(ConfigurationUnmarshaller unmarshaller,
			Configuration conf) throws Exception {
		importConf(unmarshaller, conf, 1);
	}

	/**
	 * Recursively import the configuration files
	 * 
	 * @param unmarshaller
	 *            the unmarshaller used to unmarshall the configuration file to
	 *            import
	 * @param conf
	 *            the configuration in which we get the list of files to import
	 * @param depth
	 *            used to log properly the URL of the file to import
	 * @throws Exception
	 *             if an exception occurs while loading the files to import
	 */
	private void importConf(ConfigurationUnmarshaller unmarshaller,
			Configuration conf, int depth) throws Exception {
		List urls = conf.getImports();
		if (urls != null) {
			StringBuilder prefix = new StringBuilder(depth);
			for (int i = 0; i < depth; i++) {
				prefix.append('\t');
			}
			for (int i = 0; i < urls.size(); i++) {
				String uri = (String) urls.get(i);
				URL urlObject = getURL(uri);
				if (urlObject != null) {
					// Set the URL of imported file
					currentURL.set(urlObject);
					conf = unmarshaller.unmarshall(urlObject);
					configurations.mergeConfiguration(conf);
					importConf(unmarshaller, conf, depth + 1);
				} else {
					 log.warn("Couldn't process the URL for " + uri + " configuration file ignored ");
				}
			}
		}
	}

	public void processRemoveConfiguration() {
		if (configurations == null)
			return;
		List list = configurations.getRemoveConfiguration();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				String type = (String) list.get(i);
				configurations.removeConfiguration(type);
			}
		}
	}

	public Component getComponent(String service) {
		return configurations.getComponent(service);
	}

	public Component getComponent(Class clazz) throws Exception {
		return configurations.getComponent(clazz.getName());
	}

	public Collection getComponents() {
		if (configurations == null)
			return null;
		return configurations.getComponents();
	}

	public boolean isValidateSchema() {
		return validateSchema;
	}

	public void setValidateSchema(boolean validateSchema) {
		this.validateSchema = validateSchema;
	}

	public URL getResource(String url, String defaultURL) throws Exception {
		if (url == null)
			url = defaultURL;
		return getResource(url);
	}

	public URL getResource(String uri) throws Exception {
		return getURL(uri);
	}

	public InputStream getInputStream(String url, String defaultURL)
			throws Exception {
		if (url == null)
			url = defaultURL;
		return getInputStream(url);
	}

	public InputStream getInputStream(String uri) throws Exception {
		final URL url = getURL(uri);
		if (url == null) {
			throw new IOException(
					"Resource ("
							+ uri
							+ ") could not be found or the invoker doesn't have adequate privileges to get the resource");
		}

		return url.openStream();

	}

	public URL getURL(String url) throws Exception {
		return getURL(scontext, url);
	}

	private URL getURL(final ServletContext context, String url)
			throws Exception {
		if (url == null) {
			return null;
		} else if (url.startsWith("jar:")) {
			final String path = removePrefix("jar:/", url);
			final ClassLoader cl = Thread.currentThread()
					.getContextClassLoader();
			return cl.getResource(path);
		} else if (url.startsWith("classpath:")) {
			final String path = removePrefix("classpath:/", url);
			final ClassLoader cl = Thread.currentThread()
					.getContextClassLoader();
			return cl.getResource(path);
		} else if (url.startsWith("war:")) {
			String path = removePrefix("war:", url);
			if (context != null) {
				final String fPath = path;
				return context.getResource(WAR_CONF_LOCATION + fPath);
			}
			if (scontextClassLoader != null) {
				if (path.startsWith("/")) {
					// The ClassLoader doesn't support the first "/"
					path = path.substring(1);
				}
				final String fPath = path;
				return scontextClassLoader.getResource(fPath);
			}
			throw new Exception(
					"unsupport war uri in this configuration service");
		} else if (url.startsWith("file:")) {
			url = resolveFileURL(url);
			return new URL(url);
		} else if (url.indexOf(":") < 0 && contextPath != null) {
			return new URL(contextPath + url.replace('\\', '/'));
		}
		return null;
	}

	/**
	 * This methods is used to convert the given into a valid url, it will:
	 * <ol>
	 * <li>Resolve variables in the path if they exist</li>
	 * <li>Replace windows path separators with proper separators</li>
	 * <li>Ensure that the path start with file:///</li>
	 * </ol>
	 * , then it will
	 * 
	 * @param url
	 *            the url to resolve
	 * @return the resolved url
	 */
	private String resolveFileURL(String url) {
		url = Deserializer.resolveVariables(url);
		// we ensure that we don't have windows path separator in the url
		url = url.replace('\\', '/');
		if (!url.startsWith("file:///")) {
			// The url is invalid, so we will fix it
			// it happens when we use a path of type file://${path}, under
			// linux or mac os the path will start with a '/' so the url
			// will be correct but under windows we will have something
			// like C:\ so the first '/' is missing
			if (url.startsWith("file://")) {
				// The url is of type file://, so one '/' is missing
				url = "file:///" + url.substring(7);
			} else if (url.startsWith("file:/")) {
				// The url is of type file:/, so two '/' are missing
				url = "file:///" + url.substring(6);
			} else {
				// The url is of type file:, so three '/' are missing
				url = "file:///" + url.substring(5);
			}
		}
		return url;
	}

	public boolean isDefault(String value) {
		return value == null || value.length() == 0 || "default".equals(value);
	}

	protected String removePrefix(String prefix, String url) {
		return url.substring(prefix.length(), url.length());
	}

	@Override
	public Configuration getConfiguration() {
		// TODO Auto-generated method stub
		return configurations;
	}
}
