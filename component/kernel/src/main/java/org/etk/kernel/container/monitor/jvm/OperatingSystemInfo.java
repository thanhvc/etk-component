package org.etk.kernel.container.monitor.jvm;

import java.net.URL;

public interface OperatingSystemInfo {

	public String getArch();

	public String getName();

	public int getAvailableProcessors();

	public String getVersion();

	public URL createURL(String file) throws Exception;
}
