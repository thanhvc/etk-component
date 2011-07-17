package org.etk.model.apt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

import org.etk.model.plugins.entity.binding.EntityBinding;
import org.etk.model.plugins.entity.binding.PropertyBinding;
import org.etk.orm.api.PropertyLiteral;
import org.etk.orm.apt.FormatterStyle;
import org.etk.orm.apt.TypeFormatter;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeInfo;

class PropertyLiteralGenerator {

  /** . */
  private final EntityBinding entityBinding;

  PropertyLiteralGenerator(EntityBinding beanMapping) {
    this.entityBinding = beanMapping;
  }

  void build(Filer filer) throws IOException {
    String qualifiedName = entityBinding.getEntity().getClassType().getName() + "_";
    JavaFileObject jfo = filer.createSourceFile(qualifiedName); // Lack of the originating elt!!!!
    PrintWriter out = new PrintWriter(jfo.openWriter());
    build(out);
    out.close();
  }

  private void build(Appendable code) throws IOException {

    //
    ClassTypeInfo owner = entityBinding.getEntity().getClassType();
    code.append("package ").append(owner.getPackageName()).append(";\n");
    code.append("import ").append(PropertyLiteral.class.getName()).append(";\n");

    //
    code.append("public class ").append(owner.getSimpleName()).append("_ {\n");

    for (PropertyBinding pm : entityBinding.getProperties().values()) {
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
