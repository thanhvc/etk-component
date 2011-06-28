package org.etk.kernel.container;

import java.util.List;

public interface ComponentLifecyclePlugin {
	public String getName();

	public void setName(String s);

	public String getDescription();

	public void setDescription(String s);

	public List<String> getManageableComponents();

	public void setManageableComponents(List<String> list);

	public void initComponent(KernelContainer container, Object component) throws Exception;

	public void startComponent(KernelContainer container, Object component) throws Exception;

	public void stopComponent(KernelContainer container, Object component) throws Exception;

	public void destroyComponent(KernelContainer container, Object component) throws Exception;
}