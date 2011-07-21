package org.etk.orm.plugins.bean.mapping;


import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.SimpleValueInfo;
import org.etk.orm.plugins.bean.ValueKind;

public class AttributeMapping extends PropertyMapping<PropertyInfo<SimpleValueInfo, ValueKind.Single>, SimpleValueInfo, ValueKind.Single> {

  /** . */
  private final NodeAttributeType type;

  public AttributeMapping(PropertyInfo<SimpleValueInfo, ValueKind.Single> property, NodeAttributeType type) {
    super(property);
    this.type = type;
  }

  @Override
  public boolean isTypeCovariant() {

    // 
    return false;
  }

  public NodeAttributeType getType() {
    return type;
  }

  @Override
  public void accept(MappingVisitor visitor) {
    visitor.attributeMapping(this);
  }
}