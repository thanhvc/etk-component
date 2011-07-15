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

import java.util.List;

import org.etk.model.core.ObjectContext;
import org.etk.model.plugins.entity.PropertyInfo;
import org.etk.model.plugins.entity.SimpleValueInfo;
import org.etk.model.plugins.entity.binder.PropertyBinder;
import org.etk.model.plugins.entity.binding.ValueBinding;
import org.etk.orm.core.ListType;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.jcr.PropertyMetaType;
import org.etk.orm.plugins.bean.type.SimpleTypeProvider;
import org.etk.orm.plugins.vt2.ValueDefinition;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public class JSONPropertyMultiValuedPropertyBinder<O extends ObjectContext<O>, E, I, K extends ValueKind.Multi>
                         extends PropertyBinder<PropertyInfo<SimpleValueInfo<K>, ValueKind.Single>, SimpleValueInfo<K>, O, ValueKind.Single> {

  /** . */
  private final String jcrPropertyName;

  /** . */
  private final ListType listType;

  /** . */
  private final SimpleValueInfo elementType;

  /** . */
  private final ValueDefinition<I, E> vt;

  public JSONPropertyMultiValuedPropertyBinder(
      Class<O> contextType,
      SimpleTypeProvider<I, E> vt,
      ValueBinding<K> info) {
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