package org.etk.kernel.container.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExternalComponentPlugins {

	private String targetComponent;

	/**
	 * Indicates whether it has to be sorted or not
	 */
	private boolean dirty;

	private ArrayList<ComponentPlugin> componentPlugins;

	public String getTargetComponent() {
		return targetComponent;
	}

	public void setTargetComponent(String s) {
		targetComponent = s;
	}

	public List<ComponentPlugin> getComponentPlugins() {
		if (dirty && componentPlugins != null) {
			// Sort the list of component plugins first
			Collections.sort(componentPlugins);
			dirty = false;
		}
		return componentPlugins;
	}

	public void setComponentPlugins(ArrayList<ComponentPlugin> list) {
		componentPlugins = list;
		dirty = true;
	}

	public void merge(ExternalComponentPlugins other) {
		if (other == null)
			return;
		List<ComponentPlugin> otherPlugins = other.getComponentPlugins();
		if (otherPlugins == null)
			return;
		if (componentPlugins == null)
			componentPlugins = new ArrayList<ComponentPlugin>();
		componentPlugins.addAll(otherPlugins);
		dirty = true;
	}
}
