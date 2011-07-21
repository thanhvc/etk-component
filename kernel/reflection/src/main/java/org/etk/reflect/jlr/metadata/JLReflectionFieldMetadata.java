package org.etk.reflect.jlr.metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.etk.reflect.api.metadata.FieldMetadata;
import org.etk.reflect.core.AccessScope;

/**
 * Define the Java language FieldMedata, 
 * which provides the information for field.
 * 
 * @author thanh_vucong
 *
 */
public class JLReflectionFieldMetadata implements FieldMetadata<Type, Field> {

  public Iterable<Field> getDeclaredFields(Type classType) {
    Class<?> clazz = (Class<?>)classType;
    return Arrays.asList(clazz.getDeclaredFields());
  }

  public Type getType(Field field) {
    return field.getGenericType();
  }

  public String getName(Field field) {
    return field.getName();
  }

  public AccessScope getAccess(Field field) {
    return JLReflectionMetadata.getAccess(field);
  }

  public boolean isStatic(Field field) {
    return Modifier.isStatic(field.getModifiers());
  }

  public boolean isFinal(Field field) {
    return Modifier.isFinal(field.getModifiers());
  }

  public Type getOwner(Field field) {
    return field.getDeclaringClass();
  }
}