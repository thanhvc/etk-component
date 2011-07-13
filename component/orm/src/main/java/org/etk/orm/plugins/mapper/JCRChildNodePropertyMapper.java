package org.etk.orm.plugins.mapper;

import org.etk.orm.core.EntityContext;
import org.etk.orm.plugins.bean.BeanValueInfo;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.PropertyMapping;

public abstract class JCRChildNodePropertyMapper<P extends PropertyInfo<BeanValueInfo, ValueKind.Single>> extends JCRNodePropertyMapper<P, BeanValueInfo, EntityContext> {

  public JCRChildNodePropertyMapper(PropertyMapping<P, BeanValueInfo, ValueKind.Single> info) throws ClassNotFoundException {
    super(EntityContext.class, info);
  }
}