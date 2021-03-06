package org.etk.core.rest.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.RuntimeDelegate;

import org.etk.common.logging.Logger;
import org.etk.core.rest.PerRequestObjectFactory;
import org.etk.core.rest.SingletonObjectFactory;
import org.etk.core.rest.impl.resource.AbstractResourceDescriptorImpl;
import org.etk.core.rest.impl.resource.ResourceDescriptorValidator;
import org.etk.core.rest.impl.uri.UriPattern;
import org.etk.core.rest.resource.AbstractResourceDescriptor;
import org.etk.core.rest.resource.ResourceContainer;
import org.etk.core.rest.resource.ResourceDescriptorVisitor;
import org.etk.kernel.container.KernelContainer;
import org.etk.kernel.container.KernelContainerContext;

/**
 * Lookup for root resource eXo container components at startup and
 * register/unregister resources via specified methods.
 * 
 *
 */
public final class ResourceBinder {

  /**
   * Logger.
   */
  private static final Logger LOG = Logger.getLogger(ResourceBinder.class);

  private static final Comparator<ObjectFactory<AbstractResourceDescriptor>> RESOURCE_COMPARATOR = new ResourceComparator();

  /**
   * Compare two {@link SingletonResourceFactory}.
   */
  private static final class ResourceComparator implements Comparator<ObjectFactory<AbstractResourceDescriptor>> {
    /**
     * Compare two ResourceClass for order.
     * 
     * @param o1 first ResourceClass to be compared
     * @param o2 second ResourceClass to be compared
     * @return positive , zero or negative dependent of {@link UriPattern}
     *         comparison
     * @see Comparator#compare(Object, Object)
     * @see UriPattern
     * @see UriPattern#URIPATTERN_COMPARATOR
     */
    public int compare(ObjectFactory<AbstractResourceDescriptor> o1, ObjectFactory<AbstractResourceDescriptor> o2) {
      return UriPattern.URIPATTERN_COMPARATOR.compare(o1.getObjectModel().getUriPattern(),
                                                      o2.getObjectModel().getUriPattern());
    }
  };

  /**
   * Root resource descriptors.
   */
  private final List<ObjectFactory<AbstractResourceDescriptor>> rootResources = new ArrayList<ObjectFactory<AbstractResourceDescriptor>>();

  /**
   * Validator.
   */
  private final ResourceDescriptorVisitor rdv = ResourceDescriptorValidator.getInstance();

  private final ProviderBinder providers;

  private int size = 0;

  /**
   * @see RuntimeDelegate
   */
  private final RuntimeDelegate rd;

  /**
   * @param containerContext eXo container context
   * @throws Exception if can't set instance of {@link RuntimeDelegate}
   */
  @SuppressWarnings("unchecked")
  public ResourceBinder(KernelContainerContext containerContext) throws Exception {
    // Initialize RuntimeDelegate instance
    // This is first component in life cycle what needs.
    // TODO better solution to initialize RuntimeDelegate
    rd = new RuntimeDelegateImpl();
    RuntimeDelegate.setInstance(rd);
    providers = ProviderBinder.getInstance();
    
    KernelContainer container = containerContext.getContainer();

    // Lookup Applications
    List<Application> al = container.getComponentInstancesOfType(Application.class);
    for (Application a : al) {
      try {
        addApplication(a);
      } catch (Exception e) {
        LOG.error("Failed add JAX-RS application " + a.getClass().getName(), e);
      }
    }

    // Lookup all object which implements ResourceContainer interface and
    // process them to be add as root resources.
    for (Object resource : container.getComponentInstancesOfType(ResourceContainer.class)) {
      bind(resource);
    }

  }

