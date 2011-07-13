package org.etk.orm.plugins.bean.type;

import org.etk.orm.plugins.bean.mapping.jcr.PropertyMetaType;


/**
 * The mapping between a Table.Field property type and a simple type. A simple type is any class
 * that can be converted back and forth to a particular JCR type.
 *
 */
public interface SimpleTypeMapping {

  /**
   * Returns the property meta type.
   *
   * @return the property meta type
   */
  PropertyMetaType<?> getPropertyMetaType();

  /**
   * Create a simple type provider for this mapping. Note that this operation is only guaranted during the runtime
   * phase as it requires to load the {@code org.chromattic.spi.type.SimpleTypeProvider} class.
   *
   * @return the simple type provider
   */
  SimpleTypeProvider<?, ?> create();

}
