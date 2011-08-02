package org.etk.core.rest.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Path;

import org.etk.core.rest.ComponentLifecycleScope;
import org.etk.core.rest.FieldInjector;
import org.etk.core.rest.FilterDescriptor;
import org.etk.core.rest.impl.resource.PathValue;
import org.etk.core.rest.impl.uri.UriPattern;
import org.etk.core.rest.resource.ResourceDescriptorVisitor;


public class FilterDescriptorImpl implements FilterDescriptor {

  /**
   * Filter class.
   */
  private final Class<?>                  filterClass;

  /**
   * @see PathValue
   */
  private final PathValue                 path;

  /**
   * @see UriPattern
   */
  private final UriPattern                uriPattern;

  /**
   * Filter class constructors.
   * 
   * @see ConstructorDescriptor
   */
  private final List<ConstructorDescriptor> constructors;

  /**
   * Filter class fields.
   */
  private final List<FieldInjector>       fields;

  /**
   * @param filterClass {@link Class} of filter
   */
  public FilterDescriptorImpl(Class<?> filterClass) {
    this(filterClass, ComponentLifecycleScope.PER_REQUEST);
  }

  /**
   * @param filter instance
   */
  public FilterDescriptorImpl(Object filter) {
    this(filter.getClass(), ComponentLifecycleScope.SINGLETON);
  }

  /**
   * @param filterClass filter class
   * @param scope filter scope
   * @see ComponentLifecycleScope
   */
  private FilterDescriptorImpl(Class<?> filterClass, ComponentLifecycleScope scope) {
    final Path p = filterClass.getAnnotation(Path.class);
    if (p != null) {
      this.path = new PathValue(p.value());
      this.uriPattern = new UriPattern(p.value());
    } else {
      this.path = null;
      this.uriPattern = null;
    }
    
    this.filterClass = filterClass;
    
    this.constructors = new ArrayList<ConstructorDescriptor>();
    this.fields = new ArrayList<FieldInjector>();
    if (scope == ComponentLifecycleScope.PER_REQUEST) {
      for (Constructor<?> constructor : filterClass.getConstructors()) {
        constructors.add(new ConstructorDescriptorImpl(filterClass, constructor));
      }
      if (constructors.size() == 0) {
        String msg = "Not found accepted constructors for filter class " + filterClass.getName();
        throw new RuntimeException(msg);
      }
      // Sort constructors in number parameters order
      if (constructors.size() > 1) {
        Collections.sort(constructors, ConstructorDescriptorImpl.CONSTRUCTOR_COMPARATOR);
      }
      // process field
      for (java.lang.reflect.Field jfield : filterClass.getDeclaredFields()) {
        fields.add(new FieldInjectorImpl(filterClass, jfield));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void accept(ResourceDescriptorVisitor visitor) {
    visitor.visitFilterDescriptor(this);
  }

  /**
   * {@inheritDoc}
   */
  public List<ConstructorDescriptor> getConstructorDescriptors() {
    return constructors;
  }

  /**
   * {@inheritDoc}
   */
  public List<FieldInjector> getFieldInjectors() {
    return fields;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getObjectClass() {
    return filterClass;
  }

  /**
   * {@inheritDoc}
   */
  public PathValue getPathValue() {
    return path;
  }

  /**
   * {@inheritDoc}
   */
  public UriPattern getUriPattern() {
    return uriPattern;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer("[ FilterDescriptorImpl: ");
    sb.append("path: " + getPathValue() + "; ")
      .append("filter class: " + getObjectClass() + "; ")
      .append(getConstructorDescriptors() + "; ")
      .append(getFieldInjectors())
      .append(" ]");
    return sb.toString();
  }

}
