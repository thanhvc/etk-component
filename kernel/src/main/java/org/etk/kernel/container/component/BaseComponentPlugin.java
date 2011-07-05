package org.etk.kernel.container.component;


public class BaseComponentPlugin implements ComponentPlugin {

	protected String name;

	protected String desc;

	public String getName() {
		return name;
	}

	public void setName(String s) {
		name = s;
	}

	public String getDescription() {
		return desc;
	}

	public void setDescription(String s) {
		desc = s;
	}
}