  /**
   * @param application Application
   * @see Application
   */
  @SuppressWarnings("unchecked")
  public void addApplication(Application application) {
    for (Object obj : application.getSingletons()) {
      if (obj.getClass().getAnnotation(Provider.class) != null) {
        // singleton provider
        if (obj instanceof ContextResolver) {
          providers.addContextResolver((ContextResolver) obj);
        }
        if (obj instanceof ExceptionMapper) {
          providers.addExceptionMapper((ExceptionMapper) obj);
        }
        if (obj instanceof MessageBodyReader) {
          providers.addMessageBodyReader((MessageBodyReader) obj);
        }
        if (obj instanceof MessageBodyWriter) {
          providers.addMessageBodyWriter((MessageBodyWriter) obj);
        }
      } else {
        bind(obj); // singleton resource
      }
    }
    for (Class clazz : application.getClasses()) {
      if (clazz.getAnnotation(Provider.class) != null) {
        // per-request provider
        if (ContextResolver.class.isAssignableFrom(clazz)) {
          providers.addContextResolver(clazz);
        }
        if (ExceptionMapper.class.isAssignableFrom(clazz)) {
          providers.addExceptionMapper(clazz);
        }
        if (MessageBodyReader.class.isAssignableFrom(clazz)) {
          providers.addMessageBodyReader(clazz);
        }
        if (MessageBodyWriter.class.isAssignableFrom(clazz)) {
          providers.addMessageBodyWriter(clazz);
        }
      } else {
        bind(clazz); // per-request resource
      }
    }
  }

  /**
   * Register supplied Object as root resource if it has valid JAX-RS
   * annotations and no one resource with the same UriPattern already
   * registered.
   * 
   * @param resource candidate to be root resource
   * @return true if resource was bound and false if resource was not bound
   *         cause it is not root resource
   */
  public boolean bind(final Object resource) {
    final Path path = resource.getClass().getAnnotation(Path.class);

    AbstractResourceDescriptor descriptor = null;
    if (path != null) {
      try {
        descriptor = new AbstractResourceDescriptorImpl(resource);
      } catch (Exception e) {
        String msg = "Unexpected error occurs when process resource class "
            + resource.getClass().getName();
        LOG.error(msg, e);
        return false;
      }
    } else {
      String msg = "Resource class " + resource.getClass().getName() + " it is not root resource. "
          + "Path annotation javax.ws.rs.Path is not specified for this class.";
      LOG.warn(msg);
      return false;
    }

    // validate AbstractResourceDescriptor
    try {
      descriptor.accept(rdv);
    } catch (Exception e) {
      LOG.error("Validation of root resource failed. ", e);
      return false;
    }

    synchronized (rootResources) {
      // check does exist other resource with the same URI pattern
      for (ObjectFactory<AbstractResourceDescriptor> exist : rootResources) {
        if (exist.getObjectModel().getUriPattern().equals(descriptor.getUriPattern())) {
          String msg = "Resource class " + descriptor.getObjectClass().getName()
              + " can't be registered. Resource class " + exist.getClass().getName()
              + " with the same pattern " + exist.getObjectModel().getUriPattern().getTemplate()
              + " already registered.";
          LOG.warn(msg);
          return false;
        }
      }

      // Singleton resource
      ObjectFactory<AbstractResourceDescriptor> res = new SingletonObjectFactory<AbstractResourceDescriptor>(descriptor, resource);
      rootResources.add(res);
      Collections.sort(rootResources, RESOURCE_COMPARATOR);
      LOG.info("Bind new resource " + res.getObjectModel().getUriPattern().getTemplate() + " : "
          + descriptor.getObjectClass());
    }
    size++;
    return true;
  }

