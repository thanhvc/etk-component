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
package org.etk.model.plugins.entity.type;

import java.util.HashMap;
import java.util.Map;

import org.etk.model.plugins.vt2.PropertyMetaType;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
/**
 * This code is synchronized. Normally it should not have performance impact on runtime, i.e
 * this should not be used at runtime and the result should be cached somewhere in the runtime layer.
 *
 */
class PropertyTypeEntry {

  /** . */
  private final SimpleTypeMappingImpl<?> defaultValueTypeInfo;

  /** . */
  private final Map<PropertyMetaType<?>, SimpleTypeMappingImpl<?>> metaTypeMapping;

  PropertyTypeEntry(PropertyTypeEntry that) {
    this.defaultValueTypeInfo = that.defaultValueTypeInfo;
    this.metaTypeMapping = new HashMap<PropertyMetaType<?>, SimpleTypeMappingImpl<?>>(that.metaTypeMapping);
  }

  PropertyTypeEntry(SimpleTypeMappingImpl<?> defaultValueTypeInfo) {
    Map<PropertyMetaType<?>, SimpleTypeMappingImpl<?>> metaTypeMapping = new HashMap<PropertyMetaType<?>, SimpleTypeMappingImpl<?>>();
    metaTypeMapping.put(defaultValueTypeInfo.getPropertyMetaType(), defaultValueTypeInfo);

    //
    this.defaultValueTypeInfo = defaultValueTypeInfo;
    this.metaTypeMapping = metaTypeMapping;
  }

  public SimpleTypeMappingImpl<?> getDefault() {
    return defaultValueTypeInfo;
  }

  public synchronized <I> SimpleTypeMappingImpl<I> add(SimpleTypeMappingImpl<I> valueType) {
    if (!valueType.external.equals(defaultValueTypeInfo.external)) {
      throw new IllegalArgumentException("Was expecting those types to be equals " + valueType.external + " " + defaultValueTypeInfo.external);
    }
    metaTypeMapping.put(valueType.getPropertyMetaType(), valueType);
    return valueType;
  }

  public synchronized <I> SimpleTypeMappingImpl<I> get(PropertyMetaType<I> propertyMT) {
    return (SimpleTypeMappingImpl<I>)metaTypeMapping.get(propertyMT);
  }

  public synchronized SimpleTypeMappingImpl<?> resolve(PropertyMetaType<?> propertyMT) {
    SimpleTypeMappingImpl<?> valueTypeInfo = metaTypeMapping.get(propertyMT);
    if (valueTypeInfo == null) {
      valueTypeInfo = defaultValueTypeInfo;
    }
    return valueTypeInfo;
  }
}
