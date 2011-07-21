package org.etk.orm.plugins.bean.type;

import org.etk.orm.plugins.bean.mapping.jcr.PropertyMetaType;
import org.etk.reflect.api.ClassTypeInfo;

public class EnumSimpleTypeMapping implements SimpleTypeMapping {

  /** . */
  private final ClassTypeInfo enumInfo;

  public EnumSimpleTypeMapping(ClassTypeInfo enumInfo) {
    this.enumInfo = enumInfo;
  }

  public PropertyMetaType<String> getPropertyMetaType() {
    return PropertyMetaType.STRING;
  }

  public SimpleTypeProvider<?, ?> create() {
    // todo : maybe need a cache here?
    Class clazz = (Class<Object>)enumInfo.unwrap();
    return new EnumSimpleTypeProvider(clazz);
  }
}