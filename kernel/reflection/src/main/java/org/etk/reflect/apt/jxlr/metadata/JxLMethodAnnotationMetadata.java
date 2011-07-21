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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public class JxLMethodAnnotationMetadata extends JxLAnnotationMetadata<ExecutableElement> {

  public <A extends Annotation> A resolveDeclaredAnnotation(ExecutableElement method, Class<A> annotationClass) {
    return method.getAnnotation(annotationClass);
  }

  @Override
  public Collection<AnnotationMirror> getDeclaredAnnotation(ExecutableElement method) {
    List<? extends AnnotationMirror> list = method.getAnnotationMirrors();
    ArrayList<AnnotationMirror> tmp = new ArrayList<AnnotationMirror>(list.size());
    for (AnnotationMirror am : list) {
      tmp.add(am);
    }
    return tmp;
  }
}
