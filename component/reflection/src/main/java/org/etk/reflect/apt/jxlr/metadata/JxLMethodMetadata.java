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
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;

import org.etk.reflect.api.metadata.MethodMetadata;
import org.etk.reflect.core.AccessScope;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public class JxLMethodMetadata implements MethodMetadata<Object, ExecutableElement> {

  public Iterable<ExecutableElement> getDeclaredMethods(Object classType) {
    if (classType instanceof TypeElement) {
      TypeElement typeElt = (TypeElement)classType;
      return ElementFilter.methodsIn(typeElt.getEnclosedElements());
    } else {
      return getDeclaredMethods(((DeclaredType)classType).asElement());
    }
  }

  public String getName(ExecutableElement executableElement) {
    return executableElement.getSimpleName().toString();
  }

  public Object getReturnType(ExecutableElement executableElement) {
    return executableElement.getReturnType();
  }

  public Iterable<Object> getParameterTypes(ExecutableElement executableElement) {
    ExecutableType executableType = (ExecutableType)executableElement.asType();
    List<Object> parameterTypes = new ArrayList<Object>();
    for (TypeMirror parameterType : executableType.getParameterTypes()) {
      parameterTypes.add(parameterType);
    }
    return parameterTypes;
  }

  @Override
  public Iterable<String> getParameterNames(ExecutableElement executableElement) {
    List<String> names = new ArrayList<String>();
    for (VariableElement elt : executableElement.getParameters()) {
      names.add(elt.getSimpleName().toString());
    }
    return names;
  }

  public AccessScope getAccess(ExecutableElement executableElement) {
    return JxLReflectionMetadata.getAccess(executableElement);
  }

  public Iterable<Object> getTypeParameters(ExecutableElement executableElement) {
    List<Object> typeParameters = new ArrayList<Object>();
    for (TypeParameterElement typeParameterElt : executableElement.getTypeParameters()) {
      typeParameters.add(typeParameterElt.asType());
    }
    return typeParameters;
  }

  public ExecutableElement getGenericDeclaration(Object typeVariable) {
    return (ExecutableElement)((TypeParameterElement)((TypeVariable)typeVariable).asElement()).getGenericElement();
  }

  public Object getOwner(ExecutableElement executableElement) {
    return executableElement.getEnclosingElement();
  }

  public boolean isAbstract(ExecutableElement executableElement) {
    return JxLReflectionMetadata.isAbstract(executableElement);
  }

  public boolean isStatic(ExecutableElement executableElement) {
    return JxLReflectionMetadata.isStatic(executableElement);
  }

  public boolean isNative(ExecutableElement executableElement) {
    return JxLReflectionMetadata.isNative(executableElement);
  }

  public boolean isFinal(ExecutableElement executableElement) {
    return JxLReflectionMetadata.isFinal(executableElement);
  }

  public Iterable<Object> getThrownTypes(ExecutableElement method) {
    ArrayList<Object> throwTypes = new ArrayList<Object>();
    for (TypeMirror thrownType : method.getThrownTypes()) {
      throwTypes.add(thrownType);
    }
    return throwTypes;
  }
}

