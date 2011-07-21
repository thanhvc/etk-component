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

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;

import org.etk.reflect.api.metadata.FieldMetadata;
import org.etk.reflect.core.AccessScope;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public class JxLFieldMetadata implements FieldMetadata<Object, VariableElement> {

  @Override
  public Iterable<VariableElement> getDeclaredFields(Object classType) {
    if (classType instanceof TypeElement) {
      TypeElement typeElt = (TypeElement)classType;
      return ElementFilter.fieldsIn(typeElt.getEnclosedElements());
    } else {
      return getDeclaredFields(((DeclaredType)classType).asElement());
    }
  }

  @Override
  public String getName(VariableElement variableElement) {
    return variableElement.getSimpleName().toString();
  }

  @Override
  public AccessScope getAccess(VariableElement variableElement) {
    return JxLReflectionMetadata.getAccess(variableElement);
  }

  @Override
  public Object getType(VariableElement variableElement) {
    return variableElement.asType();
  }

  @Override
  public boolean isStatic(VariableElement variableElement) {
    return JxLReflectionMetadata.isStatic(variableElement);
  }

  @Override
  public boolean isFinal(VariableElement variableElement) {
    return JxLReflectionMetadata.isFinal(variableElement);
  }

  @Override
  public Object getOwner(VariableElement variableElement) {
    return variableElement.getEnclosingElement();
  }
}
