package org.etk.orm.plugins.mapper.property;

import org.etk.orm.core.DomainSession;
import org.etk.orm.core.EntityContext;
import org.etk.orm.core.NameKind;
import org.etk.orm.core.ThrowableFactory;
import org.etk.orm.plugins.bean.BeanValueInfo;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.RelationshipMapping;
import org.etk.orm.plugins.mapper.JCRChildNodePropertyMapper;


public class JCRNamedChildPropertyMapper extends JCRChildNodePropertyMapper<PropertyInfo<BeanValueInfo, ValueKind.Single>> {

  /** . */
  private final String relatedName;

  /** . */
  private final String relatedPrefix;

  public JCRNamedChildPropertyMapper(RelationshipMapping.OneToOne.Hierarchic info) throws ClassNotFoundException {
    super(info);

    //
    this.relatedName = info.getLocalName();
    this.relatedPrefix = info.getPrefix();
  }

  @Override
  public Object get(EntityContext context) throws Throwable {
    EntityContext parentCtx = context.getParent();

    //
    if (parentCtx != null) {

      // Decode name
      String externalRelatedName = parentCtx.decodeName(relatedName, NameKind.OBJECT);

      //
      EntityContext parentChildWithRelatedNameCtx = parentCtx.getChild(relatedPrefix, externalRelatedName);

      // Find out if we are mapped on this parent by the related name
      if (parentChildWithRelatedNameCtx == context) {
        Class<?> relatedClass =  getRelatedClass();
        return parentCtx.adapt(relatedClass);
      }
    }

    //
    return null;
  }

  @Override
  public void set(EntityContext context, Object parent) throws Throwable {
    if (parent == null) {
      context.remove();
    } else {
      DomainSession session = context.getSession();

      // Get parent context
      EntityContext parentCtx = session.unwrapEntity(parent);

      // Decode name
      String externalRelatedName = parentCtx.decodeName(relatedName, NameKind.OBJECT);

      //
      parentCtx.addChild(ThrowableFactory.TODO, ThrowableFactory.ISE, relatedPrefix, externalRelatedName, context);
    }
  }
}