package org.etk.orm.plugins.mapper.property;

import org.etk.orm.core.EntityContext;
import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.PropertiesMapping;
import org.etk.orm.plugins.mapper.PropertyMapper;


public class JCRPropertyDetypedPropertyMapper<V extends ValueInfo, O extends ObjectContext<O>>
                           extends PropertyMapper<PropertyInfo<V, ValueKind.Map>, V, O, ValueKind.Map> {

  /** . */
  final String namePattern;
  
  /** . */
  final String namePrefix;
  
  /** . */
  final ValueKind valueKind;

  public JCRPropertyDetypedPropertyMapper(Class<O> contextType, PropertiesMapping<V> info) {
    super(contextType, info);

    //
    String prefix = info.getPrefix();
    String namePrefix;
    String namePattern;
    if (prefix != null && prefix.length() > 0) {
      namePrefix = prefix + ":";
      namePattern = prefix + ":*";
    } else {
      namePrefix = null;
      namePattern = null;
    }

    //
    this.namePattern = namePattern;
    this.namePrefix = namePrefix;
    this.valueKind = info.getValueKind();
  }

  @Override
  public Object get(O context) throws Throwable {
    EntityContext entity = context.getEntity();
    Object collection = entity.getAttribute(this);
    if (collection == null) {
      collection = new PropertyMap(this, entity);
      entity.setAttribute(this, collection);
    }
    return collection;
  }
}
