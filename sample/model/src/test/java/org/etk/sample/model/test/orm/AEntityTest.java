package org.etk.sample.model.test.orm;

import java.util.Set;

import org.etk.model.api.annotations.Entity;
import org.etk.model.plugins.entity.binding.EntityBinding;
import org.etk.model.plugins.entity.binding.EntityTypeKind;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.core.AnnotationType;
import org.etk.sample.model.test.AbstractTestCase;

public class AEntityTest extends AbstractTestCase {

  @Override
  protected void createDomain() {
    addClass(AEntity.class);
  }
  
  public void testClassTypeInfo() throws Exception {
    Set<ClassTypeInfo> classTypes = builder.getClassInfoTypes();
    assertEquals("Set<ClassTypeInfo> must be size = 1", 1, classTypes.size());
    
    for(ClassTypeInfo classType : classTypes) {
      assertClassTypeInfo(classType);
    }
    
    AEntity entity = entitySession.create(AEntity.class, "aentity");
    entity.setName("thanhvc");
    
    assertEquals("AEntity.getName() must be equal thanhvc:: ", "thanhvc", entity.getName());
    
    DumpUtils.dumpEntityBinding(builder.getBindings());
  }
  
 
  
  /**
   * Supports the ClassTypeInfo
   * @param classType
   * @throws Exception
   */
  private void assertClassTypeInfo(ClassTypeInfo classType) throws Exception {
    assertEquals(classType.getSimpleName(), "AEntity");
    assertNotNull("Must have annotation Entity: ", classType.getDeclaredAnnotation(AnnotationType.get(Entity.class)));
    
    Entity entity = classType.getDeclaredAnnotation(AnnotationType.get(Entity.class));
    
    assertEquals(classType.getSimpleName(), entity.name());
    assertNotNull("Name property must not be null: ", classType.getDeclaredField("name"));
    assertNotNull("Description property must not be null: ", classType.getDeclaredField("description"));
  }

  private void assertEntityBinding(EntityBinding binding) throws Exception {
    assertEquals(binding.getEntityTypeName(), "AEntity");
    assertEquals(binding.getEntityTypeKind(), EntityTypeKind.ENTITY);
    assertEquals(2, binding.getProperties().size());
  }
  
  
  

}
