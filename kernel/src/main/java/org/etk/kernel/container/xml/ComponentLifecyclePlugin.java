package org.etk.kernel.container.xml;

import java.net.URL;

import org.etk.kernel.container.configuration.ConfigurationManagerImpl;
import org.jibx.runtime.IMarshallingContext;

public class ComponentLifecyclePlugin {

	final URL documentURL;

	private String type;

	private ManageableComponents manageableComponents;

	private InitParams initParams;

	public ComponentLifecyclePlugin() {
		documentURL = ConfigurationManagerImpl.getCurrentURL();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ManageableComponents getManageableComponents() {
		return manageableComponents;
	}

	public void setManageableComponents(ManageableComponents mc) {
		manageableComponents = mc;
	}

	public InitParams getInitParams() {
		return initParams;
	}

	public void setInitParams(InitParams initParams) {
		this.initParams = initParams;
	}

	public void preGet(IMarshallingContext ictx) {
		ConfigurationMarshallerUtil.addURLToContent(documentURL, ictx);
	}
}
