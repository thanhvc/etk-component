package org.etk.orm.plugins.mapper;

import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.PropertyMapping;

public abstract class JCRNodePropertyMapper<P extends PropertyInfo<V, ValueKind.Single>, 
                V extends ValueInfo, O extends ObjectContext<O>> extends RelatedPropertyMapper<P, V, O, ValueKind.Single> {

  /** . */
  private final Class relatedClass;

  protected JCRNodePropertyMapper(Class<O> contextType, PropertyMapping<P, V, ValueKind.Single> info) throws ClassNotFoundException {
    super(contextType, info);

    // We use the classloader from the bean
    Class<?> clazz = (Class<Object>) info.getOwner().getBean().getClassType().unwrap();
    ClassLoader cl = clazz.getClassLoader();

    //
    relatedClass = cl.loadClass(info.getValue().getEffectiveType().getName());
  }

  public Class<?> getRelatedClass() {
    return relatedClass;
  }
}
