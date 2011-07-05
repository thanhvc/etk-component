package org.etk.kernel.container.monitor.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.URL;

public class OperatingSystemInfoImpl implements OperatingSystemInfo {
	private OperatingSystemMXBean mxbean_;

	public OperatingSystemInfoImpl() {
		mxbean_ = ManagementFactory.getOperatingSystemMXBean();
	}

	public String getArch() {
		return mxbean_.getArch();
	}

	public String getName() {
		return mxbean_.getName();
	}

	public String getVersion() {
		return mxbean_.getVersion();
	}

	public int getAvailableProcessors() {
		return mxbean_.getAvailableProcessors();
	}

	public URL createURL(String file) throws Exception {
		return new URL("file:" + file);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("Operating System: ").append(getName()).append("\n");
		b.append("Operating  System Version : ").append(getVersion()).append("\n");
		b.append("CPU Achitechure : ").append(getArch()).append("\n");
		b.append("Number of Processors : ").append(getAvailableProcessors()).append("\n");
		return b.toString();
	}
}