  /**
   * @param resourceClass class of candidate to be root resource
   * @return true if resource was bound and false if resource was not bound
   *         cause it is not root resource
   */
  public boolean bind(final Class<?> resourceClass) {
    final Path path = resourceClass.getAnnotation(Path.class);

    AbstractResourceDescriptor descriptor = null;
    if (path != null) {
      try {
        descriptor = new AbstractResourceDescriptorImpl(resourceClass);
      } catch (Exception e) {
        String msg = "Unexpected error occurs when process resource class "
            + resourceClass.getName();
        LOG.error(msg, e);
        return false;
      }
    } else {
      String msg = "Resource class " + resourceClass.getName() + " it is not root resource. "
          + "Path annotation javax.ws.rs.Path is not specified for this class.";
      LOG.warn(msg);
      return false;
    }

    // validate AbstractResourceDescriptor
    try {
      descriptor.accept(rdv);
    } catch (Exception e) {
      LOG.error("Validation of root resource failed. ", e);
      return false;
    }

    synchronized (rootResources) {
      // check does exist other resource with the same URI pattern
      for (ObjectFactory<AbstractResourceDescriptor> exist : rootResources) {
        AbstractResourceDescriptor existDescriptor = exist.getObjectModel();
        if (exist.getObjectModel().getUriPattern().equals(descriptor.getUriPattern())) {

          String msg = "Resource class " + descriptor.getObjectClass().getName()
              + " can't be registered. Resource class "
              + existDescriptor.getObjectClass().getName() + " with the same pattern "
              + exist.getObjectModel().getUriPattern().getTemplate() + " already registered.";
          LOG.warn(msg);
          return false;
        }
      }
      // per-request resource
      ObjectFactory<AbstractResourceDescriptor> res = new PerRequestObjectFactory<AbstractResourceDescriptor>(descriptor);
      rootResources.add(res);
      Collections.sort(rootResources, RESOURCE_COMPARATOR);
      LOG.info("Bind new resource " + res.getObjectModel().getUriPattern().getRegex() + " : "
          + resourceClass);
    }
    size++;
    return true;
  }

  /**
   * Remove root resource of supplied class from root resource collection.
   * 
   * @param clazz root resource class
   * @return true if resource was unbound false otherwise
   */
  @SuppressWarnings("unchecked")
  public boolean unbind(Class clazz) {
    synchronized (rootResources) {
      Iterator<ObjectFactory<AbstractResourceDescriptor>> i = rootResources.iterator();
      while (i.hasNext()) {
        ObjectFactory<AbstractResourceDescriptor> res = i.next();
        Class c = res.getObjectModel().getObjectClass();
        if (clazz.equals(c)) {
          i.remove();
          LOG.info("Remove ResourceContainer " + res.getObjectModel().getUriPattern().getTemplate()
              + " : " + c);
          size--;
          return true;
        }
      }
      return false;
    }
  }

  public boolean unbind(String uriTemplate) {
    synchronized (rootResources) {
      Iterator<ObjectFactory<AbstractResourceDescriptor>> i = rootResources.iterator();
      while (i.hasNext()) {
        ObjectFactory<AbstractResourceDescriptor> res = i.next();
        String t = res.getObjectModel().getUriPattern().getTemplate();
        if (t.equals(uriTemplate)) {
          i.remove();
          LOG.info("Remove ResourceContainer " + res.getObjectModel().getUriPattern().getTemplate());
          size--;
          return true;
        }
      }
      return false;
    }
  }

  /**
   * Clear the list of ResourceContainer description.
   */
  public void clear() {
    rootResources.clear();
    size = 0;
  }

  /**
   * @return all registered root resources
   */
  public List<ObjectFactory<AbstractResourceDescriptor>> getResources() {
    return rootResources;
  }

  /**
   * @return number of bound resources
   */
  public int getSize() {
    return size;
  }

  /**
   * @return all registered root resources
   */
  @Deprecated
  public List<AbstractResourceDescriptor> getRootResources() {
    List<AbstractResourceDescriptor> l = new ArrayList<AbstractResourceDescriptor>(rootResources.size());
    synchronized (rootResources) {
      for (ObjectFactory<AbstractResourceDescriptor> f : rootResources)
        l.add(f.getObjectModel());
    }
    return l;
  }

}

