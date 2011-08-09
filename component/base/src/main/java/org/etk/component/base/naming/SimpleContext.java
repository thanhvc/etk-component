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
package org.etk.component.base.naming;

import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public class SimpleContext implements Context {

  private static Hashtable objects = new Hashtable();

  public SimpleContext() {
  }

  public Object lookup(Name name) throws NamingException {
    throw new NamingException("Not supported");
  }

  public Object lookup(String name) throws NamingException {
    Object obj = objects.get(name);
    if (obj instanceof Reference) {
      final Reference ref = (Reference) obj;
      String factoryCN = ref.getFactoryClassName();
      try {
        final ObjectFactory factory = (ObjectFactory) Class.forName(factoryCN).newInstance();
        return factory.getObjectInstance(ref, null, null, null);
      } catch (Exception e) {
        e.printStackTrace();
        throw new NamingException("Exception: " + e);
      }
    }
    return obj;
  }

  public void bind(Name name, Object value) throws NamingException {
    throw new NamingException("Not supported");
  }

  public void bind(String name, Object value) throws NamingException {
    // System.out.println("Bind: "+name+" "+value+" "+objects);
    objects.put(name, value);
  }

  public void rebind(Name name, Object value) throws NamingException {
    throw new NamingException("Not supported");
  }

  public void rebind(String name, Object value) throws NamingException {
    objects.put(name, value);
  }

  public void unbind(Name name) throws NamingException {
    throw new NamingException("Not supported");
  }

  public void unbind(String name) throws NamingException {
    objects.remove(name);
  }

  public void rename(Name name1, Name name2) throws NamingException {
    throw new NamingException("Not supported");
  }

  public void rename(String name1, String name2) throws NamingException {
    Object val = objects.get(name1);
    objects.remove(name1);
    objects.put(name2, val);
  }

  public NamingEnumeration<NameClassPair> list(Name arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public NamingEnumeration<NameClassPair> list(String arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public NamingEnumeration<Binding> listBindings(Name arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public NamingEnumeration<Binding> listBindings(String arg0) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  public void destroySubcontext(Name arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public void destroySubcontext(String arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public Context createSubcontext(Name arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public Context createSubcontext(String arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public Object lookupLink(Name arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public Object lookupLink(String arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public NameParser getNameParser(Name arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public NameParser getNameParser(String arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public Name composeName(Name arg0, Name arg1) throws NamingException {
    throw new NamingException("Not supported");
  }

  public String composeName(String arg0, String arg1) throws NamingException {
    throw new NamingException("Not supported");
  }

  public Object addToEnvironment(String arg0, Object arg1) throws NamingException {
    throw new NamingException("Not supported");
  }

  public Object removeFromEnvironment(String arg0) throws NamingException {
    throw new NamingException("Not supported");
  }

  public Hashtable<?, ?> getEnvironment() throws NamingException {
    throw new NamingException("Not supported");
  }

  public void close() throws NamingException {
    objects.clear();
  }

  public String getNameInNamespace() throws NamingException {
    throw new NamingException("Not supported");
  }

}
