package org.etk.orm.plugins.bean.mapping;

import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.SimpleValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.jcr.PropertyDefinitionMapping;

public class ValueMapping<K extends ValueKind>
                                               extends
                                               PropertyMapping<PropertyInfo<SimpleValueInfo<K>, ValueKind.Single>, SimpleValueInfo<K>, ValueKind.Single> {

  /** . */
  final PropertyDefinitionMapping<?> propertyDefinition;

  public ValueMapping(PropertyInfo<SimpleValueInfo<K>, ValueKind.Single> property,
                      PropertyDefinitionMapping propertyDefinition) {
    super(property);

    //
    this.propertyDefinition = propertyDefinition;
  }

  public boolean isTypeCovariant() {
    if (parent == null) {
      return true;
    } else {
      ValueMapping<?> a = (ValueMapping<?>) parent;
      return propertyDefinition.getMetaType() != a.propertyDefinition.getMetaType();
    }
  }

  public PropertyDefinitionMapping<?> getPropertyDefinition() {
    return propertyDefinition;
  }

  @Override
  public void accept(MappingVisitor visitor) {
    if (property.getValueKind() == ValueKind.SINGLE) {
      visitor.singleValueMapping((ValueMapping<ValueKind.Single>) this);
    } else {
      visitor.multiValueMapping((ValueMapping<ValueKind.Multi>) this);
    }
  }
}
