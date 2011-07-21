package org.etk.orm.plugins.mapper.onetomany.hierarchical;

import org.etk.orm.core.DomainSession;
import org.etk.orm.core.EntityContext;
import org.etk.orm.core.ThrowableFactory;
import org.etk.orm.plugins.bean.BeanValueInfo;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.RelationshipMapping;
import org.etk.orm.plugins.mapper.JCRChildNodePropertyMapper;

public class JCRAnyChildCollectionPropertyMapper extends JCRChildNodePropertyMapper<PropertyInfo<BeanValueInfo, ValueKind.Single>> {

  /** . */
  private final String prefix;

  public JCRAnyChildCollectionPropertyMapper(RelationshipMapping.ManyToOne.Hierarchic info) throws ClassNotFoundException {
    super(info);

    //
    this.prefix = info.getPrefix();
  }

  @Override
  public Object get(EntityContext context) throws Throwable {
    EntityContext parentCtx = context.getParent();
    if (parentCtx != null) {
      Class<?> relatedClass =  getRelatedClass();
      return parentCtx.adapt(relatedClass);
    }
    return null;
  }

  @Override
  public void set(EntityContext context, Object parent) throws Throwable {
    if (parent == null) {
      context.remove();
    } else {
      DomainSession session = context.getSession();
      EntityContext parentContext = session.unwrapEntity(parent);
      parentContext.addChild(ThrowableFactory.IAE, ThrowableFactory.ISE, prefix, context);
    }
  }
}