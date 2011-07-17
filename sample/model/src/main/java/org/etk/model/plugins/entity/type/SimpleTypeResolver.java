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

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.etk.model.plugins.vt2.PropertyMetaType;
import org.etk.orm.api.annotations.SimpleType;
import org.etk.orm.plugins.bean.type.SimpleTypeProvider;
import org.etk.orm.plugins.bean.type.SimpleTypeProviders;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.api.annotation.AnnotationInfo;
import org.etk.reflect.api.annotation.AnnotationParameterInfo;
import org.etk.reflect.api.definition.ClassKind;
import org.etk.reflect.api.definition.LiteralKind;
import org.etk.reflect.core.AnnotationType;
import org.etk.reflect.core.TypeResolverImpl;
import org.etk.reflect.jlr.metadata.JLReflectionMetadata;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public class SimpleTypeResolver {
  /** . */
  static final TypeResolver<Type> typeDomain = TypeResolverImpl.create(JLReflectionMetadata.newInstance());

  /** . */
  private static final SimpleTypeResolver base;

  /** . */
  private static final EnumMap<LiteralKind, TypeInfo> literalWrappers;

  static {

    // The base mappings
    SimpleTypeResolver _base = new SimpleTypeResolver(new HashMap<TypeInfo, PropertyTypeEntry>());

    // Numeric
    _base.add(SimpleTypeProviders.INTEGER.class);
    _base.add(SimpleTypeProviders.LONG.class);
    _base.add(SimpleTypeProviders.BOOLEAN.class);
    _base.add(SimpleTypeProviders.FLOAT.class);
    _base.add(SimpleTypeProviders.DOUBLE.class);

    // String
    _base.add(SimpleTypeProviders.STRING.class);

    // Path
    _base.add(SimpleTypeProviders.PATH.class);

    // Name
    _base.add(SimpleTypeProviders.NAME.class);

    // Binary
    _base.add(SimpleTypeProviders.BINARY.class);
    _base.add(SimpleTypeProviders.BYTE_ARRAY.class);

    // Date
    _base.add(SimpleTypeProviders.DATE.class);
    _base.add(SimpleTypeProviders.CALENDAR.class);
    _base.add(SimpleTypeProviders.TIMESTAMP.class);

    // Primitive unwrapping
    EnumMap<LiteralKind, TypeInfo> _literalWrappers = new EnumMap<LiteralKind, TypeInfo>(LiteralKind.class);
    _literalWrappers.put(LiteralKind.BOOLEAN, typeDomain.resolve(Boolean.class));
    _literalWrappers.put(LiteralKind.INT, typeDomain.resolve(Integer.class));
    _literalWrappers.put(LiteralKind.LONG, typeDomain.resolve(Long.class));
    _literalWrappers.put(LiteralKind.FLOAT, typeDomain.resolve(Float.class));
    _literalWrappers.put(LiteralKind.DOUBLE, typeDomain.resolve(Double.class));

    //
    base = _base;
    literalWrappers = _literalWrappers;
  }

  /** . */
  private final Map<TypeInfo, PropertyTypeEntry> typeMappings;

  private SimpleTypeResolver(Map<TypeInfo, PropertyTypeEntry> typeMappings) {
    this.typeMappings = typeMappings;
  }

  /**
   * The default constructor.
   */
  public SimpleTypeResolver() {
    this(base);
  }

  /**
   * Deep clone constructor.
   *
   * @param that that resolver to clone
   */
  public SimpleTypeResolver(SimpleTypeResolver that) {
    if (that == null) {
      throw new NullPointerException();
    }

    //
    HashMap<TypeInfo, PropertyTypeEntry> typeMappings = new HashMap<TypeInfo, PropertyTypeEntry>();
    for (Map.Entry<TypeInfo, PropertyTypeEntry> entry : that.typeMappings.entrySet()) {
      typeMappings.put(entry.getKey(), new PropertyTypeEntry(entry.getValue()));
    }

    //
    this.typeMappings = typeMappings;
  }

  private synchronized <I, E> void add(Class<? extends SimpleTypeProvider<I, E>> provider) {
    ClassTypeInfo bilto = (ClassTypeInfo)typeDomain.resolve(provider);
    SimpleTypeMappingImpl<I> a = new SimpleTypeMappingImpl<I>(bilto);
    PropertyTypeEntry existing = typeMappings.get(a.external);
    if (existing == null) {
      typeMappings.put(a.external, new PropertyTypeEntry(a));
    } else {
      existing.add(a);
    }
  }

  public synchronized SimpleTypeBinding resolveType(TypeInfo typeInfo) {
    return resolveType(typeInfo, null);
  }

  public synchronized SimpleTypeBinding resolveType(TypeInfo typeInfo,
                                                    PropertyMetaType<?> propertyMT) {
    SimpleTypeBinding jsonType = null;
    if (typeInfo instanceof ClassTypeInfo) {
      ClassTypeInfo cti = (ClassTypeInfo)typeInfo;
      if (cti.getKind() == ClassKind.ENUM) {
        jsonType = new EnumSimpleTypeBinding(cti);
      }
    }

    //
    if (jsonType == null) {
      if (typeInfo instanceof org.etk.reflect.api.SimpleTypeInfo) {
        org.etk.reflect.api.SimpleTypeInfo sti = (org.etk.reflect.api.SimpleTypeInfo)typeInfo;
        if (sti.isPrimitive()) {
          typeInfo = literalWrappers.get(sti.getLiteralType());
        }
      }
      PropertyTypeEntry entry = typeMappings.get(typeInfo);
      if (entry != null) {
        if (propertyMT != null) {
          jsonType = entry.get(propertyMT);
        } else {
          jsonType = entry.getDefault();
        }
      }
    }

    //
    if (jsonType == null) {
      if (typeInfo instanceof ClassTypeInfo) {
        ClassTypeInfo cti = (ClassTypeInfo)typeInfo;
        AnnotationType<AnnotationInfo, ClassTypeInfo> at = AnnotationType.get((ClassTypeInfo)typeDomain.resolve(SimpleType.class));
        AnnotationInfo ai = cti.getDeclaredAnnotation(at);
        if (ai != null) {
          AnnotationParameterInfo param = ai.getParameter("value");
          ClassTypeInfo abc = (ClassTypeInfo)param.getValue();
          SimpleTypeMappingImpl vtii = new SimpleTypeMappingImpl(abc);
          if (propertyMT != null && propertyMT != vtii.getPropertyMetaType()) {
            throw new UnsupportedOperationException("todo " + vtii.getPropertyMetaType() + " " + propertyMT);
          }
          typeMappings.put(typeInfo, new PropertyTypeEntry(vtii));
          jsonType = vtii;
        }
      }
    }

    //
    return jsonType;
  }
}