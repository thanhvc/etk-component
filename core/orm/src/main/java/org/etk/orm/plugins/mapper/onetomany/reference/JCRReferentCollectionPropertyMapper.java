package org.etk.orm.plugins.mapper.onetomany.reference;

import java.util.EnumMap;

import org.etk.orm.api.RelationshipType;
import org.etk.orm.core.EntityContext;
import org.etk.orm.plugins.bean.BeanValueInfo;
import org.etk.orm.plugins.bean.PropertyInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.RelationshipMapping;
import org.etk.orm.plugins.jcr.LinkType;
import org.etk.orm.plugins.mapper.JCRNodeCollectionPropertyMapper;


public class JCRReferentCollectionPropertyMapper extends
        JCRNodeCollectionPropertyMapper<PropertyInfo<BeanValueInfo, ValueKind.Collection>, EntityContext, ValueKind.Collection> {

/** . */
final static EnumMap<RelationshipType, LinkType> relationshipToLinkMapping;

  static {
    EnumMap<RelationshipType, LinkType> tmp = new EnumMap<RelationshipType, LinkType>(RelationshipType.class);
    tmp.put(RelationshipType.REFERENCE, LinkType.REFERENCE);
    tmp.put(RelationshipType.PATH, LinkType.PATH);
    relationshipToLinkMapping = tmp;
  }

  /** . */
  final String propertyName;
  
  /** . */
  final LinkType linkType;

  public JCRReferentCollectionPropertyMapper(RelationshipMapping.OneToMany.Reference<ValueKind.Collection> info) throws ClassNotFoundException {
    super(EntityContext.class, info);

    //
    this.propertyName = info.getMappedBy();
    this.linkType = relationshipToLinkMapping.get(info.getType());
  }

  @Override
  public Object get(final EntityContext context) throws Throwable {
    Object collection = context.getAttribute(this);
    if (collection == null) {
      collection = new ReferentCollection(context, this);
      context.setAttribute(this, collection);
    }
    return collection;
  }
}
