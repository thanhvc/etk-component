package org.etk.model.plugins.json;

import java.util.List;

import org.etk.model.plugins.vt2.PropertyMetaType;
import org.etk.orm.plugins.bean.mapping.jcr.ItemDefinitionMapping;


public class PropertyDefinitionMapping<I> extends ItemDefinitionMapping {

  /** . */
  private final String name;

  /** . */
  private final List<String> defaultValue;

  /** . */
  private final PropertyMetaType<I> metaType;

  public PropertyDefinitionMapping(String name, PropertyMetaType<I> metaType, List<String> defaultValue, boolean mandatory) {
    super(mandatory);

    //
    this.name = name;
    this.metaType = metaType;
    this.defaultValue = defaultValue;
  }

  public String getName() {
    return name;
  }

  public PropertyMetaType<I> getMetaType() {
    return metaType;
  }

  public List<String> getDefaultValue() {
    return defaultValue;
  }
}