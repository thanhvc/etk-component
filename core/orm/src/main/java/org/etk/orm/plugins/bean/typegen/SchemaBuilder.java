/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.orm.plugins.bean.typegen;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.jcr.PropertyType;

import org.etk.orm.api.RelationshipType;
import org.etk.orm.plugins.bean.BeanInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.annotations.NotReferenceable;
import org.etk.orm.plugins.bean.annotations.Skip;
import org.etk.orm.plugins.bean.mapping.BeanMapping;
import org.etk.orm.plugins.bean.mapping.BeanMappingBuilder;
import org.etk.orm.plugins.bean.mapping.MappingVisitor;
import org.etk.orm.plugins.bean.mapping.NodeTypeKind;
import org.etk.orm.plugins.bean.mapping.PropertiesMapping;
import org.etk.orm.plugins.bean.mapping.RelationshipMapping;
import org.etk.orm.plugins.bean.mapping.ValueMapping;
import org.etk.orm.plugins.bean.mapping.jcr.PropertyMetaType;
import org.etk.orm.plugins.bean.type.SimpleTypeResolver;
import org.etk.orm.plugins.common.JCR;
import org.etk.orm.plugins.mapper.SetMap;
import org.etk.reflect.api.ClassTypeInfo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public class SchemaBuilder {

  /** . */
  private final SimpleTypeResolver simpleTypeResolver;

  public SchemaBuilder() {
    this(new SimpleTypeResolver());
  }

  public SchemaBuilder(SimpleTypeResolver simpleTypeResolver) {
    this.simpleTypeResolver = simpleTypeResolver;
  }

  public Map<ClassTypeInfo, NodeType> build(Collection<BeanMapping> mappings) {
    Map<ClassTypeInfo, NodeType> schema = new HashMap<ClassTypeInfo, NodeType>();
    Visitor visitor = new Visitor();
    for (BeanMapping mapping : mappings) {
      mapping.accept(visitor);
      BeanInfo bean = mapping.getBean();
      if (bean.isDeclared()) {
        ClassTypeInfo key = bean.getClassType();
        schema.put(key, visitor.getNodeType(key));
      }
    }
    visitor.end();
    return schema;
  }

  public Map<ClassTypeInfo, NodeType> build(Set<ClassTypeInfo> classTypes) {
    BeanMappingBuilder amp = new BeanMappingBuilder(simpleTypeResolver);
    Map<ClassTypeInfo, BeanMapping> mappings = amp.build(classTypes);
    return build(mappings.values());
  }

  private static class Visitor extends MappingVisitor {

    /** . */
    private final LinkedHashMap<ClassTypeInfo, NodeType> nodeTypes;

    /** . */
    private NodeType current;

    /** . */
    private final SetMap<ClassTypeInfo, ClassTypeInfo> embeddedSuperTypesMap;

    private Visitor() {
      this.nodeTypes = new LinkedHashMap<ClassTypeInfo, NodeType>();
      this.embeddedSuperTypesMap = new SetMap<ClassTypeInfo, ClassTypeInfo>();
    }

    public NodeType getNodeType(ClassTypeInfo type) {
      return nodeTypes.get(type);
    }

    private NodeType resolve(BeanMapping mapping) {
      NodeType nodeType = nodeTypes.get(mapping.getBean().getClassType());
      if (nodeType == null) {
        if (mapping.getBean().getAnnotation(Skip.class) == null) {
          boolean referenceable = mapping.getBean().getAnnotation(NotReferenceable.class) == null;
          nodeType = new NodeType(mapping, referenceable);
          nodeTypes.put(mapping.getBean().getClassType(), nodeType);
        }
      }
      return nodeType;
    }

    @Override
    public void singleValueMapping(ValueMapping<ValueKind.Single> mapping) {
      if (current != null) {
        if (mapping.getValue().getValueKind() == ValueKind.SINGLE) {
          if (mapping.isTypeCovariant() && mapping.getProperty().getAnnotation(Skip.class) == null) {
            current.properties.put(mapping.getPropertyDefinition().getName(), new PropertyDefinition(mapping.getPropertyDefinition(), false));
          }
        } else {
          if (mapping.isTypeCovariant() && mapping.getProperty().getAnnotation(Skip.class) == null) {
            current.properties.put(mapping.getPropertyDefinition().getName(), new PropertyDefinition(mapping.getPropertyDefinition(), true));
          }
        }
      }
    }

    @Override
    public void oneToManyReference(RelationshipMapping.OneToMany.Reference mapping) {
      if (mapping.isTypeCovariant() && mapping.getProperty().getAnnotation(Skip.class) == null) {
        gerenateOneToManyReference(mapping.getOwner(), mapping.getRelatedBeanMapping(), mapping.getType(), mapping.getMappedBy());
      }
    }

    private void gerenateOneToManyReference(
        BeanMapping oneMapping,
        BeanMapping manyMapping,
        RelationshipType mappingType,
        String mappedBy) {
      NodeType related = resolve(manyMapping);
      PropertyDefinition def;
      if (mappingType == RelationshipType.REFERENCE) {
        def = new PropertyDefinition(mappedBy, false, PropertyType.REFERENCE);
        NodeType nodeType = resolve(oneMapping);
        if (nodeType != null) {
          def.addValueConstraint(nodeType.getName());
        }
      } else if (mappingType == RelationshipType.PATH) {
        def = new PropertyDefinition(mappedBy, false, PropertyType.PATH);
      } else {
        throw new AssertionError();
      }
      related.properties.put(mappedBy, def);
    }

    @Override
    public void manyToOneReference(RelationshipMapping.ManyToOne.Reference mapping) {
      if (mapping.isTypeCovariant() && mapping.getProperty().getAnnotation(Skip.class) == null) {
        gerenateOneToManyReference(mapping.getRelatedBeanMapping(), mapping.getOwner(), mapping.getType(), mapping.getMappedBy());
      }
    }

    @Override
    public void propertiesMapping(PropertiesMapping<?> mapping) {
      if (current != null) {
        if (mapping.getProperty().getAnnotation(Skip.class) == null) {
          PropertyMetaType metatype = mapping.getMetaType();
          int code = metatype != null ? metatype.getCode() : PropertyType.UNDEFINED;
          boolean multiple = mapping.getValueKind() != ValueKind.SINGLE;
          PropertyDefinition pd = current.properties.get("*");
          if (pd == null) {
            current.properties.put("*", new PropertyDefinition("*", multiple, code));
          } else {
            if (pd.getType() != code) {
              current.properties.put("*", new PropertyDefinition("*", multiple, PropertyType.UNDEFINED));
            }
          }
        }
      }
    }

    @Override
    public void manyToOneHierarchic(RelationshipMapping.ManyToOne.Hierarchic mapping) {
      if (current != null) {
        if (mapping.isTypeCovariant()) {
          BeanMapping relatedBeanMapping = mapping.getRelatedBeanMapping();
          NodeType related = resolve(relatedBeanMapping);
          if (related != null) {
            related.addChildNodeType("*", false, false, current.mapping);
          }
        }
      }
    }

    @Override
    public void oneToManyHierarchic(RelationshipMapping.OneToMany.Hierarchic mapping) {
      if (current != null) {
        BeanMapping relatedBeanMapping = mapping.getRelatedBeanMapping();
        if (mapping.isTypeCovariant()) {
          current.addChildNodeType("*", false, false, relatedBeanMapping);
        }
      }
    }

    @Override
    public void oneToOneEmbedded(RelationshipMapping.OneToOne.Embedded mapping) {
      if (current != null) {
        BeanMapping relatedBeanMapping = mapping.getRelatedBeanMapping();
        if (mapping.isOwner()) {
          if (relatedBeanMapping.getNodeTypeKind() == NodeTypeKind.PRIMARY) {
            embeddedSuperTypesMap.get(current.mapping.getBean().getClassType()).add(relatedBeanMapping.getBean().getClassType());
          }
        } else {
          if (current.mapping.getNodeTypeKind() == NodeTypeKind.PRIMARY) {
            embeddedSuperTypesMap.get(relatedBeanMapping.getBean().getClassType()).add(current.mapping.getBean().getClassType());
          }
        }
      }
    }

    @Override
    public void oneToOneHierarchic(RelationshipMapping.OneToOne.Hierarchic mapping) {
      if (current != null) {
        if (mapping.isTypeCovariant()) {
          BeanMapping relatedBeanMapping = mapping.getRelatedBeanMapping();
          if (mapping.isOwner()) {
            current.addChildNodeType(
                JCR.qualify(mapping.getPrefix(), mapping.getLocalName()),
                mapping.getMandatory(),
                mapping.getAutocreated(),
                relatedBeanMapping);
          } else {
            NodeType related = resolve(relatedBeanMapping);
            if (related != null) {
              related.addChildNodeType(
                  JCR.qualify(mapping.getPrefix(), mapping.getLocalName()),
                  false,
                  mapping.getAutocreated(),
                  current.mapping);
            }
          }
        }
      }
    }

    @Override
    public void startBean(BeanMapping mapping) {
      current = resolve(mapping);
    }

    @Override
    public void endBean() {
      current = null;
    }

    public void end() {
      // Resolve super types
      for (NodeType nodeType : nodeTypes.values()) {
        ClassTypeInfo cti = nodeType.mapping.getBean().getClassType();

        // Take all delcared node types and find out which are the super types
        // based on the relationship between the java types
        for (NodeType otherNodeType : nodeTypes.values()) {
          if (otherNodeType != nodeType) {
            if (cti.isSubType((otherNodeType).mapping.getBean().getClassType())) {
              nodeType.superTypes.add(otherNodeType);
            }
          }
        }

        // Add the embedded super types
        for (ClassTypeInfo embeddedSuperTypeInfo : embeddedSuperTypesMap.get(cti)) {
          nodeType.superTypes.add(nodeTypes.get(embeddedSuperTypeInfo));
        }

        // Now resolve the minimum set of declared super types
        foo:
        for (NodeType superNodeType : nodeType.superTypes) {
          for (NodeType otherSuperNodeType : nodeType.superTypes) {
            if (otherSuperNodeType != superNodeType && ((NodeType)otherSuperNodeType).mapping.getBean().getClassType().isSubType(((NodeType)superNodeType).mapping.getBean().getClassType())) {
              continue foo;
            }
          }
          nodeType.declaredSuperTypes.add(superNodeType);
        }
      }
    }
  }
}
