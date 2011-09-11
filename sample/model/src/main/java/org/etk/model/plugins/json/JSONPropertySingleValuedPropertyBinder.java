/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.model.plugins.json;

import org.etk.model.core.ObjectContext;
import org.etk.model.plugins.entity.PropertyInfo;
import org.etk.model.plugins.entity.SimpleValueInfo;
import org.etk.model.plugins.entity.binder.PropertyBinder;
import org.etk.model.plugins.entity.binding.PropertyValueBinding;
import org.etk.model.plugins.vt2.PropertyMetaType;
import org.etk.model.plugins.vt2.ValueDefinition;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.type.SimpleTypeProvider;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public class JSONPropertySingleValuedPropertyBinder <O extends ObjectContext<O>, E, I> 
       extends PropertyBinder<PropertyInfo<SimpleValueInfo<ValueKind.Single>, ValueKind.Single>, SimpleValueInfo<ValueKind.Single>, O, ValueKind.Single> {

  /** . */
  private final String jsonPropertyName;

  /** . */
  private final ValueDefinition<I, E> vt;

  public JSONPropertySingleValuedPropertyBinder(Class<O> contextType,
                                                SimpleTypeProvider<I, E> vt,
                                                PropertyValueBinding<ValueKind.Single> info) {
    super(contextType, info);

    //
    this.jsonPropertyName = info.getPropertyDefinition().getName();
    this.vt = new ValueDefinition<I, E>((Class) info.getValue().getEffectiveType().unwrap(),
                                        (PropertyMetaType<I>) info.getPropertyDefinition().getMetaType(),
                                        vt,
                                        info.getPropertyDefinition().getDefaultValue());
  }

  @Override
  public Object get(O context) throws Throwable {
    return get(context, vt);
  }

  private <V> V get(O context, ValueDefinition<?, V> d) throws Throwable {
    return context.getPropertyValue(jsonPropertyName, d);
  }

  @Override
  public void set(O context, Object o) throws Throwable {
    set(context, vt, o);
  }

  private <V> void set(O context, ValueDefinition<?, V> vt, Object o) throws Throwable {
    Class<V> javaType = vt.getObjectType();
    if (o == null) {
      context.setPropertyValue(jsonPropertyName, vt, null);
    } else if (javaType.isInstance(o)) {
      V v = javaType.cast(o);
      context.setPropertyValue(jsonPropertyName, vt, v);
    } else {
      throw new ClassCastException("Cannot cast " + o.getClass().getName() + " to " + javaType.getName());
    }
  }
}
