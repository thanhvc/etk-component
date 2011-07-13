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
public class Emitter {

  /** . */
  private static final int INITIAL = 0;

  /** . */
  private static final int OPEN = 1;

  /** . */
  private static final int CLOSED = 2;

  /** . */
  private int status;

  /** . */
  private Emitter currentChild;

  /** . */
  protected final Handler handler;

  public Emitter(Handler handler) {
    this.status = INITIAL;
    this.currentChild = null;
    this.handler = handler;
  }

  protected final void checkInitial() {
    if (status != INITIAL) {
      throw new IllegalStateException();
    }
  }

  protected void emmitBeginning() throws SAXException {
    //
  }

  protected void emmitEnd() throws SAXException {
    //
  }

  protected final void emitChild(Emitter child) throws SAXException {
    switch (status) {
      case INITIAL:
        emmitBeginning();
        status = OPEN;
        break;
      case OPEN:
        if (currentChild != null && currentChild.status != CLOSED) {
          currentChild.close();
        }
        break;
      default:
        throw new IllegalStateException("Illegal transition:" + status);
    }
    currentChild = child;
  }

  public final void close() throws SAXException {
    switch (status) {
      case INITIAL:
        emmitBeginning();
        emmitEnd();
        status = CLOSED;
        break;
      case OPEN:
        if (currentChild != null && currentChild.status != CLOSED) {
          currentChild.close();
        }
        emmitEnd();
        status = CLOSED;
        break;
      default:
        throw new IllegalStateException("Illegal transition:" + status);
    }
  }
}

