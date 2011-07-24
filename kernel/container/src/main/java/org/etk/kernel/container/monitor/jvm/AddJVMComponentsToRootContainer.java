package org.etk.kernel.container.monitor.jvm;

import java.lang.management.ManagementFactory;

import org.etk.kernel.container.BaseContainerLifecyclePlugin;
import org.etk.kernel.container.KernelContainer;


public class AddJVMComponentsToRootContainer extends BaseContainerLifecyclePlugin {

	public void initContainer(KernelContainer container) {
		attemptToRegisterMXComponent(container, ManagementFactory.getOperatingSystemMXBean());
		attemptToRegisterMXComponent(container, ManagementFactory.getRuntimeMXBean());
		attemptToRegisterMXComponent(container, ManagementFactory.getThreadMXBean());
		attemptToRegisterMXComponent(container, ManagementFactory.getClassLoadingMXBean());
		attemptToRegisterMXComponent(container, ManagementFactory.getCompilationMXBean());

		attemptToRegisterMXComponent(container, new MemoryInfo());
		attemptToRegisterMXComponent(container, JVMRuntimeInfo.MEMORY_MANAGER_MXBEANS, ManagementFactory.getMemoryManagerMXBeans());
		attemptToRegisterMXComponent(container, JVMRuntimeInfo.MEMORY_POOL_MXBEANS, ManagementFactory.getMemoryPoolMXBeans());
		attemptToRegisterMXComponent(container, JVMRuntimeInfo.GARBAGE_COLLECTOR_MXBEANS, ManagementFactory.getGarbageCollectorMXBeans());
	}

	private void attemptToRegisterMXComponent(KernelContainer container, Object mxComponent) {
		if (mxComponent != null) {

			container.registerComponentInstance(mxComponent);

		}
	}

	private void attemptToRegisterMXComponent(KernelContainer container, Object mxKey, Object mxComponent) {
		if (mxComponent != null) {

			container.registerComponentInstance(mxKey, mxComponent);

		}
	}

}