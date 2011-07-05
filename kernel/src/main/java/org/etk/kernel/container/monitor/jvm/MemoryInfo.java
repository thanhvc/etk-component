package org.etk.kernel.container.monitor.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryInfo {
	private MemoryMXBean mxbean_;

	public MemoryInfo() {
		mxbean_ = ManagementFactory.getMemoryMXBean();
	}

	public MemoryUsage getHeapMemoryUsage() {
		return mxbean_.getHeapMemoryUsage();
	}

	public MemoryUsage getNonHeapMemoryUsage() {
		return mxbean_.getNonHeapMemoryUsage();
	}

	public int getObjectPendingFinalizationCount() {
		return mxbean_.getObjectPendingFinalizationCount();
	}

	public boolean isVerbose() {
		return mxbean_.isVerbose();
	}

	public void printMemoryInfo() {
		System.out.println("  Memory Heap Usage: "
				+ mxbean_.getHeapMemoryUsage());
		System.out.println("  Memory Non Heap Usage"
				+ mxbean_.getNonHeapMemoryUsage());
	}
}
