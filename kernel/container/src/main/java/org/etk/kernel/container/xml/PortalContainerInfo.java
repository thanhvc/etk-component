package org.etk.kernel.container.xml;

import javax.servlet.ServletContext;

public class PortalContainerInfo {

	private String containerName;

	public PortalContainerInfo() {
	}

	public PortalContainerInfo(ServletContext context) {
		containerName = context.getServletContextName();
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String name) {
		containerName = name;
	}
}
