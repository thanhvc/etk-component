package org.etk.orm.apt;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import org.etk.orm.api.annotations.MixinType;
import org.etk.orm.api.annotations.NamespaceMapping;
import org.etk.orm.api.annotations.NodeTypeDefs;
import org.etk.orm.api.annotations.PrimaryType;
import org.etk.orm.plugins.bean.mapping.BeanMapping;
import org.etk.orm.plugins.bean.mapping.BeanMappingBuilder;
import org.etk.orm.plugins.bean.typegen.CNDNodeTypeSerializer;
import org.etk.orm.plugins.bean.typegen.NodeType;
import org.etk.orm.plugins.bean.typegen.NodeTypeSerializer;
import org.etk.orm.plugins.bean.typegen.SchemaBuilder;
import org.etk.orm.plugins.bean.typegen.XMLNodeTypeSerializer;
import org.etk.orm.plugins.mapper.SetMap;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.apt.jxlr.metadata.JxLReflectionMetadata;
import org.etk.reflect.core.TypeResolverImpl;

@SupportedSourceVersion(SourceVersion.RELEASE_5)
@SupportedAnnotationTypes({
  "org.etk.orm.api.annotations.PrimaryType",
  "org.etk.orm.api.annotations.MixinType",
  "org.etk.orm.api.annotations.Generate"})
public class ORMProcessor extends AbstractProcessor {

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
    elts.addAll(roundEnv.getElementsAnnotatedWith(PrimaryType.class));
    elts.addAll(roundEnv.getElementsAnnotatedWith(MixinType.class));

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
        JavaFileObject jfo = filer.createSourceFile(typeElt.getQualifiedName() + "_ORM", typeElt);
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
    BeanMappingBuilder amp = new BeanMappingBuilder();
    Map<ClassTypeInfo, BeanMapping> beanMappings = amp.build(classTypes);

    // Validate model
    Map<ClassTypeInfo, NodeType> schema = new SchemaBuilder().build(beanMappings.values());

    // Build property literals
    for (BeanMapping beanMapping : beanMappings.values()) {
      if (!beanMapping.isAbstract()) {
        new PropertyLiteralGenerator(beanMapping).build(filer);
      }
    }

    //
    for (String packageName : packageToClassTypes.keySet()) {
      env.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing node type package " + packageName);
      List<NodeType> nodeTypes = new ArrayList<NodeType>();

      //
      Map<String, String> mappings = Collections.emptyMap();

      //
      for (ClassTypeInfo cti : packageToClassTypes.get(packageName)) {
        PackageMetaData packageMetaData = packageMetaDatas.get(packageName);
        if (packageMetaData.prefixMappings.size() > 0) {
          if (mappings.isEmpty()) {
            mappings = new HashMap<String, String>();
          }
          mappings.putAll(packageMetaData.prefixMappings);
        }
        NodeType nodeType = schema.get(cti);
        if (nodeType != null) {
          nodeTypes.add(nodeType);
        }
      }

      //
      FileObject cndFile = filer.createResource(StandardLocation.SOURCE_OUTPUT, packageName, "nodetypes.cnd");
      NodeTypeSerializer cndSerializer = new CNDNodeTypeSerializer(nodeTypes, mappings);
      Writer cndWriter = cndFile.openWriter();
      cndSerializer.writeTo(cndWriter);
      cndWriter.close();

      //
      FileObject xmlFile = filer.createResource(StandardLocation.SOURCE_OUTPUT, packageName, "nodetypes.xml");
      NodeTypeSerializer xmlSerializer = new XMLNodeTypeSerializer(nodeTypes, mappings);
      Writer xmlWriter = xmlFile.openWriter();
      xmlSerializer.writeTo(xmlWriter);
      xmlWriter.close();
    }
  }
}
