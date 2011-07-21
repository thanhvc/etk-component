package org.etk.kernel.container.definition;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

/**
 * This class is used to define a {@link PortalContainer} and its dependencies.
 * The dependencies are in fact all the web applications that the
 * {@link PortalContainer} needs to be properly initialized. Be aware that the
 * dependency order is used to define the initialization order.
 * 
 * Created by The eXo Platform SAS Author : Nicolas Filotto
 * nicolas.filotto@exoplatform.com 8 sept. 2009
 */
public class PortalContainerDefinition {

	/**
	 * The name of the related {@link PortalContainer}
	 */
	private String name;

	/**
	 * The realm name of the related {@link PortalContainer}
	 */
	private String realmName;

	/**
	 * The name of the {@link ServletContext} of the rest web application
	 */
	private String restContextName;

	/**
	 * The list of all the context names that are needed to initialized properly
	 * the {@link PortalContainer}. The order of all the dependencies will
	 * define the initialization order
	 */
	private List<String> dependencies;

	/**
	 * A {@link Map} of parameters that we would like to tie the portal
	 * container. Those parameters could have any type of value.
	 */
	private Map<String, Object> settings;

	/**
	 * The path of the external properties file to load as default settings to
	 * the portal container.
	 */
	private String externalSettingsPath;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

	public String getRealmName() {
		return realmName;
	}

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	public String getRestContextName() {
		return restContextName;
	}

	public void setRestContextName(String restContextName) {
		this.restContextName = restContextName;
	}

	public Map<String, Object> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, Object> settings) {
		this.settings = settings;
	}

	public String getExternalSettingsPath() {
		return externalSettingsPath;
	}

	public void setExternalSettingsPath(String externalSettingsPath) {
		this.externalSettingsPath = externalSettingsPath;
	}
}
