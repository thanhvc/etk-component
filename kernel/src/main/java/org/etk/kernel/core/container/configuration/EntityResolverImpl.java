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
package org.etk.kernel.core.container.configuration;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 20, 2011  
 */
public class EntityResolverImpl implements EntityResolver {

  private final Map<String, String> systemIdToResourcePath;
  private final ConcurrentMap<String, byte[]> systemIdSource;
  @Override
  public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
                                                                    IOException {
   
    return null;
  }

}
