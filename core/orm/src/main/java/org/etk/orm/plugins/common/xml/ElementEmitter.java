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

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 13, 2011  
 */
public class ElementEmitter extends XMLEmitter {

  /** . */
  private static final AttributesImpl EMPTY = new AttributesImpl();

  /** . */
  private final String qName;

  /** . */
  private AttributesImpl attrs;

  /** . */
  private Map<String, String> namespaces;

  public ElementEmitter(Handler handler, String qName) {
    super(handler);

    //
    this.qName = qName;
    this.attrs = EMPTY;
  }


  @Override
  protected void emmitBeginning() throws SAXException {
    handler.content.startElement("", "", qName, attrs);
  }

  @Override
  protected void emmitEnd() throws SAXException {
    if (namespaces != null) {
      for (String prefix : namespaces.keySet()) {
        handler.content.endPrefixMapping(prefix);
      }
    }

    //
    handler.content.endElement("", "", qName);
  }

  public ElementEmitter element(String qName) throws SAXException {
    if (qName == null) {
      throw new NullPointerException();
    }
    ElementEmitter child = new ElementEmitter(handler, qName);
    emitChild(child);
    return child;
  }

  public void content(String data) throws SAXException {
    emitChild(null);
    handler.content.characters(data.toCharArray(), 0, data.length());
  }

  public void withNamespace(String prefix, String uri) throws SAXException {
    if (namespaces == null) {
      namespaces = new HashMap<String, String>();
    }
    if (namespaces.containsKey(prefix)) {
      throw new IllegalStateException("Prefix " + prefix + " already bound to " + uri);
    }
    namespaces.put(prefix, uri);
    handler.content.startPrefixMapping(prefix, uri);
  }

  public ElementEmitter withAttribute(String qName, String value) {
    checkInitial();
    if (qName == null) {
      throw new NullPointerException();
    }
    if (value == null) {
      throw new NullPointerException();
    }
    if (attrs == EMPTY) {
      attrs = new AttributesImpl();
    }
    attrs.addAttribute("", "", qName, "", value);
    return this;
  }


}