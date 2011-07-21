package org.etk.orm.plugins.bean.mapping;

import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;


public abstract class PropertyMapping<P extends PropertyInfo<V, K>, V extends ValueInfo, K extends ValueKind> {

  /** . */
  BeanMapping owner;

  /** The optional parent. */
  PropertyMapping parent;

  /** . */
  final P property;

  public PropertyMapping(P property) {
    this.property = property;
  }

  public PropertyMapping getParent() {
    return parent;
  }

  public BeanMapping getOwner() {
    return owner;
  }

  public String getName() {
    return property.getName();
  }

  public P getProperty() {
    return property;
  }

  public V getValue() {
    return property.getValue();
  }

  public abstract void accept(MappingVisitor visitor);

  /**
   * Returns true if the property type is covariant, meaning that it redefines the type from an ancestor
   * with a subclass.
   *
   * @return true if the property is type covariant
   */
  public abstract boolean isTypeCovariant();
}
