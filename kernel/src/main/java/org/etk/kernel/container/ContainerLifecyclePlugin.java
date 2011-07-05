package org.etk.kernel.container;

import org.etk.kernel.container.xml.InitParams;

public interface ContainerLifecyclePlugin {

	public String getName();

	public void setName(String s);

	public String getDescription();

	public void setDescription(String s);

	public InitParams getInitParams();

	public void setInitParams(InitParams params);

	public void initContainer(KernelContainer container) throws Exception;

	public void startContainer(KernelContainer container) throws Exception;

	public void stopContainer(KernelContainer container) throws Exception;

	public void destroyContainer(KernelContainer container) throws Exception;
}