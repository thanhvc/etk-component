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
package org.etk.sandbox.entity.plugins.model.configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 29, 2011  
 */
public class EntityResolverImpl implements EntityResolver {

  /** . */
  private final Map<String, String> systemIdToResourcePath;

  /** . */
  private final ConcurrentMap<String, byte[]> systemIdToSource;

  /** . */
  private final ClassLoader loader;

  public EntityResolverImpl(ClassLoader loader,
      Map<String, String> systemIdToResourcePath) {
    // Defensive copy
    this.systemIdToResourcePath = new HashMap<String, String>(
        systemIdToResourcePath);
    this.systemIdToSource = new ConcurrentHashMap<String, byte[]>();
    this.loader = loader;
  }

  public InputSource resolveEntity(String publicId, String systemId)
      throws SAXException, IOException {
    if (systemId != null) {
      byte[] data = systemIdToSource.get(systemId);

      //
      if (data == null) {
        String path = systemIdToResourcePath.get(systemId);
        if (path != null) {
          InputStream in = loader.getResourceAsStream(path);
          if (in != null) {
            //data = IOUtil.getStreamContentAsBytes(in);
          }
        }

        // Black list it, we won't find it
        if (data == null) {
          data = new byte[0];
        }

        // Put in cache
        systemIdToSource.put(systemId, data);

        // Some basic prevention against stupid use of this class that
        // could cause OOME
        if (systemIdToSource.size() > 1000) {
          systemIdToSource.clear();
        }
      }

      //
      if (data != null && data.length > 0) {
        return new InputSource(new ByteArrayInputStream(data));
      }
    }

    //
    return null;
  }

}
