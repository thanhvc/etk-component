package org.etk.orm.plugins.bean.type;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.etk.orm.api.annotations.SimpleType;
import org.etk.orm.plugins.bean.mapping.jcr.PropertyMetaType;
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

  public synchronized SimpleTypeMapping resolveType(TypeInfo typeInfo) {
    return resolveType(typeInfo, null);
  }

  public synchronized SimpleTypeMapping resolveType(
    TypeInfo typeInfo,
    PropertyMetaType<?> propertyMT) {
    SimpleTypeMapping jcrType = null;
    if (typeInfo instanceof ClassTypeInfo) {
      ClassTypeInfo cti = (ClassTypeInfo)typeInfo;
      if (cti.getKind() == ClassKind.ENUM) {
        jcrType = new EnumSimpleTypeMapping(cti);
      }
    }

    //
    if (jcrType == null) {
      if (typeInfo instanceof org.etk.reflect.api.SimpleTypeInfo) {
        org.etk.reflect.api.SimpleTypeInfo sti = (org.etk.reflect.api.SimpleTypeInfo)typeInfo;
        if (sti.isPrimitive()) {
          typeInfo = literalWrappers.get(sti.getLiteralType());
        }
      }
      PropertyTypeEntry entry = typeMappings.get(typeInfo);
      if (entry != null) {
        if (propertyMT != null) {
          jcrType = entry.get(propertyMT);
        } else {
          jcrType = entry.getDefault();
        }
      }
    }

    //
    if (jcrType == null) {
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
          jcrType = vtii;
        }
      }
    }

    //
    return jcrType;
  }
}