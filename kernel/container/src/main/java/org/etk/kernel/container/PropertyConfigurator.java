package org.etk.kernel.container;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.etk.common.logging.Logger;
import org.etk.common.utils.PropertyManager;
import org.etk.kernel.container.configuration.ConfigurationManager;
import org.etk.kernel.container.util.ContainerUtil;
import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.PropertiesParam;
import org.etk.kernel.container.xml.Property;
import org.etk.kernel.container.xml.ValueParam;
import org.picocontainer.Startable;

/**
 * <p>
 * The property configurator configures a set of system properties via the
 * {@link PropertyManager} static methods. It is possible to configure
 * properties from the init params or from an external file.
 * </p>
 * 
 * <p>
 * The constructor will inspect the
 * {@link org.exoplatform.container.xml.InitParams} params argument to find a
 * param named <code>properties</code> with an expected type of
 * {@link PropertiesParam}. The properties contained in that argument will be
 * sourced into the property manager. When such properties are loaded from an
 * XML configuration file, the values are evaluated and property substitution
 * occurs.
 * </p>
 * 
 * <p>
 * When the property {@link PropertyManager#PROPERTIES_URL} is not null and
 * points to a valid property file it will loaded and sourced. Property values
 * will be evaluated and property substitution will occur. When the file name
 * ends with the <code>.properties</code> properties are loaded using the
 * {@link Properties#load(java.io.InputStream)} method. When the file name ends
 * with the <code>.xml</code> properties are loaded using the
 * {@link Properties#loadFromXML(java.io.InputStream)} method. Suffix checks are
 * done ignoring the case.
 * </p>
 * 
 * <p>
 * When properties are loaded from an URL, the order of the properties
 * declarations in the file matters.
 * </p>
 * 
 */
public class PropertyConfigurator implements Startable {

	/** The logger. */
	private final Logger log = Logger.getLogger(PropertyConfigurator.class);

	public PropertyConfigurator(InitParams params, ConfigurationManager confManager) {
		PropertiesParam propertiesParam = params.getPropertiesParam("properties");
		if (propertiesParam != null) {
			log.debug("Going to initialize properties from init param");
			for (Iterator<Property> i = propertiesParam.getPropertyIterator(); i.hasNext();) {
				Property property = i.next();
				String name = property.getName();
				String value = property.getValue();
				log.debug("Adding property from init param " + name + " = "	+ value);
				PropertyManager.setProperty(name, value);
			}
		}

		//
		String path = null;
		ValueParam pathParam = params.getValueParam("properties.url");
		if (pathParam != null) {
			log.debug("Using file path " + path + " found from configuration");
			path = pathParam.getValue();
		}

		//
		String systemPath = PropertyManager.getProperty(PropertyManager.PROPERTIES_URL);
		if (systemPath != null) {
			log.debug("Using file path " + path	+ " found from system properties");
			path = systemPath;
		}

		//
		if (path != null) {
			log.debug("Found property file path " + path);
			try {
				URL url = confManager.getURL(path);
				Map<String, String> props = ContainerUtil.loadProperties(url);
				if (props != null) {
					for (Map.Entry<String, String> entry : props.entrySet()) {
						String propertyName = entry.getKey();
						String propertyValue = entry.getValue();
						PropertyManager.setProperty(propertyName, propertyValue);
					}
				}
			} catch (Exception e) {
				log.error("Cannot load property file " + path, e);
			}
		}
	}

	public void start() {
	}

	public void stop() {
	}
}
