package org.etk.orm.apt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

import org.etk.orm.api.PropertyLiteral;
import org.etk.orm.plugins.bean.mapping.BeanMapping;
import org.etk.orm.plugins.bean.mapping.PropertyMapping;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeInfo;

class PropertyLiteralGenerator {

  /** . */
  private final BeanMapping beanMapping;

  PropertyLiteralGenerator(BeanMapping beanMapping) {
    this.beanMapping = beanMapping;
  }

  void build(Filer filer) throws IOException {
    String qualifiedName = beanMapping.getBean().getClassType().getName() + "_";
    JavaFileObject jfo = filer.createSourceFile(qualifiedName); // Lack of the originating elt!!!!
    PrintWriter out = new PrintWriter(jfo.openWriter());
    build(out);
    out.close();
  }

  private void build(Appendable code) throws IOException {

    //
    ClassTypeInfo owner = beanMapping.getBean().getClassType();
    code.append("package ").append(owner.getPackageName()).append(";\n");
    code.append("import ").append(PropertyLiteral.class.getName()).append(";\n");

    //
    code.append("public class ").append(owner.getSimpleName()).append("_ {\n");

    for (PropertyMapping pm : beanMapping.getProperties().values()) {
      TypeInfo type = pm.getValue().getEffectiveType();
      StringBuilder toto = new StringBuilder();
      new TypeFormatter(owner, FormatterStyle.CAST, toto).format(type);

      code.append("public static final PropertyLiteral<").
        append(owner.getName()).
        append(",").
        append(toto).
        append("> ").append(pm.getName()).append(" = new PropertyLiteral<").
        append(owner.getName()).
        append(",").
        append(toto).
        append(">").
        append("(").
        append(owner.getName()).append(".class").
        append(",").
        append("\"").append(pm.getName()).append("\"").
        append(",").
        append(toto).append(".class").
        append(");\n");
    }

    code.append("}\n");
  }
}
