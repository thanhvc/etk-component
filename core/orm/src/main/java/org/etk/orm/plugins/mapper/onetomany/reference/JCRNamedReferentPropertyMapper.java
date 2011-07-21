package org.etk.orm.plugins.mapper.onetomany.reference;

import org.etk.orm.core.DomainSession;
import org.etk.orm.core.EntityContext;
import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.BeanValueInfo;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.RelationshipMapping;
import org.etk.orm.plugins.jcr.LinkType;
import org.etk.orm.plugins.mapper.JCRNodePropertyMapper;


public class JCRNamedReferentPropertyMapper<O extends ObjectContext<O>> 
             extends JCRNodePropertyMapper<PropertyInfo<BeanValueInfo, ValueKind.Single>, BeanValueInfo, O> {

  /** . */
  private final String propertyName;

  /** . */
  private final LinkType linkType;

  public JCRNamedReferentPropertyMapper(Class<O> contextType, RelationshipMapping.ManyToOne.Reference info) throws ClassNotFoundException {
    super(contextType, info);

    //
    this.propertyName = info.getMappedBy();
    this.linkType = JCRReferentCollectionPropertyMapper.relationshipToLinkMapping.get(info.getType());
  }

  @Override
  public Object get(O context) throws Throwable {
    Class<?> relatedClass = getRelatedClass();
    EntityContext relatedCtx = context.getEntity().getReferenced(propertyName, linkType);
    if (relatedCtx == null) {
      return null;
    } else {
      Object related = relatedCtx.getObject();
      if (relatedClass.isInstance(related)) {
        return related;
      } else {
        throw new ClassCastException("Related with class " + related.getClass().getName() + " is not of class " + relatedClass);
      }
    }
  }

  @Override
  public void set(O ctx, Object value) throws Throwable {
    DomainSession session = ctx.getEntity().getSession();
    EntityContext referencedCtx = null;
    if (value != null) {
      referencedCtx = session.unwrapEntity(value);
    }
    ctx.getEntity().setReferenced(propertyName, referencedCtx, linkType);
  }
}