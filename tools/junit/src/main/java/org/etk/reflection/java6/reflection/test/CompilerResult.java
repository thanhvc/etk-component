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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 20, 2011  
 */
public class CompilerResult extends ForwardingJavaFileManager {

  private final Map<FileKey, LocalFileObject> files;
  
  protected CompilerResult(StandardJavaFileManager standardJavaFileManager) {
    super(standardJavaFileManager);
    this.files = new HashMap<FileKey, LocalFileObject>();
  }
  
  public LocalFileObject getClassFile(String name) {
    return files.get(new FileKey(JavaFileObject.Kind.CLASS, name));
  }
  
  @Override
  public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, javax.tools.FileObject sibling) throws IOException {
    if((kind == JavaFileObject.Kind.CLASS || kind == JavaFileObject.Kind.SOURCE)) {
      LocalFileObject fileObject = new LocalFileObject(className, kind);
      files.put(new FileKey(kind, className), fileObject);
      return fileObject;
    } else {
      throw new IOException("Cannot handle " + kind + " " + location);
    }
    
  }
  
  @Override
  public javax.tools.FileObject getFileForOutput(Location location,
                                                 String packageName,
                                                 String relativeName,
                                                 javax.tools.FileObject sibling) throws IOException {
    throw new IOException();
  }

}
