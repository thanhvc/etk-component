package org.etk.orm.plugins.mapper.property;

import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.SimpleValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.ValueMapping;
import org.etk.orm.plugins.bean.mapping.jcr.PropertyMetaType;
import org.etk.orm.plugins.bean.type.SimpleTypeProvider;
import org.etk.orm.plugins.mapper.PropertyMapper;
import org.etk.orm.plugins.vt2.ValueDefinition;

public class JCRPropertySingleValuedPropertyMapper<O extends ObjectContext<O>, E, I> 
       extends PropertyMapper<PropertyInfo<SimpleValueInfo<ValueKind.Single>, ValueKind.Single>, SimpleValueInfo<ValueKind.Single>, O, ValueKind.Single> {

  /** . */
  private final String jcrPropertyName;

  /** . */
  private final ValueDefinition<I, E> vt;

  public JCRPropertySingleValuedPropertyMapper(
      Class<O> contextType,
      SimpleTypeProvider<I, E> vt,
      ValueMapping<ValueKind.Single> info) {
    super(contextType, info);

    //
    this.jcrPropertyName = info.getPropertyDefinition().getName();
    this.vt = new ValueDefinition<I, E>(
        (Class)info.getValue().getEffectiveType().unwrap(),
        (PropertyMetaType<I>)info.getPropertyDefinition().getMetaType(),
        vt,
        info.getPropertyDefinition().getDefaultValue());
  }

  @Override
  public Object get(O context) throws Throwable {
    return get(context, vt);
  }

  private <V> V get(O context, ValueDefinition<?, V> d) throws Throwable {
    return context.getPropertyValue(jcrPropertyName, d);
  }

  @Override
  public void set(O context, Object o) throws Throwable {
    set(context, vt, o);
  }

  private <V> void set(O context, ValueDefinition<?, V> vt, Object o) throws Throwable {
    Class<V> javaType = vt.getObjectType();
    if (o == null) {
      context.setPropertyValue(jcrPropertyName, vt, null);
    } else if (javaType.isInstance(o)) {
      V v = javaType.cast(o);
      context.setPropertyValue(jcrPropertyName, vt, v);
    } else {
      throw new ClassCastException("Cannot cast " + o.getClass().getName() + " to " + javaType.getName());
    }
  }
}