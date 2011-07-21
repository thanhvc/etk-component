package org.etk.orm.plugins.mapper.nodeattribute;

import org.etk.orm.api.Status;
import org.etk.orm.core.EntityContext;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.SimpleValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.AttributeMapping;
import org.etk.orm.plugins.bean.mapping.NodeAttributeType;
import org.etk.orm.plugins.mapper.PropertyMapper;


public class JCRNodeAttributePropertyMapper 
       extends PropertyMapper<PropertyInfo<SimpleValueInfo, ValueKind.Single>, SimpleValueInfo, EntityContext, ValueKind.Single> {

  /** . */
  private final NodeAttributeType type;

  public JCRNodeAttributePropertyMapper(AttributeMapping info) {
    super(EntityContext.class, info);

    //
    this.type = info.getType();
  }

  @Override
  public Object get(EntityContext context) throws Throwable {
    if (context.getStatus() == Status.REMOVED) {
      throw new IllegalStateException("Node is removed");
    }
    return context.getAttribute(type);
  }

  @Override
  public void set(EntityContext context, Object value) {
    if (context.getStatus() == Status.REMOVED) {
      throw new IllegalStateException("Node is removed");
    }
    if (type == NodeAttributeType.NAME) {
      context.setLocalName((String)value);
    } else {
      throw new UnsupportedOperationException();
    }
  }
}
