package org.etk.kernel.container.monitor.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.etk.kernel.container.xml.ExoProperties;
import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.PropertiesParam;
import org.picocontainer.Startable;

public class JVMRuntimeInfoImpl implements JVMRuntimeInfo, Startable {
	private RuntimeMXBean mxbean_;

	public JVMRuntimeInfoImpl(InitParams params) throws Exception {
		//mxbean_ = ManagementFactory.getRuntimeMXBean();

		PropertiesParam param = params.getPropertiesParam("add.system.properties");
		if (param != null) {
			ExoProperties props = param.getProperties();
			Iterator i = props.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry entry = (Map.Entry) i.next();
				System.setProperty((String) entry.getKey(), (String) entry.getValue());
			}
		}
	}

	public String getName() {
		return mxbean_.getName();
	}

	public String getSpecName() {
		return mxbean_.getSpecName();
	}

	public String getSpecVendor() {
		return mxbean_.getSpecVendor();
	}

	public String getSpecVersion() {
		return mxbean_.getSpecVersion();
	}

	public String getManagementSpecVersion() {
		return mxbean_.getManagementSpecVersion();
	}

	public String getVmName() {
		return mxbean_.getVmName();
	}

	public String getVmVendor() {
		return mxbean_.getVmVendor();
	}

	public String getVmVersion() {
		return mxbean_.getVmVersion();
	}

	public List getInputArguments() {
		return mxbean_.getInputArguments();
	}

	public Map getSystemProperties() {
		return mxbean_.getSystemProperties();
	}

	public boolean getBootClassPathSupported() {
		return mxbean_.isBootClassPathSupported();
	}

	public String getBootClassPath() {
		return mxbean_.getBootClassPath();
	}

	public String getClassPath() {
		return mxbean_.getClassPath();
	}

	public String getLibraryPath() {
		return mxbean_.getLibraryPath();
	}

	public long getStartTime() {
		return mxbean_.getStartTime();
	}

	public long getUptime() {
		return mxbean_.getUptime();
	}

	public boolean isManagementSupported() {
		return true;
	}

	public String getSystemPropertiesAsText() {
		StringBuffer b = new StringBuffer();
		Iterator i = System.getProperties().entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry) i.next();
			b.append(entry.getKey()).append("=").append(entry.getValue())
					.append("\n");
		}
		return b.toString();
	}

	public void start() {
	}

	public void stop() {
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("Name: ").append(getName()).append("\n");
		b.append("Specification Name: ").append(getSpecName()).append("\n");
		b.append("Specification Vendor: ").append(getSpecVendor()).append("\n");
		b.append("Specification Version: ").append(getSpecVersion()).append("\n");
		b.append("Management Spec Version: ").append(getManagementSpecVersion()).append("\n\n");

		b.append("Virtual Machine Name: ").append(getVmName()).append("\n");
		b.append("Virtual Machine Vendor: ").append(getVmVendor()).append("\n");
		b.append("Virtual Machine Version: ").append(getVmVersion()).append("\n\n");

		b.append("Input Arguments: ").append(getInputArguments()).append("\n");
		b.append("System Properties: ").append(getSystemProperties()).append("\n\n");

		b.append("Boot Class Path Support: ").append(getBootClassPathSupported()).append("\n");
		b.append("Boot Class Path: ").append(getBootClassPath()).append("\n");
		b.append("Class Path: ").append(getClassPath()).append("\n");
		b.append("Library Path: ").append(getLibraryPath()).append("\n\n");

		b.append("Start Time: ").append(new Date(getStartTime())).append("\n");
		b.append("Up Time: ").append(getUptime() / (1000 * 60)).append("min\n");
		return b.toString();
	}
}
