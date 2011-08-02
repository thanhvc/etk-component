package org.etk.core.rest.impl;

import org.etk.core.rest.resource.ResourceDescriptorVisitor;

/**
 * Common essence for all resource descriptors.
 * 
 */
public interface ResourceDescriptor {

  /**
   * Method is useful for validation.
   * 
   * @param visitor See {@link ResourceDescriptorVisitor}
   */
  void accept(ResourceDescriptorVisitor visitor);

}
