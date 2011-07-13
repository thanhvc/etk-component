package org.etk.orm.plugins.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.etk.orm.api.format.ObjectFormatter;
import org.etk.orm.core.EmbeddedContext;
import org.etk.orm.core.EntityContext;
import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.mapping.AttributeMapping;
import org.etk.orm.plugins.bean.mapping.BeanMapping;
import org.etk.orm.plugins.bean.mapping.CreateMapping;
import org.etk.orm.plugins.bean.mapping.DestroyMapping;
import org.etk.orm.plugins.bean.mapping.FindByIdMapping;
import org.etk.orm.plugins.bean.mapping.MappingVisitor;
import org.etk.orm.plugins.bean.mapping.NodeTypeKind;
import org.etk.orm.plugins.bean.mapping.PropertiesMapping;
import org.etk.orm.plugins.bean.mapping.RelationshipMapping;
import org.etk.orm.plugins.bean.mapping.ValueMapping;
import org.etk.orm.plugins.bean.type.SimpleTypeProvider;
import org.etk.orm.plugins.bean.type.SimpleTypeResolver;
import org.etk.orm.plugins.common.ObjectInstantiator;
import org.etk.orm.plugins.mapper.nodeattribute.JCRNodeAttributePropertyMapper;
import org.etk.orm.plugins.mapper.onetomany.hierarchical.AnyChildMultiValueMapper;
import org.etk.orm.plugins.mapper.onetomany.hierarchical.JCRAnyChildCollectionPropertyMapper;
import org.etk.orm.plugins.mapper.onetomany.hierarchical.JCRAnyChildParentPropertyMapper;
import org.etk.orm.plugins.mapper.onetomany.reference.JCRNamedReferentPropertyMapper;
import org.etk.orm.plugins.mapper.onetomany.reference.JCRReferentCollectionPropertyMapper;
import org.etk.orm.plugins.mapper.onetoone.embedded.JCREmbeddedParentPropertyMapper;
import org.etk.orm.plugins.mapper.onetoone.embedded.JCREmbeddedPropertyMapper;
import org.etk.orm.plugins.mapper.property.JCRNamedChildParentPropertyMapper;
import org.etk.orm.plugins.mapper.property.JCRNamedChildPropertyMapper;
import org.etk.orm.plugins.mapper.property.JCRPropertyDetypedPropertyMapper;
import org.etk.orm.plugins.mapper.property.JCRPropertyMultiValuedPropertyMapper;
import org.etk.orm.plugins.mapper.property.JCRPropertySingleValuedPropertyMapper;
import org.etk.orm.plugins.vt2.ValueTypeFactory;


public class MapperBuilder {
  /** . */
  private final SimpleTypeResolver simpleTypeResolver;

  /** . */
  private final ValueTypeFactory valueTypeFactory;

  public MapperBuilder(SimpleTypeResolver simpleTypeResolver) {
    this.simpleTypeResolver = simpleTypeResolver;
    this.valueTypeFactory = new ValueTypeFactory(simpleTypeResolver);
  }

  public Collection<ObjectMapper<?>> build(Collection<BeanMapping> beanMappings) {

    Context ctx = new Context();

    ctx.start();

    for (BeanMapping beanMapping : beanMappings) {
      beanMapping.accept(ctx);
    }

    ctx.end();

    return ctx.beanMappers.values();
  }

  /**
   * Initializes the BeanMapping Context.
   * @author thanh_vucong
   *
   */
  private class Context extends MappingVisitor {

    private BeanMapping beanMapping;

    private SetMap<BeanMapping, MethodMapper.Create> createMethods;

    private Map<BeanMapping, ObjectMapper<?>> beanMappers;

    private Class<? extends ObjectContext> contextType;

    Set<MethodMapper<?>> methodMappers;
    Set<PropertyMapper<?, ?, ?, ?>> propertyMappers;

    public void start() {
      this.beanMappers = new HashMap<BeanMapping, ObjectMapper<?>>();
      this.createMethods = new SetMap<BeanMapping, MethodMapper.Create>();
    }

    @Override
    public void startBean(BeanMapping mapping) {
      this.beanMapping = mapping;
      this.contextType = mapping.getNodeTypeKind() == NodeTypeKind.PRIMARY ? EntityContext.class : EmbeddedContext.class;
      this.propertyMappers = new HashSet<PropertyMapper<?, ?, ?, ?>>();
      this.methodMappers = new HashSet<MethodMapper<?>>();
    }

    @Override
    public void singleValueMapping(ValueMapping<ValueKind.Single> mapping) {
      if (mapping.getValue().getValueKind() == ValueKind.SINGLE) {
        SimpleTypeProvider vt = valueTypeFactory.create(mapping.getValue().getDeclaredType(), mapping.getPropertyDefinition().getMetaType());
        JCRPropertySingleValuedPropertyMapper mapper = new JCRPropertySingleValuedPropertyMapper(contextType, vt, mapping);
        propertyMappers.add(mapper);
      } else {
        SimpleTypeProvider vt = valueTypeFactory.create(mapping.getValue().getDeclaredType(), mapping.getPropertyDefinition().getMetaType());
        JCRPropertyMultiValuedPropertyMapper mapper = new JCRPropertyMultiValuedPropertyMapper(contextType, vt, mapping);
        propertyMappers.add(mapper);
      }
    }

    @Override
    public void multiValueMapping(ValueMapping<ValueKind.Multi> mapping) {
    }

