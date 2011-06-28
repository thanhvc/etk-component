package org.etk.kernel.container.component;

import org.etk.kernel.container.KernelContainer;

public interface ComponentLifecycle {
	public void initComponent(KernelContainer container) throws Exception;

	public void startComponent(KernelContainer container) throws Exception;

	public void stopComponent(KernelContainer container) throws Exception;

	public void destroyComponent(KernelContainer container) throws Exception;
}