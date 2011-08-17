package org.etk.model.apt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.etk.model.api.annotations.Entity;
import org.etk.model.plugins.entity.binding.EntityBinding;
import org.etk.model.plugins.entity.binding.BindingBuilder;
import org.etk.orm.api.annotations.NamespaceMapping;
import org.etk.orm.api.annotations.NodeTypeDefs;
import org.etk.orm.plugins.bean.mapping.BeanMapping;
import org.etk.orm.plugins.bean.typegen.NodeType;
import org.etk.orm.plugins.bean.typegen.SchemaBuilder;
import org.etk.orm.plugins.mapper.SetMap;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.apt.jxlr.metadata.JxLReflectionMetadata;
import org.etk.reflect.core.TypeResolverImpl;

@SupportedSourceVersion(SourceVersion.RELEASE_5)
@SupportedAnnotationTypes({
  "org.etk.model.api.annotations.Entity"})
public class EntityProcessor extends AbstractProcessor {

  /** . */
  private final TypeResolver<Object> domain = TypeResolverImpl.create(JxLReflectionMetadata.newInstance());

  /** . */
  private ProcessingEnvironment env;

  @Override
  public void init(ProcessingEnvironment env) {
    //
    this.env = env;
    //
    super.init(env);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    try {
      return _process(annotations, roundEnv);
    }
    catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  private boolean _process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    Map<String, PackageMetaData> packageMetaData = new HashMap<String, PackageMetaData>();


    Set<? extends Element> a = roundEnv.getElementsAnnotatedWith(NodeTypeDefs.class);
    for (Element e : a) {
      PackageElement pkgElt = (PackageElement) e;
      String packageName = new StringBuilder().append(pkgElt.getQualifiedName()).toString();
      NodeTypeDefs ntDefs = pkgElt.getAnnotation(NodeTypeDefs.class);
      Map<String, String> prefixMappings = Collections.emptyMap();
      for (NamespaceMapping mapping : ntDefs.namespaces()) {
        if (prefixMappings.isEmpty()) {
          prefixMappings = new HashMap<String, String>();
        }
        prefixMappings.put(mapping.prefix(), mapping.uri());
      }
      packageMetaData.put(packageName,
                          new PackageMetaData(packageName, prefixMappings, ntDefs.deep()));
    }

    Set<Element> elts = new HashSet<Element>();
    elts.addAll(roundEnv.getElementsAnnotatedWith(Entity.class));
    
    try {
      process(roundEnv, elts, packageMetaData);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    return true;
  }

  private void process( RoundEnvironment roundEnv, Set<Element> elts,
    Map<String, PackageMetaData> packageMetaDatas) throws Exception {

    Filer filer = processingEnv.getFiler();

    Set<ClassTypeInfo> classTypes = new HashSet<ClassTypeInfo>();

    SetMap<String, ClassTypeInfo> packageToClassTypes = new SetMap<String, ClassTypeInfo>();


    for (Element elt : elts) {
      TypeElement typeElt = (TypeElement)elt;

      //
      ClassTypeInfo cti = (ClassTypeInfo)domain.resolve(typeElt);

      //
      TreeMap<Integer, PackageMetaData> packageSorter = new TreeMap<Integer, PackageMetaData>();
      for (PackageMetaData packageMetaData : packageMetaDatas.values()) {
        int dist = packageMetaData.distance(cti);
        if (dist >= 0) {
          packageSorter.put(dist, packageMetaData);
        }
      }

      // Find the most appropriate package in those which are declared
      if (packageSorter.size() > 0) {
        PackageMetaData packageMetaData = packageSorter.values().iterator().next();
        Set<ClassTypeInfo> set = packageToClassTypes.get(packageMetaData.packageName);
        set.add(cti);
      }

      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "About to process the type " + cti.getName());
      classTypes.add(cti);
      try {
        JavaFileObject jfo = filer.createSourceFile(typeElt.getQualifiedName() + "_ENTITY", typeElt);
        PrintWriter out = new PrintWriter(jfo.openWriter());
        StringBuilder builder = new StringBuilder();
        new ProxyTypeGenerator(cti).build(builder);
        out.write(builder.toString());
        out.close();
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    // Build mapping
    BindingBuilder amp = new BindingBuilder();
    Map<ClassTypeInfo, EntityBinding> beanMappings = amp.build(classTypes);

    // Build property literals
    for (EntityBinding beanMapping : beanMappings.values()) {
      if (!beanMapping.isAbstract()) {
        new PropertyLiteralGenerator(beanMapping).build(filer);
      }
    }
  }
}
