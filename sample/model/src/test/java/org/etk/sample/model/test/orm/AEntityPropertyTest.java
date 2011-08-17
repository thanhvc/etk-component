package org.etk.sample.model.test.orm;

import java.util.Collection;
import java.util.Set;

import org.etk.model.api.annotations.Entity;
import org.etk.model.plugins.entity.binding.EntityBinding;
import org.etk.reflect.api.ClassTypeInfo;
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

    DumpUtils.dumpEntityBinding(bindings);
    
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
}
