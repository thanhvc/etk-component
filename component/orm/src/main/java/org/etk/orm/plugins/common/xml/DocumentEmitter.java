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

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public class DocumentEmitter extends XMLEmitter {

  /** . */
  private ElementEmitter documentEmitter;


  public DocumentEmitter(ContentHandler handler) {
    this(handler, null);
  }

  public DocumentEmitter(ContentHandler contentHandler, LexicalHandler lexicalHandler) {
    super(new Handler(contentHandler, lexicalHandler));

    //
    this.documentEmitter = null;
  }

  public ElementEmitter documentElement(String qName) throws SAXException {
    if (documentEmitter != null) {
      throw new IllegalStateException();
    }
    if (qName == null) {
      throw new NullPointerException();
    }
    documentEmitter = new ElementEmitter(handler, qName);
    emitChild(documentEmitter);
    return documentEmitter;
  }



}

