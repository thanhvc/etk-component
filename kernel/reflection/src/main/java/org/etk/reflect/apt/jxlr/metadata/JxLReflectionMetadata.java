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

import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import org.etk.reflect.api.metadata.AnnotationMetadata;
import org.etk.reflect.api.metadata.FieldMetadata;
import org.etk.reflect.api.metadata.MethodMetadata;
import org.etk.reflect.api.metadata.ReflectionMetadata;
import org.etk.reflect.api.metadata.TypeMetadata;
import org.etk.reflect.core.AccessScope;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public class JxLReflectionMetadata implements ReflectionMetadata<Object, ExecutableElement, AnnotationMirror, ExecutableElement, VariableElement> {

    public static JxLReflectionMetadata newInstance() {
      return instance;
    }

    /** . */
    private static final JxLReflectionMetadata instance = new JxLReflectionMetadata();

    /** . */
    private final JxLTypeMetadata typeModel = new JxLTypeMetadata();

    /** . */
    private final JxLFieldMetadata fieldModel = new JxLFieldMetadata();

    /** . */
    private final JxLMethodMetadata methodModel = new JxLMethodMetadata();

    /** . */
    private final JxLTypeAnnotationMetadata typeAnnotationModel = new JxLTypeAnnotationMetadata();

    /** . */
    private final JxLMethodAnnotationMetadata methodAnnotationModel = new JxLMethodAnnotationMetadata();

    /** . */
    private final JxLFieldAnnotationMetadata fieldAnnotationModel = new JxLFieldAnnotationMetadata();

    @Override
    public TypeMetadata<Object> getTypeModel() {
      return typeModel;
    }

    @Override
    public FieldMetadata<Object, VariableElement> getFieldModel() {
      return fieldModel;
    }

    @Override
    public MethodMetadata<Object, ExecutableElement> getMethodModel() {
      return methodModel;
    }

    @Override
    public AnnotationMetadata<Object, Object, AnnotationMirror, ExecutableElement> getTypeAnnotationMetadata() {
      return typeAnnotationModel;
    }

    @Override
    public AnnotationMetadata<Object, ExecutableElement, AnnotationMirror, ExecutableElement> getMethodAnnotationMetadata() {
      return methodAnnotationModel;
    }

    @Override
    public AnnotationMetadata<Object, VariableElement, AnnotationMirror, ExecutableElement> getFieldAnnotationMetadata() {
      return fieldAnnotationModel;
    }

    static AccessScope getAccess(Element executableElement) {
      Set<Modifier> modifiers = executableElement.getModifiers();
      if (modifiers.contains(Modifier.PUBLIC)) {
        return AccessScope.PUBLIC;
      } else if (modifiers.contains(Modifier.PRIVATE)) {
        return AccessScope.PRIVATE;
      } else if (modifiers.contains(Modifier.PROTECTED)) {
        return AccessScope.PROTECTED;
      } else {
        return AccessScope.PACKAGE_PROTECTED;
      }
    }

    static boolean isAbstract(Element executableElement) {
      return executableElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    static boolean isStatic(Element executableElement) {
      return executableElement.getModifiers().contains(Modifier.STATIC);
    }

    static boolean isNative(Element executableElement) {
      return executableElement.getModifiers().contains(Modifier.NATIVE);
    }

    static boolean isFinal(Element executableElement) {
      return executableElement.getModifiers().contains(Modifier.FINAL);
    }
}
