package org.etk.orm.plugins.bean.mapping;

import org.etk.orm.plugins.bean.ValueKind;

public class MappingVisitor {
  public void startBean(BeanMapping mapping) { }

  public void singleValueMapping(ValueMapping<ValueKind.Single> mapping) { }

  public void multiValueMapping(ValueMapping<ValueKind.Multi> mapping) { }

  public void propertiesMapping(PropertiesMapping<?> mapping) { }

  public void attributeMapping(AttributeMapping mapping) { }

  public void oneToOneHierarchic(RelationshipMapping.OneToOne.Hierarchic mapping) { }

  public void oneToManyHierarchic(RelationshipMapping.OneToMany.Hierarchic mapping) { }

  public void manyToOneHierarchic(RelationshipMapping.ManyToOne.Hierarchic mapping) { }

  public void oneToManyReference(RelationshipMapping.OneToMany.Reference mapping) { }

  public void manyToOneReference(RelationshipMapping.ManyToOne.Reference mapping) { }

  public void oneToOneEmbedded(RelationshipMapping.OneToOne.Embedded mapping) { }

  public void visit(CreateMapping mapping) { }

  public void visit(DestroyMapping mapping) { }

  public void visit(FindByIdMapping mapping) { }

  public void endBean() { }

}
