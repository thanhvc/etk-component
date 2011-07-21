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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 20, 2011  
 */
public class LocalFileObject extends SimpleJavaFileObject {

  private ByteArrayOutputStream content;
  private boolean closed;
  
  public LocalFileObject(String className, Kind kind) {
    super(URI.create(className.replace('.', '/') + kind.extension), kind);
    this.content = null;
    this.closed = false;
  }
  
  public byte[] getBytes() {
    if (closed) {
      return content.toByteArray().clone();
    } else {
      throw new IllegalStateException();
    }
  }
  
  @Override
  public OutputStream openOutputStream() throws IOException {
    if (content == null) {
      return content = new ByteArrayOutputStream() {
        @Override
        public void close() throws IOException {
          super.close();
          closed = true;
        }
      };
    } else {
      throw new IOException("Already opened");
    }
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    if (content == null) {
      throw new IOException("No content");
    }
    if (!closed) {
      throw new IOException("Not closed");
    }
    return content.toString();
  }

  @Override
  public InputStream openInputStream() throws IOException {
    if (content == null) {
      throw new IOException("No content");
    }
    if (!closed) {
      throw new IOException("Not closed");
    }
    return new ByteArrayInputStream(content.toByteArray());
  }
}
