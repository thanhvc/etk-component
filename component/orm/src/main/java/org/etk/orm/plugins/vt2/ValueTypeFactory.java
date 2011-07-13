package org.etk.orm.plugins.vt2;

import org.etk.orm.plugins.bean.mapping.jcr.PropertyMetaType;
import org.etk.orm.plugins.bean.type.SimpleTypeMapping;
import org.etk.orm.plugins.bean.type.SimpleTypeProvider;
import org.etk.orm.plugins.bean.type.SimpleTypeResolver;
import org.etk.reflect.api.TypeInfo;

public class ValueTypeFactory {
  /** . */
  private final SimpleTypeResolver typeResolver;

  public ValueTypeFactory(SimpleTypeResolver typeResolver) {
    this.typeResolver = typeResolver;
  }

  public <I> SimpleTypeProvider<I, ?> create(TypeInfo type, PropertyMetaType<I> jcrType) {
    SimpleTypeMapping vti = typeResolver.resolveType(type, jcrType);
    if (vti == null) {
      throw new IllegalArgumentException("could not find type provider for " + type);
    }

    //
    SimpleTypeProvider vt = vti.create();

    //
    if (!vt.getInternalType().equals(jcrType.getJavaValueType())) {
      throw new AssertionError("todo with type " + type + " / property type" + vt);
    }

    //
    return (SimpleTypeProvider<I, ?>)vt;
  }
}