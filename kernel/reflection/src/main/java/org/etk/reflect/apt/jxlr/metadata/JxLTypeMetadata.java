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
package org.etk.reflect.apt.jxlr.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import org.etk.reflect.api.definition.ClassKind;
import org.etk.reflect.api.definition.LiteralKind;
import org.etk.reflect.api.definition.TypeKind;
import org.etk.reflect.api.metadata.GenericDeclarationKind;
import org.etk.reflect.api.metadata.TypeMetadata;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public class JxLTypeMetadata implements TypeMetadata<Object> {

  /** . */
  static final Map<String, LiteralKind> primitiveWrapperMap;

  static {
    Map<String, LiteralKind> map = new HashMap<String, LiteralKind>();
    map.put(Boolean.class.getName(), LiteralKind.BOOLEAN);
    map.put(Byte.class.getName(), LiteralKind.BYTE);
    map.put(Short.class.getName(), LiteralKind.SHORT);
    map.put(Integer.class.getName(), LiteralKind.INT);
    map.put(Long.class.getName(), LiteralKind.LONG);
    map.put(Float.class.getName(), LiteralKind.FLOAT);
    map.put(Double.class.getName(), LiteralKind.DOUBLE);
    primitiveWrapperMap = map;
  }

  public TypeKind getKind(Object type) {
    if (type instanceof TypeElement) {
      // We always assume that a type element is a class
      TypeElement elt = (TypeElement)type;
      String name = elt.getQualifiedName().toString();
      if (primitiveWrapperMap.containsKey(name)) {
        return TypeKind.SIMPLE;
      } else {
        return TypeKind.CLASS;
      }
    } else {
      TypeMirror mirror = (TypeMirror)type;
      switch (mirror.getKind()) {
        case VOID:
          return TypeKind.VOID;
        case BOOLEAN:
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
          return TypeKind.SIMPLE;
        case DECLARED:
          // In case of
          DeclaredType declaredType = (DeclaredType)mirror;
          if (declaredType.getTypeArguments().isEmpty()) {
            String name = ((TypeElement)declaredType.asElement()).getQualifiedName().toString();
            if (primitiveWrapperMap.containsKey(name)) {
              return TypeKind.SIMPLE;
            } else {
              return TypeKind.CLASS;
            }
          } else {
            return TypeKind.PARAMETERIZED;
          }
        case TYPEVAR:
          return TypeKind.VARIABLE;
        case WILDCARD:
          return TypeKind.WILDCARD;
        case ARRAY:
          return TypeKind.ARRAY;
        default:
          throw new AssertionError("Cannot handle type " + type + " of kind " + mirror.getKind());
      }
    }
  }

  public Object getEnclosing(Object classType) {
    if (classType instanceof TypeElement) {
      TypeElement typeElt = (TypeElement)classType;
      switch (typeElt.getNestingKind()) {
        case TOP_LEVEL:
          return null;
        case MEMBER:
          return typeElt.getEnclosingElement();
        case LOCAL:
        case ANONYMOUS:
          throw new UnsupportedOperationException("todo nesting kind");
      }
      return typeElt.getQualifiedName().toString();
    } else {
      TypeMirror typeMirror = (TypeMirror)classType;
      switch (typeMirror.getKind()) {
        case BOOLEAN:
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
        case VOID:
          return null;
        case DECLARED:
          DeclaredType declaredType = (DeclaredType)classType;
          return getEnclosing(declaredType.asElement());
        default:
          throw new AssertionError("Cannot handle type " + typeMirror + " of kind  " + typeMirror.getKind() + " as class type");
      }
    }
  }

  public String getClassName(Object classType) {
    if (classType instanceof TypeElement) {
      TypeElement typeElt = (TypeElement)classType;
      switch (typeElt.getNestingKind()) {
        case TOP_LEVEL:
          return typeElt.getQualifiedName().toString();
        case MEMBER:
          return getClassName(typeElt.getEnclosingElement()) + "$" + typeElt.getSimpleName();
        case LOCAL:
        case ANONYMOUS:
          throw new UnsupportedOperationException("todo nesting kind");
      }
      return typeElt.getQualifiedName().toString();
    } else {
      TypeMirror typeMirror = (TypeMirror)classType;
      switch (typeMirror.getKind()) {
        case BOOLEAN:
          return boolean.class.getName();
        case BYTE:
          return byte.class.getName();
        case SHORT:
          return short.class.getName();
        case INT:
          return int.class.getName();
        case LONG:
          return long.class.getName();
        case FLOAT:
          return float.class.getName();
        case DOUBLE:
          return double.class.getName();
        case VOID:
          return Void.class.getName();
        case DECLARED:
          DeclaredType declaredType = (DeclaredType)classType;
          return getClassName(declaredType.asElement());
        default:
          throw new AssertionError("Cannot handle type " + typeMirror + " of kind  " + typeMirror.getKind() + " as class type");
      }
    }
  }

  public Iterable<Object> getInterfaces(Object classType) {
    if (classType instanceof TypeElement) {
      TypeElement typeElt = (TypeElement)classType;
      List<Object> interfaces = new ArrayList<Object>();
      for (TypeMirror itf : typeElt.getInterfaces()) {
        interfaces.add(itf);
      }
      return interfaces;
    } else {
      DeclaredType declaredType = (DeclaredType)classType;
      return getInterfaces(declaredType.asElement());
    }
  }

  public ClassKind getClassKind(Object classType) {
    if (classType instanceof TypeElement) {
      TypeElement typeElt = (TypeElement)classType;
      switch (typeElt.getKind()) {
        case ENUM:
          return ClassKind.ENUM;
        case ANNOTATION_TYPE:
          return ClassKind.ANNOTATION;
        case INTERFACE:
          return ClassKind.INTERFACE;
        case CLASS:
          return ClassKind.CLASS;
        default:
          throw new AssertionError("Cannot handle type " + typeElt + " of kind  " + typeElt.getKind() + " as class type");
      }
    } else {
      TypeMirror mirror = (TypeMirror)classType;
      switch (mirror.getKind()) {
        case BOOLEAN:
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
          return ClassKind.CLASS;
        case VOID:
          return ClassKind.CLASS;
        case DECLARED:
          DeclaredType declared = (DeclaredType)mirror;
          return getClassKind(declared.asElement());
        default:
          throw new AssertionError();
      }
    }
  }

  public Object getSuperClass(Object classType) {
    if (classType instanceof TypeElement) {
      TypeElement typeElt = (TypeElement)classType;
      TypeMirror superType = typeElt.getSuperclass();
      if (superType instanceof NoType) {
        return null;
      } else {
        return superType;
      }
    } else {
      DeclaredType declaredType = (DeclaredType)classType;
      return getSuperClass(declaredType.asElement());
    }
  }

  public Iterable<Object> getTypeParameters(Object classType) {
    if (classType instanceof TypeElement) {
      TypeElement typeElt = (TypeElement)classType;
      List<Object> typeParameters = new ArrayList<Object>();
      for (TypeParameterElement typeParameterElt : typeElt.getTypeParameters()) {
        typeParameters.add(typeParameterElt.asType());
      }
      return typeParameters;
    } else {
      DeclaredType declaredType = (DeclaredType)classType;
      return getTypeParameters(declaredType.asElement());
    }
  }

  public String getName(Object typeVariable) {
    TypeParameterElement tpe = (TypeParameterElement)((TypeVariable)typeVariable).asElement();
    return tpe.getSimpleName().toString();
  }

  public GenericDeclarationKind getGenericDeclarationKind(Object typeVariable) {
    TypeParameterElement tpe = (TypeParameterElement)((TypeVariable)typeVariable).asElement();
    Element genericElt =tpe.getGenericElement();
    switch (genericElt.getKind()) {
      case INTERFACE:
      case CLASS:
        return GenericDeclarationKind.TYPE;
      case METHOD:
        return GenericDeclarationKind.METHOD;
      default:
        throw new AssertionError();
    }
  }

  public LiteralKind getLiteralType(Object simpleType) {
    if (simpleType instanceof TypeElement) {
      TypeElement elt = (TypeElement)simpleType;
      String name = elt.getQualifiedName().toString();
      LiteralKind literalType = primitiveWrapperMap.get(name);
      if (literalType == null) {
        throw new AssertionError("Cannot handle type " + simpleType);
      } else {
        return literalType;
      }
    } else {
      TypeMirror mirror = (TypeMirror)simpleType;
      switch (mirror.getKind()) {
        case BOOLEAN:
          return LiteralKind.BOOLEAN;
        case BYTE:
          return LiteralKind.BYTE;
        case SHORT:
          return LiteralKind.SHORT;
        case INT:
          return LiteralKind.INT;
        case LONG:
          return LiteralKind.LONG;
        case FLOAT:
          return LiteralKind.FLOAT;
        case DOUBLE:
          return LiteralKind.DOUBLE;
        case DECLARED:
          return getLiteralType(((DeclaredType)mirror).asElement());
        default:
          throw new AssertionError("Cannot handle type " + simpleType + " of kind " + mirror.getKind());
      }
    }
  }

  public Object getComponentType(Object arrayType) {
    return ((ArrayType)arrayType).getComponentType();
  }

  public boolean isPrimitive(Object simpleType) {
    if (simpleType instanceof TypeElement) {
      TypeElement elt = (TypeElement)simpleType;
      String name = elt.getQualifiedName().toString();
      if (primitiveWrapperMap.containsKey(name)) {
        return false;
      } else {
        throw new AssertionError("Cannot handle type " + simpleType);
      }
    } else {
      TypeMirror mirror = (TypeMirror)simpleType;
      switch (mirror.getKind()) {
        case BOOLEAN:
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
          return true;
        case DECLARED:
          return isPrimitive(((DeclaredType)mirror).asElement());
        default:
          throw new AssertionError("Cannot handle type " + simpleType + " of kind " + mirror.getKind());
      }
    }
  }

  public Object getGenericDeclaration(Object typeVariable) {
    return ((TypeParameterElement)((TypeVariable)typeVariable).asElement()).getGenericElement();
  }

  public Object getRawType(Object parameterizedType) {
    return ((DeclaredType)parameterizedType).asElement();
  }

  public Iterable<Object> getTypeArguments(Object parameterizedType) {
    List<Object> typeArguments = new ArrayList<Object>();
    for (TypeMirror typeArgument : ((DeclaredType)parameterizedType).getTypeArguments()) {
      typeArguments.add(typeArgument);
    }
    return typeArguments;
  }

  private List<Object> resolveBounds(TypeMirror bound) {
    if (bound == null) {
      return Collections.emptyList();
    } else {
      ArrayList<Object> bounds = new ArrayList<Object>();
      if (bound instanceof DeclaredType) {
        DeclaredType bilto = (DeclaredType)bound;
        TypeElement boundElt = (TypeElement)bilto.asElement();
        switch (boundElt.getNestingKind()) {
          case ANONYMOUS:

            // We need to rewrite it
            if (!(boundElt.getSuperclass() instanceof NoType)) {
              bounds.add(boundElt.getSuperclass());
            }
            for (TypeMirror itf : boundElt.getInterfaces()) {
              bounds.add(itf);
            }
            break;
          default:
            bounds.add(bound);
            break;
        }
      } else {
        bounds.add(bound);
      }
      return bounds;
    }
  }

  public Iterable<Object> getBounds(Object typeVariable) {
    TypeMirror upperBound = ((TypeVariable)typeVariable).getUpperBound();
    return resolveBounds(upperBound);
  }

  public Iterable<Object> getUpperBounds(Object wildcardType) {
    TypeMirror extendsBound = ((WildcardType)wildcardType).getExtendsBound();
    return resolveBounds(extendsBound);
  }

  public Iterable<Object> getLowerBounds(Object wildcardType) {
    TypeMirror superBounds = ((WildcardType)wildcardType).getSuperBound();
    return resolveBounds(superBounds);
  }
}