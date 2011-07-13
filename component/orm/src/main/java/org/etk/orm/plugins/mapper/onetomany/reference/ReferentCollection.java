package org.etk.orm.plugins.mapper.onetomany.reference;

import java.util.AbstractCollection;
import java.util.Iterator;

import org.etk.orm.core.EntityContext;


public class ReferentCollection extends AbstractCollection<Object> {

  /** . */
  private final EntityContext context;

  /** . */
  private final JCRReferentCollectionPropertyMapper mapper;

  public ReferentCollection(EntityContext context, JCRReferentCollectionPropertyMapper mapper) {
    this.context = context;
    this.mapper = mapper;
  }

  @Override
  public boolean add(Object o) {
    EntityContext referentCtx = context.getSession().unwrapEntity(o);
    return context.addReference(mapper.propertyName, referentCtx, mapper.linkType);
  }

  public Iterator<Object> iterator() {
    Class<?> filterClass = mapper.getRelatedClass();
    return (Iterator<Object>)context.getReferents(mapper.propertyName, filterClass, mapper.linkType);
  }

  public int size() {
    int size = 0;
    Iterator<Object> iterator = iterator();
    while (iterator.hasNext()) {
      iterator.next();
      size++;
    }
    return size;
  }
}