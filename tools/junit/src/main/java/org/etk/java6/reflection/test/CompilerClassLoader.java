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
package org.etk.java6.reflection.test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 20, 2011  
 */
public class CompilerClassLoader extends ClassLoader {
  private final CompilerResult compilerResult;
  
  private final Map<String, Class<?>> cache;
  
  public CompilerClassLoader(ClassLoader classLoader, CompilerResult compilerResult) {
    super(classLoader);
    
    this.compilerResult = compilerResult;
    this.cache = new HashMap<String, Class<?>>();
  }
  
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    Class<?> clazz = cache.get(name);
    
    if (clazz == null) {
      LocalFileObject cf = compilerResult.getClassFile(name);
      if (cf == null) {
        clazz = super.loadClass(name, resolve);
      } else {
        byte[] bytes = cf.getBytes();
        clazz = defineClass(name, bytes, 0, bytes.length, null);
        if (resolve) {
          resolveClass(clazz);
        }
        cache.put(name, clazz);
        return clazz;
        
      }
    }
    return clazz;
  }

}
