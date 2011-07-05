package org.etk.kernel.container;

import org.etk.kernel.container.xml.InitParams;

abstract public class BaseContainerLifecyclePlugin implements
		ContainerLifecyclePlugin {

	private String name;

	private String description;

	private InitParams params;

	public String getName() {
		return name;
	}

	public void setName(String s) {
		name = s;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String s) {
		description = s;
	}

	public InitParams getInitParams() {
		return params;
	}

	public void setInitParams(InitParams params) {
		this.params = params;
	}

	public void initContainer(KernelContainer container) throws Exception {

	}

	public void startContainer(KernelContainer container) throws Exception {

	}

	public void stopContainer(KernelContainer container) throws Exception {

	}

	public void destroyContainer(KernelContainer container) throws Exception {

	}
}