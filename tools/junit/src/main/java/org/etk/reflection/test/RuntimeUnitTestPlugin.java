package org.etk.reflection.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.core.TypeResolverImpl;
import org.etk.reflect.jlr.metadata.JLReflectionMetadata;

public class RuntimeUnitTestPlugin extends UnitTestPlugin {

  @Override
  protected void execute(ReflectUnitTest unitTest, Set<Class<?>> classes) throws Exception {
    TypeResolver<java.lang.reflect.Type> typeResolver = TypeResolverImpl.create(new JLReflectionMetadata(), false);
    Map<String, java.lang.reflect.Type> types = new HashMap<String, java.lang.reflect.Type>();
    
    for(Class<?> clazz : classes) {
      org.etk.reflection.test.Type classTypeAnnotation = clazz.getAnnotation(org.etk.reflection.test.Type.class);
      if(classTypeAnnotation != null) {
        types.put(classTypeAnnotation.value(), clazz);
      }
      
      for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
        org.etk.reflection.test.Method methodTypeAnnotation = method.getAnnotation(org.etk.reflection.test.Method.class);
        if (methodTypeAnnotation != null) {
          types.put(methodTypeAnnotation.value(), method.getGenericReturnType());
        }
      }
      
      TypeDomain<java.lang.reflect.Type> domain = new TypeDomain<java.lang.reflect.Type>(typeResolver, types);
      unitTest.run("org.etk.reflect.jlr.metadata", domain);
    }
  }

}
