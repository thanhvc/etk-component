package org.etk.orm.plugins.mapper.onetoone.embedded;

import org.etk.orm.core.EmbeddedContext;
import org.etk.orm.core.EntityContext;
import org.etk.orm.plugins.bean.BeanValueInfo;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.RelationshipMapping;
import org.etk.orm.plugins.mapper.RelatedPropertyMapper;


public class JCREmbeddedParentPropertyMapper extends
  RelatedPropertyMapper<PropertyInfo<BeanValueInfo, ValueKind.Single>, BeanValueInfo, EntityContext, ValueKind.Single> {

  /** . */
  private final Class relatedClass;

  public JCREmbeddedParentPropertyMapper(RelationshipMapping.OneToOne.Embedded info) throws ClassNotFoundException {
    super(EntityContext.class, info);

    // We use the classloader from the bean
    Class<?> clazz = (Class<?>) info.getOwner().getBean().getClassType().unwrap();
    ClassLoader cl = clazz.getClassLoader();

    //
    this.relatedClass = cl.loadClass(info.getValue().getClassType().getName());
  }

  @Override
  public Class<?> getRelatedClass() {
    return relatedClass;
  }

  @Override
  public Object get(EntityContext context) throws Throwable {
    EmbeddedContext mixinCtx = context.getEmbedded(relatedClass);
    return mixinCtx != null ? mixinCtx.getObject() : null;
  }

  @Override
  public void set(EntityContext context, Object value) throws Throwable {
    if (value == null) {
      throw new UnsupportedOperationException("todo mixin removal");
    }

    //
    if (!relatedClass.isInstance(value)) {
      throw new ClassCastException();
    }

    //
    EmbeddedContext mixinCtx = context.getSession().unwrapMixin(value);

    //
    context.addMixin(mixinCtx);
  }
}