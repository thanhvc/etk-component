package org.etk.kernel.container.monitor.jvm;

import java.util.List;
import java.util.Map;

public interface JVMRuntimeInfo {
	final static public String MEMORY_MANAGER_MXBEANS = "MemoryManagerMXBean";

	final static public String MEMORY_POOL_MXBEANS = "MemoryPoolMXBeans";

	final static public String GARBAGE_COLLECTOR_MXBEANS = "GarbageCollectorMXBeans";

	String getName();

	String getSpecName();

	String getSpecVendor();

	String getSpecVersion();

	String getManagementSpecVersion();

	String getVmName();

	String getVmVendor();

	String getVmVersion();

	List getInputArguments();

	Map getSystemProperties();

	boolean getBootClassPathSupported();

	String getBootClassPath();

	String getClassPath();

	String getLibraryPath();

	long getStartTime();

	long getUptime();

	public boolean isManagementSupported();

	public String getSystemPropertiesAsText();
}
