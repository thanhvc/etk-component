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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 4, 2011  
 */
public class UnsyncByteArrayOutputStream extends OutputStream {

  public UnsyncByteArrayOutputStream() {
    this(32);
  }

  public UnsyncByteArrayOutputStream(int size) {
    buffer = new byte[size];
  }

  public void reset() {
    index = 0;
  }

  public int size() {
    return index;
  }

  public byte[] toByteArray() {
    byte[] newBuffer = new byte[index];

    System.arraycopy(buffer, 0, newBuffer, 0, index);

    return newBuffer;
  }

  public String toString() {
    return new String(buffer, 0, index);
  }

  public String toString(String charsetName)
    throws UnsupportedEncodingException {

    return new String(buffer, 0, index, charsetName);
  }

  public byte[] unsafeGetByteArray() {
    return buffer;
  }

  public ByteBuffer unsafeGetByteBuffer() {
    return ByteBuffer.wrap(buffer, 0, index);
  }

  public void write(byte[] byteArray) {
    write(byteArray, 0, byteArray.length);
  }

  public void write(byte[] byteArray, int offset, int length) {
    if (length <= 0) {
      return;
    }

    int newIndex = index + length;

    if (newIndex > buffer.length) {
      int newBufferSize = Math.max(buffer.length << 1, newIndex);

      byte[] newBuffer = new byte[newBufferSize];

      System.arraycopy(buffer, 0, newBuffer, 0, index);

      buffer = newBuffer;
    }

    System.arraycopy(byteArray, offset, buffer, index, length);

    index = newIndex;
  }

  public void write(int b) {
    int newIndex = index + 1;

    if (newIndex > buffer.length) {
      int newBufferSize = Math.max(buffer.length << 1, newIndex);

      byte[] newBuffer = new byte[newBufferSize];

      System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);

      buffer = newBuffer;
    }

    buffer[index] = (byte)b;

    index = newIndex;
  }

  public void writeTo(OutputStream outputStream) throws IOException {
    outputStream.write(buffer, 0, index);
  }

  protected byte[] buffer;
  protected int index;

}