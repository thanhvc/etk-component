package org.etk.orm.plugins.mapper;

import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.BeanValueInfo;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.PropertyMapping;

public abstract class JCRNodeCollectionPropertyMapper<P extends PropertyInfo<BeanValueInfo, K>, O extends ObjectContext<O>, 
                                                                          K extends ValueKind.Multi> extends RelatedPropertyMapper<P, BeanValueInfo, O, K> {

  /** . */
  private final Class relatedClass;

  public JCRNodeCollectionPropertyMapper(Class<O> contextType,
                                         PropertyMapping<P, BeanValueInfo, K> info) throws ClassNotFoundException {
    super(contextType, info);

    // We use the classloader from the bean
    Class<?> clazz = (Class<?>) info.getOwner().getBean().getClassType().unwrap();
    ClassLoader cl = clazz.getClassLoader();

    //
    relatedClass = cl.loadClass(info.getValue().getClassType().getName());
  }

  public Class<?> getRelatedClass() {
    return relatedClass;
  }
}
