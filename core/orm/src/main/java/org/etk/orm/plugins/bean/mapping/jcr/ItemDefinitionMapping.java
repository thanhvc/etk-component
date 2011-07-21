package org.etk.orm.plugins.bean.mapping.jcr;

public class ItemDefinitionMapping {

  /** . */
  private final boolean mandatory;

  public ItemDefinitionMapping(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public boolean isMandatory() {
    return mandatory;
  }
}

