package org.etk.orm.plugins.bean;

import org.etk.reflect.api.ClassTypeInfo;

/**
 * The bean filter accepts or rejects transitive bean declarations.
 *
 */
public interface BeanFilter {

  /**
   * Decide whether or not to accept the specified type as resolved by the {@link BeanInfoBuilder}.
   * When the type is accepted, it will be modeled as a {@link BeanValueInfo}, otherwise it will be modelled
   * as a {@link SimpleValueInfo}.
   *
   * @param cti the type to accept or reject
   * @return the acceptance
   */
  boolean accept(ClassTypeInfo cti);

}