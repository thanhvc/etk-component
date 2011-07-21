package org.etk.orm.plugins.mapper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.etk.orm.api.NameConflictResolution;
import org.etk.orm.api.format.ObjectFormatter;
import org.etk.orm.core.MethodInvoker;
import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.mapping.BeanMapping;
import org.etk.orm.plugins.bean.mapping.NodeTypeKind;
import org.etk.orm.plugins.bean.mapping.PropertyMapping;
import org.etk.reflect.api.MethodInfo;

public class ObjectMapper<C extends ObjectContext<C>> {

  /** . */
  private final BeanMapping mapping;

  /** . */
  protected final Class<?> objectClass;

  /** . */
  private final String nodeTypeName;

  /** . */
  final Set<MethodMapper<C>> methodMappers;

  /** . */
  final Set<PropertyMapper<?, ?, C, ?>> propertyMappers;

  /** . */
  private final Map<Method, MethodInvoker<C>> dispatchers;

  /** The optional formatter for this object. */
  private final ObjectFormatter formatter;

  /** . */
  private final NameConflictResolution onDuplicate;

  /** . */
  private final NodeTypeKind kind;

  /** . */
  private final boolean abstract_;

  /** . */
  private final Map<String, PropertyMapper<?, ?, C, ?>> propertyMapperMap;

  public ObjectMapper(
    BeanMapping mapping,
    boolean abstract_,
    Class<?> objectClass,
    Set<PropertyMapper<?, ?, C, ?>> propertyMappers,
    Set<MethodMapper<C>> methodMappers,
    NameConflictResolution onDuplicate,
    ObjectFormatter formatter,
//    Instrumentor instrumentor,
    String typeName,
    NodeTypeKind kind) {

    // Build the mapper map
    Map<String, PropertyMapper<?, ?, C, ?>> propertyMapperMap = new HashMap<String, PropertyMapper<?, ?, C, ?>>();
    for (PropertyMapper<?, ?, C, ?> propertyMapper : propertyMappers) {
      propertyMapperMap.put(propertyMapper.getInfo().getName(), propertyMapper);
    }

    // Build the dispatcher map
    Map<Method, MethodInvoker<C>> dispatchers = new HashMap<Method, MethodInvoker<C>>();
    for (PropertyMapper<?, ?, C, ?> propertyMapper : propertyMappers) {
      PropertyMapping<?, ?, ?> info = propertyMapper.getInfo();
      MethodInfo getter = info.getProperty().getGetter();
      if (getter != null) {
        dispatchers.put((Method)getter.unwrap(), propertyMapper.getGetter());
      }
      MethodInfo setter = info.getProperty().getSetter();
      if (setter != null) {
        dispatchers.put((Method)setter.unwrap(), propertyMapper.getSetter());
      }
    }
    for (MethodMapper<C> methodMapper : methodMappers) {
      dispatchers.put((Method)methodMapper.getMethod().unwrap(), methodMapper);
    }

    //
    this.mapping = mapping;
    this.abstract_ = abstract_;
    this.dispatchers = dispatchers;
    this.objectClass = objectClass;
    this.methodMappers = methodMappers;
    this.formatter = formatter;
    this.onDuplicate = onDuplicate;
    this.propertyMappers = propertyMappers;
    this.nodeTypeName = typeName;
    this.kind = kind;
    this.propertyMapperMap = propertyMapperMap;
  }

  public MethodInvoker<C> getInvoker(Method method) {
    return dispatchers.get(method);
  }

  public BeanMapping getMapping() {
    return mapping;
  }

  public boolean isAbstract() {
    return abstract_; 
  }

  public NodeTypeKind getKind() {
    return kind;
  }

  public String getNodeTypeName() {
    return nodeTypeName;
  }

  public ObjectFormatter getFormatter() {
    return formatter;
  }

  public Set<MethodMapper<C>> getMethodMappers() {
    return methodMappers;
  }

  public Set<PropertyMapper<?, ?, C, ?>> getPropertyMappers() {
    return propertyMappers;
  }

  public PropertyMapper<?, ?, C, ?> getPropertyMapper(String name) {
    return propertyMapperMap.get(name);
  }

  public Class<?> getObjectClass() {
    return objectClass;
  }

  public NameConflictResolution getOnDuplicate() {
    return onDuplicate;
  }

  @Override
  public String toString() {
    return "EntityMapper[class=" + objectClass + ",typeName=" + nodeTypeName + "]";
  }
}