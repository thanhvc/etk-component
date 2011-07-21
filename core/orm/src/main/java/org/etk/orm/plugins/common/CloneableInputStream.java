package org.etk.orm.plugins.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


public final class CloneableInputStream extends ByteArrayInputStream implements Cloneable {

  public CloneableInputStream(byte buf[]) {
    super(buf);
  }

  public CloneableInputStream(InputStream in) throws IOException {
    super(IO.getBytes(in));
  }

  @Override
  public CloneableInputStream clone() {
    // We don't have to call super as this class is final
    return new CloneableInputStream(buf);
  }
}