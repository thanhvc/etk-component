package org.etk.model.plugins.entity.binding;

import org.etk.model.plugins.entity.PropertyInfo;
import org.etk.model.plugins.vt2.PropertyMetaType;
import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;

public class PropertiesBinding<V extends ValueInfo> extends AbstractPropertyBinding<PropertyInfo<V, ValueKind.Map>,V, ValueKind.Map> {

  /** . */
  private final PropertyMetaType<?> metaType;

  /** . */
  private final ValueKind valueKind;

  /** . */
  private final String prefix;

  public PropertiesBinding(PropertyInfo<V, ValueKind.Map> property, String prefix, PropertyMetaType<?> metaType, ValueKind valueKind) {
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

  @Override
  public void accept(BindingVisitor visitor) {
    visitor.propertiesBinding(this);
    
  }
}