    @Override
    public void oneToOneHierarchic(RelationshipMapping.OneToOne.Hierarchic mapping) {
      try {
        if (mapping.isOwner()) {
          JCRNamedChildParentPropertyMapper mapper = new JCRNamedChildParentPropertyMapper(contextType, mapping);
          propertyMappers.add(mapper);
        } else {
          JCRNamedChildPropertyMapper mapper = new JCRNamedChildPropertyMapper(mapping);
          propertyMappers.add(mapper);
        }
      } catch (ClassNotFoundException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @Override
    public void oneToManyHierarchic(RelationshipMapping.OneToMany.Hierarchic mapping) {
      AnyChildMultiValueMapper valueMapper;
      ValueKind valueKind = mapping.getProperty().getValueKind();
      if (valueKind instanceof ValueKind.Map) {
        valueMapper = new AnyChildMultiValueMapper.Map();
      } else if (valueKind instanceof ValueKind.List) {
        valueMapper = new AnyChildMultiValueMapper.List();
      } else if (valueKind instanceof ValueKind.Collection) {
        valueMapper = new AnyChildMultiValueMapper.Collection();
      } else {
        throw new AssertionError();
      }
      try {
        JCRAnyChildParentPropertyMapper mapper = new JCRAnyChildParentPropertyMapper(contextType, mapping, valueMapper);
        propertyMappers.add(mapper);
      } catch (ClassNotFoundException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @Override
    public void manyToOneHierarchic(RelationshipMapping.ManyToOne.Hierarchic mapping) {
      try {
        JCRAnyChildCollectionPropertyMapper mapper = new JCRAnyChildCollectionPropertyMapper(mapping);
        propertyMappers.add(mapper);
      } catch (ClassNotFoundException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @Override
    public void oneToManyReference(RelationshipMapping.OneToMany.Reference mapping) {
      try {
        JCRReferentCollectionPropertyMapper mapper = new JCRReferentCollectionPropertyMapper(mapping);
        propertyMappers.add(mapper);
      } catch (ClassNotFoundException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @Override
    public void manyToOneReference(RelationshipMapping.ManyToOne.Reference mapping) {
      try {
        JCRNamedReferentPropertyMapper mapper = new JCRNamedReferentPropertyMapper(contextType, mapping);
        propertyMappers.add(mapper);
      } catch (ClassNotFoundException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @Override
    public void oneToOneEmbedded(RelationshipMapping.OneToOne.Embedded mapping) {
      try {
        if (mapping.isOwner()) {
          JCREmbeddedParentPropertyMapper mapper = new JCREmbeddedParentPropertyMapper(mapping);
          propertyMappers.add(mapper);
        } else {
          JCREmbeddedPropertyMapper mapper = new JCREmbeddedPropertyMapper(mapping);
          propertyMappers.add(mapper);
        }
      } catch (ClassNotFoundException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @Override
    public void propertiesMapping(PropertiesMapping<?> mapping) {
      JCRPropertyDetypedPropertyMapper mapper = new JCRPropertyDetypedPropertyMapper(contextType, mapping);
      propertyMappers.add(mapper);
    }

    @Override
    public void attributeMapping(AttributeMapping mapping) {
      JCRNodeAttributePropertyMapper mapper = new JCRNodeAttributePropertyMapper(mapping);
      propertyMappers.add(mapper);
    }

    @Override
    public void visit(CreateMapping mapping) {
      MethodMapper.Create mapper = new MethodMapper.Create(mapping.getMethod());
      methodMappers.add(mapper);
      createMethods.get(mapping.getBeanMapping()).add(mapper);
    }

    @Override
    public void visit(DestroyMapping mapping) {
      MethodMapper mapper = new MethodMapper.Destroy(mapping.getMethod());
      methodMappers.add(mapper);
    }

    @Override
    public void visit(FindByIdMapping mapping) {
      try {
        MethodMapper mapper = new MethodMapper.FindById(mapping.getMethod(), mapping.getType());
        methodMappers.add(mapper);
      } catch (ClassNotFoundException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @Override
    public void endBean() {

      ObjectMapper<?> mapper;
      if (beanMapping.getNodeTypeKind() == NodeTypeKind.PRIMARY) {

        // Get the formatter
        ObjectFormatter formatter = null;
        if (beanMapping.getFormatterClassType() != null) {
          Class<? extends ObjectFormatter> formatterClass = (Class<ObjectFormatter>)beanMapping.getFormatterClassType().unwrap();
          formatter = ObjectInstantiator.newInstance(formatterClass);
        }

        mapper = new ObjectMapper(
            beanMapping,
            beanMapping.isAbstract(),
            (Class<?>)beanMapping.getBean().getClassType().unwrap(),
            propertyMappers,
            methodMappers,
            beanMapping.getOnDuplicate(),
            formatter,
            beanMapping.getNodeTypeName(),
            beanMapping.getNodeTypeKind()
        );

      } else {

        mapper = new ObjectMapper(
            beanMapping,
            beanMapping.isAbstract(),
            (Class<?>)beanMapping.getBean().getClassType().unwrap(),
            propertyMappers,
            methodMappers,
            beanMapping.getOnDuplicate(),
            null,
            beanMapping.getNodeTypeName(),
            beanMapping.getNodeTypeKind()
        );
      }

      //
      beanMappers.put(beanMapping, mapper);
    }

    public void end() {
      for (BeanMapping beanMapping : createMethods.keySet()) {
        ObjectMapper beanMapper = beanMappers.get(beanMapping);
        for (MethodMapper.Create createMapper : createMethods.get(beanMapping)) {
          createMapper.mapper = beanMapper ;
        }
      }
    }
  }
}
