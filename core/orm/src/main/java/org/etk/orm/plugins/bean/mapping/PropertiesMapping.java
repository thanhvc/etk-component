package org.etk.orm.plugins.bean.mapping;

import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.jcr.PropertyMetaType;

public class PropertiesMapping<V extends ValueInfo> extends PropertyMapping<PropertyInfo<V, ValueKind.Map>,V, ValueKind.Map> {

  /** . */
  private final PropertyMetaType<?> metaType;

  /** . */
  private final ValueKind valueKind;

  /** . */
  private final String prefix;

  public PropertiesMapping(PropertyInfo<V, ValueKind.Map> property, String prefix, PropertyMetaType<?> metaType, ValueKind valueKind) {
    super(property);

    //
    this.prefix = prefix;
    this.metaType = metaType;
    this.valueKind = valueKind;
  }

  public String getPrefix() {
    return prefix;
  }

  public PropertyMetaType<?> getMetaType() {
    return metaType;
  }

  public ValueKind getValueKind() {
    return valueKind;
  }

  @Override
  public void accept(MappingVisitor visitor) {
    visitor.propertiesMapping(this);
  }

  public boolean isTypeCovariant() {
/*
    if (parent == null) {
      return true;
    } else {
      PropertiesMapping<?> a = null;
      return property.getValue().getBean() != a.property.getValue().getBean();
    }
*/
    // Implement that properly based on the type of "*"
    return true;
  }
}
