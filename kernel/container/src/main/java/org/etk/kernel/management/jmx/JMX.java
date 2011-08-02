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
package org.etk.kernel.management.jmx;

import java.util.Map;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Various JMX utilities.
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public class JMX {

  private JMX() {
  }

  /**
   * This method create an object name from a generic map argument.
   * 
   * @param domain The domain part of the object name.
   * @param table A hash table containing one or more key properties. The key of
   *          each entry in the table is the key of a key property in the object
   *          name. The associated value in the table is the associated value in
   *          the object name.
   * @return an ObjectName corresponding to the given domain and key mappings.
   * @exception MalformedObjectNameException The <code>domain</code> contains an
   *              illegal character, or one of the keys or values in
   *              <code>table</code> contains an illegal character, or one of
   *              the values in <code>table</code> does not follow the rules for
   *              quoting.
   * @exception NullPointerException One of the parameters is null.
   */
  public static ObjectName createObjectName(String domain, Map<String, String> table) throws MalformedObjectNameException,
                                                                                     NullPointerException {
    StringBuilder name = new StringBuilder(128);
    name.append(domain).append(':');
    int i = 0;
    for (Map.Entry<String, String> entry : table.entrySet()) {
      if (i++ > 0) {
        name.append(",");
      }
      name.append(entry.getKey()).append('=').append(entry.getValue());
    }
    return ObjectName.getInstance(name.toString());
  }
}
