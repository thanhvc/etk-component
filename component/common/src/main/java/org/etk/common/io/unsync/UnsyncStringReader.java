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

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 4, 2011  
 */
public class UnsyncStringReader extends Reader {

  public UnsyncStringReader(String string) {
    this.string = string;
    stringLength = string.length();
  }

  public void close() {
    string = null;
  }

  public void mark(int readAheadLimit) throws IOException {
    if (string == null) {
      throw new IOException("String is null");
    }
    markIndex = index;
  }

  public boolean markSupported() {
    return true;
  }

  public int read() throws IOException {
    if (string == null) {
      throw new IOException("String is null");
    }

    if (index >= stringLength) {
      return -1;
    }

    return string.charAt(index++);
  }

  public int read(char[] charArray) throws IOException {
    return read(charArray, 0, charArray.length);
  }

  public int read(char[] charArray, int offset, int length)
    throws IOException {

    if (string == null) {
      throw new IOException("String is null");
    }

    if (length <= 0) {
      return 0;
    }

    if (index >= stringLength) {
      return -1;
    }

    int read = length;

    if ((index + read) > stringLength) {
      read = stringLength - index;
    }

    string.getChars(index, index + read, charArray, offset);

    index += read;

    return read;
  }

  public int read(CharBuffer charBuffer) throws IOException {
    int remaining = charBuffer.remaining();

    char[] charArray = new char[remaining];

    int read = read(charArray, 0, remaining);

    if (read > 0) {
      charBuffer.put(charArray, 0, read);
    }

    return read;
  }

  public boolean ready() throws IOException {
    if (string == null) {
      throw new IOException("String is null");
    }

    return true;
  }

  public void reset() throws IOException {
    if (string == null) {
      throw new IOException("String is null");
    }

    index = markIndex;
  }

  public long skip(long skip) {
    if (index >= stringLength) {
      return 0;
    }

    if ((skip + index) > stringLength) {
      skip = stringLength - index;
    }

    index += skip;

    return skip;
  }

  protected int index;
  protected int stringLength;
  protected int markIndex;
  protected String string;

}
