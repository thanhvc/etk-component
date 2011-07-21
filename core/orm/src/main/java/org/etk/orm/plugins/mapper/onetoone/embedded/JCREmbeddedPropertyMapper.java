package org.etk.orm.plugins.mapper.onetoone.embedded;

import org.etk.orm.core.EmbeddedContext;
import org.etk.orm.core.EntityContext;
import org.etk.orm.plugins.bean.BeanValueInfo;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.RelationshipMapping;
import org.etk.orm.plugins.mapper.JCRNodePropertyMapper;

public class JCREmbeddedPropertyMapper extends 
                JCRNodePropertyMapper<PropertyInfo<BeanValueInfo, ValueKind.Single>, BeanValueInfo, EmbeddedContext> {


  public JCREmbeddedPropertyMapper(RelationshipMapping.OneToOne.Embedded info) throws ClassNotFoundException {
    super(EmbeddedContext.class, info);
  }

  @Override
  public Object get(EmbeddedContext context) throws Throwable {
    EntityContext entityCtx = context.getEntity();
    if (entityCtx != null) {
      Object related = entityCtx.getObject();
      Class<?> relatedClass = getRelatedClass();
      return relatedClass.isInstance(related) ? related : null;
    } else {
      return null;
    }
  }

  @Override
  public void set(EmbeddedContext context, Object value) throws Throwable {
    if (value == null) {
      throw new UnsupportedOperationException("todo mixin removal");
    }

    //
    EntityContext entityCtx = context.getSession().unwrapEntity(value);

    //
    entityCtx.addMixin(context);
  }
}