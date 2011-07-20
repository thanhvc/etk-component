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

import javax.tools.JavaFileObject;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 20, 2011  
 */
final class FileKey {

  /** . */
  final JavaFileObject.Kind kind;

  /** . */
  final String name;

  public FileKey(JavaFileObject.Kind kind, String name) {
    if (kind == null) {
      throw new NullPointerException();
    }
    if (name == null) {
      throw new NullPointerException();
    }

    //
    this.kind = kind;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (o instanceof FileKey) {
      FileKey that = (FileKey)o;
      return kind.equals(that.kind) && name.equals(that.name);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return kind.hashCode() ^ name.hashCode();
  }
}
