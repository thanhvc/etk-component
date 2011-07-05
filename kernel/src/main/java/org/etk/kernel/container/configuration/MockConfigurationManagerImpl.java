package org.etk.kernel.container.configuration;

import java.net.URL;

import javax.servlet.ServletContext;

import org.etk.kernel.container.KernelContainer;


public class MockConfigurationManagerImpl extends ConfigurationManagerImpl {
	private String confDir_;

	public MockConfigurationManagerImpl(ServletContext context) throws Exception {
		super(context, KernelContainer.getProfiles());
		confDir_ = System.getProperty("mock.portal.dir") + "/WEB-INF";
	}

	public URL getURL(String uri) throws Exception {
		if (uri.startsWith("jar:")) {
			String path = removePrefix("jar:/", uri);
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			return cl.getResource(path);
		} else if (uri.startsWith("classpath:")) {
			String path = removePrefix("classpath:/", uri);
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			return cl.getResource(path);
		} else if (uri.startsWith("war:")) {
			String path = removePrefix("war:", uri);
			URL url = new URL("file:" + confDir_ + path);
			return url;
		} else if (uri.startsWith("file:")) {
			return new URL(uri);
		}
		return null;
	}
}
