package org.etk.orm.plugins.mapper.onetomany.hierarchical;

import org.etk.orm.core.EntityContext;
import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.BeanValueInfo;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.RelationshipMapping;
import org.etk.orm.plugins.mapper.JCRNodeCollectionPropertyMapper;

public class JCRAnyChildParentPropertyMapper<O extends ObjectContext<O>, K extends ValueKind.Multi>
                                                   extends JCRNodeCollectionPropertyMapper<PropertyInfo<BeanValueInfo, K>, O, K> {

  /** . */
  private final AnyChildMultiValueMapper<K> valueMapper;

  /** . */
  private final String                      prefix;

  public JCRAnyChildParentPropertyMapper(Class<O> contextType,
                                         RelationshipMapping.OneToMany.Hierarchic<K> info,
                                         AnyChildMultiValueMapper<K> valueMapper) throws ClassNotFoundException {
    super(contextType, info);

    //
    this.valueMapper = valueMapper;
    this.prefix = info.getPrefix();
  }

  // Maybe use generic type here of the multivalue kind
  @Override
  public Object get(O context) throws Throwable {
    EntityContext entity = context.getEntity();
    Object collection = entity.getAttribute(this);
    if (collection == null) {
      collection = valueMapper.createValue(context.getEntity(), prefix, getRelatedClass());
      entity.setAttribute(this, collection);
    }
    return collection;
  }
}
