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
package org.etk.common.io.unsync;

import java.io.Writer;

import org.etk.common.utils.StringBundler;
import org.etk.common.utils.StringPool;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 4, 2011  
 */
public class UnsyncStringWriter extends Writer {

  public UnsyncStringWriter() {
    this(true);
  }

  public UnsyncStringWriter(boolean useStringBundler) {
    if (useStringBundler) {
      stringBundler = new StringBundler();
    }
    else {
      stringBuilder = new StringBuilder();
    }
  }

  public UnsyncStringWriter(boolean useStringBundler, int initialCapacity) {
    if (useStringBundler) {
      stringBundler = new StringBundler(initialCapacity);
    }
    else {
      stringBuilder = new StringBuilder(initialCapacity);
    }
  }

  public UnsyncStringWriter(int initialCapacity) {
    this(true, initialCapacity);
  }

  public UnsyncStringWriter append(char c) {
    write(c);

    return this;
  }

  public UnsyncStringWriter append(CharSequence charSequence) {
    if (charSequence == null) {
      write(StringPool.NULL);
    }
    else {
      write(charSequence.toString());
    }

    return this;
  }

  public UnsyncStringWriter append(
    CharSequence charSequence, int start, int end) {

    if (charSequence == null) {
      charSequence = StringPool.NULL;
    }

    write(charSequence.subSequence(start, end).toString());

    return this;
  }

  public void close() {
  }

  public void flush() {
  }

  public StringBuilder getStringBuilder() {
    return stringBuilder;
  }

  public StringBundler getStringBundler() {
    return stringBundler;
  }

  public void reset() {
    if (stringBundler != null) {
      stringBundler.setIndex(0);
    }
    else {
      stringBuilder.setLength(0);
    }
  }

  public String toString() {
    if (stringBundler != null) {
      return stringBundler.toString();
    }
    else {
      return stringBuilder.toString();
    }
  }

  public void write(char[] charArray, int offset, int length) {
    if (length <= 0) {
      return;
    }

    if (stringBundler != null) {
      stringBundler.append(new String(charArray, offset, length));
    }
    else {
      stringBuilder.append(charArray, offset, length);
    }
  }

  public void write(char[] charArray) {
    write(charArray, 0, charArray.length);

  }

  public void write(int c) {
    if (stringBundler != null) {
      stringBundler.append(String.valueOf((char)c));
    }
    else {
      stringBuilder.append((char)c);
    }
  }

  public void write(String string) {
    if (stringBundler != null) {
      stringBundler.append(string);
    }
    else {
      stringBuilder.append(string);
    }
  }

  public void write(String string, int offset, int length) {
    if (stringBundler != null) {
      stringBundler.append(string.substring(offset, offset + length));
    }
    else {
      stringBuilder.append(string.substring(offset, offset + length));
    }
  }

  protected StringBuilder stringBuilder;
  protected StringBundler stringBundler;

}
