package org.etk.core.rest.impl;

import java.util.List;

import org.etk.core.rest.FieldInjector;

/**
 * Abstract description of object.
 * 
 */
public interface ObjectModel {

  /**
   * @return collections constructor, MAY return empty collection or null if
   *         object is singleton. There is no setter for this to add new
   *         ConstructorInjector use
   *         <code>ObjectModel.getConstructorDescriptors().add(ConstructorInjector)</code>
   */
  List<ConstructorDescriptor> getConstructorDescriptors();

  /**
   * @return collections of object fields, MAY return empty collection or null
   *         if object is singleton. There is no setter for this to add new
   *         ConstructorInjector use
   *         <code>ObjectModel.getFieldInjectors().add(FieldInjector)</code>
   */
  List<FieldInjector> getFieldInjectors();

  /**
   * @return {@link Class} of object
   */
  Class<?> getObjectClass();

}