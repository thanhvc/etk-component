/**
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.etk.core.rest.impl.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.etk.core.rest.impl.ApplicationContext;
import org.etk.core.rest.impl.ApplicationContextImpl;
import org.etk.core.rest.impl.MultivaluedMapImpl;
import org.etk.core.rest.provider.EntityProvider;

@Provider
@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
@Produces({ MediaType.APPLICATION_FORM_URLENCODED })
public class MultivaluedMapEntityProvider implements EntityProvider<MultivaluedMap<String, String>> {

  /**
   * {@inheritDoc}
   */
  public boolean isReadable(Class<?> type,
                            Type genericType,
                            Annotation[] annotations,
                            MediaType mediaType) {
    if (type == MultivaluedMap.class) {
      try {
        ParameterizedType t = (ParameterizedType) genericType;
        Type[] ta = t.getActualTypeArguments();
        if (ta.length == 2 && ta[0] == String.class && ta[1] == String.class)
          return true;
        return false;
      } catch (ClassCastException e) {
        return false;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public MultivaluedMap<String, String> readFrom(Class<MultivaluedMap<String, String>> type,
                                                 Type genericType,
                                                 Annotation[] annotations,
                                                 MediaType mediaType,
                                                 MultivaluedMap<String, String> httpHeaders,
                                                 InputStream entityStream) throws IOException {
    ApplicationContext context = ApplicationContextImpl.getCurrent();
    Object o = context.getAttributes().get("org.exoplatform.ws.rs.entity.form");
    if (o != null)
      return (MultivaluedMap<String, String>) o;

    MultivaluedMap<String, String> form = new MultivaluedMapImpl();
    int r = -1;
    StringBuffer sb = new StringBuffer();
    try {
      while ((r = entityStream.read()) != -1) {
        if (r != '&') {
          sb.append((char) r);
        } else {
          addPair(sb.toString().trim(), form);
          sb.setLength(0);
        }
      }
      // keep the last part
      addPair(sb.toString(), form);

      context.getAttributes().put("org.exoplatform.ws.rs.entity.form", form);

      return form;
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Parse string and add key/value pair in the {@link MultivaluedMap}.
   * 
   * @param s string for processing
   * @param f {@link MultivaluedMap} to add result of parsing
   * @throws UnsupportedEncodingException if supplied string can't be decoded
   */
  private static void addPair(String s, MultivaluedMap<String, String> f) throws UnsupportedEncodingException {
    if (s.length() == 0)
      return;
    int eq = s.indexOf('=');
    String name;
    String value;
    if (eq < 0) {
      name = URLDecoder.decode(s, "UTF-8");
      value = "";
    } else {
      name = URLDecoder.decode(s.substring(0, eq), "UTF-8");
      value = URLDecoder.decode(s.substring(eq + 1), "UTF-8");
    }
    f.add(name, value);
  }

  /**
   * {@inheritDoc}
   */
  public long getSize(MultivaluedMap<String, String> t,
                      Class<?> type,
                      Type genericType,
                      Annotation[] annotations,
                      MediaType mediaType) {
    return -1;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isWriteable(Class<?> type,
                             Type genericType,
                             Annotation[] annotations,
                             MediaType mediaType) {
    return MultivaluedMap.class.isAssignableFrom(type);
  }

  /**
   * {@inheritDoc}
   */
  public void writeTo(MultivaluedMap<String, String> t,
                      Class<?> type,
                      Type genericType,
                      Annotation[] annotations,
                      MediaType mediaType,
                      MultivaluedMap<String, Object> httpHeaders,
                      OutputStream entityStream) throws IOException {
    int i = 0;
    for (Map.Entry<String, List<String>> e : t.entrySet()) {
      for (String value : e.getValue()) {
        if (i > 0)
          entityStream.write('&');
        String name = URLEncoder.encode(e.getKey(), "UTF-8");
        entityStream.write(name.getBytes());
        i++;
        if (value != null) {
          entityStream.write('=');
          value = URLEncoder.encode(value, "UTF-8");
          entityStream.write(value.getBytes());
        }
      }
    }
  }

}
