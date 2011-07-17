package org.etk.sample.model.test.orm;

import java.util.Collection;
import java.util.Set;

import org.etk.model.api.annotations.Entity;
import org.etk.model.apt.FormatterStyle;
import org.etk.model.apt.TypeFormatter;
import org.etk.model.plugins.entity.binding.EntityBinding;
import org.etk.model.plugins.entity.binding.EntityTypeKind;
import org.etk.model.plugins.entity.binding.PropertyBinding;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.core.AnnotationType;
import org.etk.sample.model.test.AbstractTestCase;

public class AEntityPropertyTest extends AbstractTestCase {

  @Override
  protected void createDomain() {
    addClass(AEntityProperty.class);
  }
  
  public void testClassTypeInfo() throws Exception {
    Set<ClassTypeInfo> classTypes = builder.getClassInfoTypes();
    assertEquals("Set<ClassTypeInfo> must be size = 1", 1, classTypes.size());
    
    for(ClassTypeInfo classType : classTypes) {
      assertClassTypeInfo(classType);
    }
    
    AEntityProperty entity = entitySession.create(AEntityProperty.class, "aentityproperty");
    entity.setName("thanhvc");
    entity.setDescription("description");
    
    assertEquals("AEntity.getName() must be equal thanhvc:: ", "thanhvc", entity.getName());
    
    assertEquals("AEntity.getDescription() must be equal description:: ", "description", entity.getDescription());
    
    entity.setName("thanhvc1");
    assertEquals("AEntity.getName() must be equal thanhvc1:: ", "thanhvc1", entity.getName());
  }
  
  public void testEntityBinding() throws Exception {
    Collection<EntityBinding> bindings = builder.getBindings();

    for (EntityBinding binding : bindings) {
      assertEntityBinding(binding);
      for (PropertyBinding proBinding : binding.getProperties().values()) {
        dumpPropertyBinding(binding.getEntity().getClassType(), proBinding);
      }
    }
    
    
  }
  
  /**
   * Supports the ClassTypeInfo
   * @param classType
   * @throws Exception
   */
  private void assertClassTypeInfo(ClassTypeInfo classType) throws Exception {
    assertEquals(classType.getSimpleName(), "AEntityProperty");
    assertNotNull("Must have annotation Entity: ", classType.getDeclaredAnnotation(AnnotationType.get(Entity.class)));
    
    Entity entity = classType.getDeclaredAnnotation(AnnotationType.get(Entity.class));
    
    assertEquals(classType.getSimpleName(), entity.name());
    
  }

  private void assertEntityBinding(EntityBinding binding) throws Exception {
    assertEquals(binding.getEntityTypeName(), "AEntityProperty");
    assertEquals(binding.getEntityTypeKind(), EntityTypeKind.ENTITY);
    assertEquals(2, binding.getProperties().size());
  }
  
  private void dumpPropertyBinding(ClassTypeInfo owner, PropertyBinding proBinding) throws Exception {
    System.out.println("PropertyName = " + proBinding.getName());
    TypeInfo type = proBinding.getValue().getEffectiveType();
    StringBuilder toto = new StringBuilder();
    new TypeFormatter(owner, FormatterStyle.CAST, toto).format(type);
    
    System.out.println("PropertyValue = " + toto.toString());
    MethodInfo getterMethodInfo = proBinding.getProperty().getGetter();
    
    System.out.println("GetterName = " + getterMethodInfo.getName());
  }
  

}
