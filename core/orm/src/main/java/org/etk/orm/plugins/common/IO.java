package org.etk.orm.plugins.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IO {

  public static byte[] getBytes(InputStream in) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[256];
    for (int l = in.read(buffer);l != -1;l = in.read(buffer)) {
      baos.write(buffer, 0, l);
    }
    return baos.toByteArray();
  }
}
