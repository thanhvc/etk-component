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
package org.etk.sample.model.test.orm;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.etk.model.apt.FormatterStyle;
import org.etk.model.apt.TypeFormatter;
import org.etk.model.plugins.entity.binding.EntityBinding;
import org.etk.model.plugins.entity.binding.MethodBinding;
import org.etk.model.plugins.entity.binding.AbstractPropertyBinding;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.TypeInfo;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com Aug
 * 17, 2011
 */
public class DumpUtils {

  public static void dumpEntityBinding(Collection<EntityBinding> bindings) throws Exception {
    String header = "-----Binding Class: ";

    for (EntityBinding binding : bindings) {
      header += binding.getEntityTypeName();
      header = StringUtils.rightPad(header, 88, "-");
      System.out.println("\n\n\n" + header);

      System.out.println("|       Getter        |       Setter        |                    Type                  |");
      System.out.println("----------------------------------------------------------------------------------------");
      for (AbstractPropertyBinding proBinding : binding.getProperties().values()) {
        dumpPropertyBinding(binding.getEntity().getClassType(), proBinding);
      }
      System.out.println("-----------------------------------------------------------------------------------------");
    }
    // Initialize new table
    header = "-----Binding Class: ";

    for (EntityBinding binding : bindings) {
      header += binding.getEntityTypeName();
      header = StringUtils.rightPad(header, 88, "-");
      System.out.println("\n\n\n" + header);

      System.out.println("|       Method                  |                           Type                       |");
      System.out.println("----------------------------------------------------------------------------------------");
      for (MethodBinding methodBinding : binding.getMethods()) {
        dumpMethodBinding(binding.getEntity().getClassType(), methodBinding);
      }
      System.out.println("----------------------------------------------------------------------------------------");
    }
  }

  private static void dumpPropertyBinding(ClassTypeInfo owner, AbstractPropertyBinding proBinding) {

    TypeInfo type = proBinding.getValue().getEffectiveType();
    StringBuilder toto = new StringBuilder();
    new TypeFormatter(owner, FormatterStyle.CAST, toto).format(type);

    MethodInfo getterMethodInfo = proBinding.getProperty().getGetter();
    MethodInfo setterMethodInfo = proBinding.getProperty().getSetter();

    String getterMethodName = getterMethodInfo.getName();
    String setterMethodName = setterMethodInfo.getName();

    getterMethodName = StringUtils.rightPad(getterMethodName, 20, " ");
    getterMethodName = StringUtils.leftPad(getterMethodName, 1);

    setterMethodName = StringUtils.rightPad(setterMethodName, 20, " ");
    setterMethodName = StringUtils.leftPad(setterMethodName, 1);
    String typeStr = StringUtils.rightPad(toto.toString(), 42, " ");

    System.out.println("|" + getterMethodName + " |" + setterMethodName + " |" + typeStr + "|");

  }

  private static void dumpMethodBinding(ClassTypeInfo owner, MethodBinding methodBinding) throws Exception {
    MethodInfo methodInfo = methodBinding.getMethod();

    String methodName = methodInfo.getName();
    TypeInfo type = methodInfo.getReturnType();

    StringBuilder toto = new StringBuilder();
    new TypeFormatter(owner, FormatterStyle.LITERAL, toto).format(type);

    methodName = StringUtils.rightPad(methodName, 30, " ");
    String typeStr = StringUtils.rightPad(toto.toString(), 54, " ");
    System.out.println("|" + methodName + " |" + typeStr + "|");

  }
}
