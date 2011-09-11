package org.etk.model.plugins.entity.binding;

import org.etk.model.plugins.entity.PropertyInfo;
import org.etk.model.plugins.entity.SimpleValueInfo;
import org.etk.model.plugins.json.PropertyDefinitionMapping;
import org.etk.orm.plugins.bean.ValueKind;

public class PropertyValueBinding<K extends ValueKind> extends
           AbstractPropertyBinding<PropertyInfo<SimpleValueInfo<K>, ValueKind.Single>, SimpleValueInfo<K>, ValueKind.Single> {

  /** . */
  final PropertyDefinitionMapping<?> propertyDefinition;

  public PropertyValueBinding(PropertyInfo<SimpleValueInfo<K>, ValueKind.Single> property,
                      PropertyDefinitionMapping propertyDefinition) {
    super(property);

    //
    this.propertyDefinition = propertyDefinition;
  }

  public boolean isTypeCovariant() {
    if (parent == null) {
      return true;
    } else {
      PropertyValueBinding<?> a = (PropertyValueBinding<?>) parent;
      return propertyDefinition.getMetaType() != a.propertyDefinition.getMetaType();
    }
  }

  public PropertyDefinitionMapping<?> getPropertyDefinition() {
    return propertyDefinition;
  }

  @Override
  public void accept(BindingVisitor visitor) {
    if (property.getValueKind() == ValueKind.SINGLE) {
      visitor.singleValueMapping((PropertyValueBinding<ValueKind.Single>) this);
    } else {
      visitor.multiValueMapping((PropertyValueBinding<ValueKind.Multi>) this);
    }
  }

}
