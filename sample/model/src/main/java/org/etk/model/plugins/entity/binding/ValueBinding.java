package org.etk.model.plugins.entity.binding;

import org.etk.model.plugins.entity.PropertyInfo;
import org.etk.model.plugins.entity.SimpleValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.jcr.PropertyDefinitionMapping;

public class ValueBinding<K extends ValueKind>
                                               extends
                                               PropertyBinding<PropertyInfo<SimpleValueInfo<K>, ValueKind.Single>, SimpleValueInfo<K>, ValueKind.Single> {

  /** . */
  final PropertyDefinitionMapping<?> propertyDefinition;

  public ValueBinding(PropertyInfo<SimpleValueInfo<K>, ValueKind.Single> property,
                      PropertyDefinitionMapping propertyDefinition) {
    super(property);

    //
    this.propertyDefinition = propertyDefinition;
  }

  public boolean isTypeCovariant() {
    if (parent == null) {
      return true;
    } else {
      ValueBinding<?> a = (ValueBinding<?>) parent;
      return propertyDefinition.getMetaType() != a.propertyDefinition.getMetaType();
    }
  }

  public PropertyDefinitionMapping<?> getPropertyDefinition() {
    return propertyDefinition;
  }

  
}
