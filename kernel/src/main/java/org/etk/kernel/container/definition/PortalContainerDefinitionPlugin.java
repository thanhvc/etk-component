package org.etk.kernel.container.definition;

import java.util.List;

import org.etk.kernel.container.component.BaseComponentPlugin;
import org.etk.kernel.container.xml.InitParams;

/**
 * This class allows you to dynamically define a new portal container with all
 * its dependencies
 * 
 * Created by The eXo Platform SAS Author : Nicolas Filotto
 * nicolas.filotto@exoplatform.com 8 sept. 2009
 */
public class PortalContainerDefinitionPlugin extends BaseComponentPlugin {

	/**
	 * The initial parameter of this plugin
	 */
	private final InitParams params;

	public PortalContainerDefinitionPlugin(InitParams params) {
		this.params = params;
	}

	/**
	 * @return all the {@link PortalContainerDefinition} related to this plugin
	 */
	public List<PortalContainerDefinition> getPortalContainerDefinitions() {
		return params.getObjectParamValues(PortalContainerDefinition.class);
	}
}
