package org.etk.core.rest.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.etk.common.logging.Logger;
import org.etk.core.rest.ConstructorParameter;
import org.etk.core.rest.ConstructorParameterImpl;
import org.etk.core.rest.impl.method.ParameterHelper;
import org.etk.core.rest.impl.method.ParameterResolver;
import org.etk.core.rest.impl.method.ParameterResolverFactory;
import org.etk.core.rest.resource.ResourceDescriptorVisitor;
import org.etk.kernel.container.KernelContainer;
import org.etk.kernel.container.KernelContainerContext;

public class ConstructorDescriptorImpl implements ConstructorDescriptor {

  /**
   * Logger.
   */
  private static final Logger                            LOG                    = Logger.getLogger(ConstructorDescriptorImpl.class);

  /**
   * ConstructorDescriptor comparator.
   */
  public static final Comparator<ConstructorDescriptor> CONSTRUCTOR_COMPARATOR = new ConstructorComparator();

  /**
   * Compare two ConstructorDescriptor in number parameters order.
   */
  private static class ConstructorComparator implements Comparator<ConstructorDescriptor> {
    
    /**
     * {@inheritDoc}
     */
    public int compare(ConstructorDescriptor o1, ConstructorDescriptor o2) {
      int r = o2.getParameters().size() - o1.getParameters().size();
      if (r == 0)
        LOG.warn("Two constructors with the same number of parameter found " + o1 + " and " + o2);
      return r;
    }
  }

  /**
   * Constructor.
   */
  private final Constructor<?>             constructor;

  /**
   * Collection of constructor's parameters.
   */
  private final List<ConstructorParameter> parameters;

  /**
   * Resource class.
   */
  private final Class<?>                   resourceClass;

  /**
   * @param resourceClass resource class
   * @param constructor {@link Constructor}
   */
  public ConstructorDescriptorImpl(Class<?> resourceClass, Constructor<?> constructor) {
    this.resourceClass = resourceClass;
    this.constructor = constructor;

    Class<?>[] paramTypes = constructor.getParameterTypes();

    if (paramTypes.length == 0) {

      parameters = java.util.Collections.emptyList();

    } else {

      Type[] getParamTypes = constructor.getGenericParameterTypes();
      Annotation[][] annotations = constructor.getParameterAnnotations();
      List<ConstructorParameter> params = new ArrayList<ConstructorParameter>(paramTypes.length);

      for (int i = 0; i < paramTypes.length; i++) {

        String defaultValue = null;
        Annotation annotation = null;
        boolean encoded = false;

        // is resource provider
        boolean provider = resourceClass.getAnnotation(Provider.class) != null;
        List<String> allowedAnnotation;
        if (provider)
          allowedAnnotation = ParameterHelper.PROVIDER_CONSTRUCTOR_PARAMETER_ANNOTATIONS;
        else
          allowedAnnotation = ParameterHelper.RESOURCE_CONSTRUCTOR_PARAMETER_ANNOTATIONS;

        for (Annotation a : annotations[i]) {
          Class<?> ac = a.annotationType();
          if (allowedAnnotation.contains(ac.getName())) {

            if (annotation == null) {
              annotation = a;
            } else {
              String msg = "JAX-RS annotations on one of constructor parameters are equivocality. "
                  + "Annotations: " + annotation + " and " + a
                  + " can't be applied to one parameter.";
              throw new RuntimeException(msg);
            }

            // @Encoded has not sense for Provider. Provider may use only
            // @Context annotation for constructor parameters
          } else if (ac == Encoded.class && !provider) {
            encoded = true;
            // @Default has not sense for Provider. Provider may use only
            // @Context annotation for constructor parameters
          } else if (ac == DefaultValue.class && !provider) {
            defaultValue = ((DefaultValue) a).value();
          } else {
            LOG.warn("Constructor parameter contains unknown or not valid JAX-RS annotation " + a
                + ". It will be ignored.");
          }
        }

        encoded = encoded || resourceClass.getAnnotation(Encoded.class) != null;

        ConstructorParameter cp = new ConstructorParameterImpl(annotation,
                                                               annotations[i],
                                                               paramTypes[i],
                                                               getParamTypes[i],
                                                               defaultValue,
                                                               encoded);

        params.add(cp);
      }

      parameters = java.util.Collections.unmodifiableList(params);
    }

  }

  /**
   * {@inheritDoc}
   */
  public void accept(ResourceDescriptorVisitor visitor) {
    visitor.visitConstructorInjector(this);
  }

  /**
   * {@inheritDoc}
   */
  public Constructor<?> getConstructor() {
    return constructor;
  }

  /**
   * {@inheritDoc}
   */
  public List<ConstructorParameter> getParameters() {
    return parameters;
  }

  /**
   * {@inheritDoc}
   */
  public Object createInstance(ApplicationContext context) {
    KernelContainer container = KernelContainerContext.getCurrentContainer();
    Object[] p = new Object[parameters.size()];
    int i = 0;
    for (ConstructorParameter cp : parameters) {
      Annotation a = cp.getAnnotation();
      if (a != null) {
        ParameterResolver<?> pr = ParameterResolverFactory.createParameterResolver(a);
        try {
          p[i] = pr.resolve(cp, context);
        } catch (Exception e) {

          if (LOG.isDebugEnabled())
            e.printStackTrace();

          Class<?> ac = a.annotationType();
          if (ac == MatrixParam.class || ac == QueryParam.class || ac == PathParam.class)
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());

          throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build());
        }
      } else {
        // If parameter not has not annotation then get constructor parameter
        // from container, this is out of scope JAX-RS specification.
        Object tmp = container.getComponentInstanceOfType(cp.getParameterClass());

        if (tmp == null) {
          String msg = "Can't instantiate resource " + resourceClass + " by using constructor "
              + this + ". Not found parameter " + cp;
          throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                                    .entity(msg)
                                                    .build());
        }

        p[i] = tmp;
      }

      i++;
    }

    try {
      return constructor.newInstance(p);
    } catch (IllegalArgumentException argExc) {
      // should not be thrown, arguments already checked
      throw new InternalException(argExc);
    } catch (InstantiationException instExc) {
      // should not be thrown
      throw new InternalException(instExc);
    } catch (IllegalAccessException accessExc) {
      // should not be thrown
      throw new InternalException(accessExc);
    } catch (InvocationTargetException invExc) {
      // constructor may produce exceptions
      if (LOG.isDebugEnabled())
        invExc.printStackTrace();
      // get cause of exception that method produces
      Throwable cause = invExc.getCause();
      // if WebApplicationException than it may contain response
      if (WebApplicationException.class == cause.getClass())
        throw (WebApplicationException) cause;

      throw new InternalException(cause);
    } catch (Throwable thr) {
      throw new InternalException(thr);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer("[ ConstructorInjectorImpl: ");
    sb.append("constructor: " + getConstructor().getName() + "; ");
    for (ConstructorParameter cp : getParameters())
      sb.append(cp.toString()).append(" ");
    sb.append(" ]");
    return sb.toString();
  }

}

