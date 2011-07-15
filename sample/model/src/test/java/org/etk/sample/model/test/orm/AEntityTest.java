package org.etk.sample.model.test.orm;

import java.util.List;
import java.util.Set;

import org.etk.model.api.annotations.Entity;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;
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

  

}
