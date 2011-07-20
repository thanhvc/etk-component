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
package org.etk.reflection.java6.reflection.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.apt.jxlr.metadata.JxLReflectionMetadata;
import org.etk.reflect.core.TypeResolverImpl;
import org.etk.reflection.test.ReflectUnitTest;
import org.etk.reflection.test.Type;
import org.etk.reflection.test.TypeDomain;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 20, 2011  
 */
public class AnnotationProcessorTestRunner extends AbstractProcessor {
  private final ReflectUnitTest unitTest;
  
  private List<Throwable> failures = new ArrayList<Throwable>();
  
  private boolean executed;
  
  public AnnotationProcessorTestRunner(ReflectUnitTest unitTest) {
    this.unitTest = unitTest;
    
  }
  
  public List<Throwable> getFailures() {
    return failures;
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (executed) {
      return false;
    } else {
      executed = true;
    }
    
    //Get the related types
    Set<? extends Element> elts = roundEnv.getElementsAnnotatedWith(Type.class);
    Map<String, Object> types = new HashMap<String, Object>();
    for(Element elt : elts) {
      Type typeAnn = elt.getAnnotation(Type.class);
      String id = typeAnn.value();
      if (elt instanceof ExecutableElement) {
        types.put(id, ((ExecutableElement) elt).getReturnType());
      } else if (elt instanceof TypeElement) {
        types.put(id, elt);
      } else {
        throw new AssertionError();
      }
    }
    TypeResolver<Object> resolver = TypeResolverImpl.create(new JxLReflectionMetadata(), false);
    
    try {
      TypeDomain<Object> domain = new TypeDomain<Object>(resolver, types);
      unitTest.run("org.etk.reflection.apt", domain);
      
    } catch(Throwable e) {
      failures.add(e);
    }
    
    return false;
  }
}
