package org.etk.kernel.container.xml;

import java.net.URL;

import org.etk.kernel.container.configuration.ConfigurationManagerImpl;
import org.jibx.runtime.IMarshallingContext;

public class ContainerLifecyclePlugin implements Comparable<ContainerLifecyclePlugin> {

	final URL documentURL;

	private String name;

	private String type;

	private String description;

	private int priority;

	private InitParams initParams;

	public ContainerLifecyclePlugin() {
		documentURL = ConfigurationManagerImpl.getCurrentURL();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public InitParams getInitParams() {
		return initParams;
	}

	public void setInitParams(InitParams initParams) {
		this.initParams = initParams;
	}

	public int compareTo(ContainerLifecyclePlugin o) {
		return getPriority() - o.getPriority();
	}

	public void preGet(IMarshallingContext ictx) {
		ConfigurationMarshallerUtil.addURLToContent(documentURL, ictx);
	}
}
