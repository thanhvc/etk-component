package org.etk.kernel.container;

import javax.servlet.ServletContext;

/**
 * This class is used to define the initialization context of a web application
 * 
 * Created by The eXo Platform SAS Author : Nicolas Filotto
 * nicolas.filotto@exoplatform.com 11 sept. 2009
 */
public class WebAppInitContext {

	/**
	 * The servlet context of the web application
	 */
	private final ServletContext servletContext;

	/**
	 * The class loader of the web application;
	 */
	private final ClassLoader webappClassLoader;

	public WebAppInitContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		this.webappClassLoader = Thread.currentThread().getContextClassLoader();
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String getServletContextName() {
		return servletContext.getServletContextName();
	}

	public ClassLoader getWebappClassLoader() {
		return webappClassLoader;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof WebAppInitContext) {
			return getServletContextName().equals(
					((WebAppInitContext) o).getServletContextName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getServletContextName().hashCode();
	}
}
