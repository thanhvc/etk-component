package org.etk.orm.plugins.mapper.property;

import org.etk.common.logging.Logger;
import org.etk.orm.core.EntityContext;
import org.etk.orm.core.NameKind;
import org.etk.orm.core.ObjectContext;
import org.etk.orm.core.ThrowableFactory;
import org.etk.orm.plugins.bean.BeanValueInfo;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.RelationshipMapping;
import org.etk.orm.plugins.mapper.JCRNodePropertyMapper;

public class JCRNamedChildParentPropertyMapper<O extends ObjectContext<O>> 
                   extends JCRNodePropertyMapper<PropertyInfo<BeanValueInfo, ValueKind.Single>, BeanValueInfo, O> {

  /** . */
  private String relatedName;

  /** . */
  private final String relatedPrefix;

  /** . */
  private final Logger log = Logger.getLogger(JCRNamedChildParentPropertyMapper.class);

  public JCRNamedChildParentPropertyMapper(
      Class<O> contextType,
      RelationshipMapping.OneToOne.Hierarchic info) throws ClassNotFoundException {
    super(contextType, info);

    //
    this.relatedName = info.getLocalName();
    this.relatedPrefix = info.getPrefix();
  }

  @Override
  public Object get(O ctx) throws Throwable {
    // Decode name
    EntityContext entityCtx = ctx.getEntity();

    //
    String externalRelatedName = entityCtx.decodeName(relatedName, NameKind.OBJECT);

    //
    EntityContext childCtx = entityCtx.getChild(relatedPrefix, externalRelatedName);
    if (childCtx != null) {
      Object o = childCtx.getObject();
      Class<?> relatedClass = getRelatedClass();
      if (relatedClass.isInstance(o)) {
        return o;
      } else {
        throw new ClassCastException();
      }
    } else {
      return null;
    }
  }

  @Override
  public void set(O context, Object child) throws Throwable {
    EntityContext entity = context.getEntity();

    // Decode name
    String externalRelatedName = entity.decodeName(relatedName, NameKind.OBJECT);

    if (child != null) {
      EntityContext entityCtx = entity.getSession().unwrapEntity(child);
      entity.addChild(ThrowableFactory.TODO, ThrowableFactory.ISE, relatedPrefix, externalRelatedName, entityCtx);
    } else {
      entity.removeChild(relatedPrefix, externalRelatedName);
    }
  }
}