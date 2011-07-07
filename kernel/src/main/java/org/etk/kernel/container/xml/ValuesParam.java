package org.etk.kernel.container.xml;

import java.util.ArrayList;
import java.util.Iterator;

public class ValuesParam extends Parameter {

  private ArrayList values = new ArrayList(2);

  public ArrayList getValues() {
    return values;
  }

  public void setValues(ArrayList values) {
    this.values = values;
  }

  public String getValue() {
    if (values.size() == 0)
      return null;
    return (String) values.get(0);
  }

  public String toString() {
    Iterator it = values.iterator();
    StringBuilder builder = new StringBuilder();
    while (it.hasNext()) {
      Object object = (Object) it.next();
      builder.append(object);
      if (it.hasNext()) {
        builder.append(",");
      }
    }
    return builder.toString();
  }
}
