package org.etk.orm.plugins.mapper;

import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.PropertyMapping;

public abstract class RelatedPropertyMapper<P extends PropertyInfo<V, K>, V extends ValueInfo, C extends ObjectContext<C>, K extends ValueKind>
                                                                                                                                                extends
                                                                                                                                                PropertyMapper<P, V, C, K> {

  protected RelatedPropertyMapper(Class<C> contextType, PropertyMapping<P, V, K> info) {
    super(contextType, info);
  }

  public abstract Class<?> getRelatedClass();

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[name=" + info.getProperty().getName() + "]";
  }
}
