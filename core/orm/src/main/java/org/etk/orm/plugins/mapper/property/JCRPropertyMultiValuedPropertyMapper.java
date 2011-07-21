package org.etk.orm.plugins.mapper.property;

import java.util.List;

import org.etk.orm.core.ListType;
import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.SimpleValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.ValueMapping;
import org.etk.orm.plugins.bean.mapping.jcr.PropertyMetaType;
import org.etk.orm.plugins.bean.type.SimpleTypeProvider;
import org.etk.orm.plugins.mapper.PropertyMapper;
import org.etk.orm.plugins.vt2.ValueDefinition;


public class JCRPropertyMultiValuedPropertyMapper<O extends ObjectContext<O>, E, I, K extends ValueKind.Multi>
extends PropertyMapper<PropertyInfo<SimpleValueInfo<K>, ValueKind.Single>, SimpleValueInfo<K>, O, ValueKind.Single> {

/** . */
private final String jcrPropertyName;

/** . */
private final ListType listType;

/** . */
private final SimpleValueInfo elementType;

/** . */
private final ValueDefinition<I, E> vt;

public JCRPropertyMultiValuedPropertyMapper(
    Class<O> contextType,
    SimpleTypeProvider<I, E> vt,
    ValueMapping<K> info) {
  super(contextType, info);

  //
  ListType listType;
  ValueKind.Multi valueKind = info.getValue().getValueKind();
  if (valueKind == ValueKind.ARRAY) {
    listType = ListType.ARRAY;
  } else if (valueKind == ValueKind.LIST) {
    listType = ListType.LIST;
  } else {
    throw new AssertionError();
  }

  //
  this.listType = listType;
  this.jcrPropertyName = info.getPropertyDefinition().getName();
  this.elementType = info.getValue();
  this.vt = new ValueDefinition<I, E>((Class)info.getValue().getEffectiveType().unwrap(), (PropertyMetaType<I>)info.getPropertyDefinition().getMetaType(), vt, info.getPropertyDefinition().getDefaultValue());
}

@Override
public Object get(O context) throws Throwable {
  List<E> list = context.getPropertyValues(jcrPropertyName, vt, listType);
  return list == null ? null : listType.unwrap(vt, list);
}

@Override
public void set(O context, Object value) throws Throwable {
  List<E> list = value == null ? null : listType.wrap(vt, value);
  context.setPropertyValues(jcrPropertyName, vt, listType, list);
}
}