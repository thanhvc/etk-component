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

import java.io.File;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 20, 2011  
 */
public class Compiler {

  private final Set<Class<?>> classes;
  
  private final List<File> files;
  
  private final int pkgCount;
  private final JavaCompiler.CompilationTask task;
  private final DiagnosticCollector<JavaFileObject> diagnostics;
  
  private final StringWriter out;
  
  private final CompilerResult result;
  
  public Compiler(Set<Class<?>> classes) throws URISyntaxException {
    List<File> files = new ArrayList<File>();
    
    Set<String> pkgs = new HashSet<String>();
     
    for (Class<?> clazz : classes) {
      if (!clazz.isMemberClass()) {
        pkgs.add(clazz.getPackage().getName());
        String path = clazz.getName().replace('.', '/') + ".java";
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        if (url == null) {
          throw new AssertionError("Could not find source code " + path);
        }
        
        File fic = new File(url.toURI());
        if (!fic.exists()) {
          throw new AssertionError("Could not access source code" + url);
        }
        
        files.add(fic);
      }
    }
    
    int pkgCount = 0;
    for(String pkg : pkgs) {
      String path = pkg.replace('.', '/') + "/package-info.java";
      URL url = Thread.currentThread().getContextClassLoader().getResource(path);
      if (url != null) {
        File fic = new File(url.toURI());
        if (!fic.exists()) {
          throw new AssertionError("Could not access source code " + url);
        }
        files.add(fic);
        pkgCount++;
      }
      
    }
    //Preparing to create CompilationTask to hook in the JavaCompiler.
    
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
    Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);
    CompilerResult result = new CompilerResult(compiler.getStandardFileManager(diagnostics, null, null));
    
    StringWriter out = new StringWriter();
    List<String> options = Arrays.asList("-source", "1.5", "-target", "1.5");
    
    JavaCompiler.CompilationTask task = compiler.getTask(out, result, diagnostics, options, null, compilationUnits);
    
    this.classes = classes;
    this.files = files;
    this.pkgCount = pkgCount;
    this.task = task;
    this.diagnostics = diagnostics;
    this.out = out;
    this.result = result;
  }
  
  public boolean compile(Processor processor) {
    if (processor != null) {
      task.setProcessors(Collections.singleton(processor));
    }
    return task.call();
  }
  
  public String getDiagnostic() {
    StringBuilder sb = new StringBuilder();

    for (File file : files) {
      sb.append("file:").append(file.getAbsolutePath()).append("\n\n");
    }

    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
      // sb.append("source:").append(diagnostic.getSource().getName()).append("\n");
      sb.append("msg:").append(diagnostic.getMessage(Locale.ENGLISH)).append("\n");
      sb.append("code:").append(diagnostic.getCode()).append("\n");
      sb.append("column:").append(diagnostic.getColumnNumber()).append("\n");
      sb.append("line:").append(diagnostic.getLineNumber()).append("\n");
      sb.append("start:").append(diagnostic.getStartPosition()).append("\n");
      sb.append("end:").append(diagnostic.getEndPosition()).append("\n");
      sb.append("kind:").append(diagnostic.getKind()).append("\n\n");
    }

    //
    sb.append("out:").append(out.getBuffer()).append("\n\n");

    //
    return sb.toString();
  }
}
