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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import org.etk.reflect.api.metadata.AnnotationMetadata;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public abstract class JxLAnnotationMetadata<E> implements AnnotationMetadata<Object, E, AnnotationMirror, ExecutableElement> {
  @Override
  public Object getAnnotationType(AnnotationMirror annotationMirror) {
    return annotationMirror.getAnnotationType();
  }

  @Override
  public Collection<ExecutableElement> getAnnotationParameters(AnnotationMirror annotation) {
    return ElementFilter.methodsIn(annotation.getAnnotationType().asElement().getEnclosedElements());
  }

  @Override
  public String getAnnotationParameterName(ExecutableElement parameter) {
    return parameter.getSimpleName().toString();
  }

  @Override
  public Object getAnnotationParameterType(ExecutableElement parameter) {
    return parameter.getReturnType();
  }

  @Override
  public List<?> getAnnotationParameterValue(AnnotationMirror annotation, ExecutableElement parameter) {
    AnnotationValue annotationValue = annotation.getElementValues().get(parameter);
    if (annotationValue == null) {
      throw new UnsupportedOperationException();
    } else {
      Object value = annotationValue.getValue();
      if (value instanceof List) {
        List list = new ArrayList((List)value);
        for (int i = 0;i < list.size();i++) {
          list.set(i, unwrap(list.get(i)));
        }
        return list;
      } else {
        return Arrays.asList(unwrap(value));
      }
    }
  }

  private Object unwrap(Object o) {
    if (o instanceof VariableElement) {
      return ((VariableElement)o).getSimpleName().toString();
    } else if (o instanceof AnnotationValue) {
      return ((AnnotationValue)o).getValue();
    } else {
      return o;
    }
  }
}
