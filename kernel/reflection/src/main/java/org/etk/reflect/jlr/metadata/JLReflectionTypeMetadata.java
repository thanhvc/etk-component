package org.etk.reflect.jlr.metadata;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.etk.reflect.api.definition.ClassKind;
import org.etk.reflect.api.definition.LiteralKind;
import org.etk.reflect.api.definition.TypeKind;
import org.etk.reflect.api.metadata.GenericDeclarationKind;
import org.etk.reflect.api.metadata.TypeMetadata;

public class JLReflectionTypeMetadata implements TypeMetadata<Type> { 

  private static final Map<Class<?>, LiteralKind> primitiveMap = new HashMap<Class<?>, LiteralKind>();
  
  {
    primitiveMap.put(Boolean.class, LiteralKind.BOOLEAN);
    primitiveMap.put(boolean.class, LiteralKind.BOOLEAN);
    primitiveMap.put(Byte.class, LiteralKind.BYTE);
    primitiveMap.put(byte.class, LiteralKind.BYTE);
    primitiveMap.put(Short.class, LiteralKind.SHORT);
    primitiveMap.put(short.class, LiteralKind.SHORT);
    primitiveMap.put(Integer.class, LiteralKind.INT);
    primitiveMap.put(int.class, LiteralKind.INT);
    primitiveMap.put(Long.class, LiteralKind.LONG);
    primitiveMap.put(long.class, LiteralKind.LONG);
    primitiveMap.put(Float.class, LiteralKind.FLOAT);
    primitiveMap.put(float.class, LiteralKind.FLOAT);
    primitiveMap.put(Double.class, LiteralKind.DOUBLE);
    primitiveMap.put(double.class, LiteralKind.DOUBLE);
  }
  
  public TypeKind getKind(Type type) {
    if (type instanceof Class) {
      Class classType = (Class)type;
      if (classType.isArray()) {
        return TypeKind.ARRAY;
      } else {
        if (classType.isPrimitive()) {
          if (classType == void.class) {
            return TypeKind.VOID;
          } else {
            return TypeKind.SIMPLE;
          }
        } else if (
          classType == Boolean.class ||
          classType == Float.class ||
          classType == Byte.class ||
          classType == Short.class ||
          classType == Integer.class ||
          classType == Long.class ||
          classType == Double.class) {
          return TypeKind.SIMPLE;
        } else if (classType == Void.class) {
          return TypeKind.VOID;
        } else {
          return TypeKind.CLASS;
        }
      }
    } else if (type instanceof ParameterizedType) {
      return TypeKind.PARAMETERIZED;
    } else if (type instanceof TypeVariable) {
      return TypeKind.VARIABLE;
    } else if (type instanceof WildcardType) {
      return TypeKind.WILDCARD;
    } else if (type instanceof GenericArrayType) {
      return TypeKind.ARRAY;
    } else {
      throw new AssertionError();
    }
  }

  public LiteralKind getLiteralType(Type simpleType) {
    Class classType = (Class)simpleType;
    return primitiveMap.get(classType);
  }

  public Type getComponentType(Type arrayType) {
    if (arrayType instanceof Class) {
      return ((Class)arrayType).getComponentType();
    } else {
      return ((GenericArrayType)arrayType).getGenericComponentType();
    }
  }

  public boolean isPrimitive(Type simpleType) {
    Class classType = (Class)simpleType;
    return classType.isPrimitive();
  }

  public Type getEnclosing(Type classType) {
    return ((Class<?>)classType).getEnclosingClass();
  }

  public String getClassName(Type classType) {
    return ((Class<?>)classType).getName();
  }

  public ClassKind getClassKind(Type classType) {
    Class tmp = (Class)classType;
    if (tmp.isAnnotation()) {
      return ClassKind.ANNOTATION;
    } else if (tmp.isEnum()) {
      return ClassKind.ENUM;
    } else if (tmp.isInterface()) {
      return ClassKind.INTERFACE;
    } else {
      return ClassKind.CLASS;
    }
  }

  public Iterable<Type> getInterfaces(Type classType) {
    return Arrays.asList(((Class<?>)classType).getGenericInterfaces());
  }

  public Type getSuperClass(Type classType) {
    return ((Class<?>)classType).getGenericSuperclass();
  }

  public Iterable<Type> getTypeParameters(Type classType) {
    ArrayList<Type> typeParameters = new ArrayList<Type>();
    for (TypeVariable typeParameter : ((Class)classType).getTypeParameters()) {
      typeParameters.add(typeParameter);
    }
    return typeParameters;
  }

  public Type getGenericDeclaration(Type typeVariable) {
    return  (Type)((TypeVariable)typeVariable).getGenericDeclaration();
  }

  public GenericDeclarationKind getGenericDeclarationKind(Type typeVariable) {
    GenericDeclaration genDecl = ((TypeVariable)typeVariable).getGenericDeclaration();
    if (genDecl instanceof Type) {
      return GenericDeclarationKind.TYPE;
    } else if (genDecl instanceof Method) {
      return GenericDeclarationKind.METHOD;
    } else {
      throw new UnsupportedOperationException("Not yet implemented");
    }
  }

  public String getName(Type typeVariable) {
    return ((TypeVariable)typeVariable).getName();
  }

  public Iterable<Type> getBounds(Type typeVariable) {
    return Arrays.asList(((TypeVariable)typeVariable).getBounds());
  }

  public Type getRawType(Type parameterizedType) {
    return ((ParameterizedType)parameterizedType).getRawType();
  }

  public Iterable<Type> getTypeArguments(Type parameterizedType) {
    return Arrays.asList(((ParameterizedType)parameterizedType).getActualTypeArguments());
  }

  public Iterable<Type> getUpperBounds(Type wildcardType) {
    Type[] upperBounds = ((WildcardType)wildcardType).getUpperBounds();
    return Arrays.asList(upperBounds);
  }

  public Iterable<Type> getLowerBounds(Type wildcardType) {
    return Arrays.asList(((WildcardType)wildcardType).getLowerBounds());
  }
}
