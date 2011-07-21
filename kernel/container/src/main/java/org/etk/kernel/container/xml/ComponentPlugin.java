package org.etk.kernel.container.xml;

import java.net.URL;

import org.etk.kernel.container.configuration.ConfigurationManagerImpl;
import org.jibx.runtime.IMarshallingContext;

public class ComponentPlugin implements Comparable<ComponentPlugin> {

	private URL documentURL;
	private String name;
	private String type;
	private String setMethod;
	private String description;
	private InitParams initParams;
	private int priority;
	
	public ComponentPlugin() {
		documentURL = ConfigurationManagerImpl.getCurrentURL();
		
	}
	
	
	public URL getDocumentURL() {
		return documentURL;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getSetMethod() {
		return setMethod;
	}


	public void setSetMethod(String setMethod) {
		this.setMethod = setMethod;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public InitParams getInitParams() {
		return initParams;
	}


	public void setInitParams(InitParams initParams) {
		this.initParams = initParams;
	}


	public int getPriority() {
		return priority;
	}


	public void setPriority(int priority) {
		this.priority = priority;
	}


	public int compareTo(ComponentPlugin o) {
	      return getPriority() - o.getPriority();
	}

	public void preGet(IMarshallingContext ictx) {
	      ConfigurationMarshallerUtil.addURLToContent(documentURL, ictx);
	}   
}
