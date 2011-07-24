package org.etk.kernel.container.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.etk.common.logging.Logger;
import org.etk.kernel.container.KernelContainer;
import org.etk.kernel.container.configuration.ConfigurationManager;
import org.etk.kernel.container.xml.Component;
import org.etk.kernel.container.xml.ComponentLifecyclePlugin;
import org.etk.kernel.container.xml.ContainerLifecyclePlugin;
import org.etk.kernel.container.xml.Deserializer;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;

@SuppressWarnings("unchecked")
public class ContainerUtil {
	/** The logger. */
	 private static final Logger log = Logger.getLogger(ContainerUtil.class);

	static public Constructor<?>[] getSortedConstructors(Class<?> clazz)
			throws NoClassDefFoundError {
		Constructor<?>[] constructors = clazz.getConstructors();
		for (int i = 0; i < constructors.length; i++) {
			for (int j = i + 1; j < constructors.length; j++) {
				if (constructors[i].getParameterTypes().length < constructors[j]
						.getParameterTypes().length) {
					Constructor<?> tmp = constructors[i];
					constructors[i] = constructors[j];
					constructors[j] = tmp;
				}
			}
		}
		return constructors;
	}

	static public Collection<URL> getConfigurationURL(String configuration) throws Exception {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Collection c = Collections.list(cl.getResources(configuration));
		Map<String, URL> map = new HashMap<String, URL>();
		Iterator i = c.iterator();
		while (i.hasNext()) {
			URL url = (URL) i.next();
			String key = url.toString();
			// jboss bug, jboss has a very weird behavior. It copy all the jar
			// files
			// and
			// deploy them to a temp dir and include both jars, the one in sar
			// and tmp
			// dir,
			// in the class path. It cause the configuration run twice
			int index1 = key.lastIndexOf("exo-");
			int index2 = key.lastIndexOf("etk.");
			int index = index1 < index2 ? index2 : index1;
			if (index >= 0)
				key = key.substring(index);
			map.put(key, url);
		}

		i = map.values().iterator();
		// while(i.hasNext()) {
		// URL url = (URL) i.next() ;
		// System.out.println("==> Add " + url);
		// }
		return map.values();
	}

	static public void addContainerLifecyclePlugin(KernelContainer container, ConfigurationManager conf) {
		Iterator i = conf.getConfiguration().getContainerLifecyclePluginIterator();
		while (i.hasNext()) {
			ContainerLifecyclePlugin plugin = (ContainerLifecyclePlugin) i.next();
			addContainerLifecyclePlugin(container, plugin);
		}
	}

	private static void addContainerLifecyclePlugin(KernelContainer container, ContainerLifecyclePlugin plugin) {
		try {
			Class clazz = Class.forName(plugin.getType());
			org.etk.kernel.container.ContainerLifecyclePlugin cplugin = (org.etk.kernel.container.ContainerLifecyclePlugin) 
			                                              container.createComponent(clazz, plugin.getInitParams());
			cplugin.setName(plugin.getName());
			cplugin.setDescription(plugin.getDescription());
			container.addContainerLifecylePlugin(cplugin);
		} catch (Exception ex) {
			log.error("Failed to instanciate plugin " + plugin.getType() + ": " + ex.getMessage(), ex);
		}
	}

	static public void addComponentLifecyclePlugin(KernelContainer container, ConfigurationManager conf) {
		Collection plugins = conf.getConfiguration().getComponentLifecyclePlugins();
		Iterator i = plugins.iterator();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		while (i.hasNext()) {
			ComponentLifecyclePlugin plugin = (ComponentLifecyclePlugin) i.next();
			try {
				Class classType = loader.loadClass(plugin.getType());
				org.etk.kernel.container.ComponentLifecyclePlugin instance = (org.etk.kernel.container.ComponentLifecyclePlugin) classType.newInstance();
				container.addComponentLifecylePlugin(instance);
			} catch (Exception ex) {
				log.error("Failed to instanciate plugin " + plugin.getType() + ": " + ex.getMessage(), ex);
			}
		}
	}

	static public void addComponents(KernelContainer container, ConfigurationManager conf) {
		Collection components = conf.getComponents();
		if (components == null)
			return;
		Iterator i = components.iterator();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		while (i.hasNext()) {
			Component component = (Component) i.next();
			String type = component.getType();
			String key = component.getKey();
			try {
				Class classType = loader.loadClass(type);
				
				if (key == null) {
					if (component.isMultiInstance()) {
						container.registerComponent(new ConstructorInjectionComponentAdapter(classType, classType));
						 log.debug("===>>> Thread local component " + classType.getName() + " registered.");
					} else {
						container.registerComponentImplementation(classType);
					}
				} else {
					try {
						Class keyType = loader.loadClass(key);
						if (component.isMultiInstance()) {
							container.registerComponent(new ConstructorInjectionComponentAdapter(keyType, classType));
							 log.debug("===>>> Thread local component " + classType.getName() + " registered.");
						} else {
							container.registerComponentImplementation(keyType, classType);
						}
					} catch (Exception ex) {
						container.registerComponentImplementation(key, classType);
					}
				}
			} catch (ClassNotFoundException ex) {
				log.error("Cannot register the component corresponding to key = '" + key + "' and type = '" + type + "'", ex);
			}
		}
	}

	/**
	 * Loads the properties file corresponding to the given url
	 * 
	 * @param url
	 *            the url of the properties file
	 * @return a {@link Map} of properties
	 */
	public static Map<String, String> loadProperties(URL url) {
		return loadProperties(url, true);
	}

	/**
	 * Loads the properties file corresponding to the given url
	 * 
	 * @param url
	 *            the url of the properties file
	 * @param resolveVariables
	 *            indicates if the variables must be resolved
	 * @return a {@link Map} of properties
	 */
	public static Map<String, String> loadProperties(URL url, boolean resolveVariables) {
		LinkedHashMap<String, String> props = null;
		String path = null;
		InputStream in = null;
		try {
			//
			if (url != null) {
				path = url.getPath();
				in = url.openStream();
			}

			//
			if (in != null) {
				String fileName = url.getFile();
				if (Tools.endsWithIgnoreCase(path, ".properties")) {
					
					if (log.isDebugEnabled())
						log.debug("Attempt to load property file " + path);
					props = PropertiesLoader.load(in);
				} else if (Tools.endsWithIgnoreCase(fileName, ".xml")) {
					if (log.isDebugEnabled())
						log.debug("Attempt to load property file " + path
								+ " with XML format");
					props = PropertiesLoader.loadFromXML(in);
				} else if (log.isDebugEnabled()) {
					log.debug("Will not load property file" + path
							+ " because its format is not recognized");
				}
				if (props != null && resolveVariables) {
					// Those properties are used for variables resolution
					final Map<String, Object> currentProps = new HashMap<String, Object>();
					for (Map.Entry<String, String> entry : props.entrySet()) {
						String propertyName = entry.getKey();
						String propertyValue = entry.getValue();
						propertyValue = Deserializer.resolveVariables(
								propertyValue, currentProps);
						props.put(propertyName, propertyValue);
						currentProps.put(propertyName, propertyValue);
					}
				}
			} else {
				log.error("Could not load property file " + path);
			}
		} catch (Exception e) {
			log.error("Cannot load property file " + path, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ignore) {
				}
			}
		}
		return props;
	}
}
