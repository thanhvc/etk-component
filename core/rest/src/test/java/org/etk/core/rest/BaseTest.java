package org.etk.core.rest;

import org.etk.core.rest.impl.ApplicationContextImpl;
import org.etk.core.rest.impl.ProviderBinder;
import org.etk.core.rest.impl.RequestHandlerImpl;
import org.etk.core.rest.impl.ResourceBinder;
import org.etk.kernel.container.ApplicationContainer;
import org.etk.kernel.test.spi.AbstractApplicationTest;

public class BaseTest extends AbstractApplicationTest {

  protected ApplicationContainer container;
  protected ProviderBinder providers;
  protected ResourceBinder binder;
  protected RequestHandlerImpl requestHandler;
  
  public void setUp() throws Exception {
    container = getContainer();
    binder = (ResourceBinder) container.getComponentInstanceOfType(ResourceBinder.class);
    requestHandler = (RequestHandlerImpl) container.getComponentInstanceOfType(RequestHandlerImpl.class);
    ProviderBinder.setInstance(new ProviderBinder());
    providers = ProviderBinder.getInstance();
    ApplicationContextImpl.setCurrent(new ApplicationContextImpl(null, null, providers));
    binder.clear();
  }
  
  public boolean registry(Object resource) throws Exception {
    return binder.bind(resource);
  }
  
  public boolean registry(Class<?> resourceClass) throws Exception {
    return binder.bind(resourceClass);
  }
  
  public boolean unregistry(Object resource) {
    return binder.unbind(resource.getClass());
  }
  
  public boolean unregistry(Class<?> resourceClass) {
    return binder.unbind(resourceClass);
  }
  
  public void testMe() throws Exception {
    assertTrue(true);
  }
}
