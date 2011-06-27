package org.etk.kernel.core.container.xml;

import java.net.URL;
import java.util.ArrayList;

import org.etk.kernel.core.container.configuration.ConfigurationManagerImpl;


public class Component {

	private final URL documentURL;
	private String key;
	private String type;
	private String jmxName;
	private String description;
	private ArrayList plugins;
	private ArrayList<ComponentPlugin> componentPlugins;
	private ArrayList listeners;
	private InitParams initParams;
	private boolean showDeployInfo = false;
	private boolean multiInstance = false;
	public Component() {
		documentURL = ConfigurationManagerImpl.getCurrentURL();
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getJmxName() {
		return jmxName;
	}
	public void setJmxName(String jmxName) {
		this.jmxName = jmxName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList getPlugins() {
		return plugins;
	}
	public void setPlugins(ArrayList plugins) {
		this.plugins = plugins;
	}
	public ArrayList<ComponentPlugin> getComponentPlugins() {
		return componentPlugins;
	}
	public void setComponentPlugins(ArrayList<ComponentPlugin> componentPlugins) {
		this.componentPlugins = componentPlugins;
	}
	public ArrayList getListeners() {
		return listeners;
	}
	public void setListeners(ArrayList listeners) {
		this.listeners = listeners;
	}
	public InitParams getInitParams() {
		return initParams;
	}
	public void setInitParams(InitParams initParams) {
		this.initParams = initParams;
	}
	public boolean isShowDeployInfo() {
		return showDeployInfo;
	}
	public void setShowDeployInfo(boolean showDeployInfo) {
		this.showDeployInfo = showDeployInfo;
	}
	public boolean isMultiInstance() {
		return multiInstance;
	}
	public void setMultiInstance(boolean multiInstance) {
		this.multiInstance = multiInstance;
	}
	public URL getDocumentURL() {
		return documentURL;
	}
	
	
	
	
	
}
