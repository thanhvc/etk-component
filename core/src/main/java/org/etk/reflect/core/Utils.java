package org.etk.reflect.core;

import java.util.ArrayList;
import java.util.List;

import org.etk.reflect.api.ArrayTypeInfo;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.ParameterizedTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeVariableInfo;
import org.etk.reflect.api.WildcardTypeInfo;


public class Utils {

  public static TypeInfo resolve(ClassTypeInfo context, TypeInfo type) {
    if (type instanceof ClassTypeInfo) {
      return resolve(context, (ClassTypeInfo)type);
    } else if (type instanceof ParameterizedTypeInfo) {
      return resolve(context, (ParameterizedTypeInfo)type);
    } else if (type instanceof TypeVariableInfo) {
      return resolve(context, (TypeVariableInfo)type);
    } else if (type instanceof ArrayTypeInfo) {
      return resolve(context, (ArrayTypeInfo)type);
    } else if (type instanceof WildcardTypeInfo) {
      return resolve(context, (WildcardTypeInfo)type);
    } else {
      throw new UnsupportedOperationException("Cannot resolve type " + type + " with class " + type.getClass().getName());
    }
  }

  static TypeInfo resolve(ClassTypeInfo context, final WildcardTypeInfo wildcardType) {
    final ArrayList<TypeInfo> resolvedUpperBounds = new ArrayList<TypeInfo>();
    for (TypeInfo upperBound : wildcardType.getUpperBounds()) {
      resolvedUpperBounds.add(resolve(context, upperBound));
    }
    final ArrayList<TypeInfo> resolvedLowerBounds = new ArrayList<TypeInfo>();
    for (TypeInfo lowerBound : wildcardType.getLowerBounds()) {
      resolvedLowerBounds.add(resolve(context, lowerBound));
    }
    return new AbstractWildcardType(((AbstractWildcardType)wildcardType).domain) {
      public List<TypeInfo> getUpperBounds() {
        return resolvedUpperBounds;
      }
      public List<TypeInfo> getLowerBounds() {
        return resolvedLowerBounds;
      }
      public Object unwrap() {
        throw new UnsupportedOperationException();
      }
    };
  }

  static TypeInfo resolve(ClassTypeInfo context, ArrayTypeInfo genericArrayType) {
	    TypeInfo componentType = genericArrayType.getComponentType();
	    final TypeInfo resolvedComponentType = resolve(context, componentType);
	    if (resolvedComponentType.equals(componentType)) {
	      return genericArrayType;
	    } else {
	      return new AbstractArrayTypeInfo(((AbstractArrayTypeInfo)genericArrayType).domain) {
	        public TypeInfo getComponentType() {
	          return resolvedComponentType;
	        }
	        public Object unwrap() {
	          throw new UnsupportedOperationException();
	        }
	      };
	    }
	  }

  static <T, M> TypeInfo resolve(ClassTypeInfo context, ClassTypeInfo classType) {
    return classType;
  }

  static <T, M> TypeInfo resolve(ClassTypeInfo context, final ParameterizedTypeInfo parameterizedType) {
    TypeInfo rawType = parameterizedType.getRawType();
    if (rawType instanceof ClassTypeInfo) {
      List<? extends TypeInfo> typeArguments = parameterizedType.getTypeArguments();
      final ArrayList<TypeInfo> resolvedTypeArguments = new ArrayList<TypeInfo>();
      for (TypeInfo typeArgument : typeArguments) {
        TypeInfo resolvedTypeArgument = resolve(context, typeArgument);
        resolvedTypeArguments.add(resolvedTypeArgument);
      }
      return new AbstractParameterizedTypeInfo(((AbstractParameterizedTypeInfo)parameterizedType).domain) {
        public TypeInfo getRawType() {
          return parameterizedType.getRawType();
        }
        public List<TypeInfo> getTypeArguments() {
          return resolvedTypeArguments;
        }
        public TypeInfo getOwnerType() {
          return parameterizedType.getOwnerType();
        }
        public Object unwrap() {
          throw new UnsupportedOperationException();
        }
      };
    } else {
      throw new UnsupportedOperationException();
    }
  }

  static <T, M> TypeInfo resolve(ClassTypeInfo context, TypeVariableInfo type) {
    return resolve((TypeInfo)context, type);
  }

  static <T, M> TypeInfo resolve(TypeInfo context, TypeVariableInfo typeVariable) {
    if (context instanceof ParameterizedTypeInfo) {
      ParameterizedTypeInfo parameterizedContext = (ParameterizedTypeInfo)context;
      TypeInfo rawType = parameterizedContext.getRawType();
      if (!rawType.equals(typeVariable.getGenericDeclaration())) {
        TypeInfo resolvedTypeVariable = resolve(rawType, typeVariable);
        if (resolvedTypeVariable instanceof TypeVariableInfo) {
          if (resolvedTypeVariable.equals(typeVariable)) {
            return resolvedTypeVariable;
          } else {
            return resolve(context, (TypeVariableInfo)resolvedTypeVariable);
          }
        } else {
          return resolvedTypeVariable;
        }
      } else {
        int index = 0;
        for (TypeVariableInfo typeVariableParameter : typeVariable.getGenericDeclaration().getTypeParameters()) {
          TypeVariableInfo toto = typeVariableParameter;
          if (typeVariable.equals(toto)) {
            return parameterizedContext.getTypeArguments().get(index);
          }
          index++;
        }
        throw new AssertionError();
      }
    } else if (context instanceof ClassTypeInfo) {
      ClassTypeInfo classContext = (ClassTypeInfo)context;
      for (TypeInfo implementedInterface : classContext.getInterfaces()) {
        TypeInfo resolvedType = resolve(implementedInterface, typeVariable);
        if (!resolvedType.equals(typeVariable)) {
          return resolvedType;
        }
      }
      TypeInfo superClass = classContext.getSuperType();
      if (superClass != null) {
        return resolve(superClass, typeVariable);
      } else {
        return typeVariable;
      }
    } else {
      throw new UnsupportedOperationException();
    }
  }
}
