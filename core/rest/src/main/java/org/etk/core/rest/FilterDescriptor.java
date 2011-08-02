package org.etk.core.rest;

import org.etk.core.rest.impl.ObjectModel;
import org.etk.core.rest.impl.ResourceDescriptor;
import org.etk.core.rest.impl.resource.PathValue;
import org.etk.core.rest.impl.uri.UriPattern;

/**
 * Description of filter.
 * 
 * @see Filter
 * @see RequestFilter
 * @see ResponseFilter
 * @see MethodInvokerFilter
 */
public interface FilterDescriptor extends ResourceDescriptor, ObjectModel {

  /**
   * @return See {@link PathValue}
   */
  PathValue getPathValue();

  /**
   * UriPattern build in same manner as for resources. For detail see section
   * 3.4 URI Templates in JAX-RS specification.
   * 
   * @return See {@link UriPattern}
   */
  UriPattern getUriPattern();

}
