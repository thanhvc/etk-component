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
package org.etk.model.plugins.entity.binder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.etk.model.plugins.entity.binding.AttributeBinding;
import org.etk.model.plugins.entity.binding.BindingVisitor;
import org.etk.model.plugins.entity.binding.EntityBinding;
import org.etk.model.plugins.entity.binding.EntityTypeKind;
import org.etk.model.plugins.entity.binding.ValueBinding;
import org.etk.model.plugins.entity.binding.ValueTypeFactory;
import org.etk.model.plugins.entity.type.SimpleTypeResolver;
import org.etk.model.plugins.json.JSONAttributePropertyBinder;
import org.etk.model.plugins.json.JSONPropertyMultiValuedPropertyBinder;
import org.etk.model.plugins.json.JSONPropertySingleValuedPropertyBinder;
import org.etk.orm.core.EntityContext;
import org.etk.orm.core.ObjectContext;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.bean.type.SimpleTypeProvider;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public class BinderBuilder {

  /** . */
  private final SimpleTypeResolver simpleTypeResolver;

  /** . */
  private final ValueTypeFactory valueTypeFactory;

  public BinderBuilder(SimpleTypeResolver simpleTypeResolver) {
    this.simpleTypeResolver = simpleTypeResolver;
    this.valueTypeFactory = new ValueTypeFactory(simpleTypeResolver);
  }

  public Collection<ObjectBinder<?>> build(Collection<EntityBinding> beanMappings) {

    Context ctx = new Context();

    ctx.start();

    for (EntityBinding beanMapping : beanMappings) {
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
  private class Context extends BindingVisitor {

    private EntityBinding beanMapping;


    private Map<EntityBinding, ObjectBinder<?>> beanMappers;

    private Class<? extends ObjectContext> contextType;

    Set<MethodBinder<?>> methodMappers;
    Set<PropertyBinder<?, ?, ?, ?>> propertyMappers;

    public void start() {
      this.beanMappers = new HashMap<EntityBinding, ObjectBinder<?>>();
    }

    @Override
    public void startBean(EntityBinding mapping) {
      this.beanMapping = mapping;
      this.contextType = mapping.getEntityTypeKind() == EntityTypeKind.ENTITY ? EntityContext.class : null;
      this.propertyMappers = new HashSet<PropertyBinder<?, ?, ?, ?>>();
      this.methodMappers = new HashSet<MethodBinder<?>>();
    }

    @Override
    public void singleValueMapping(ValueBinding<ValueKind.Single> mapping) {
      if (mapping.getValue().getValueKind() == ValueKind.SINGLE) {
        SimpleTypeProvider vt = valueTypeFactory.create(mapping.getValue().getDeclaredType(), mapping.getPropertyDefinition().getMetaType());
        JSONPropertySingleValuedPropertyBinder mapper = new JSONPropertySingleValuedPropertyBinder(contextType, vt, mapping);
        propertyMappers.add(mapper);
      } else {
        SimpleTypeProvider vt = valueTypeFactory.create(mapping.getValue().getDeclaredType(), mapping.getPropertyDefinition().getMetaType());
        JSONPropertyMultiValuedPropertyBinder mapper = new JSONPropertyMultiValuedPropertyBinder(contextType, vt, mapping);
        propertyMappers.add(mapper);
      }
    }

    @Override
    public void multiValueMapping(ValueBinding<ValueKind.Multi> mapping) {
    }

   
        @Override
    public void attributeBinding(AttributeBinding mapping) {
      JSONAttributePropertyBinder mapper = new JSONAttributePropertyBinder(mapping);
      propertyMappers.add(mapper);
    }

    

    @Override
    public void endBean() {

      ObjectBinder<?> mapper;
      mapper = new ObjectBinder(beanMapping,
                                beanMapping.isAbstract(),
                                (Class<?>) beanMapping.getBean().getClassType().unwrap(),
                                propertyMappers,
                                methodMappers,
                                null,
                                beanMapping.getEntityTypeName(),
                                beanMapping.getEntityTypeKind());

      //
      beanMappers.put(beanMapping, mapper);
    }

    public void end() {
      
    }
  }
}
