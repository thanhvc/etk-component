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
package org.etk.orm.plugins.common.xml;

import org.xml.sax.SAXException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public abstract class XMLEmitter extends Emitter {
  public XMLEmitter(Handler handler) {
    super(handler);
  }

  public final void comment(String data) throws SAXException {
    if (data == null) {
      throw new NullPointerException();
    }
    if (handler.lexical != null) {
      emitChild(null);
      handler.lexical.comment(data.toCharArray(), 0, data.length());
    }
  }
}